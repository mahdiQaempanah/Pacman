package sample.Model;

import javafx.util.Pair;

import java.util.ArrayList;

public class BoardMapBarriers {
    public final static IntegerCoordinates boardSize = new IntegerCoordinates(15, 15);
    public final static int cellSize = 40;
    private ArrayList<Pair<IntegerCoordinates, IntegerCoordinates>> boardMapBarriers;
    private static final BoardMapGenerator boardMapGenerator = new BoardMapGenerator();

    public BoardMapBarriers(ArrayList<Pair<IntegerCoordinates, IntegerCoordinates>> boardMap) {
        this.boardMapBarriers = boardMap;
    }

    public BoardMapBarriers() {
        setNewRandomMap();
    }

    public ArrayList<Pair<IntegerCoordinates, IntegerCoordinates>> getBoardMap() {
        return boardMapBarriers;
    }

    public void setNewRandomMap() {
        boardMapBarriers = boardMapGenerator.getRandomMapBarriers();
    }

    public ArrayList<Pair<IntegerCoordinates, IntegerCoordinates>> getBoardMapBarriers() {
        return this.boardMapBarriers;
    }
}
