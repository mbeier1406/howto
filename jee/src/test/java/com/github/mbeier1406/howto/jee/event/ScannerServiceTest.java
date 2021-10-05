package com.github.mbeier1406.howto.jee.event;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.github.mbeier1406.howto.jee.event.ScannerRegistry;
import com.github.mbeier1406.howto.jee.event.ScannerService;

/**
 * Tests für {@linkplain ScannerService}.
 * <p>Objekte mit injizierten Objekten ohne Container/CDI testen.</p>
 * @author mbeier
 */
@Ignore
public class ScannerServiceTest {

	public static final Logger LOGGER = LogManager.getLogger(ScannerServiceTest.class);

	/** Der Wert {@value} wird von {@linkplain ScannerRegistry#getRegisterScannerEvent()} zurückgegeben */
	public static final int ERWARTETE_ANZAHL_SCANNER_EVENT = 4711;

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	@Mock
	public ScannerRegistry scannerRegistry;

	/** Das zu testende Objekt */
	public ScannerService scannerService = new ScannerService();

	/**
	 * Initialisierung des Tests:
	 * <ol>
	 * <li>Rückgabewert für {@linkplain #scannerRegistry} konfigurieren</li>
	 * <li>Konfiguriertes Objekt {@linkplain #scannerRegistry} in den Service bringen</li>
	 * </ol>
	 */
	@Before
	public void init() {
		when(scannerRegistry.getRegisterScannerEvent()).thenReturn(ERWARTETE_ANZAHL_SCANNER_EVENT);
		try {
			Class<? extends ScannerService> c = scannerService.getClass();
			Field f = c.getDeclaredField("scannerRegistry");
			f.setAccessible(true);
			f.set(scannerService, scannerRegistry);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			LOGGER.error("", e);
			throw new RuntimeException(e);
		}
	}

	/** Überprüft, ob der Wert {@value #ERWARTETE_ANZAHL_SCANNER_EVENT} vom Service geliefert wird */
	@Test
	public void testeAusgabe() {
		assertThat(scannerService.getRegisterScannerEvent(), equalTo(ERWARTETE_ANZAHL_SCANNER_EVENT));
	}

}
