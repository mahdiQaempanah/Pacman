package sample.Model.GameObjects;

import sample.Model.BoardMapBarriers;
import sample.Model.IntegerCoordinates;

public class Ghost {
    static final public double ghostSpeed = 220;
    public final static GhostType[] orderOfGhosts;
    public final static IntegerCoordinates[] firstPositionOfGhosts;
    private GhostType type;
    private IntegerCoordinates coordinates;
    private boolean isGhostAvailable;


    static {
        orderOfGhosts = new GhostType[]{GhostType.RED_GHOST, GhostType.BLUE_GHOST, GhostType.GREEN_GHOST, GhostType.YELLOW_GHOST};

        firstPositionOfGhosts = new IntegerCoordinates[]{new IntegerCoordinates(0, 0),
                new IntegerCoordinates(0, BoardMapBarriers.boardSize.getY() - 1),
                new IntegerCoordinates(BoardMapBarriers.boardSize.getX() - 1, 0),
                new IntegerCoordinates(BoardMapBarriers.boardSize.getX() - 1, BoardMapBarriers.boardSize.getY() - 1)};
    }

    public Ghost(GhostType type, IntegerCoordinates coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public boolean isGhostAvailable() {
        return isGhostAvailable;
    }

    public void setGhostAvailable(boolean ghostAvailable) {
        isGhostAvailable = ghostAvailable;
    }

    public GhostType getType() {
        return type;
    }

    public void setType(GhostType type) {
        this.type = type;
    }

    public IntegerCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(IntegerCoordinates coordinates) {
        this.coordinates = coordinates;
    }
}
