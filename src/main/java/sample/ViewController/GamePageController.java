package sample.ViewController;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.util.Pair;
import sample.Controller.GameController;
import sample.Controller.MainController;
import sample.Model.*;
import sample.Model.GameObjects.*;
import sample.Model.GraphicObject.GhostGf;
import sample.Model.GraphicObject.PacmanGf;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.Model.GraphicObject.SoundController;
import sample.Model.Tools.GraphicTool;
import sample.Model.Tools.SmallUsefullTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GamePageController {
    public Button pauseButton;
    public Button exitButton;
    private Stage stage;
    public Label scoreLabel;
    public Label chanceLabel;
    public AnchorPane boardPane;
    private GameController gameController;

    public GhostGf[] ghosts = new GhostGf[4];
    public PacmanGf pacman = new PacmanGf();

    public Rectangle[][] cells;
    private double cellSize;

    private double barrierThickness;
    private Timeline timerForHunterPacman;
    private SoundController soundController = new SoundController();

    public void createGame(BoardMapBarriers boardMapBarriers, int cntChances, Account account, Stage stage, int score, boolean isHard) {
        gameController = new GameController(boardMapBarriers, cntChances, account, score, isHard);
        firstChangeInComponents(stage);
        insertMapBarriersToPane(boardMapBarriers);
        insertItemToCells();
        insertCellsToPane();
        insertGhostsAndPacmanToPane();
        startGame();
    }

    public void loadGame(Stage stage, Account loggingUser) {
        Game game = loggingUser.getLastGame();
        gameController = new GameController(game, loggingUser);
        firstChangeInComponents(stage);
        insertMapBarriersToPane(gameController.getGame().getBoardMapBarriers());
        insertCellsToPane();
        insertGhostsAndPacmanToPane();
        if (game.getPacman().getType() == PacmanType.PACMAN_HUNTER) {
            timerForHunterPacman = new Timeline(new KeyFrame(game.getUntilPacmanPreyMode(), (ActionEvent event) -> {
                changeGameModeToPreyPacmanMode();
                timerForHunterPacman = null;
            }));
            timerForHunterPacman.play();
        }
        startGame();

    }

    private void firstChangeInComponents(Stage stage) {
        loopForSong();
        this.cellSize = BoardMapBarriers.cellSize;
        this.stage = stage;
        this.scoreLabel.setText(String.valueOf(gameController.getGame().getScore()));
        exitButton.setDisable(true);
        chanceLabel.setText(String.valueOf(gameController.getGame().getPacmanLife()));
        boardPane.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        cells = new Rectangle[BoardMapBarriers.boardSize.getX()][BoardMapBarriers.boardSize.getY()];
    }

    private void startGame() {
        if (gameController.getGame().isHard())
            gameController.getGame().getShortestPathBetweenCells();//hard select

        stage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!pauseButton.getText().equals("Pause"))
                    return;
                MoveType tmp;
                if ((tmp = GraphicTool.translateKeyCodeToMoveType(event.getCode())) != null) {
                    try {
                        setPacmanFutureDirection(tmp);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        for (GhostGf ghost : ghosts) {
            ghost.setWaitUntilMove(new Timeline(new KeyFrame(Duration.seconds(2), (ActionEvent event) -> {
                ghost.setWaitUntilMove(null);
                startMoveForGhostAfterNotAvailable(ghost);
            })));
            ghost.getWaitUntilMove().play();
        }
    }

    private void insertMapBarriersToPane(BoardMapBarriers boardMapBarriers) {

        for (Pair<IntegerCoordinates, IntegerCoordinates> barrier : boardMapBarriers.getBoardMapBarriers()) {
            IntegerCoordinates key = barrier.getKey();
            IntegerCoordinates value = barrier.getValue();
            if (key.getX() > value.getX() || key.getY() > value.getY())
                continue;

            Line barrierLine = new Line();
            this.barrierThickness = barrierLine.getStrokeWidth();
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
            boardPane.getChildren().add(barrierLine);
        }
    }

    private void insertItemToCells() {
        Random rand = new Random();
        for (int i = 0; i < BoardMapBarriers.boardSize.getX(); i++)
            for (int j = 0; j < BoardMapBarriers.boardSize.getY(); j++) {
                if ((i == 0 || i + 1 == BoardMapBarriers.boardSize.getX()) && (j == 0 || j + 1 == BoardMapBarriers.boardSize.getY()
                        || i == BoardMapBarriers.boardSize.getX() / 2 && j == BoardMapBarriers.boardSize.getY() / 2)) {
                    gameController.getGame().getCells()[i][j] = Cell.EMPTY;
                } else {
                    boolean isEnergyDrink = rand.nextInt(BoardMapBarriers.boardSize.getX() * BoardMapBarriers.boardSize.getY()) < 5;

                    if (isEnergyDrink)
                        gameController.getGame().getCells()[i][j] = Cell.ENERGY_DRINK;

                    else
                        gameController.getGame().getCells()[i][j] = Cell.COIN;

                }
            }
    }

    private void insertCellsToPane() {
        Random rand = new Random();
        for (int i = 0; i < BoardMapBarriers.boardSize.getX(); i++)
            for (int j = 0; j < BoardMapBarriers.boardSize.getY(); j++) {
                cells[i][j] = new Rectangle(cellSize - 4, cellSize - 4);
                Coordinates position = getCellPositionInScreen(new IntegerCoordinates(i, j));
                cells[i][j].setY(position.getX());
                cells[i][j].setX(position.getY());
                String address = getClass().getResource("../../" + gameController.getGame().getCells()[i][j].getAddress()).toExternalForm();
                cells[i][j].setFill(new ImagePattern(new Image(address)));
                boardPane.getChildren().add(cells[i][j]);
            }
    }

    private void insertGhostsAndPacmanToPane() {
        String address;
        refreshPacmanInScreen();


        for (int i = 0; i < 4; i++) {
            ghosts[i] = new GhostGf();
            int finalI = i;
            ghosts[i].setIntersectionFinder(new ChangeListener<Bounds>() {
                @Override
                public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                    if (ghosts[finalI].getGhost().getBoundsInParent().intersects(pacman.getPacman().getBoundsInParent())) {
                        try {
                            handlePacmanAndGhostHit(finalI);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });
            refreshGhostInScreen(ghosts[i], i);
        }

    }

    private void moveGhost(GhostGf ghost, int id) {
        if (!pauseButton.getText().equals("Pause"))
            return;
        IntegerCoordinates nextMove = getNextMoveForGhost(ghost, id);
        TranslateTransition move = new TranslateTransition(Duration.millis(Ghost.ghostSpeed), ghost.getGhost());

        move.setFromY(0);
        move.setFromX(0);
        move.setToY(cellSize * nextMove.getX());
        move.setToX(cellSize * nextMove.getY());


        move.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                move.pause();
                ghost.setMove(null);
                refreshGhostInScreen(ghost, id);
                moveGhost(ghost, id);
            }
        });

        ghost.setMove(move);
        IntegerCoordinates ghostPosition = gameController.getGame().getGhosts()[id].getCoordinates();
        ghostPosition.setX(ghostPosition.getX() + nextMove.getX());
        ghostPosition.setY(ghostPosition.getY() + nextMove.getY());
        move.play();
    }

    private void tryToMovePacman() throws IOException {
        obtainItemInCellByPacman();
        if (gameController.getGame().getCntOfCellWithItem() == 0) {
            resetGame();
            return;
        }
        MoveType direction = gameController.getGame().getPacman().getDirectionMoveInFuture();
        IntegerCoordinates pacmanHome = gameController.getGame().getPacman().getCoordinates();

        if (direction != null) {
            IntegerCoordinates neighbor = new IntegerCoordinates(pacmanHome.getX() + direction.getDirect().getX()
                    , pacmanHome.getY() + direction.getDirect().getY());
            if (SmallUsefullTool.findObjectInCollection(new Pair<>(pacmanHome, neighbor), gameController.getGame().getBoardMapBarriers().getBoardMap()) == null
                    && !SmallUsefullTool.badCell(neighbor)) {
                gameController.getGame().getPacman().setDirectionMove(direction);
            }
        }

        if (gameController.getGame().getPacman().getDirectionMove() == null)
            return;

        gameController.getGame().getPacman().setDirectionMoveInFuture(new MoveType());
        gameController.getGame().getPacman().setDirectionMoveInFuture(null);

        direction = gameController.getGame().getPacman().getDirectionMove();

        IntegerCoordinates neighbor = new IntegerCoordinates(pacmanHome.getX() + direction.getDirect().getX(), pacmanHome.getY() + direction.getDirect().getY());

        if (SmallUsefullTool.findObjectInCollection(new Pair<>(pacmanHome, neighbor), gameController.getGame().getBoardMapBarriers().getBoardMap()) == null
                && !SmallUsefullTool.badCell(neighbor)) {
            movePacman();
        }
    }

    private void movePacman() throws IOException {
        MoveType direction = gameController.getGame().getPacman().getDirectionMove();
        TranslateTransition move = new TranslateTransition(Duration.millis(Pacman.pacmanSpeed), pacman.getPacman());

        move.setFromX(0);
        move.setFromY(0);
        move.setToX(cellSize * direction.getDirect().getY());
        move.setToY(cellSize * direction.getDirect().getX());

        move.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                move.pause();
                pacman.setMove(null);
                refreshPacmanInScreen();
                try {
                    tryToMovePacman();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });

        pacman.setMove(move);
        IntegerCoordinates pacmanPosition = gameController.getGame().getPacman().getCoordinates();
        pacmanPosition.setX(pacmanPosition.getX() + direction.getDirect().getX());
        pacmanPosition.setY(pacmanPosition.getY() + direction.getDirect().getY());
        move.play();
    }

    private void obtainItemInCellByPacman() throws IOException {
        IntegerCoordinates tablePosition = gameController.getGame().getPacman().getCoordinates();
        Cell here = gameController.getGame().getCells()[tablePosition.getX()][tablePosition.getY()];

        if (here == Cell.COIN) {
            soundController.setPacmanEatCoin();
            soundController.getPacmanEatCoin().setStopTime(Duration.millis(Pacman.pacmanSpeed));
            soundController.getPacmanEatCoin().play();

            gameController.getGame().increaseScore(5);
            scoreLabel.setText(Integer.toString(gameController.getGame().getScore()));
            gameController.getGame().decreaseCntOfItems();
        } else if (here == Cell.ENERGY_DRINK) {
            soundController.setPacmanEatFruit();
            soundController.getPacmanEatFruit().setStopTime(Duration.millis(Pacman.pacmanSpeed));
            soundController.getPacmanEatFruit().play();

            gameController.getGame().getPacman().setEatenGhost(0);
            changeGameModeToHunterPacmanMode();
            gameController.getGame().decreaseCntOfItems();
        }

        gameController.getGame().getCells()[tablePosition.getX()][tablePosition.getY()] = Cell.EMPTY;
        String address = getClass().getResource("../../" + gameController.getGame().getCells()[tablePosition.getX()][tablePosition.getY()].getAddress()).toExternalForm();
        cells[tablePosition.getX()][tablePosition.getY()].setFill(new ImagePattern(new Image(address)));
    }

    private void handlePacmanAndGhostHit(int id) throws IOException {
        if (gameController.getGame().getPacman().getType() == PacmanType.PACMAN_HUNTER) {

            soundController.setPacmanEatGhost();
            soundController.getPacmanEatGhost().play();

            gameController.getGame().getPacman().increaseEatenGhost();
            gameController.getGame().increaseScore(200 * gameController.getGame().getPacman().getEatenGhost());
            scoreLabel.setText(String.valueOf(gameController.getGame().getScore()));
            gameController.getGame().getGhosts()[id].setGhostAvailable(false);
            goBackGhostToHomeCell(id);


            ghosts[id].getMove().pause();
            ghosts[id].setMove(null);
            refreshGhostInScreen(ghosts[id], id);
            ghosts[id].getGhost().boundsInParentProperty().removeListener(ghosts[id].getIntersectionFinder());

            ghosts[id].setWaitUntilMove(new Timeline(new KeyFrame(Duration.seconds(5), (ActionEvent event) -> {
                ghosts[id].setWaitUntilMove(null);
                gameController.getGame().getGhosts()[id].setGhostAvailable(true);
                moveGhost(ghosts[id], id);
                ghosts[id].getGhost().boundsInParentProperty().addListener(ghosts[id].getIntersectionFinder());
            })));
            ghosts[id].getWaitUntilMove().play();
        } else {
            soundController.setGhostEatPacman();
            soundController.getGhostEatPacman().play();
            soundController.getSong().pause();

            int cntChances = gameController.getGame().getPacmanLife();
            if (cntChances == 0)
                handleEndGame();
            else {
                cntChances--;
                gameController.getGame().setPacmanLife(cntChances);
                resetGame();
                return;
            }
        }
    }

    private void changeGameModeToHunterPacmanMode() {
        gameController.getGame().getPacman().setType(PacmanType.PACMAN_HUNTER);
        String address = getClass().getResource("../../" + gameController.getGame().getPacman().getType().getAddress()).toExternalForm();
        pacman.getPacman().setFill(new ImagePattern(new Image(address)));

        for (int i = 0; i < 4; i++) {
            gameController.getGame().getGhosts()[i].setType(GhostType.GHOST_PREY);
            address = getClass().getResource("../../" + gameController.getGame().getGhosts()[i].getType().getAddress()).toExternalForm();
            ghosts[i].getGhost().setFill(new ImagePattern(new Image(address)));
        }
        stage.show();

        if (timerForHunterPacman != null)
            timerForHunterPacman.stop();

        timerForHunterPacman = new Timeline(new KeyFrame(Duration.seconds(10), (ActionEvent event) -> {
            changeGameModeToPreyPacmanMode();
            timerForHunterPacman = null;
        }));
        timerForHunterPacman.play();
    }

    private void changeGameModeToPreyPacmanMode() {
        gameController.getGame().getPacman().setType(PacmanType.PACMAN);
        String address = getClass().getResource("../../" + gameController.getGame().getPacman().getType().getAddress()).toExternalForm();
        pacman.getPacman().setFill(new ImagePattern(new Image(address)));

        for (int i = 0; i < 4; i++) {
            gameController.getGame().getGhosts()[i].setType(Ghost.orderOfGhosts[i]);
            address = getClass().getResource("../../" + gameController.getGame().getGhosts()[i].getType().getAddress()).toExternalForm();
            ghosts[i].getGhost().setFill(new ImagePattern(new Image(address)));
        }
        stage.show();
    }

    private void resetGame() throws IOException {
        soundController.getSong().pause();
        if (timerForHunterPacman != null)
            timerForHunterPacman.pause();
        timerForHunterPacman = null;

        if (pacman.getMove() != null)
            pacman.getMove().pause();
        pacman.setMove(null);

        stage.getScene().setOnKeyPressed(null);

        for (GhostGf ghost : ghosts) {
            if (ghost.getMove() != null)
                ghost.getMove().pause();
            ghost.setMove(null);

            if (ghost.getWaitUntilMove() != null)
                ghost.getWaitUntilMove().pause();
            ghost.setWaitUntilMove(null);
        }

        boardPane.getChildren().clear();
        GhostGf[] ghosts = new GhostGf[4];
        PacmanGf pacman = new PacmanGf();

        createGame(gameController.getGame().getBoardMapBarriers()
                , gameController.getGame().getPacmanLife()
                , gameController.getAccount()
                , stage,
                gameController.getGame().getScore(),
                gameController.getGame().isHard());
        stage.show();
    }

    private void startMoveForGhostAfterNotAvailable(GhostGf ghost) {
        int id = findGhostIdWithGhostObject(ghost);
        if (id == -1)
            return;
        gameController.getGame().getGhosts()[id].setGhostAvailable(true);
        moveGhost(ghost, id);
    }

    private void goBackGhostToHomeCell(int id) {
        boardPane.getChildren().remove(ghosts[id].getGhost());
        gameController.getGame().getGhosts()[id].setCoordinates(new IntegerCoordinates(Ghost.firstPositionOfGhosts[id].getX(), Ghost.firstPositionOfGhosts[id].getY()));
        Coordinates position = getCellPositionInScreen(gameController.getGame().getGhosts()[id].getCoordinates());
        ghosts[id].getGhost().setX(position.getY());
        ghosts[id].getGhost().setY(position.getX());
    }

    private void refreshGhostInScreen(GhostGf ghost, int id) {
        boardPane.getChildren().remove(ghost.getGhost());
        ghosts[id].setGhost(new Rectangle(cellSize - 4, cellSize - 4));
        Coordinates position = getCellPositionInScreen(gameController.getGame().getGhosts()[id].getCoordinates());
        ghosts[id].getGhost().setX(position.getY());
        ghosts[id].getGhost().setY(position.getX());
        String address = getClass().getResource("../../" + gameController.getGame().getGhosts()[id].getType().getAddress()).toExternalForm();
        ghosts[id].getGhost().setFill(new ImagePattern(new Image(address)));
        if (gameController.getGame().getGhosts()[id].isGhostAvailable()) {
            ghost.getGhost().boundsInParentProperty().addListener(ghost.getIntersectionFinder());
        }
        boardPane.getChildren().add(ghosts[id].getGhost());
    }

    private void refreshPacmanInScreen() {
        boardPane.getChildren().remove(pacman.getPacman());
        pacman.setPacman(new Rectangle(cellSize - 4, cellSize - 4));
        Coordinates position = getCellPositionInScreen(gameController.getGame().getPacman().getCoordinates());
        pacman.getPacman().setX(position.getY());
        pacman.getPacman().setY(position.getX());
        String address = getClass().getResource("../../" + gameController.getGame().getPacman().getType().getAddress()).toExternalForm();
        pacman.getPacman().setFill(new ImagePattern(new Image(address)));
        boardPane.getChildren().add(pacman.getPacman());
    }

    private void refreshCellInScreen(int i, int j) {
        boardPane.getChildren().remove(cells[i][j]);
        cells[i][j] = new Rectangle(cellSize - 4, cellSize - 4);
        Coordinates position = getCellPositionInScreen(new IntegerCoordinates(i, j));
        cells[i][j].setY(position.getX());
        cells[i][j].setX(position.getY());
        String address = getClass().getResource("../../" + gameController.getGame().getCells()[i][j].getAddress()).toExternalForm();
        cells[i][j].setFill(new ImagePattern(new Image(address)));
        boardPane.getChildren().add(cells[i][j]);
    }

    private IntegerCoordinates getNextMoveForGhost(GhostGf ghost, int id) {
        IntegerCoordinates ghostPosition = gameController.getGame().getGhosts()[id].getCoordinates();
        IntegerCoordinates pacmanPosition = gameController.getGame().getPacman().getCoordinates();

        int[] X = {0, 0, -1, 1};
        int[] Y = {1, -1, 0, 0};
        ArrayList<IntegerCoordinates> options = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            IntegerCoordinates neighbor = new IntegerCoordinates(ghostPosition.getX() + X[i], ghostPosition.getY() + Y[i]);
            if (SmallUsefullTool.findObjectInCollection(new Pair<>(ghostPosition, neighbor), gameController.getGame().getBoardMapBarriers().getBoardMap()) == null
                    && !SmallUsefullTool.badCell(neighbor))
                options.add(new IntegerCoordinates(X[i], Y[i]));
        }


        int ansId = 0;

        if (gameController.getGame().isHard()) {
            int shortestPath;

            if (gameController.getGame().getPacman().getType() != PacmanType.PACMAN_HUNTER) {
                shortestPath = 500;
                for (int i = 0; i < options.size(); i++) {
                    IntegerCoordinates neighbor = new IntegerCoordinates(ghostPosition.getX() + options.get(i).getX(), ghostPosition.getY() + options.get(i).getY());
                    if (shortestPath > gameController.getGame().getShortestPath(neighbor, gameController.getGame().getPacman().getCoordinates())) {
                        shortestPath = gameController.getGame().getShortestPath(neighbor, gameController.getGame().getPacman().getCoordinates());
                        ansId = i;
                    }
                }
            } else {
                shortestPath = -1;
                for (int i = 0; i < options.size(); i++) {
                    IntegerCoordinates neighbor = new IntegerCoordinates(ghostPosition.getX() + options.get(i).getX(), ghostPosition.getY() + options.get(i).getY());
                    if (shortestPath < gameController.getGame().getShortestPath(neighbor, gameController.getGame().getPacman().getCoordinates())) {
                        shortestPath = gameController.getGame().getShortestPath(neighbor, gameController.getGame().getPacman().getCoordinates());
                        ansId = i;
                    }
                }
            }
        } else {
            Random random = new Random();
            ansId = random.nextInt(options.size());
        }
        return options.get(ansId);
    }

    private void setPacmanFutureDirection(MoveType direction) throws IOException {
        gameController.getGame().getPacman().setDirectionMoveInFuture(direction);
        if (pacman.getMove() == null)
            tryToMovePacman();
    }

    private int findGhostIdWithGhostObject(GhostGf ghost) {
        for (int i = 0; i < ghosts.length; i++) {
            if (ghosts[i] == ghost)
                return i;
        }
        return -1;
    }

    private Coordinates getCellPositionInScreen(IntegerCoordinates tablePosition) {

        return new Coordinates(cellSize * tablePosition.getX() + this.barrierThickness * tablePosition.getX() + 2,
                cellSize * tablePosition.getY() + this.barrierThickness * tablePosition.getY() + 2);
    }

    public void handlePause(ActionEvent actionEvent) {
        if (pauseButton.getText().equals("Pause")) {
            pauseButton.setText("Resume");
            exitButton.setDisable(false);
            stopGameMove();
        } else {
            pauseButton.setText("Pause");
            exitButton.setDisable(true);
            restartGameMove();
        }
    }

    private void stopGameMove() {
        for (GhostGf ghost : ghosts) {
            if (ghost.getWaitUntilMove() != null)
                ghost.getWaitUntilMove().pause();

            if (ghost.getMove() != null)
                ghost.getMove().pause();
        }

        if (pacman.getMove() != null)
            pacman.getMove().pause();

        if (timerForHunterPacman != null)
            timerForHunterPacman.pause();
    }

    private void restartGameMove() {
        for (GhostGf ghost : ghosts) {
            if (ghost.getWaitUntilMove() != null)
                ghost.getWaitUntilMove().play();

            if (ghost.getMove() != null)
                ghost.getMove().play();

            // else if(ghost.getWaitUntilMove() == null)
            //   moveGhost(ghost,findGhostIdWithGhostObject(ghost));
        }

        if (pacman.getMove() != null)
            pacman.getMove().play();

        if (timerForHunterPacman != null)
            timerForHunterPacman.play();
    }

    private void handleEndGame() throws IOException {
        stopGameMove();
        if (gameController.getAccount() != null) {
            Account account = gameController.getAccount();
            account.setMaxScore(Math.max(account.getMaxScore(), gameController.getGame().getScore()));
            account.setLastGame(null);
            MainController.getInstance().saveChangeForAccount(account);
        }
        exitFromGamePage();
    }

    public void handlePressExitButton(ActionEvent actionEvent) throws IOException {
        if (gameController.getAccount() != null) {
            Account account = gameController.getAccount();
            if (gameController.getGame().getPacman().getType() == PacmanType.PACMAN_HUNTER && timerForHunterPacman != null)
                gameController.getGame().setUntilPacmanPreyMode(timerForHunterPacman.getCurrentTime());
            account.setLastGame(gameController.getGame());
            MainController.getInstance().saveChangeForAccount(account);
        }
        exitFromGamePage();
    }

    private void exitFromGamePage() throws IOException {
        soundController.getSong().pause();

        if (gameController.getAccount() == null) {
            Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/WelcomePage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("../../NotCode/Fxml/UserPage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    private void loopForSong() {
        soundController.setSong();
        soundController.getSong().play();
        soundController.getSong().setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                loopForSong();
            }
        });
    }
}
