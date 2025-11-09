package ui.controllers;

import models.users.Admin;
import models.users.Customer;
import models.users.User;
import repository.OrderRepository;
import repository.ProductRepository;
import service.AuthService;
import enums.Rol;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import repository.UserRepository;
import service.OrderService;
import service.ProductService;
import service.UserService;
import ui.MainFX;

public class LoginController {

    private StackPane view = new StackPane();
    private ProductRepository productRepository = new ProductRepository();
    private UserRepository userRepository = new UserRepository(productRepository);
    private AuthService authManager = new AuthService(userRepository);
    OrderRepository orderRepository = new OrderRepository();
    private MainFX mainApp;

    private UserService userService;
    private ProductService productService;
    private OrderService orderService;

    public LoginController(MainFX mainApp) {
        this.mainApp = mainApp;

        this.userService = new UserService(userRepository, productRepository);
        this.productService = new ProductService(productRepository);
        this.orderService = new OrderService(orderRepository, userRepository, productRepository);

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
                    CustomerController customer = new CustomerController(
                            mainApp,
                            user,
                            userService,
                            productService,
                            orderService
                    );
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
