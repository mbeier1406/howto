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
		return cloneCameraList(listOfCameras);
	}

	@Override
	public String toString() {
		return "StdScannerImpl [scannerId=" + scannerId + ", scannerName=" + scannerName + ", listOfCameras="
				+ Arrays.toString(listOfCameras) + "]";
	}

}
