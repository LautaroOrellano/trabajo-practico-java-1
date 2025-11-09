package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.controllers.LoginController;

public class MainFX extends Application {

    private Stage primaryStage;
    private StackPane root = new StackPane();
    private LoginController loginController;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // Creamos el login
        loginController = new LoginController(this);
        root.getChildren().setAll(loginController.getView());

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("E-commerce UTN");
        primaryStage.show();
    }

    public void setScreen(StackPane pane) {
        root.getChildren().setAll(pane);
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public static void main(String[] args) {
        launch();
    }
}
