package sample.Model;

import java.util.ArrayList;

public class Account {
    private final String username;
    private String password;
    private int maxScore;
    private double lastGameTime;//with seconds
    private ArrayList<BoardMapBarriers> boardMapBarriers;
    private Game lastGame;

    public double getLastGameTime() {
        return lastGameTime;
    }

    public void setLastGameTime(double lastGameTime) {
        this.lastGameTime = lastGameTime;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        maxScore = 0;
        boardMapBarriers = new ArrayList<>();
    }


    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public Game getLastGame() {
        return lastGame;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public ArrayList<BoardMapBarriers> getBoardMapBarriers() {
        return boardMapBarriers;
    }

    public void setBoardMapBarriers(ArrayList<BoardMapBarriers> boardMapBarriers) {
        this.boardMapBarriers = boardMapBarriers;
    }

    public void setLastGame(Game lastGame) {
        this.lastGame = lastGame;
    }
}
