package ui.controllers;

import clases.entidades.Product;
import clases.entidades.users.User;
import clases.gestoras.MenuManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import ui.MainFX;

public class AdminController {

    private StackPane view = new StackPane();
    private MenuManager menuManager = new MenuManager();
    private User currentUser;

    public AdminController(MainFX mainApp, User user) {
        this.currentUser = user;

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label lblWelcome = new Label("Bienvenido Admin: " + currentUser.getName());

        // --- BOTONES DE MENU ---
        Button btnCrearProducto = new Button("Crear Producto");
        btnCrearProducto.setOnAction(e -> crearProducto());

        Button btnBuscarProducto = new Button("Buscar Producto por ID");
        btnBuscarProducto.setOnAction(e -> buscarProducto());

        Button btnListarProductos = new Button("Listar Productos");
        btnListarProductos.setOnAction(e -> listarProductos());

        Button btnActualizarProductos = new Button("Actualizar Producto");
        btnActualizarProductos.setOnAction(e -> actualizarProducto());

        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setOnAction(e -> mainApp.setScreen(mainApp.getLoginController().getView()));

        layout.getChildren().addAll(lblWelcome, btnCrearProducto, btnListarProductos, btnBuscarProducto,
                btnActualizarProductos, btnCerrarSesion);
        view.getChildren().add(layout);
    }

    public StackPane getView() {
        return view;
    }

    // ---------- METODOS DE ACCIÓN ----------
    private void crearProducto() {

        Stage ventana = new Stage();
        VBox layout = new VBox(10);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");

        TextField txtDescripcion = new TextField();
        txtDescripcion.setPromptText("Descripción");

        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Precio");

        TextField txtStock = new TextField();
        txtStock.setPromptText("Stock");

        Button btnCrear = new Button("Crear Producto");
        btnCrear.setOnAction(e -> {
            String name = txtNombre.getText();
            String description = txtDescripcion.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int stock = Integer.parseInt(txtStock.getText());

            menuManager.getProductManager().createProduct(name, description, precio, stock);
            mostrarMensaje("Producto creado: " + name);
            ventana.close();
        });

        layout.getChildren().addAll(txtNombre, txtDescripcion, txtPrecio, txtStock, btnCrear);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 300, 250);
        ventana.setScene(scene);
        ventana.setTitle("Crear Producto");
        ventana.show();
    }

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

    private void actualizarProducto() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Ingrese ID del producto a actualizar:");
        dialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                Product producto = menuManager.getProductManager().searchProductFX(id);

                if (producto == null) {
                    mostrarMensaje("Producto con ID " + id + " no encontrado.");
                    return;
                }

                // Crear ventana con formulario de actualización
                Stage ventana = new Stage();
                VBox layout = new VBox(10);

                TextField txtNombre = new TextField(producto.getName());
                txtNombre.setPromptText("Nombre nuevo");

                TextField txtDescripcion = new TextField(producto.getDescription());
                txtDescripcion.setPromptText("Descripción nueva");

                TextField txtPrecio = new TextField(String.valueOf(producto.getPrice()));
                txtPrecio.setPromptText("Precio nuevo");

                TextField txtStock = new TextField(String.valueOf(producto.getStock()));
                txtStock.setPromptText("Stock nuevo");

                Button btnActualizar = new Button("Actualizar Producto");
                btnActualizar.setOnAction(e -> {
                    String name = txtNombre.getText();
                    String description = txtDescripcion.getText();
                    double precio = Double.parseDouble(txtPrecio.getText());
                    int stock = Integer.parseInt(txtStock.getText());

                    menuManager.getProductManager().updateProduct(id, name, description, precio, stock);
                    mostrarMensaje("Producto actualizado: " + name);
                    ventana.close();
                });

                layout.getChildren().addAll(txtNombre, txtDescripcion, txtPrecio, txtStock, btnActualizar);
                layout.setPadding(new Insets(20));

                Scene scene = new Scene(layout, 300, 250);
                ventana.setScene(scene);
                ventana.setTitle("Actualizar Producto");
                ventana.show();

            } catch (NumberFormatException ex) {
                mostrarMensaje("ID inválido.");
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
