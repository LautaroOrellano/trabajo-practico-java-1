package ui.controllers;

import models.Product;
import models.users.User;
import service.MenuService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.MainFX;

public class CustomerController {

    private StackPane view = new StackPane();
    private MenuService menuManager = new MenuService();
    private User currentUser;

    public CustomerController(MainFX mainApp, User user) {
        this.currentUser = user;

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label lblWelcome = new Label("Bienvenido: " + currentUser.getName());
        lblWelcome.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // --- BOTONES DEL MENÚ ---
        Button btn1 = new Button("[1] Ver lista de productos");
        btn1.setMaxWidth(Double.MAX_VALUE);
        btn1.setOnAction(e -> menuManager.getProductManager().getAllProductsCustom());

        Button btn2 = new Button("[2] Buscar producto");
        btn2.setMaxWidth(Double.MAX_VALUE);
        btn2.setOnAction(e -> buscarProducto());

        Button btn3 = new Button("[3] Agregar producto al carrito");
        btn3.setMaxWidth(Double.MAX_VALUE);
        btn3.setOnAction(e -> agregarAlCarrito());

        Button btn4 = new Button("[4] Ver mi carrito");
        btn4.setMaxWidth(Double.MAX_VALUE);
        btn4.setOnAction(e -> menuManager.getUserManager().getProductsToMeCart(currentUser.getId()));

        Button btn5 = new Button("[5] Eliminar producto del carrito");
        btn5.setMaxWidth(Double.MAX_VALUE);
        btn5.setOnAction(e -> eliminarDelCarrito());

        Button btn6 = new Button("[6] Vaciar carrito");
        btn6.setMaxWidth(Double.MAX_VALUE);
        btn6.setOnAction(e -> menuManager.getUserManager().clearMeCart(currentUser.getId()));

        Button btn7 = new Button("[7] Realizar compra");
        btn7.setMaxWidth(Double.MAX_VALUE);
        btn7.setOnAction(e -> menuManager.getOrderManager().generateOrderFromCart(currentUser));

        Button btn8 = new Button("[8] Ver ultima compra");
        btn8.setMaxWidth(Double.MAX_VALUE);
        btn8.setOnAction(e -> menuManager.getOrderManager().getMeOrder(currentUser));

        Button btn9 = new Button("[9] Ver todas mis compras");
        btn9.setMaxWidth(Double.MAX_VALUE);
        btn9.setOnAction(e -> menuManager.getOrderManager().getAllOrder(currentUser));

        Button btn0 = new Button("[0] Salir del programa");
        btn0.setMaxWidth(Double.MAX_VALUE);
        btn0.setOnAction(e -> mainApp.setScreen(mainApp.getLoginController().getView()));

        layout.getChildren().addAll(
                lblWelcome, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0
        );

        view.getChildren().add(layout);
    }

    public StackPane getView() {
        return view;
    }

    // ---------- MÉTODOS AUXILIARES ----------
    private void buscarProducto() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Ingrese el nombre del producto a buscar:");
        dialog.showAndWait().ifPresent(name -> {
            menuManager.getProductManager().searchProductByName(name);
        });
    }

    private void agregarAlCarrito() {
        // Mostrar lista de productos con opción
        menuManager.getProductManager().showAllProductsWithOption();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Ingrese el número del producto a agregar al carrito:");
        dialog.showAndWait().ifPresent(productIndexStr -> {
            try {
                int index = Integer.parseInt(productIndexStr) - 1;
                Product selected = menuManager.getProductManager().getProductByIndex(index);

                if (selected == null) {
                    mostrarMensaje("Producto inválido.");
                    return;
                }

                TextInputDialog qtyDialog = new TextInputDialog();
                qtyDialog.setHeaderText("Ingrese la cantidad a agregar:");
                qtyDialog.showAndWait().ifPresent(qtyStr -> {
                    try {
                        int qty = Integer.parseInt(qtyStr);
                        if (qty <= 0) {
                            mostrarMensaje("Cantidad debe ser mayor a cero.");
                        } else if (qty > selected.getStock()) {
                            mostrarMensaje("Stock insuficiente. Disponible: " + selected.getStock());
                        } else {
                            menuManager.getUserManager().addProductToCart(currentUser.getId(), selected, qty);
                            mostrarMensaje("✅ Producto agregado: " + selected.getName() + " x" + qty);
                        }
                    } catch (NumberFormatException ex) {
                        mostrarMensaje("Cantidad inválida.");
                    }
                });

            } catch (NumberFormatException ex) {
                mostrarMensaje("Opción inválida.");
            }
        });
    }

    private void eliminarDelCarrito() {
        menuManager.getUserManager().getProductsToMeCart(currentUser.getId());

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Ingrese el número del producto a eliminar:");
        dialog.showAndWait().ifPresent(productIndexStr -> {
            try {
                int index = Integer.parseInt(productIndexStr) - 1;

                TextInputDialog qtyDialog = new TextInputDialog();
                qtyDialog.setHeaderText("Ingrese la cantidad a eliminar:");
                qtyDialog.showAndWait().ifPresent(qtyStr -> {
                    try {
                        int qty = Integer.parseInt(qtyStr);
                        menuManager.getUserManager().deleteProductToCart(currentUser.getId(), index, qty);
                        mostrarMensaje("Producto eliminado correctamente.");
                    } catch (NumberFormatException ex) {
                        mostrarMensaje("Cantidad inválida.");
                    }
                });

            } catch (NumberFormatException ex) {
                mostrarMensaje("Opción inválida.");
            }
        });
    }

    private void mostrarMensaje(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
