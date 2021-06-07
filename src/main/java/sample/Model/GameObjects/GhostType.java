package sample.Model.GameObjects;

public enum GhostType {
    RED_GHOST("NotCode/Image/GhostRed.jpg"),
    BLUE_GHOST("NotCode/Image/GhostBlue.png"),
    GREEN_GHOST("NotCode/Image/GhostGreen.jpg"),
    YELLOW_GHOST("NotCode/Image/GhostYellow.png"),
    GHOST_PREY("NotCode/Image/GhostPrey.jpg");

    private final String address;

    GhostType(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }
}
