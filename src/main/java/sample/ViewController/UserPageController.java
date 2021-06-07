package sample.ViewController;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Controller.MainController;

import java.io.IOException;

public class UserPageController {
    public Label resultLabel;


    public void handleDeleteAccount(ActionEvent actionEvent) {
        Stage stage = new Stage();
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(12);
        HBox buttons = new HBox();
        buttons.setSpacing(260.0);
        Label label = new Label("Are you sure?");
        Button yes = new Button("Yes");
        Button no = new Button("no");


        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    MainController.getInstance().deleteLoggingAccount();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                stage.close();
                resultLabel.setStyle("-fx-background-color: green;");
                resultLabel.setTextFill(Color.web("White"));
                resultLabel.setText("account removed successfully");
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), (ActionEvent e) -> {
                    try {
                        handleLogout(actionEvent);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }));
                timeline.play();
            }
        });

        buttons.getChildren().addAll(label, no, yes);
        root.add(label, 0, 0);
        root.add(buttons, 0, 2, 2, 1);

        stage.setScene(new Scene(root));
        stage.setTitle("warning");
        stage.show();
    }

    public void handleLogout(ActionEvent actionEvent) throws IOException {
        MainController.getInstance().logout();
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/WelcomePage.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void playGame(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../NotCode/Fxml/ChooseGamePage.fxml"));
        Parent root = loader.load();
        ChooseGamePageController controller = (ChooseGamePageController) loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
        controller.judgeButton();
    }

    public void showScoreBoard(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../NotCode/Fxml/ScoreBoardPage.fxml"));
        Parent root = loader.load();
        ScoreBoardPageController controller = (ScoreBoardPageController) loader.getController();
        stage.setScene(new Scene(root));
        controller.updateScoreBoard();
        stage.show();
    }

    public void changePassword(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/ChangePassword.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
