package sample.Model.GameObjects;

public enum Cell {
    EMPTY("NotCode/Image/Black.jpg"),
    COIN("NotCode/Image/4.jpg"),
    ENERGY_DRINK("NotCode/Image/3.jpg");

    private final String address;

    Cell(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }
}
