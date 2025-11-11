package ux.controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Product;
import models.users.User;
import service.ProductService;
import service.UserService;

import java.util.List;

public class CustomerVisualController {
    private User currentUser;
    private UserService userService;
    private ProductService productService;

    public CustomerVisualController(User user, UserService userService, ProductService productService) {
        this.currentUser = user;
        this.userService = userService;
        this.productService = productService;
    }

    public void showProductsVisual() {
        List<Product> products = productService.getProductsFX();
        if (products.isEmpty()) {
            System.out.println("No hay productos disponibles");
            return;
        }

        Stage stage = new Stage();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        int columns = 3;
        int col = 0;
        int row = 0;

        for (Product p : products) {
            VBox card = new VBox(5);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-background-color: #f5f5f5;");

            Label lblName = new Label(p.getName());
            lblName.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            Label lblPrice = new Label("Precio: $" + String.format("%.2f", p.getPrice()));
            Label lblStock = new Label("Stock: " + p.getStock());

            Button btnAgregar = new Button("Agregar al carrito");
            btnAgregar.setOnAction(e -> {
                // Lógica de agregar al carrito (igual que antes)
            });

            card.getChildren().addAll(lblName, lblPrice, lblStock, btnAgregar);

            grid.add(card, col, row);
            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }

        scrollPane.setContent(grid);
        Scene scene = new Scene(scrollPane, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Productos Visual");
        stage.show();
    }
}
