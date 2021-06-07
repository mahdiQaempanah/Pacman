package sample.Model;

public class IntegerCoordinates {
    private int x;
    private int y;

    public IntegerCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equal(IntegerCoordinates b) {
        return this.getX() == b.getX() && this.getY() == b.getY();
    }
}
