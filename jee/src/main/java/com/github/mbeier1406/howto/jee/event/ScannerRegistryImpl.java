package com.github.mbeier1406.howto.jee.event;

import javax.enterprise.context.ApplicationScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Einfache Implementierung der {@linkplain ScannerRegistry}, voll synchronisiert und damit wenig performant für einfache Tests.
 * @author mbeier
 */
@ApplicationScoped
public class ScannerRegistryImpl implements ScannerRegistry {

	public static final Logger LOGGER = LogManager.getLogger(ScannerRegistryImpl.class);

	private int numScannerStart = 0, numScannerStop = 0, numScannerEvent = 0;

	/** {@inheritDoc} */
	@Override
	public synchronized void registerScannerStart() {
		numScannerStart++;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void registerScannerStop() {
		numScannerStop++;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void registerScannerEvent() {
		numScannerEvent++;
	}

	/** {@inheritDoc} */
	@Override
	public int getRegisterScannerStart() {
		return numScannerStart;
	}

	/** {@inheritDoc} */
	@Override
	public int getRegisterScannerStop() {
		return numScannerStop;
	}

	/** {@inheritDoc} */
	@Override
	public int getRegisterScannerEvent() {
		return numScannerEvent;
	}

	@Override
	public String toString() {
		return "ScannerRegistryImpl [numScannerStart=" + numScannerStart + ", numScannerStop=" + numScannerStop
				+ ", numScannerEvent=" + numScannerEvent + "]";
	}

}
