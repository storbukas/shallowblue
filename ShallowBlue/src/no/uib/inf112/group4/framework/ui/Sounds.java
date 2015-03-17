package no.uib.inf112.group4.framework.ui;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * 
 * Need sounds for move piece, kill piece, promotion, check and check mate.
 * 
 */
public class Sounds {

	private final static String iconDir = "res" + File.separator + "sounds"
			+ File.separator;

	/**
	 * Method which loads sound.
	 * 
	 * @param filename
	 *            : name of file you wish to load.
	 * @return: Returns audiostream of said filename.
	 * 
	 *          Forget this atm.
	 */

	public static void testPlay(final String filename) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					File file = new File(filename);
					AudioInputStream in = AudioSystem.getAudioInputStream(file);
					AudioInputStream din = null;
					AudioFormat baseFormat = in.getFormat();
					AudioFormat decodedFormat = new AudioFormat(
							AudioFormat.Encoding.PCM_SIGNED, baseFormat
									.getSampleRate(), 16, baseFormat
									.getChannels(),
							baseFormat.getChannels() * 2, baseFormat
									.getSampleRate(), false);
					din = AudioSystem.getAudioInputStream(decodedFormat, in);
					// Play now.
					rawplay(decodedFormat, din);
					in.close();
				} catch (Exception e) {
					// Handle exception.
				}
			}
		}).start();
	}

	private static void rawplay(AudioFormat targetFormat, AudioInputStream din)
			throws IOException, LineUnavailableException {
		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			@SuppressWarnings("unused")
			int nBytesRead = 0, nBytesWritten = 0;
			while (nBytesRead != -1) {
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1)
					nBytesWritten = line.write(data, 0, nBytesRead);
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	private static SourceDataLine getLine(AudioFormat audioFormat)
			throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

	/**
	 * Plays a sound designed for when a piece is moved.
	 */
	public static void playMovePiece() {
		testPlay("res/sounds/move_piece.wav");

		// TODO May need a thread.
	}

	/**
	 * Plays a sound designed for when a piece is killed.
	 */
	public static void playKillPiece() {
		testPlay(iconDir + "kill_piece.mp3");

	}

	/**
	 * Plays a sound designed for when a pawn is promoted.
	 */
	public static void playPromotion() {
		testPlay(iconDir + "promotion.wav");

	}

	/**
	 * Plays a sound designed for when a king is put into check.
	 */
	public static void playCheck() {
		testPlay(iconDir + "check.mp3");
	}

	/**
	 * Plays a sound designed for when a king is put into check mate.
	 */
	public static void playCheckMate() {
		testPlay(iconDir + "check_mate.wav");
	}

}
