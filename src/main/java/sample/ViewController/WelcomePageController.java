package sample.ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Model.BoardMapBarriers;

import java.io.IOException;


public class WelcomePageController {
    private Stage stage;


    public void goToLoginPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/LoginPage.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToRegisterPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/RegisterPage.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToGameWithoutLogin(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../NotCode/Fxml/DeterminationGamePropertyPage.fxml"));
        Parent root = loader.load();
        DeterminationGamePropertyPageController controller = (DeterminationGamePropertyPageController) loader.getController();
        stage.setScene(new Scene(root));
        controller.create(null);
    }

    public void handleExit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
