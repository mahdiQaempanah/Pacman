package sample.Model.GameObjects;

import sample.Model.IntegerCoordinates;

public class Pacman {
    static final public double pacmanSpeed = 100;//ms
    private PacmanType type;
    private IntegerCoordinates coordinates;
    private MoveType directionMove;
    private MoveType directionMoveInFuture;
    private int eatenGhost = 0;

    public Pacman(PacmanType type, IntegerCoordinates coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public PacmanType getType() {
        return type;
    }

    public void setType(PacmanType type) {
        this.type = type;
    }

    public IntegerCoordinates getCoordinates() {
        return coordinates;
    }

    public MoveType getDirectionMove() {
        return directionMove;
    }

    public void setDirectionMove(MoveType directionMove) {
        this.directionMove = directionMove;
    }

    public MoveType getDirectionMoveInFuture() {
        return directionMoveInFuture;
    }

    public void setDirectionMoveInFuture(MoveType directionMoveInFuture) {
        this.directionMoveInFuture = directionMoveInFuture;
    }

    public int getEatenGhost() {
        return eatenGhost;
    }

    public void setEatenGhost(int eatenGhost) {
        this.eatenGhost = eatenGhost;
    }

    public void increaseEatenGhost() {
        this.eatenGhost++;
    }

}
