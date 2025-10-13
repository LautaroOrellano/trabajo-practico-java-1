package ui.controllers;

import clases.entidades.users.User;
import clases.gestoras.MenuManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import ui.MainFX;

public class CustomerController {

    private StackPane view = new StackPane();
    private MenuManager menuManager = new MenuManager();
    private User currentUser;

    public CustomerController(MainFX mainApp, User user) {
        this.currentUser = user;

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label lblWelcome = new Label("Bienvenido: " + currentUser.getName());

        // --- BOTONES DE MENU ---
        Button btnVerProductos = new Button("Ver Productos");
        btnVerProductos.setOnAction(e -> listarProductos());

        Button btnBuscarProducto = new Button("Buscar Producto por ID");
        btnBuscarProducto.setOnAction(e -> buscarProducto());

        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setOnAction(e -> mainApp.setScreen(mainApp.getLoginController().getView()));

        layout.getChildren().addAll(lblWelcome, btnVerProductos, btnBuscarProducto, btnCerrarSesion);
        view.getChildren().add(layout);
    }

    public StackPane getView() {
        return view;
    }

    // ---------- METODOS DE ACCIÓN ----------
    private void listarProductos() {
        mostrarMensaje(menuManager.getProductManager().getProducts().toString());
    }

    private void buscarProducto() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Ingrese ID del producto a buscar:");
        dialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                mostrarMensaje(menuManager.getProductManager().searchProductFX(id).toString());
            } catch (Exception ex) {
                mostrarMensaje("ID inválido o producto no encontrado.");
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
