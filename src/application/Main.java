package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.SceneLoader;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    	   System.out.println("APPLICATION JAVA LANCEE ");
        SceneLoader.setStage(stage);

      FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/ui/login.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
      stage.setTitle("Connexion - Jeu de Mémoire");

        stage.setMaximized(true);
        stage.setResizable(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
