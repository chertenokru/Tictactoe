package ru.chertenok.tictactoe;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Created by 13th on 08.04.2017.
 */
public class SoundEngine {

    private static MediaPlayer currentPlayer = null;


    public SoundEngine() {
    }

    public static void playWin(){
        String file = "/ru/chertenok/tictactoe/resources/sound/win.mp3";
        playSound( file);
    }

    public static void playLoss(){
        String file = "/ru/chertenok/tictactoe/resources/sound/loss.mp3";
        playSound(file);
    }

    public static void playTurn(){
        String file = "/ru/chertenok/tictactoe/resources/sound/turn.mp3";
        playSound(file);

    }

    private static void playSound(String file) {
        Media sound = new Media(SoundEngine.class.getResource(file).toString());
        stopSound();
        currentPlayer = new MediaPlayer(sound);
        currentPlayer.play();
    }

    public static void stopSound()
    {
        if (currentPlayer != null)
        {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
        }
    }

}
