package com.github.mbeier1406.howto.ausbildung.mt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Beispiel zur Performancemessung: hier die Latenz<p/>
 * Anhand eines Beispiels zur Imageverarbeitung wird die Zeit für
 * die Ausführung einmal sequentiell und einmal  parallelisiert gemessen.
 * @author mbeier
 */
public class Latency {

	public static final Logger LOGGER = LogManager.getLogger(Latency.class);

	/** Das zu bearbeitende Image ist {@value} */
	public static final String IMAGE_SOURCE = "src/main/resources/images/Cid.jpg";

	/** Das fertig bearbeitete Image ist {@value} */
	public static final String IMAGE_TARGET = "./target/Cid.jpg";


	/**
	 * Definiert die Maske und Shiftwerte für ARGB-Pixel.
	 */
	public static enum ARGB {
		ALPHA(0xff000000, 24), // Transparenz
		RED(0x00ff0000, 16),   // Rotwert
		GREEN(0x0000ff00, 8),  // Grünwert
		BLUE(0x000000ff, 0);   // Blauwert
		private int mask, shift;
		private ARGB(int mask, int shift) {
			this.mask = mask;
			this.shift = shift;
		}
		public int getMask() {
			return this.mask;
		}
		public int getShift() {
			return this.shift;
		}
	}


	/**
	 * Liefert zu einem ARGB-Pixel den entsprechenden Alpha, rot, grün bzw. blau-Wert.
	 * @param argb der zu extrahierende Wert
	 * @param value der Pixel von dem der Wert ermittelt werden soll
	 * @return den entsprechenden ARGB-Wert
	 */
	public static int getArgb(ARGB argb, int value) {
		return ( value & argb.getMask() ) >> argb.getShift();
	}

	/**
	 * Ereugt einen ARGB-Pixel aus den drei Farbwerten. Der Alpha-Wert wird auf <b>0xff</b> gesetzt (keine Transparenz).
	 * @param red der Rotwert
	 * @param green der Grünwert
	 * @param blue der Blauwert
	 * @return der resultierende ARGB-Wert
	 */
	public static int createRgbFromColors(int red, int green, int blue) {
		return ((((0 | blue  << ARGB.BLUE.getShift()) | green << ARGB.GREEN.getShift()) | red << ARGB.RED.getShift()) | 0xff << ARGB.ALPHA.getShift());
	}

	/**
	 * Ermittelt, ob die drei Farbwerte sich so ähnlich sind (einen ähnlichen Wert haben),
	 * so dass die daraus resultierende RGB-Farbe ein Grauton ist. Als
	 * @param red der Rotwert
	 * @param green der Grünwert
	 * @param blue der Blauwert
	 * @return <b>true</b>, wenn die Differenz aus allen Farbwerten miteinander verglichen einen definierten Wert nicht übersteigt, sonst <b>false</b>
	 */
	public static boolean isShadeOfGray(int red, int green, int blue) {
		return Math.abs(red-green) < 30 && Math.abs(red-blue) < 30 && Math.abs(green-blue) < 30;
	}

	public static void recolorImage(BufferedImage sourceImage, BufferedImage targetImage, int x, int y) {
		if ( x < 0 || x >= sourceImage.getWidth() || y < 0 || y >= sourceImage.getHeight() )
			throw new IllegalArgumentException("sourceImage="+sourceImage+"; targetImage="+targetImage+"; x="+x+"; y="+y);
		int rgb = sourceImage.getRGB(x, y);
		int red = getArgb(ARGB.RED, rgb);
		int green = getArgb(ARGB.GREEN, rgb);
		int blue = getArgb(ARGB.BLUE, rgb);
		if ( isShadeOfGray(red, green, blue) )
			red = green = blue = 255;
		rgb = createRgbFromColors(red, green, blue);
		targetImage.getRaster().setDataElements(x, y, targetImage.getColorModel().getDataElements(rgb, null));
	}


	public static void main(String[] args) throws IOException {
		final var sourceImage = ImageIO.read(new File(IMAGE_SOURCE));
		LOGGER.info("sourceImage={}", sourceImage);
		final var targetImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_RGB);
	}

}
