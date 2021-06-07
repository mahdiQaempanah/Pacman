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

public class ChangePassWordController {
    public PasswordField newPasswordField;
    public TextField oldPasswordField;
    public Label resultMessage;
    public Button backButton;

    public void handleChangePassword(ActionEvent actionEvent) throws IOException {
        ServerMessage serverMessage = MainController.getInstance().changePassword(oldPasswordField.getText(), newPasswordField.getText());
        if (serverMessage.getType().equals(ServerMessage.error)) {
            resultMessage.setText((String) serverMessage.getData().get("reason"));
        } else {
            backButton.setDisable(true);
            resultMessage.setStyle("-fx-background-color: green;");
            resultMessage.setTextFill(Color.web("White"));
            resultMessage.setText("change Password completed successfully");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), (ActionEvent event) -> {
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
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/UserPage.fxml"));
        stage.setScene(new Scene(root));
    }
}
