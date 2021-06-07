package sample.Model.GameObjects;

public enum PacmanType {
    PACMAN("NotCode/Image/Pacman.png"),
    PACMAN_HUNTER("NotCode/Image/PacmanHunter.png");

    private final String address;

    PacmanType(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }
}
