package ui.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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

        // --- CONTENEDOR PRINCIPAL ---
        BorderPane pageLayout = new BorderPane();
        pageLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #e0f7fa, #ffffff);");

        // --- HEADER ARRIBA ---
        HBox header = new HBox();
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #0288d1;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label lblWelcome = new Label("Bienvenido: " + currentUser.getName());
        lblWelcome.setStyle("-fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");
        header.getChildren().add(lblWelcome);
        pageLayout.setTop(header);

        // --- CONTENIDO CENTRAL ---
        StackPane contentPane = new StackPane();
        contentPane.setPadding(new Insets(20));
        Label placeholder = new Label("Aquí se mostrará el contenido de cada sección");
        placeholder.setStyle("-fx-font-size: 14; -fx-text-fill: #333;");
        contentPane.getChildren().add(placeholder);
        pageLayout.setCenter(contentPane);

        // --- MENÚ HAMBURGUESA FLOTANTE ---
        VBox menuContent = new VBox(8);
        menuContent.setPadding(new Insets(10));
        menuContent.setAlignment(Pos.CENTER_RIGHT);
        menuContent.setStyle("""
            -fx-background-color: white;
            -fx-border-color: lightgray;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);
        """);
        menuContent.setVisible(false); // oculto por defecto

        Button btnVerProductos = new Button("Ver Productos");
        btnVerProductos.setMaxWidth(Double.MAX_VALUE);
        btnVerProductos.setOnAction(e -> showAllProductsVisual());

        Button btnVerCarrito = new Button("Ver Carrito");
        btnVerCarrito.setMaxWidth(Double.MAX_VALUE);
        btnVerCarrito.setOnAction(e -> showCartVisual());

        Button btnUltimaCompra = new Button("Última Compra");
        btnUltimaCompra.setMaxWidth(Double.MAX_VALUE);
        btnUltimaCompra.setOnAction(e -> showLastOrderVisual());

        Button btnTodasCompras = new Button("Todas Mis Compras");
        btnTodasCompras.setMaxWidth(Double.MAX_VALUE);
        btnTodasCompras.setOnAction(e -> showAllOrdersVisual());

        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setMaxWidth(Double.MAX_VALUE);
        btnCerrarSesion.setOnAction(e -> mainApp.setScreen(mainApp.getLoginController().getView()));

        menuContent.getChildren().addAll(
                btnVerProductos, btnVerCarrito, btnUltimaCompra, btnTodasCompras, btnCerrarSesion
        );

        // --- BOTÓN HAMBURGUESA ---
        Button btnMenu = new Button("☰");
        btnMenu.setStyle("""
            -fx-background-color: #0288d1;
            -fx-text-fill: white;
            -fx-font-size: 18;
            -fx-font-weight: bold;
            -fx-background-radius: 50%;
            -fx-min-width: 45px;
            -fx-min-height: 45px;
        """);

        btnMenu.setOnAction(e -> {
            menuContent.setVisible(!menuContent.isVisible());
        });

        // --- CONTENEDOR FLOTANTE ---
        VBox floatingBox = new VBox(10, menuContent, btnMenu);
        floatingBox.setAlignment(Pos.BOTTOM_RIGHT);
        floatingBox.setPadding(new Insets(0, 20, 20, 0));

        StackPane.setAlignment(floatingBox, Pos.BOTTOM_RIGHT);

        // --- STACK PRINCIPAL ---
        StackPane mainStack = new StackPane(pageLayout, floatingBox);

        view.getChildren().add(mainStack);
    }

    public StackPane getView() {
        return view;
    }

    // ----------------- MÉTODOS VISUALES -----------------

    private void showAllProductsVisual() {
        List<Product> products = productService.getProductsFX();
        if (products.isEmpty()) {
            showAlert("No hay productos disponibles");
            return;
        }

        Stage stage = new Stage();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        for (Product p : products) {
            VBox card = new VBox(5);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-background-color: #f5f5f5;");

            Label lblName = new Label(p.getName());
            lblName.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            Label lblPrice = new Label("Precio: $" + String.format("%.2f", p.getPrice()));
            Label lblStock = new Label("Stock: " + p.getStock());

            TextField txtQty = new TextField();
            txtQty.setPromptText("Cantidad");
            txtQty.setMaxWidth(60);

            Button btnAgregar = new Button("Agregar al carrito");
            btnAgregar.setOnAction(e -> {
                try {
                    int qty = Integer.parseInt(txtQty.getText());
                    if (qty <= 0 || qty > p.getStock()) {
                        showAlert("Cantidad inválida. Stock disponible: " + p.getStock());
                        return;
                    }
                    userService.addProductToCart(currentUser.getId(), p, qty);
                    showAlert("Producto agregado al carrito: " + p.getName() + " x" + qty);
                } catch (NumberFormatException ex) {
                    showAlert("Ingrese un número válido");
                } catch (Exception ex) {
                    showAlert("Error: " + ex.getMessage());
                }
            });

            card.getChildren().addAll(lblName, lblPrice, lblStock, txtQty, btnAgregar);
            layout.getChildren().add(card);
        }

        scrollPane.setContent(layout);
        Scene scene = new Scene(scrollPane, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Productos");
        stage.show();
    }

    private void showCartVisual() {
        List<CartItem> cart = userService.getCartListFX(currentUser.getId());
        if (cart.isEmpty()) {
            showAlert("El carrito está vacío");
            return;
        }

        Stage stage = new Stage();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        double total = 0;
        for (int i = 0; i < cart.size(); i++) {
            CartItem item = cart.get(i);

            VBox card = new VBox(5);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-background-color: #e0f7fa;");

            Label lblName = new Label(item.getProduct().getName());
            lblName.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            Label lblQty = new Label("Cantidad: " + item.getQuantity());
            Label lblSubtotal = new Label("Subtotal: $" + String.format("%.2f", item.getTotalPrice()));

            Button btnEliminar = new Button("Eliminar");
            int index = i;
            btnEliminar.setOnAction(e -> {
                userService.deleteProductToCart(currentUser.getId(), index, item.getQuantity());
                stage.close();
                showCartVisual();
            });

            card.getChildren().addAll(lblName, lblQty, lblSubtotal, btnEliminar);
            layout.getChildren().add(card);

            total += item.getTotalPrice();
        }

        Label lblTotal = new Label("Total carrito: $" + String.format("%.2f", total));
        lblTotal.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Button btnGenerarOrden = new Button("Generar Orden");
        btnGenerarOrden.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
        btnGenerarOrden.setOnAction(e -> {
            try {
                orderService.generateOrderFromCart(currentUser);
                showAlert("Orden generada correctamente");
                stage.close();
            } catch (Exception ex) {
                showAlert("Error al generar orden: " + ex.getMessage());
            }
        });

        layout.getChildren().addAll(lblTotal, btnGenerarOrden);

        scrollPane.setContent(layout);
        Scene scene = new Scene(scrollPane, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Mi Carrito");
        stage.show();
    }

    private void showLastOrderVisual() {
        Order order = orderService.getLastOrderFX(currentUser);
        if (order == null) {
            showAlert("No hay órdenes para mostrar");
            return;
        }

        Stage stage = new Stage();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        for (CartItem item : order.getProductsList()) {
            VBox card = new VBox(5);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-background-color: #fff9c4;");

            Label lblName = new Label(item.getProduct().getName());
            lblName.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            Label lblQty = new Label("Cantidad: " + item.getQuantity());
            Label lblSubtotal = new Label("Subtotal: $" + String.format("%.2f", item.getTotalPrice()));

            card.getChildren().addAll(lblName, lblQty, lblSubtotal);
            layout.getChildren().add(card);
        }

        Label lblTotal = new Label("Total orden: $" + String.format("%.2f", order.getTotalPrice()));
        lblTotal.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        layout.getChildren().add(lblTotal);

        scrollPane.setContent(layout);
        Scene scene = new Scene(scrollPane, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Última Orden");
        stage.show();
    }

    private void showAllOrdersVisual() {
        List<Order> orders = orderService.getAllOrdersFX(currentUser);
        if (orders.isEmpty()) {
            showAlert("No hay órdenes para mostrar");
            return;
        }

        Stage stage = new Stage();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        for (Order order : orders) {
            VBox card = new VBox(5);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-background-color: #c8e6c9;");

            Label lblHeader = new Label("Orden #" + order.getNumOrder() + " - Total: $" + String.format("%.2f", order.getTotalPrice()));
            lblHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            card.getChildren().add(lblHeader);

            for (CartItem item : order.getProductsList()) {
                Label lblItem = new Label(item.getProduct().getName() + " x" + item.getQuantity() +
                        " → $" + String.format("%.2f", item.getTotalPrice()));
                card.getChildren().add(lblItem);
            }

            layout.getChildren().add(card);
        }

        scrollPane.setContent(layout);
        Scene scene = new Scene(scrollPane, 450, 500);
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
