import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

        private Stage primaryStage;
        private StackPane root = new StackPane();

        @Override
        public void start(Stage stage) {
            this.primaryStage = stage;

            LoginController loginController = new LoginController(this);
            root.getChildren().setAll(loginController.getView());

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("E-commerce KeyLab");
            primaryStage.show();
        }

        public void setScreen(StackPane pane) {
            root.getChildren().setAll(pane);
        }

    public static void main(String[] args) {
        launch();
    }
}