package ui.controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.CartItem;
import models.Order;
import models.Product;
import models.users.User;
import service.OrderService;
import service.ProductService;
import service.UserService;
import ui.MainFX;

import java.util.List;

public class CustomerController {

    private StackPane view = new StackPane();
    private UserService userService;
    private ProductService productService;
    private OrderService orderService;
    private User currentUser;

    public CustomerController(MainFX mainApp, User user,
                              UserService userService,
                              ProductService productService,
                              OrderService orderService) {

        this.currentUser = user;
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label lblWelcome = new Label("Bienvenido: " + currentUser.getName());

        // --- BOTONES DE MENU ---
        Button btnVerProductos = new Button("Ver Productos");
        btnVerProductos.setOnAction(e -> showAllProducts());

        Button btnBuscarProducto = new Button("Buscar Producto por Nombre");
        btnBuscarProducto.setOnAction(e -> searchProductByName());

        Button btnAgregarAlCarrito = new Button("Agregar Producto al Carrito");
        btnAgregarAlCarrito.setOnAction(e -> addProductToCart());

        Button btnVerCarrito = new Button("Ver Mi Carrito");
        btnVerCarrito.setOnAction(e -> showCart());

        Button btnEliminarDelCarrito = new Button("Eliminar Producto del Carrito");
        btnEliminarDelCarrito.setOnAction(e -> removeFromCart());

        Button btnVaciarCarrito = new Button("Vaciar Carrito");
        btnVaciarCarrito.setOnAction(e -> clearCart());

        Button btnRealizarCompra = new Button("Realizar Compra");
        btnRealizarCompra.setOnAction(e -> generateOrder());

        Button btnUltimaCompra = new Button("Ver Última Compra");
        btnUltimaCompra.setOnAction(e -> showLastOrder());

        Button btnTodasCompras = new Button("Ver Todas Mis Compras");
        btnTodasCompras.setOnAction(e -> showAllOrders());

        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setOnAction(e -> mainApp.setScreen(mainApp.getLoginController().getView()));

        layout.getChildren().addAll(lblWelcome,
                btnVerProductos, btnBuscarProducto, btnAgregarAlCarrito,
                btnVerCarrito, btnEliminarDelCarrito, btnVaciarCarrito,
                btnRealizarCompra, btnUltimaCompra, btnTodasCompras,
                btnCerrarSesion
        );

        view.getChildren().add(layout);
    }

    public StackPane getView() {
        return view;
    }

    // ----------------- MÉTODOS DE ACCIÓN -----------------

    private void showAllProducts() {
        List<Product> products = productService.getProductsFX();
        if (products.isEmpty()) {
            showAlert("No hay productos disponibles");
            return;
        }

        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        for (Product p : products) {
            Label lbl = new Label("ID: " + p.getId() + " | " + p.getName() +
                    " | Precio: $" + p.getPrice() +
                    " | Stock: " + p.getStock());
            layout.getChildren().add(lbl);
        }

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Lista de Productos");
        stage.show();
    }

    private void searchProductByName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Ingrese el nombre del producto:");
        dialog.showAndWait().ifPresent(name -> {
            Product p = productService.searchProductByNameFX(name);
            if (p == null) {
                showAlert("Producto no encontrado");
            } else {
                showAlert("Producto encontrado:\nID: " + p.getId() +
                        "\nNombre: " + p.getName() +
                        "\nPrecio: $" + p.getPrice() +
                        "\nStock: " + p.getStock());
            }
        });
    }

    private void addProductToCart() {
        List<Product> products = productService.getProductsFX();
        if (products.isEmpty()) {
            showAlert("No hay productos disponibles");
            return;
        }

        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label lbl = new Label("Seleccione ID del producto y cantidad:");
        layout.getChildren().add(lbl);

        TextField txtId = new TextField();
        txtId.setPromptText("ID producto");
        TextField txtQty = new TextField();
        txtQty.setPromptText("Cantidad");

        Button btnAdd = new Button("Agregar");
        btnAdd.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                int qty = Integer.parseInt(txtQty.getText());

                Product p = productService.searchProductFX(id);
                if (p == null) {
                    showAlert("Producto no encontrado");
                    return;
                }
                if (qty <= 0 || qty > p.getStock()) {
                    showAlert("Cantidad inválida. Stock disponible: " + p.getStock());
                    return;
                }

                userService.addProductToCart(currentUser.getId(), p, qty);
                showAlert("Producto agregado al carrito: " + p.getName() + " x" + qty);
                stage.close();
            } catch (NumberFormatException ex) {
                showAlert("Ingrese números válidos");
            } catch (Exception ex) {
                showAlert("Error: " + ex.getMessage());
            }
        });

        layout.getChildren().addAll(txtId, txtQty, btnAdd);

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Agregar al Carrito");
        stage.show();
    }

    private void showCart() {
        List<CartItem> cart = userService.getCartListFX(currentUser.getId());
        if (cart.isEmpty()) {
            showAlert("El carrito está vacío");
            return;
        }

        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        double total = 0;
        for (CartItem item : cart) {
            Label lbl = new Label(item.getProduct().getName() +
                    " x" + item.getQuantity() +
                    " → Subtotal: $" + item.getTotalPrice());
            layout.getChildren().add(lbl);
            total += item.getTotalPrice();
        }
        Label lblTotal = new Label("Total carrito: $" + total);
        layout.getChildren().add(lblTotal);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Mi Carrito");
        stage.show();
    }

    private void removeFromCart() {
        List<CartItem> cart = userService.getCartListFX(currentUser.getId());
        if (cart.isEmpty()) {
            showAlert("El carrito está vacío");
            return;
        }

        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label lbl = new Label("Ingrese índice del producto a eliminar:");
        layout.getChildren().add(lbl);

        TextField txtIndex = new TextField();
        txtIndex.setPromptText("Número del producto");
        TextField txtQty = new TextField();
        txtQty.setPromptText("Cantidad a eliminar");

        Button btnDel = new Button("Eliminar");
        btnDel.setOnAction(e -> {
            try {
                int index = Integer.parseInt(txtIndex.getText()) - 1;
                int qty = Integer.parseInt(txtQty.getText());

                if (index < 0 || index >= cart.size()) {
                    showAlert("Índice fuera de rango");
                    return;
                }
                userService.deleteProductToCart(currentUser.getId(), index, qty);
                showAlert("Producto eliminado correctamente");
                stage.close();
            } catch (NumberFormatException ex) {
                showAlert("Ingrese números válidos");
            }
        });

        layout.getChildren().addAll(txtIndex, txtQty, btnDel);

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Eliminar del Carrito");
        stage.show();
    }

    private void clearCart() {
        userService.clearMeCart(currentUser.getId());
        showAlert("Carrito vaciado correctamente");
    }

    private void generateOrder() {
        try {
            orderService.generateOrderFromCart(currentUser);
            showAlert("Orden generada correctamente");
        } catch (Exception e) {
            showAlert("Error al generar orden: " + e.getMessage());
        }
    }

    private void showLastOrder() {
        Order order = orderService.getLastOrderFX(currentUser);
        if (order == null) {
            showAlert("No hay órdenes para mostrar");
            return;
        }

        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        for (CartItem item : order.getProductsList()) {
            Label lbl = new Label(item.getProduct().getName() + " x" + item.getQuantity() +
                    " → $" + item.getTotalPrice());
            layout.getChildren().add(lbl);
        }
        Label lblTotal = new Label("Total: $" + order.getTotalPrice());
        layout.getChildren().add(lblTotal);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Última Orden");
        stage.show();
    }

    private void showAllOrders() {
        List<Order> orders = orderService.getAllOrdersFX(currentUser);
        if (orders.isEmpty()) {
            showAlert("No hay órdenes para mostrar");
            return;
        }

        Stage stage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        for (Order order : orders) {
            Label lblHeader = new Label("Orden #" + order.getNumOrder() + " - Total: $" + order.getTotalPrice());
            layout.getChildren().add(lblHeader);
            for (CartItem item : order.getProductsList()) {
                Label lblItem = new Label(item.getProduct().getName() + " x" + item.getQuantity() +
                        " → $" + item.getTotalPrice());
                layout.getChildren().add(lblItem);
            }
            layout.getChildren().add(new Separator());
        }

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Todas las Órdenes");
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
