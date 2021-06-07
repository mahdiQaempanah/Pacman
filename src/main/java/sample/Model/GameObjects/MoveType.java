package sample.Model.GameObjects;

import sample.Model.IntegerCoordinates;

public class MoveType {
    public static final String UP = "up";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String DOWN = "down";

    private String type;

    public MoveType() {

    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public IntegerCoordinates getDirect() {
        if (type.equals(MoveType.DOWN))
            return new IntegerCoordinates(-1, 0);
        if (type.equals(MoveType.UP))
            return new IntegerCoordinates(1, 0);
        if (type.equals(MoveType.LEFT))
            return new IntegerCoordinates(0, -1);
        if (type.equals(MoveType.RIGHT))
            return new IntegerCoordinates(0, 1);
        return new IntegerCoordinates(0, 0);
    }
}
