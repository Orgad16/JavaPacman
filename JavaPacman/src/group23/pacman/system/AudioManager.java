package group23.pacman.system;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antonio Zaitoun on 05/01/2019.
 */
public final class AudioManager {

    /**
     * singleton instance.
     */
    public static final AudioManager shared = new AudioManager();

    /**
     * Map which contains the audio files. The key is the name and the value is the path.
     */
    private Map<String,String> registeredAudioFiles = new HashMap<>();

    /**
     * The current volume of the media player.
     */
    private double volume = 1.0;

    /**
     * Factory function used to build a media player.
     * @param audioFile the audio file.
     * @return A media player object.
     */
    private MediaPlayer mediaPlayer(String audioFile) {

        // create media object
        Media media = new Media(getClass().getResource(audioFile).toExternalForm());

        // create media player
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.setStartTime(Duration.ZERO);
        mediaPlayer.seek(Duration.ZERO);
        return mediaPlayer;
    }

    /**
     * Register a new audio.
     *
     * @param audioName The given audio name.
     * @param fileLocation The location of the audio file.
     */
    public void register(String audioName, String fileLocation) {
        //TODO: check that file exists
        if(getClass().getResource(fileLocation) != null) {
            registeredAudioFiles.put(audioName, fileLocation);
        }else {
            System.err.println("Invalid location for file: " + fileLocation);
        }
    }

    /**
     * Play an audio.
     *
     * @param audioName The name of the audio you wish to play.
     */
    public void play(String audioName) {

        String fileLocation = registeredAudioFiles.get(audioName);

        if (fileLocation != null) {
            MediaPlayer mediaPlayer = mediaPlayer(fileLocation);
            mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);
            mediaPlayer.play();
        }
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        volume = Math.max(Math.min(1.0,volume),0.0);
        this.volume = volume;
    }

    public void mute(){
        setVolume(0.0);
    }

}
