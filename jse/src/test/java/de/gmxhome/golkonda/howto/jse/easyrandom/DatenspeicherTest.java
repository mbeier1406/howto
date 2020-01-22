package de.gmxhome.golkonda.howto.jse.easyrandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.TypePredicates;
import org.jeasy.random.api.Randomizer;
import org.junit.Before;
import org.junit.Test;

import de.gmxhome.golkonda.howto.jse.easyrandom.Datenspeicher;
import de.gmxhome.golkonda.howto.jse.easyrandom.DatumsSpeicher;

/**
 * Tests für die Klasse {@linkplain Datenspeicher}.
 * @author mbeier
 */
public class DatenspeicherTest {

	public static final Logger LOGGER = LogManager.getLogger(DatenspeicherTest.class);

	/** Mit Hilfe des {@linkplain EasyRandom}-Generators wird der {@linkplain Datenspeicher} mit Testdaten befüllt */
	public EasyRandom generator;

	/** Mit den {@linkplain EasyRandomParameters} werden die erzeugten Zufallswerte konfiguriert */
	public EasyRandomParameters parameters;

	/** Die minimale Länge eines Teststrings soll {@value} Zeichen sein */
	public static final int MIN_STR_LEN = 5;

	/** Die maximale Länge eines Teststrings soll {@value} Zeichen sein */
	public static final int MAX_STR_LEN = 10;

	/** Die minimale Länge einer Liste in {@linkplain Datenspeicher#getListOfDatumsSpeicher()} ist {@value} */
	public static final int MIN_LIST_LEN = 2;

	/** Die maximale Länge einer Liste in {@linkplain Datenspeicher#getListOfDatumsSpeicher()} ist {@value} */
	public static final int MAX_LIST_LEN = 4;


	/**
	 * Erzeugt ein {@linkplain DatumsSpeicher}-Objekt mit einem zufälligen Datum, dass
	 * bis zu zehn Monate in der Zukunft liegt.
	 * @author mbeier
	 * @see Randomizer
	 */
	public class DatumsSpeicherRandomizer implements Randomizer<DatumsSpeicher> {
		/** {@inheritDoc}} */
		@Override
		public DatumsSpeicher getRandomValue() {
			return new DatumsSpeicher().setDatum(LocalDate.now().plusMonths((int) (Math.random()*10)));
		}
	}

	/** Initialisierung von {@linkplain #generator} und {@linkplain #parameters} vor den Tests */
	@Before
	public void init() {
		parameters = new EasyRandomParameters()
				.stringLengthRange(MIN_STR_LEN, MAX_STR_LEN)
				.excludeField(FieldPredicates.named("nichtBenutzteZeichenKette").and(FieldPredicates.inClass(Datenspeicher.class)))
				.collectionSizeRange(MIN_LIST_LEN, MAX_LIST_LEN)
				.excludeType(TypePredicates.inPackage("x.y.z")) // nicht existierendes Package ausschließen
				.randomize(DatumsSpeicher.class, new DatumsSpeicherRandomizer()); // generiertes Datum soll in der Zukunft liegen 
		generator = new EasyRandom(parameters);
	}

	/** Testen, ob die Datenstruktur wie konfiguriert gefüllt wird */
	@Test
	public void testeDatenErzeugung() {
		Datenspeicher datenspeicher = generator.nextObject(Datenspeicher.class);
		LOGGER.info("datenspeicher={}", datenspeicher);
	    assertNotNull("Feld 'ganzeZahl' wurde nicht gesetzt!", datenspeicher.getGanzeZahl());
	    assertNotNull("Feld 'zeichenKette' wurde nicht gesetzt!", datenspeicher.getZeichenKette());
	    assertTrue("Feld 'nichtBenutzteZeichenKette' ist nicht null!", datenspeicher.getNichtBenutzteZeichenKette() == null);
	    assertTrue("Feld 'zeichenKette' hat nicht die korrekte Länge!",
	    		datenspeicher.getZeichenKette().length() >= MIN_STR_LEN && datenspeicher.getZeichenKette().length() <= MAX_STR_LEN );
	    assertTrue("Feld 'datumsSpeicher' liegt nicht in der Zukunft!", LocalDate.now().compareTo(datenspeicher.getDatumsSpeicher().getDatum()) <= 0);
	    assertTrue("Liste 'listOfDatumsSpeicher' hat nicht die korrekte Länge!", datenspeicher.getListOfDatumsSpeicher().size() >= MIN_LIST_LEN && datenspeicher.getListOfDatumsSpeicher().size() <= MAX_LIST_LEN);
	}

	/** Prüfen, ob eine Liste mit vorgegebener Länge korrekt erzeugt wird */
	@Test
	public void testeListenErzeugung() {
		int erwarteteLaengeListe = 4;
		List<Datenspeicher> listeDatenspeicher = generator
				.objects(Datenspeicher.class, erwarteteLaengeListe)
				.collect(Collectors.toList());
		LOGGER.info("Länge 'listeDatenspeicher'={}", listeDatenspeicher.size());
		assertEquals("Liste hat nicht die erwartete Länge "+erwarteteLaengeListe, erwarteteLaengeListe, listeDatenspeicher.size());
	}

}
