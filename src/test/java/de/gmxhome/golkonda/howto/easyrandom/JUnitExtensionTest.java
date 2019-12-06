package de.gmxhome.golkonda.howto.easyrandom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;

/**
 * Beispiele für <a href="https://glytching.github.io/junit-extensions/randomBeans">JUnit RandomBeansExtension</a>.
 * @author mbeier
 */
@ExtendWith(RandomBeansExtension.class)
public class JUnitExtensionTest {

	public static final Logger LOGGER = LogManager.getLogger(DatenspeicherTest.class);

	/** Erzeugt eine zufällige Zeichenkette, die als Methodenparameter übergeben wird */
    @Test
    @ExtendWith(RandomBeansExtension.class)
    public void wertAlsMethodenParameter(@Random String testStr) {
    	LOGGER.info("testStr={}", testStr);
    	assertThat(testStr, is(notNullValue()));
    }

}
