package com.github.mbeier1406.howto.ausbildung.mt;

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

	public static enum ARGB {
		ALPHA(0xff000000),
		RED(0x00ff0000),
		GREEN(0x0000ff00),
		BLUE(0x000000ff);
		private int mask;
		private ARGB(int mask) {
			this.mask = mask;
		}
		public int getMask() {
			return this.mask;
		}
	}

	/**
	 * Liefert zu einem ARGB-Pixel den entsprechenden Alpha, rot, grün bzw. blau-Wert.
	 * @param argb der zu extrahierende Wert
	 * @param value der Pixel von dem der Wert ermittelt werden soll
	 * @return den entsprechenden ARGB-Wert
	 */
	public static int getArgb(ARGB argb, int value) {
		return value & argb.getMask();
	}

}
