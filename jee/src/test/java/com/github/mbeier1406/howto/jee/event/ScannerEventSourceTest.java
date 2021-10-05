package com.github.mbeier1406.howto.jee.event;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.mbeier1406.howto.jee.event.ScannerEventSource;
import com.github.mbeier1406.howto.jee.event.ScannerRegistry;
import com.github.mbeier1406.howto.jee.event.ScannerStatusObserver;

/**
 * Test für die Klasse {@linkplain ScannerEventSource}
 * @author mbeier
 */
@Ignore
@RunWith(Arquillian.class)
public class ScannerEventSourceTest {

	public static final Logger LOGGER = LogManager.getLogger(ScannerEventSourceTest.class);

	/** Das zu testende Objekt */
	@Inject
	public ScannerEventSource scannerEventSource;

	/** Registratur für Start-Events zur Funktionsprüfung */
	@Inject
	public ScannerRegistry scannerRegistry;

	/** Status-Observer muss explizit erzeugt werden, da er {@code Reception.IF_EXISTS} verwendet! */
	@Inject
	public ScannerStatusObserver scannerStatusObserver;

	/** Das zu testende Archiv mit dem Objekt {@linkplain ScannerEventSource} */
	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap
				.create(JavaArchive.class)
				.addPackage(ScannerEventSource.class.getPackage())
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	/** Prüfen, ob der Container/CDI funktioniert */
	@Before
	public void init() {
		assertThat(scannerEventSource, notNullValue());
		scannerStatusObserver.toString(); // CDI lazy initialization
	}

	/** Einen "Scanner startet"-Event senden, StatusEvent muss ebenfalls gesetzt sein */
	@Test
	public void testeScannerStart() {
		scannerEventSource.sendEventStart(0, "daten");
		assertThat(scannerRegistry.getRegisterScannerStart(), is(1));
		assertThat(scannerRegistry.getRegisterScannerEvent(), anyOf(equalTo(1), equalTo(2)));
	}

	/** Einen "Scanner stoppt"-Event senden, StatusEvent muss ebenfalls gesetzt sein */
	@Test
	public void testeScannerStop() {
		scannerEventSource.sendEventStop(0, "daten");
		assertThat(scannerRegistry.getRegisterScannerStop(), is(1));
		assertThat(scannerRegistry.getRegisterScannerEvent(), anyOf(equalTo(1), equalTo(2)));
	}

}
