package com.github.mbeier1406.howto.ausbildung.arrays.impl;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;

import com.github.mbeier1406.howto.ausbildung.arrays.Scanner;

/**
 * Eine einfache Implementierung für {@linkplain Scanner} zur Demonstration des
 * Arbeitens mit Arrays.
 * @author mbeier
 */
public class StdScannerImpl implements Scanner {

	int scannerId;
	String scannerName;
	public Camera[] listOfCameras = new Camera[0];

	public StdScannerImpl(int scannerId, String scannerName) {
		this.scannerId = scannerId;
		this.scannerName = scannerName;
	}

	/** {@inheritDoc} */
	@Override
	public void setCameras(Camera... cameras) {
		this.listOfCameras = cameras;
	}

	/** {@inheritDoc} */
	@Override
	public void setCameras(int len, IntFunction<Camera> f) {
		this.listOfCameras = new Camera[len];
		Arrays.setAll(this.listOfCameras, f);
	}

	/** {@inheritDoc} */
	@Override
	public Camera[] getCameras() {
		return this.cloneCameraList(this.listOfCameras);
	}

	/** {@inheritDoc} */
	@Override
	public int getNumCameras() {
		return Objects.requireNonNull(this.listOfCameras, "Keine Liste definiert!").length;
	}

	/** {@inheritDoc} */
	@Override
	public String camerasAsString() {
		return Arrays.toString(this.listOfCameras);
	}

	/** {@inheritDoc} */
	@Override
	public Camera[] leftShiftCameras() {
		Camera c = this.listOfCameras[0];
		System.arraycopy(this.listOfCameras, 1, this.listOfCameras, 0, this.listOfCameras.length-1);
		this.listOfCameras[this.listOfCameras.length-1] = c;
		return this.cloneCameraList(this.listOfCameras);
	}

	/** {@inheritDoc} */
	@Override
	public Camera[] sortCameras() {
		Arrays.sort(this.listOfCameras);
		return getCameras();
	}

	/** {@inheritDoc} */
	@Override
	public int getAvgResolution() {
		/* Alternativ Setter in Camera erzeugen und diesen Code verwenden
		Arrays.parallelPrefix(this.listOfCameras, (c1, c2) -> {
				Camera c = new Camera(c2.getNummer(), c2.getResolution(), c2.getName());
				c.setSumResolution(((c1.getSumResolution()==0?c1.getResolution():c1.getSumResolution())+c2.getResolution()));
				return c;
			});
		*/
		Arrays.parallelPrefix(this.listOfCameras, Camera::calcSumResolution);
		return this.listOfCameras[this.listOfCameras.length-1].getSumResolution() / this.listOfCameras.length; // letzte Kamera enthält das Ergebnis
	}

	/** {@inheritDoc} */
	@Override
	public boolean listOfCameraEquals(Camera[] cameras) {
		return Arrays.deepEquals(this.listOfCameras, cameras);
	}

	/** {@inheritDoc} */
	@Override
	public Camera[] appendCameras(Camera[] cameras) {
		Camera[] tmp= Arrays.copyOf(this.listOfCameras, this.listOfCameras.length+cameras.length);
		System.arraycopy(cameras, 0, tmp, this.listOfCameras.length, cameras.length);
		this.listOfCameras = tmp;
		return getCameras();
	}

	/** {@inheritDoc} */
	@Override
	public Optional<Camera> findCamera(int nummer) {
		sortCameras();
		int index = Arrays.binarySearch(this.listOfCameras, new Camera(nummer, 0, ""));
		return index < 0 ? Optional.empty() : Optional.of(this.listOfCameras[index]);
	}

	@Override
	public String toString() {
		return "StdScannerImpl [scannerId=" + scannerId + ", scannerName=" + scannerName + ", listOfCameras="
				+ Arrays.toString(listOfCameras) + "]";
	}

}
