package sample.ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.Controller.MainController;

import java.io.IOException;

public class ChooseGamePageController {

    public Button newGameButton;
    public Button oldGameButton;

    public void judgeButton() {
        if (!MainController.getInstance().hasLoggingAccountLoadGame())
            oldGameButton.setDisable(true);
    }

    public void handleExit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/UserPage.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void createNewGame(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../NotCode/Fxml/DeterminationGamePropertyPage.fxml"));
        Parent root = loader.load();
        DeterminationGamePropertyPageController controller = (DeterminationGamePropertyPageController) loader.getController();
        stage.setScene(new Scene(root));
        controller.create(MainController.getInstance().getLoggingUser());
    }

    public void loadOldGame(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../NotCode/Fxml/GamePage.fxml"));
        Parent root = loader.load();
        GamePageController controller = (GamePageController) loader.getController();
        stage.setScene(new Scene(root));
        controller.loadGame(stage, MainController.getInstance().getLoggingUser());
        stage.show();
    }
}
