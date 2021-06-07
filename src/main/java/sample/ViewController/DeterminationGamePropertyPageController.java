package sample.ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;
import sample.Controller.MainController;
import sample.Model.Account;
import sample.Model.BoardMapBarriers;
import sample.Model.IntegerCoordinates;

import java.io.IOException;
import java.util.ArrayList;

public class DeterminationGamePropertyPageController {
    public Label pacmanLifeLabel;
    public ChoiceBox levelBox;
    public ChoiceBox mapBox;
    public AnchorPane mapPane;
    public Label warningLabel;
    private int endChoiceForMap = -1;
    private int endChoiceForLevel = -1;

    Account account;
    ArrayList<BoardMapBarriers> boardMapBarriers;

    public void create(Account account) {
        mapPane.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        levelBox.getItems().add("Easy");
        levelBox.getItems().add("Hard");
        this.account = account;

        if (this.account == null) {
            boardMapBarriers = new ArrayList<>();
        } else {
            boardMapBarriers = this.account.getBoardMapBarriers();
        }

        mapBox.getItems().add("new Map");
        for (int i = 0; i < boardMapBarriers.size(); i++)
            mapBox.getItems().add("Map " + i);

        mapBox.setOnAction((event) -> {
            try {
                showMap();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        levelBox.setOnAction((event) -> {
            if (((String) levelBox.getValue()).equals("Hard"))
                endChoiceForLevel = 1;
            else
                endChoiceForLevel = 0;
        });
    }

    public void increaseLife(ActionEvent actionEvent) {
        int life = Integer.valueOf(pacmanLifeLabel.getText());
        if (life == 5)
            return;
        life++;
        pacmanLifeLabel.setText(String.valueOf(life));
    }

    public void decreaseLife(ActionEvent actionEvent) {
        int life = Integer.valueOf(pacmanLifeLabel.getText());
        if (life == 2)
            return;
        life--;
        pacmanLifeLabel.setText(String.valueOf(life));
    }

    public void showMap() throws IOException {
        String value = (String) mapBox.getValue();
        BoardMapBarriers map;
        if (value.equals("new Map")) {
            boardMapBarriers.add(new BoardMapBarriers());
            if (account != null) {
                MainController.getInstance().saveChangeForAccount(account);
            }
            mapBox.getItems().add("Map " + String.valueOf(boardMapBarriers.size() - 1));
        } else {
            int id = Integer.valueOf(value.split(" ")[1]);
            endChoiceForMap = id;
            insertMapBarriersInPane(boardMapBarriers.get(id));
        }
    }

    private void insertMapBarriersInPane(BoardMapBarriers boardMapBarriers) {
        int cellSize = BoardMapBarriers.cellSize;
        mapPane.getChildren().clear();


        for (Pair<IntegerCoordinates, IntegerCoordinates> barrier : boardMapBarriers.getBoardMapBarriers()) {
            IntegerCoordinates key = barrier.getKey();
            IntegerCoordinates value = barrier.getValue();
            if (key.getX() > value.getX() || key.getY() > value.getY())
                continue;

            Line barrierLine = new Line();
            barrierLine.setStyle("-fx-stroke: white;");

            if (key.getX() < value.getX()) {
                double startY = cellSize * (key.getX() + 1) + barrierLine.getStrokeWidth() * key.getX();
                double startX = cellSize * (key.getY()) + barrierLine.getStrokeWidth() * key.getY();
                barrierLine.setStartX(startX);
                barrierLine.setStartY(startY);
                barrierLine.setEndX(startX + cellSize);
                barrierLine.setEndY(startY);
            } else {
                double startY = cellSize * (key.getX()) + barrierLine.getStrokeWidth() * key.getX();
                double startX = cellSize * (key.getY() + 1) + barrierLine.getStrokeWidth() * key.getY();
                barrierLine.setStartX(startX);
                barrierLine.setStartY(startY);
                barrierLine.setEndX(startX);
                barrierLine.setEndY(startY + cellSize);
            }
            mapPane.getChildren().add(barrierLine);
        }

    }

    public void startGame(ActionEvent actionEvent) throws IOException {
        if (endChoiceForMap == -1) {
            warningLabel.setText("please select map");
            return;
        }
        if (endChoiceForLevel == -1) {
            warningLabel.setText("please select Level");
            return;
        }

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../NotCode/Fxml/GamePage.fxml"));
        Parent root = loader.load();
        GamePageController controller = (GamePageController) loader.getController();
        stage.setScene(new Scene(root));
        boolean isHard = (endChoiceForLevel == 1);

        if (account == null) {
            controller.createGame(boardMapBarriers.get(endChoiceForMap), Integer.valueOf(pacmanLifeLabel.getText()), null, stage, 0, isHard);
        } else {
            controller.createGame(boardMapBarriers.get(endChoiceForMap), Integer.valueOf(pacmanLifeLabel.getText()), account, stage, 0, isHard);
        }
        stage.show();
    }

    public void handleExit(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        Parent root;
        if (account == null) {
            root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/WelcomePage.fxml"));
        } else {
            root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/UserPage.fxml"));
        }
        stage.setScene(new Scene(root));
    }
}
