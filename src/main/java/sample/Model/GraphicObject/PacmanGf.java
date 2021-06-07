package sample.Model.GraphicObject;

import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;


public class PacmanGf {
    private TranslateTransition move;
    private Rectangle pacman;

    public TranslateTransition getMove() {
        return move;
    }

    public void setMove(TranslateTransition move) {
        this.move = move;
    }

    public Rectangle getPacman() {
        return pacman;
    }

    public void setPacman(Rectangle pacman) {
        this.pacman = pacman;
    }
}
