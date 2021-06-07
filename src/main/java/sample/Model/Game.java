package sample.Model;

import javafx.util.Duration;
import javafx.util.Pair;
import sample.Model.GameObjects.*;
import sample.Model.Tools.SmallUsefullTool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {


    private final Cell[][] cells;
    private int score;
    private int pacmanLife;
    private Ghost[] ghosts;
    private final Pacman pacman;
    private BoardMapBarriers boardMapBarriers;
    private int cntOfCellWithItem;
    private Duration untilPacmanPreyMode;
    private final boolean isHard;
    private int[][][][] shortestPath;

    public Game(BoardMapBarriers boardMapBarriers, int cntChances, int score, boolean isHard) {
        this.boardMapBarriers = boardMapBarriers;
        this.pacmanLife = cntChances;
        this.score = score;
        this.cntOfCellWithItem = BoardMapBarriers.boardSize.getX() * BoardMapBarriers.boardSize.getY() - 4;
        this.isHard = isHard;

        cells = new Cell[BoardMapBarriers.boardSize.getX()][BoardMapBarriers.boardSize.getY()];

        for (Cell[] rowCells : cells) {
            for (Cell cell : rowCells) {
                cell = Cell.EMPTY;
            }
        }

        this.pacman = new Pacman(PacmanType.PACMAN, new IntegerCoordinates(BoardMapBarriers.boardSize.getX() / 2, BoardMapBarriers.boardSize.getY() / 2));


        this.ghosts = new Ghost[4];
        for (int i = 0; i < 4; i++) {
            ghosts[i] = new Ghost(Ghost.orderOfGhosts[i], new IntegerCoordinates(Ghost.firstPositionOfGhosts[i].getX(), Ghost.firstPositionOfGhosts[i].getY()));
        }
        this.ghosts = new Ghost[]{new Ghost(Ghost.orderOfGhosts[0], new IntegerCoordinates(0, 0)),
                new Ghost(Ghost.orderOfGhosts[1], new IntegerCoordinates(0, BoardMapBarriers.boardSize.getY() - 1)),
                new Ghost(Ghost.orderOfGhosts[2], new IntegerCoordinates(BoardMapBarriers.boardSize.getX() - 1, 0)),
                new Ghost(Ghost.orderOfGhosts[3], new IntegerCoordinates(BoardMapBarriers.boardSize.getX() - 1, BoardMapBarriers.boardSize.getY() - 1))};
    }


    public Cell[][] getCells() {
        return cells;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public int getPacmanLife() {
        return pacmanLife;
    }

    public void setPacmanLife(int pacmanLife) {
        this.pacmanLife = pacmanLife;
    }

    public Ghost[] getGhosts() {
        return ghosts;
    }

    public Pacman getPacman() {
        return pacman;
    }

    public BoardMapBarriers getBoardMapBarriers() {
        return boardMapBarriers;
    }

    public void setBoardMapBarriers(BoardMapBarriers boardMapBarriers) {
        this.boardMapBarriers = boardMapBarriers;
    }


    public int getCntOfCellWithItem() {
        return cntOfCellWithItem;
    }

    public void decreaseCntOfItems() {
        this.cntOfCellWithItem--;
    }

    public Duration getUntilPacmanPreyMode() {
        return untilPacmanPreyMode;
    }

    public void setUntilPacmanPreyMode(Duration untilPacmanPreyMode) {
        this.untilPacmanPreyMode = untilPacmanPreyMode;
    }

    public boolean isHard() {
        return isHard;
    }

    public int getShortestPath(IntegerCoordinates cell1, IntegerCoordinates cell2) {
        return shortestPath[cell1.getX()][cell1.getY()][cell2.getX()][cell2.getY()];
    }


    public void getShortestPathBetweenCells() {
        if (shortestPath != null)
            return;

        int maxX = BoardMapBarriers.boardSize.getX();
        int maxY = BoardMapBarriers.boardSize.getY();
        shortestPath = new int[maxX][maxY][maxX][maxY];

        for (int x = 0; x < maxX; x++)
            for (int y = 0; y < maxY; y++) {
                getShortestPathFromThisCell(x, y);
            }

        for (int i = 0; i < maxX; i++)
            for (int j = 0; j < maxY; j++)
                for (int x = 0; x < maxX; x++)
                    for (int y = 0; y < maxY; y++) {
                        shortestPath[x][y][i][j]--;
                    }

        System.out.println(shortestPath[0][0][10][10]);
    }

    private void getShortestPathFromThisCell(int x, int y) {
        shortestPath[x][y][x][y] = 1;
        Queue<IntegerCoordinates> q = new ConcurrentLinkedQueue<>();
        q.add(new IntegerCoordinates(x, y));
        int[] X = {0, 0, 1, -1};
        int[] Y = {-1, 1, 0, 0};
        while (q.size() > 0) {
            IntegerCoordinates cell = q.poll();
            for (int i = 0; i < 4; i++) {
                IntegerCoordinates neighbor = new IntegerCoordinates(cell.getX() + X[i], cell.getY() + Y[i]);
                if (SmallUsefullTool.findObjectInCollection(new Pair<>(cell, neighbor), boardMapBarriers.getBoardMap()) == null &&
                        !SmallUsefullTool.badCell(neighbor) &&
                        shortestPath[x][y][neighbor.getX()][neighbor.getY()] == 0) {
                    shortestPath[x][y][neighbor.getX()][neighbor.getY()] = 1 + shortestPath[x][y][cell.getX()][cell.getY()];
                    q.add(neighbor);
                }
            }
        }
    }
}
