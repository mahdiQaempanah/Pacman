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

public class LoginPageController {
    public Label resultMessage;
    private Stage stage;
    public TextField usernameField;
    public PasswordField passwordField;
    public Button registerButton;
    public Button backButton;


    public void handleLogin(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        ServerMessage serverMessage = MainController.getInstance().attemptForLogin(username, password);

        if (serverMessage.getType().equals(ServerMessage.error)) {
            resultMessage.setText("error: " + (String) serverMessage.getData().get("detail"));
        } else {
            backButton.setDisable(true);
            resultMessage.setStyle("-fx-background-color: green;");
            resultMessage.setTextFill(Color.web("White"));
            resultMessage.setText("Login completed successfully");

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), (ActionEvent event) -> {
                try {
                    goToUserPage(actionEvent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            timeline.play();
        }
    }

    private void goToUserPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/UserPage.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/WelcomePage.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


}
