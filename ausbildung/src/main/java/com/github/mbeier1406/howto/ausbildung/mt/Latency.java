package com.github.mbeier1406.howto.ausbildung.mt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	/** Das in einem Thread fertig bearbeitete Image ist {@value} */
	public static final String IMAGE_TARGET_SINGLE = "./target/Cid_single.jpg";

	/** Das in mehreren Threads fertig bearbeitete Image ist {@value} */
	public static final String IMAGE_TARGET_MULTI = "./target/Cid_multi.jpg";


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
		return Math.abs(red-green) < 5 && Math.abs(red-blue) < 5 && Math.abs(green-blue) < 5;
	}

	/**
	 * Übernimmt einen Pixel aus dem Original-Image in das Ziel-Image.
	 * Dabei wird er auf die Farbe schwarz geändert, wenn er als ein Grau-Ton erkannt wird.
	 * @param sourceImage Quell-/Original-Image
	 * @param targetImage Ziel-Image
	 * @param x X-Koordinate des Pixels
	 * @param y Y-Koordinate des Pixels
	 * @see #isShadeOfGray(int, int, int)
	 */
	public static void recolorPixel(BufferedImage sourceImage, BufferedImage targetImage, int x, int y) {
		if ( x < 0 || x >= sourceImage.getWidth() || y < 0 || y >= sourceImage.getHeight() )
			throw new IllegalArgumentException("sourceImage="+sourceImage+"; targetImage="+targetImage+"; x="+x+"; y="+y);
		int rgb = sourceImage.getRGB(x, y);
		int red = getArgb(ARGB.RED, rgb);
		int green = getArgb(ARGB.GREEN, rgb);
		int blue = getArgb(ARGB.BLUE, rgb);
		if ( isShadeOfGray(red, green, blue) )
			red = green = blue = 20;
		rgb = createRgbFromColors(red, green, blue);
		targetImage.getRaster().setDataElements(x, y, targetImage.getColorModel().getDataElements(rgb, null));
	}

	/**
	 * Nimmt aus dem Quell-/Original-Image einem definierten Bereich, ersetzt die Grautöne durch schwarz und schreibt
	 * das Ergebnis in das Ziel-Image.
	 * @param sourceImage Quell-Image
	 * @param targetImage Ziel-Image
	 * @param leftCorner Start des Berechs von links
	 * @param topCorner Start des Bereichs von oben
	 * @param width Ersetzungsbereichs nach rechts
	 * @param height Ersetzungsbereich nach unten
	 */
	public static void recolorImage(BufferedImage sourceImage, BufferedImage targetImage, int leftCorner, int topCorner, int width, int height) {
		for ( int i=leftCorner; i < leftCorner+width && i < sourceImage.getWidth(); i++ )
			for ( int j=topCorner; j < topCorner+height && j < sourceImage.getHeight(); j++ )
				recolorPixel(sourceImage, targetImage, i, j);
	}

	/**
	 * Führt die Verarbeitung in einem einzigen Thread aus.
	 * @param sourceImage Quell-Image
	 * @param targetImage Ziel-Image
	 */
	public static void singleThreadedVerarbeitung(BufferedImage sourceImage, BufferedImage targetImage) {
		recolorImage(sourceImage, targetImage, 0, 0, sourceImage.getWidth(), sourceImage.getHeight());
	}

	/**
	 * Führt die Verarbeitung in mehreren Threads aus.
	 * @param sourceImage Quell-Image
	 * @param targetImage Ziel-Image
	 * @param numberOfThreads Anzahl Threads
	 */
	public static void multiThreadedVerarbeitung(BufferedImage sourceImage, BufferedImage targetImage, int numberOfThreads) {
		int width = sourceImage.getWidth(); // immer das ganze Image von links nach rechts bearbeiten
		int height = sourceImage.getHeight() / numberOfThreads; // jeder Thread bearbeitet einen gleich großen Bereich
		List<Thread> listOfThreads = new ArrayList<>();

		for ( int i=0; i < numberOfThreads; i++ ) {
			final int bereichsNummer = i; // der wievielte Bereich von oben nach unten
			final var t = new Thread(() -> {
				/* Bereich von ganz links nach rechts, ite Teilbereich verarbeiten */
				recolorImage(sourceImage, targetImage, 0, bereichsNummer*height, width, height);				
			});
			listOfThreads.add(t);
			t.start();
		}
		listOfThreads.stream().forEach(t -> { try { t.join(); } catch (InterruptedException e) {} });
	}


	/** Ruft die Image-Verarbeitung sequentiell und paralleleisiert auf und vergleicht die Verarbeitungszeit */
	public static void main(String[] args) throws IOException {
		int anzahlThreads = Integer.parseInt(System.getProperty("anzahlThreads", "3"));
		final var sourceImage = ImageIO.read(new File(IMAGE_SOURCE));
		LOGGER.info("sourceImage={}", sourceImage);
		final var targetImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_RGB);

		long startSingle = System.currentTimeMillis();
		singleThreadedVerarbeitung(sourceImage, targetImage);
		ImageIO.write(targetImage, "jpg", new File(IMAGE_TARGET_SINGLE));
		long endSingle = System.currentTimeMillis();

		long startMulti = System.currentTimeMillis();
		multiThreadedVerarbeitung(sourceImage, targetImage, anzahlThreads);
		ImageIO.write(targetImage, "jpg", new File(IMAGE_TARGET_MULTI));
		long endtMulti = System.currentTimeMillis();

		LOGGER.info("Dauer Single: {}; Dauer Multi: {}", endSingle-startSingle, endtMulti-startMulti);
	}

}
