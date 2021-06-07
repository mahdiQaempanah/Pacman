package sample.Controller;

import sample.Model.Account;
import sample.Model.BoardMapBarriers;
import sample.Model.Game;

public class GameController {


    private final Game game;
    private final Account account;

    public GameController(BoardMapBarriers boardMapBarriers, int cntChances, Account account, int score, boolean isHard) {
        this.account = account;
        game = new Game(boardMapBarriers, cntChances, score, isHard);
    }

    public GameController(Game game, Account loggingUser) {
        this.game = game;
        this.account = loggingUser;
    }

    public Game getGame() {
        return game;
    }

    public Account getAccount() {
        return account;
    }

}
