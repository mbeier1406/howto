package com.github.mbeier1406.howto.ausbildung.arrays.impl;

import java.util.Arrays;

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
	public Camera[] getCameras() {
		return this.cloneCameraList(this.listOfCameras);
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

	@Override
	public String toString() {
		return "StdScannerImpl [scannerId=" + scannerId + ", scannerName=" + scannerName + ", listOfCameras="
				+ Arrays.toString(listOfCameras) + "]";
	}

}
