package com.github.mbeier1406.howto.ausbildung.arrays;

import java.util.Arrays;

import com.github.mbeier1406.howto.ausbildung.arrays.impl.StdScannerImpl;

/**
 * Definiert Methoden zur Demonstration des Arbeitens mit Arrays
 * anhand des Beispiels eines Scanners mit einem Feld (Array/Reihung) von Cameras.
 * @author mbeier
 * @see StdScannerImpl
 */
public interface Scanner {

	/**
	 * Der Scanner soll ein Array von Cameras enthalten.
	 * Die Methode {@linkplain Camera#equals(Object)} wird benötigt, um
	 * die Instanzen von zwei Cameras  auf Gleichheit überprüfen zu können.
	 * Die Methode {@linkplain Camera#clone()} wird zum Klonen verwendet, damit
	 * der Scanner eine Kopie der Liste der Cameras zurückgeben kann (zum Beispiel
	 * in {@linkplain Scanner#getCameras()}), damit die interne Liste "von außen"
	 * nicht geändert werden kann. Die Klasse implementiert das {@linkplain Comparable}-Interface,
	 * damit das Sortieren der Kameras funktioniert ({@linkplain Scanner#sortCameras()} über
	 * die {@linkplain #nummer} aufsteigend). Die Methode {@linkplain #calcSumResolution(Camera, Camera)}
	 * soll die Verwendung von {@linkplain Arrays#parallelPrefix(Object[], java.util.function.BinaryOperator))}
	 * demonstrieren.
	 */
	public class Camera implements Comparable<Camera> {
		private final int nummer;
		private final int resolution;
		private final String name;
		private int sumResolution = 0; // ab der zweiten Kamera != 0 in 
		public Camera(int nummer, int resolution, String name) {
			super();
			this.nummer = nummer;
			this.resolution = resolution;
			this.name = name;
		}
		public int getNummer() {
			return nummer;
		}
		public int getResolution() {
			return resolution;
		}
		public String getName() {
			return name;
		}
		public int getSumResolution() {
			return sumResolution;
		}
		public static Camera calcSumResolution(Camera c1, Camera c2) {
			c2.sumResolution = ((c1.sumResolution==0?c1.resolution:c1.sumResolution) + c2.resolution);
			return c2;
		}
		@Override
		public boolean equals(Object o) {
		    if ( this == o ) return true;
		    if ( o == null ) return false;
		    if ( getClass() != o.getClass() ) return false;
		    Camera c = (Camera) o;
			return  name.equals(c.getName()) &&
					resolution == c.getResolution() &&
					nummer == c.getNummer();
		}
		@Override
		public Camera clone() {
			return new Camera(this.nummer, this.resolution, this.name);
		}
		@Override
		public String toString() {
			return "Camera [nummer=" + nummer + ", resolution=" + resolution + ", name=" + name + ", sumResolution="
					+ sumResolution + "]";
		}
		@Override
		public int compareTo(Camera o) {
			return Integer.compare(this.nummer, o.nummer);
		}
	}


	/**
	 * Setzt eine Liste von Kameras in einem Scanner, die evtl. bereits enthaltene
	 * Liste wird ersetzt. Alternativ wäre diese Signatur:
	 * {@code public void setCamears(Camera[] cameras);}.
	 * Demonstriert <i>vararg</i> Parameter.
	 * @param cameras Auflistung der Kameras des Scanners
	 */
	public void setCameras(Camera... cameras);

	/**
	 * Liefet eine <u>Kopie</u> der im Scanner gesetzten Kameras (demonstriert das Kopieren/Clonen von Arrays).
	 * @return Kopie der Liste der Kameras
	 * @see #cloneCameraList(Camera[])
	 */
	public Camera[] getCameras();

	/**
	 * Liste der Kameras als String, demonstriert {@linkplain Arrays#toString()}.
	 * @return die Kameras als Zeichenkette
	 */
	public String camerasAsString();

	/**
	 * Ändert die Kamera-Reihenfole im Scanner nach links, die im ersten Index <i>0</i> wird auf den letzten
	 * Platz (Index <i>Length-1</i>) verschoben (demonstriert {@linkplain System#arraycopy(Object, int, Object, int, int)}).
	 * @return eine Kopie der neuen Kameraliste im Scanner
	 */
	public Camera[] leftShiftCameras();

	/**
	 * Sortiert die Kameralist aufsteigend nach der {@linkplain Camera#nummer} (demonstriert
	 * die Nutzung von {@linkplain Arrays#sort(Object[])})
	 * @return eine Kopie der neuen Kameraliste im Scanner
	 */
	public Camera[] sortCameras();

	/**
	 * Liefert die durchschnittliche Auflösung über alle Kameras.
	 * <b>Achtung</b>: die Methode Ändert das (interne) Feld {@linkplain Camera#sumResolution} in
	 * allen Kameras Index > 0 in {@link Scanner#getCameras()} auf die jeweiligeSumme bis Index. Es wird der Wert der letzen
	 * Kamera in der Liste geteilt durch die Anzahl zurückgegeben. Die Methode soll die Verwendung von
	 * {@linkplain Arrays#parallelPrefix(Object[], java.util.function.BinaryOperator))} zeigen.
	 * @return Durchschnittliche Auflösung über alle Kameras in der Liste 
	 */
	public int getAvgResolution();

	/**
	 * Klont ein Array von {@linkplain Camera}s.
	 * @param cameras die zu klonende Liste
	 * @return die geklonte Liste
	 */
	public default Camera[] cloneCameraList(Camera[] cameras) {
	    return java.util.Arrays.stream(cameras).map(camera -> camera.clone()).toArray(Camera[]::new);
	}

}
