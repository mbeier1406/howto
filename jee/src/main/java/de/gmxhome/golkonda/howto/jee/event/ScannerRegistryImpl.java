package de.gmxhome.golkonda.howto.jee.event;

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

	private int numScannerStart = 0, numScannerStop = 0;

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
	public int getRegisterScannerStart() {
		return numScannerStart;
	}

	/** {@inheritDoc} */
	@Override
	public int getRegisterScannerStop() {
		return numScannerStop;
	}

	@Override
	public String toString() {
		return "ScannerRegistryImpl [numScannerStart=" + numScannerStart + ", numScannerStop=" + numScannerStop + "]";
	}

}
