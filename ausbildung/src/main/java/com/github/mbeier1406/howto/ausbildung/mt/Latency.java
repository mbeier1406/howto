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
		ALPHA(0xff000000, 24),
		RED(0x00ff0000, 16),
		GREEN(0x0000ff00, 8),
		BLUE(0x000000ff, 0);
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

}
