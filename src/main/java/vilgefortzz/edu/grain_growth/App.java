package vilgefortzz.edu.grain_growth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Created by vilgefortzz on 06/10/18
 */
public class App extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL layout = getClass().getClassLoader().getResource("scene.fxml");
        if (layout == null) { throw new Exception("Cannot load a scene layout"); }

        Parent root = FXMLLoader.load(layout);

        primaryStage.setTitle("Grain growth");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
