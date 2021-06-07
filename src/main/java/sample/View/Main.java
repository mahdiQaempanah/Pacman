package sample.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.Model.Coordinates;


public class Main extends Application {
    public static final Coordinates stageSize = new Coordinates(1950, 1030);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/WelcomePage.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setWidth(stageSize.getX());
        primaryStage.setHeight(stageSize.getY());
        primaryStage.setTitle("Pacman Game");
        primaryStage.getIcons().add(new Image(getClass().getResource("../../NotCode/Image/icon.png").toExternalForm()));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
