package de.gmxhome.golkonda.howto.jee.event;

import static org.hamcrest.MatcherAssert.assertThat;
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
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test für die Klasse {@linkplain ScannerEventSource}
 * @author mbeier
 */
@RunWith(Arquillian.class)
public class ScannerEventSourceTest {

	public static final Logger LOGGER = LogManager.getLogger(ScannerEventSourceTest.class);

	/** Das zu testende Objekt */
	@Inject
	public ScannerEventSource scannerEventSource;

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
	}

	/** Einen "Scanner startet"-Event senden */
	@Test
	public void testeScannerStart() {
		scannerEventSource.sendEventStart(1, "daten");
	}

	/** Einen "Scanner stoppt"-Event senden */
	@Test
	public void testeScannerStop() {
		scannerEventSource.sendEventStop(1, "daten");
	}

}
