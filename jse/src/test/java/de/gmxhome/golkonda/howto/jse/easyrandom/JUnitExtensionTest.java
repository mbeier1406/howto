package de.gmxhome.golkonda.howto.jse.easyrandom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.gmxhome.golkonda.howto.jse.easyrandom.Datenspeicher;
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

    /** Erzeugt eine Liste mit fünf Einträgen */
    @Random(size = 5, type = Datenspeicher.class)
    private List<Datenspeicher> listOfDatenspeicher;

    /** Prüfen, ob {@linkplain #listOfDatenspeicher} fünf Elemente hat */
    @Test
    public void pruefeLaengeListOfDatenspeicher() {
    	LOGGER.info("Anzahl Elemente listOfDatenspeicher={}", listOfDatenspeicher.size());
    	assertThat(listOfDatenspeicher.size(), is(5));
    	assertThat(listOfDatenspeicher.get(0).getZeichenKette(), is(notNullValue()));
    }

}
