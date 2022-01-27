package com.github.mbeier1406.howto.ausbildung.arrays;

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
	 * nicht geändert werden kann.
	 */
	public class Camera {
		int nummer;
		int resolution;
		String name;
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
			return "Camera [nummer=" + nummer + ", resolution=" + resolution + ", name=" + name + "]";
		}
	}


	/**
	 * Setzt eine Liste von Kameras in einem Scanner, die evtl. berits enthaltene
	 * Liste wird ersetzt. Alternativ wäre diese Signatur:
	 * {@code public void setCamears(Camera[] cameras);}.
	 * @param cameras Auflistung der Kameras des Scanners
	 */
	public void setCameras(Camera... cameras);

	/**
	 * Liefet eine <u>Kopie</u> der im Scanner gesetzten Kameras.
	 * @return Kopie der Liste der Kameras
	 */
	public Camera[] getCameras();

	/**
	 * Klont ein Array von {@linkplain Camera}s.
	 * @param cameras die zu klonende Liste
	 * @return die geklonte Liste
	 */
	public default Camera[] cloneCameraList(Camera[] cameras) {
	    return java.util.Arrays.stream(cameras).map(camera -> camera.clone()).toArray(Camera[]::new);
	}

}
