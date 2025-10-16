package ui.controllers;

import clases.entidades.users.Admin;
import clases.entidades.users.Customer;
import clases.entidades.users.User;
import clases.gestoras.AuthManager;
import enums.Rol;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ui.MainFX;

import java.util.ArrayList;

public class LoginController {

    private StackPane view = new StackPane();
    private AuthManager authManager = new AuthManager(new ArrayList<>());
    private MainFX mainApp;

    public LoginController(MainFX mainApp) {
        this.mainApp = mainApp;
        authManager.register(new Admin("Lautaro", "Orellano", "lautaro@gmail.com", "1234"));
        authManager.register(new Customer("fran", "roldan", "fran@gmail.com", "abcd",
                12345678L, 123456789L, "Calle Falsa 123", 25));

        TextField emailField = new TextField();
        PasswordField passField = new PasswordField();
        Button btnLogin = new Button("Login");
        Label lblMessage = new Label();

        VBox layout = new VBox(10, new Label("Email:"), emailField,
                new Label("Password:"), passField, btnLogin, lblMessage);
        layout.setPadding(new Insets(20));

        btnLogin.setOnAction(e -> {
            User user = authManager.login(emailField.getText(), passField.getText());
            if (user == null) {
                lblMessage.setText("Credenciales incorrectas");
            } else {
                // Redirige según rol
                if (user.getRol() == Rol.ADMIN) {
                    AdminController admin = new AdminController(mainApp, user);
                    mainApp.setScreen(admin.getView());
                } else {
                    CustomerController customer = new CustomerController(mainApp, user);
                    mainApp.setScreen(customer.getView());
                }
            }
        });

        view.getChildren().add(layout);
    }

    public StackPane getView() {
        return view;
    }
}
