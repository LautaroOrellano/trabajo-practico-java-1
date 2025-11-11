package ux.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.CartItem;
import models.Order;
import models.Product;
import models.users.User;
import service.OrderService;
import service.ProductService;
import service.UserService;
import ux.MainFX;

import java.io.File;
import java.util.List;

public class CustomerController {

    private final StackPane view = new StackPane();
    private final User currentUser;
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    public CustomerController(MainFX mainApp, User user,
                              UserService userService,
                              ProductService productService,
                              OrderService orderService) {

        this.currentUser = user;
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;

        // ====================== LAYOUT PRINCIPAL ======================
        BorderPane pageLayout = new BorderPane();
        pageLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #e0f7fa, #ffffff);");

        // ====================== HEADER ======================
        VBox headerContainer = new VBox();
        headerContainer.setStyle("-fx-background-color: #0288d1;");
        headerContainer.setPrefHeight(120);

        // ---------- PRIMERA FILA (bienvenida | título | menú) ----------
        HBox headerTop = new HBox();
        headerTop.setPadding(new Insets(12, 20, 12, 20));
        headerTop.setSpacing(10);
        headerTop.setAlignment(Pos.CENTER);
        headerTop.setStyle("-fx-background-color: #0288d1;");

        // Elementos del header superior
        Label lblWelcome = new Label("Bienvenido: " + currentUser.getName());
        lblWelcome.setStyle("-fx-text-fill: white; -fx-font-size: 15; -fx-font-weight: bold;");

        Label lblTitle = new Label("Ecommerce UTN");
        lblTitle.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-weight: bold;");

        Button btnMenu = new Button("☰");
        btnMenu.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: white;
            -fx-font-size: 22;
            -fx-font-weight: bold;
            -fx-cursor: hand;
        """);

        // Espaciadores dinámicos para mantener proporciones
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        // Estructura: [Bienvenido] --- [Título centrado] --- [Botón Menú]
        headerTop.getChildren().addAll(lblWelcome, leftSpacer, lblTitle, rightSpacer, btnMenu);
        VBox.setMargin(headerTop, new Insets(0, 0, 5, 0)); // pequeño margen inferior

        // ---------- NAVBAR ----------
        HBox navBar = new HBox(40);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(10));
        navBar.setStyle("-fx-background-color: #0277bd; -fx-border-color: #01579b; -fx-border-width: 0 0 2 0;");

        // Etiquetas del menú de navegación
        Label lblInicio = new Label("Inicio");
        Label lblProductos = new Label("Productos");
        Label lblContacto = new Label("Contacto");

        // Estilo común y efecto hover
        for (Label nav : new Label[]{lblInicio, lblProductos, lblContacto}) {
            nav.setStyle("-fx-text-fill: white; -fx-font-size: 15; -fx-font-weight: bold; -fx-cursor: hand;");
            nav.setOnMouseEntered(e ->
                    nav.setStyle("-fx-text-fill: #ffeb3b; -fx-font-size: 15; -fx-font-weight: bold; -fx-cursor: hand;")
            );
            nav.setOnMouseExited(e ->
                    nav.setStyle("-fx-text-fill: white; -fx-font-size: 15; -fx-font-weight: bold; -fx-cursor: hand;")
            );
        }

        navBar.getChildren().addAll(lblInicio, lblProductos, lblContacto);

        // ---------- ENSAMBLAR TODO ----------
        headerContainer.getChildren().addAll(headerTop, navBar);
        pageLayout.setTop(headerContainer);

        // ====================== CONTENIDO CENTRAL ======================
        VBox contentContainer = new VBox(20);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.setPadding(new Insets(30));

        Label title = new Label("Productos disponibles");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #0288d1;");

        GridPane productGrid = new GridPane();
        productGrid.setHgap(20);
        productGrid.setVgap(20);
        productGrid.setAlignment(Pos.CENTER);

        // ================== PRODUCTOS SIMULADOS ==================
        String[] productNames = {"Teclado Mecánico", "Mouse Gamer", "Auriculares", "Monitor 24\""};
        String[] productPrices = {"$45.000", "$30.000", "$25.000", "$120.000"};
        String[] imagePaths = {
                "images/teclado.webp",
                "https://via.placeholder.com/150x100.png?text=Mouse",
                "https://via.placeholder.com/150x100.png?text=Auriculares",
                "https://via.placeholder.com/150x100.png?text=Monitor"
        };

        // ================== GRID DE PRODUCTOS ==================
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // ================== CREACIÓN DE CARDS ==================
        for (int i = 0; i < productNames.length; i++) {
            VBox card = new VBox(10);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(15));
            card.setPrefSize(200, 250);
            card.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 10; -fx-background-radius: 10;");

            // --- Imagen ---
            ImageView imageView = new ImageView();
            imageView.setFitWidth(150);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);

            String path = imagePaths[i];

            try {
                if (path.startsWith("http")) {
                    imageView.setImage(new Image(path, false));
                } else {
                    File imageFile = new File(System.getProperty("user.dir") + File.separator + path);
                    System.out.println("🔍 Buscando imagen en: " + imageFile.getAbsolutePath());
                    if (imageFile.exists()) {
                        imageView.setImage(new Image(imageFile.toURI().toString()));
                    } else {
                        System.out.println("⚠ No se encontró: " + imageFile.getAbsolutePath());
                        // Imagen por defecto
                        imageView.setImage(new Image("https://via.placeholder.com/150x100.png?text=Sin+Imagen"));
                    }
                }
            } catch (Exception e) {
                System.out.println("❌ Error al cargar imagen: " + e.getMessage());
                imageView.setImage(new Image("https://via.placeholder.com/150x100.png?text=Error"));
            }

            // --- Texto ---
            Label name = new Label(productNames[i]);
            name.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            Label price = new Label(productPrices[i]);
            price.setTextFill(Color.DARKBLUE);

            // --- Botón ---
            Button buyButton = new Button("Agregar al carrito");
            buyButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-background-radius: 8;");
            final String productName = productNames[i];
            buyButton.setOnAction(e -> System.out.println("🛒 Producto agregado: " + productName));

            // --- Armar card ---
            card.getChildren().addAll(imageView, name, price, buyButton);

            // --- Agregar al grid ---
            grid.add(card, i % 2, i / 2); // 2 columnas
        }

        // Centrar todo el grid en la pantalla
        Label pageTitle = new Label("Productos disponibles");
        pageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        pageTitle.setTextFill(Color.web("#0288d1"));

        VBox mainContent = new VBox(30, pageTitle, grid);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(30));
        mainContent.setStyle("-fx-background-color: #f0f0f0;");

        // Mostrar en tu contenedor principal (por ejemplo, 'placeholder')
        pageLayout.setCenter(mainContent);

        // ====================== MENÚ DESPLEGABLE ======================
        VBox menuContent = new VBox(8);
        menuContent.setPadding(new Insets(10));
        menuContent.setAlignment(Pos.TOP_RIGHT);
        menuContent.setStyle("""
            -fx-background-color: white;
            -fx-border-color: lightgray;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);
        """);
        menuContent.setVisible(false);

        Button btnVerProductos = new Button("Ver Productos");
        Button btnVerCarrito = new Button("Ver Carrito");
        Button btnUltimaCompra = new Button("Última Compra");
        Button btnTodasCompras = new Button("Todas Mis Compras");
        Button btnCerrarSesion = new Button("Cerrar Sesión");

        Button[] menuButtons = {btnVerProductos, btnVerCarrito, btnUltimaCompra, btnTodasCompras, btnCerrarSesion};
        for (Button b : menuButtons) {
            b.setMaxWidth(Double.MAX_VALUE);
            b.setStyle("""
                -fx-background-color: #0288d1;
                -fx-text-fill: white;
                -fx-background-radius: 5;
                -fx-font-weight: bold;
            """);
        }

        // === Conectar con tus métodos funcionales ===
        btnVerProductos.setOnAction(e -> showAllProductsVisual());
        btnVerCarrito.setOnAction(e -> showCartVisual());
        btnUltimaCompra.setOnAction(e -> showLastOrderVisual());
        btnTodasCompras.setOnAction(e -> showAllOrdersVisual());
        btnCerrarSesion.setOnAction(e -> mainApp.setScreen(mainApp.getLoginController().getView()));

        menuContent.getChildren().addAll(menuButtons);
        btnMenu.setOnAction(e -> menuContent.setVisible(!menuContent.isVisible()));

        StackPane mainStack = new StackPane(pageLayout, menuContent);
        StackPane.setAlignment(menuContent, Pos.TOP_RIGHT);
        StackPane.setMargin(menuContent, new Insets(60, 20, 0, 0));

        view.getChildren().add(mainStack);
    }

    // ====================== MÉTODOS REALES ======================

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
                    showAlert("Producto agregado: " + p.getName() + " x" + qty);
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
        stage.setScene(new Scene(scrollPane, 400, 500));
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
            card.setStyle("-fx-border-color: gray; -fx-background-color: #e0f7fa;");

            Label lblName = new Label(item.getProduct().getName());
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
        stage.setScene(new Scene(scrollPane, 400, 400));
        stage.setTitle("Carrito");
        stage.show();
    }

    private void showLastOrderVisual() {
        Order order = orderService.getLastOrderFX(currentUser);
        if (order == null) {
            showAlert("No hay órdenes para mostrar");
            return;
        }

        Stage stage = new Stage();
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        for (CartItem item : order.getProductsList()) {
            VBox card = new VBox(5);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-border-color: gray; -fx-background-color: #fff9c4;");
            card.getChildren().addAll(
                    new Label(item.getProduct().getName()),
                    new Label("Cantidad: " + item.getQuantity()),
                    new Label("Subtotal: $" + String.format("%.2f", item.getTotalPrice()))
            );
            layout.getChildren().add(card);
        }

        layout.getChildren().add(new Label("Total orden: $" + String.format("%.2f", order.getTotalPrice())));
        scroll.setContent(layout);
        stage.setScene(new Scene(scroll, 400, 400));
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
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        for (Order order : orders) {
            VBox card = new VBox(5);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-border-color: gray; -fx-background-color: #c8e6c9;");
            Label lblHeader = new Label("Orden #" + order.getNumOrder() + " - Total: $" + String.format("%.2f", order.getTotalPrice()));
            lblHeader.setStyle("-fx-font-weight: bold;");
            card.getChildren().add(lblHeader);

            for (CartItem item : order.getProductsList()) {
                card.getChildren().add(
                        new Label(item.getProduct().getName() + " x" + item.getQuantity() +
                                " → $" + String.format("%.2f", item.getTotalPrice()))
                );
            }
            layout.getChildren().add(card);
        }

        scroll.setContent(layout);
        stage.setScene(new Scene(scroll, 450, 500));
        stage.setTitle("Todas las Órdenes");
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public StackPane getView() {
        return view;
    }
}
