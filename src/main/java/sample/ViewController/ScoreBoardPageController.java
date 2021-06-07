package sample.ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.Controller.MainController;
import sample.Model.Account;
import sample.Model.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;

public class ScoreBoardPageController {
    public HBox scoreBoardView;

    public void handleBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/UserPage.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void reloadScoreBoard(ActionEvent actionEvent) throws IOException {
        updateScoreBoard();
    }

    public void updateScoreBoard() throws IOException {
        ServerMessage serverMessage = MainController.getInstance().getTopUsers(10);
        ListView listView = new ListView();
        listView.getItems().clear();
        ArrayList<Account> topUsers = (ArrayList<Account>) serverMessage.getData().get("topUsers");

        int rank = 0;
        for (int i = 0; i < topUsers.size(); i++) {
            if (i == 0 || topUsers.get(i).getMaxScore() != topUsers.get(i - 1).getMaxScore())
                rank++;
            listView.getItems().add(rank + ". " + topUsers.get(i).getUsername() + " : " + topUsers.get(i).getMaxScore());
        }

        scoreBoardView.getChildren().clear();
        scoreBoardView.getChildren().add(listView);
    }
}
