package sample.Model.GraphicObject;

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.scene.shape.Rectangle;


public class GhostGf {

    private ChangeListener intersectionFinder;
    private TranslateTransition move;
    private Rectangle ghost;
    private Timeline waitUntilMove;

    public ChangeListener getIntersectionFinder() {
        return intersectionFinder;
    }

    public void setIntersectionFinder(ChangeListener intersectionFinder) {
        this.intersectionFinder = intersectionFinder;
    }

    public TranslateTransition getMove() {
        return move;
    }

    public void setMove(TranslateTransition move) {
        this.move = move;
    }

    public Rectangle getGhost() {
        return ghost;
    }

    public void setGhost(Rectangle ghost) {
        this.ghost = ghost;
    }

    public Timeline getWaitUntilMove() {
        return waitUntilMove;
    }

    public void setWaitUntilMove(Timeline waitUntilMove) {
        this.waitUntilMove = waitUntilMove;
    }
}
