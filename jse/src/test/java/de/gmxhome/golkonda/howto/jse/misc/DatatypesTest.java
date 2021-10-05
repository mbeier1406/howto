package de.gmxhome.golkonda.howto.jse.misc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für {@linkplain Datatypes}
 * @author mbeier
 */
public class DatatypesTest {

	public static final Logger LOGGER = LogManager.getLogger(DatatypesTest.class);

	@Test
	public void testeGetMaxShort() {
		assertThat(Datatypes.getMaxShort(), equalTo((short) 32767));
	}

}
