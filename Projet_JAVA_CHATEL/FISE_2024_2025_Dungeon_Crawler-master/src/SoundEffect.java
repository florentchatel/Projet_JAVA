import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;

public class SoundEffect {

    //création d'une méthode pour jouer un fichier audio
    public static void playWav(String filePath) {
        try {
            //on charge le fichier audio
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            //pour pouvoir jouer le son
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            //joue le son
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

