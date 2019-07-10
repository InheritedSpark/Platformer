package Sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer implements Runnable {

	private ArrayList<String> musicFiles;
	private int currentIndex;

	public MusicPlayer(String... files) {
		musicFiles = new ArrayList<String>();
		for (String file : files) {
			musicFiles.add(file);
		}
	}

	public void playSound(String fileName) {
			File soundFile = new File(fileName);
			AudioInputStream ais;
			try {
				ais = AudioSystem.getAudioInputStream(soundFile);
				AudioFormat format = ais.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(ais);
				FloatControl gameControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
				gameControl.setValue(5);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void run() {
		playSound(musicFiles.get(currentIndex));
	}

}
