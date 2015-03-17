package no.uib.inf112.group4.framework.ui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import no.uib.inf112.group4.framework.PlayerColor;
import no.uib.inf112.group4.pieces.PieceType;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

/**
 * 
 * This class will convert the SVG icons into PNG and return them as
 * BufferedImage objects, making them ready for the "drawPieces" method in the
 * BoardCanvas class.
 * 
 * The word "Transcoder" is used often in this class and it basically means a
 * converter between different formats, in this case it's between the formats
 * SVG and PNG.
 * 
 * This class is using the external dependencies in the folder
 * "svg dependencies".
 */
public class Icons {

	private final static String iconDir = "res//graphic//";

	/**
	 * Returns the buffered Image of the SVG file requested in the parameter.
	 * Used to reduce duplication.
	 */
	private static BufferedImage getPieceImage(String filename) {
		try {

			// Read the input SVG file into transcoder Input.
			String svgUriInput = Paths.get(filename).toUri().toURL().toString();

			TranscoderInput inputSVGImage = new TranscoderInput(svgUriInput);

			// Define output stream to PNG image and attach to transcoder
			// Output.
			ByteArrayOutputStream pngOstream = new ByteArrayOutputStream();
			TranscoderOutput outputPNGImage = new TranscoderOutput(pngOstream);

			// Create PNG transcoder and define hints if required.
			PNGTranscoder converter = new PNGTranscoder();

			try {

				float size = 135;
				converter.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT,
						size);
				converter.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH,
						size);
				// Convert and write output.
				converter.transcode(inputSVGImage, outputPNGImage);
				BufferedImage image;
				try {

					image = ImageIO.read(new ByteArrayInputStream(pngOstream
							.toByteArray()));

					pngOstream.flush();
					pngOstream.close();
					return image;

				} catch (IOException e) {
					System.out.println("IO Exception: " + e);
					e.printStackTrace();
				}

			} catch (TranscoderException e) {
				System.out
						.println("Error in the conversion between SVG and PNG: "
								+ e);
				e.printStackTrace();
				System.out.println("Failed to load graphics at path "
						+ svgUriInput + ", terminating program.");
				System.exit(0);
			}

		} catch (MalformedURLException e) {
			System.out.println("Malformed URL Exception: " + e);
			e.printStackTrace();
		}

		return null;
	}

	private static BufferedImage[][] imageList = null;
	private static boolean isLoading = false;

	private static void loadImages() {
		isLoading = true;
		Thread t = new Thread() {
			@Override
			public void run() {
				System.out.println("Loading images...");
				long time = System.currentTimeMillis();
				BufferedImage[][] iL = new BufferedImage[PlayerColor.values().length][PieceType
						.values().length];
				for (PlayerColor pc : PlayerColor.values()) {
					for (PieceType pt : PieceType.values()) {
						iL[pc.ordinal()][pt.ordinal()] = getPieceImage(iconDir
								+ pc.toString().toLowerCase() + "_"
								+ pt.toString().toLowerCase() + ".svg");
					}
				}
				imageList = iL;
				time = System.currentTimeMillis() - time;
				System.out.println("Images loaded in " + time + "ms");
			}
		};
		t.start();
	}

	public static BufferedImage getImage(PlayerColor color, PieceType type) {
		if (imageList == null) {
			if (!isLoading)
				loadImages();
			return null;
		}
		return imageList[color.ordinal()][type.ordinal()];
	}

}