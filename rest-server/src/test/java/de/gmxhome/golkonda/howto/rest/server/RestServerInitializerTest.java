package de.gmxhome.golkonda.howto.rest.server;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * 
 * @author mbeier
 *
 */
@RunWith(Arquillian.class)
public class RestServerInitializerTest {

	public static final Logger LOGGER = LogManager.getLogger(RestServerInitializerTest.class);

	@Deployment
	public static WebArchive createDeployment() {
		String simpleName = RestServerInitializer.class.getSimpleName();
	    String archiveName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);

	    LOGGER.atInfo();
	    JavaArchive testJar = ShrinkWrap
	    		.create(JavaArchive.class)
	            .addPackage(RestServerInitializer.class.getPackage())
	            //.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
	            //.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
	            ;
//return testJar;
	    		return ShrinkWrap
				.create(WebArchive.class)
				.addAsLibraries(testJar)
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	public RestServer restServer;

	@Before
	public void init() {
		LOGGER.info("aaaa");
		restServer.log();
	}

	@Test
	public void testeStartup() {
		LOGGER.info("XXXXXXXXXX");
	}

}
