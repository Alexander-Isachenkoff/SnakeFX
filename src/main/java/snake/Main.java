package snake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public static void setRoot(Parent parent) {
        stage.getScene().setRoot(parent);
    }

    static void toLevels() {
        loadRoot("fxml/levels.fxml");
    }

    static void toMainMenu() {
        loadRoot("fxml/menu.fxml");
    }

    static void toLevelEditor() {
        loadRoot("fxml/editor.fxml");
    }

    private static void loadRoot(String fxml) {
        Parent parent;
        try {
            parent = new FXMLLoader(Main.class.getResource(fxml)).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setRoot(parent);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Main.stage = stage;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/menu.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

}
