package sample.ViewController;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Controller.MainController;
import sample.Model.ServerMessage;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterPageController {
    public PasswordField passwordField;
    public TextField usernameField;
    public Button registerButton;
    public Button backButton;
    public Label resultMessage;

    private Stage stage;

    public void show(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/sample.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void handleRegister(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        ServerMessage serverMessage = MainController.getInstance().attemptForRegister(username, password);

        if (serverMessage.getType().equals(ServerMessage.error)) {
            resultMessage.setText("error: " + (String) serverMessage.getData().get("detail"));
        } else {
            backButton.setDisable(true);
            resultMessage.setStyle("-fx-background-color: green;");
            resultMessage.setTextFill(Color.web("White"));
            resultMessage.setText("Registration completed successfully");

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), (ActionEvent event) -> {
                try {
                    handleBack(actionEvent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            timeline.play();
        }

    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/WelcomePage.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
