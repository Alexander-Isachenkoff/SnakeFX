package snake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/main.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

}
