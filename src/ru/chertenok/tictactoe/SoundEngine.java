package ru.chertenok.tictactoe;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * Created by 13th on 08.04.2017.
 */
public class SoundEngine {

    private static MediaPlayer currentPlayer = null;


    public SoundEngine() {
    }

    public static void playWin(){
        String file = "resources/sound/win.mp3";
        playSound(file);
    }

    public static void playLoss(){
        String file = "resources/sound/loss.mp3";
        playSound(file);
    }

    public static void playTurn(){
        String file = "resources/sound/turn.mp3";
        playSound(file);

    }

    private static void playSound(String file) {
        Media sound = new Media(new File(file).toURI().toString());
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
