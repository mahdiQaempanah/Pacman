package sample.Model.GraphicObject;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundController {
    private final static Media mSong = new Media(SoundController.class.getResource("../../../NotCode/Sound/superMario.mp3").toExternalForm());
    private final static Media mGhostEatPacman = new Media(SoundController.class.getResource("../../../NotCode/Sound/pacman_death.wav").toExternalForm());
    private final static Media mPacmanEatGhost = new Media(SoundController.class.getResource("../../../NotCode/Sound/pacman_eatghost.wav").toExternalForm());
    private final static Media mPacmanEatCoin = new Media(SoundController.class.getResource("../../../NotCode/Sound/pacman_eatcoin.wav").toExternalForm());
    private final static Media mPacmanEatFruit = new Media(SoundController.class.getResource("../../../NotCode/Sound/pacman_eatfruit.wav").toExternalForm());

    private MediaPlayer song;
    private MediaPlayer ghostEatPacman;
    private MediaPlayer pacmanEatGhost;
    private MediaPlayer pacmanEatCoin;
    private MediaPlayer pacmanEatFruit;

    public SoundController() {
        setGhostEatPacman();
        setPacmanEatCoin();
        setSong();
        setPacmanEatGhost();
        setPacmanEatFruit();
    }

    public void setSong() {
        this.song = new MediaPlayer(mSong);
    }

    public void setGhostEatPacman() {
        this.ghostEatPacman = new MediaPlayer(mGhostEatPacman);
    }

    public void setPacmanEatGhost() {
        this.pacmanEatGhost = new MediaPlayer(mPacmanEatGhost);
    }

    public void setPacmanEatCoin() {
        this.pacmanEatCoin = new MediaPlayer(mPacmanEatCoin);
    }

    public void setPacmanEatFruit() {
        this.pacmanEatFruit = new MediaPlayer(mPacmanEatFruit);
    }

    public MediaPlayer getSong() {
        return song;
    }

    public MediaPlayer getGhostEatPacman() {
        return ghostEatPacman;
    }

    public MediaPlayer getPacmanEatGhost() {
        return pacmanEatGhost;
    }

    public MediaPlayer getPacmanEatCoin() {
        return pacmanEatCoin;
    }

    public MediaPlayer getPacmanEatFruit() {
        return pacmanEatFruit;
    }
}
