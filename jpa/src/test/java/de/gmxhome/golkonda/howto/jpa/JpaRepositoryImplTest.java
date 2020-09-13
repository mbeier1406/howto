package de.gmxhome.golkonda.howto.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.StringContains.containsString;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import de.gmxhome.golkonda.howto.jpa.model.Scanner;

/**
 * Tests für die Klasse {@linkplain JpaRepositoryImpl}.
 * @author mbeier
 * @see JpaRepository
 */
public class JpaRepositoryImplTest {

	public static final Logger LOGGER = LogManager.getLogger(JpaRepositoryImplTest.class);

	/** Scannerbezeichnung für übergreifende Tests ist {@value} */
	private static final String SCANNER_1X = "Scanner 1X";

	/** Scannernummer für übergreifende Tests ist {@value} */
	private static final int SCANNER1_ID = 1;

	/**
	 * Prüft, ob das Ergebnis einer DB-Operation dem erwarteten Ergebnis entspricht.
	 * @param scanner das zu prüfende Scanner-Objekt nach der Datenbankoperation
	 * @param scannerNummer die erwartete Scanner-Nummer
	 * @param scannerBezeichnung die erwartete Scanner-Bezeichnung
	 */
	public void pruefeErgebnisDBOperation(Scanner scanner, int scannerNummer, String scannerBezeichnung) {
		LOGGER.info("scanner={}", scanner);
		assertThat(scanner, allOf(
				hasProperty("id", notNullValue()),
				hasProperty("scannerId", equalTo(scannerNummer)),
				hasProperty("bezeichnung", equalTo(scannerBezeichnung))));		
	}

	/**
	 * Einen {@linkplain Scanner}-Datensatz in der Datenbank speichern und danach aktualisieren. Falls er schon vorhanden ist,
	 * Fehler ignorieren.
	 * @throws Exception bei technischen Fehlern
	 */
	@Test
	public void testeSaveAndMerge() throws Exception {
		try ( JpaRepository<Scanner, Long> jpaRepository = new JpaRepositoryImpl<>(Scanner.class); ) {
			int scannerId = SCANNER1_ID;
			String scannerBezeichnung1 = "Scanner 1", scannerBezeichnung2 = SCANNER_1X;
			Scanner scanner = new Scanner(scannerId, scannerBezeichnung1);
			pruefeErgebnisDBOperation(jpaRepository.save(scanner), scannerId, scannerBezeichnung1);
			scanner.setBezeichnung(scannerBezeichnung2);
			pruefeErgebnisDBOperation(jpaRepository.merge(scanner), scannerId, scannerBezeichnung2);
		}
		catch ( Exception e ) {
			// Nur Fehler für doppelte Einträge sind in Ordnung
			assertThat(e.getCause().getCause(), instanceOf(SQLIntegrityConstraintViolationException.class));
			assertThat(e.getCause().getLocalizedMessage(), containsString("Duplicate entry"));
		}
	}

	/**
	 * Einen {@linkplain Scanner}-Datensatz aus der Datenbank laden.
	 * @throws Exception bei technischen Fehlern
	 */
	@Test
	public void ladeScanner() throws Exception {
		try ( JpaRepository<Scanner, Long> jpaRepository = new JpaRepositoryImpl<>(Scanner.class); ) {
			CriteriaBuilder cb = jpaRepository.getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Scanner> cq = (CriteriaQuery<Scanner>) cb.createQuery(Scanner.class);
			Root<Scanner> root = (Root<Scanner>) cq.from(Scanner.class);
			cq.select(root).where(cb.equal(root.get("bezeichnung"), SCANNER_1X));
			Optional<List<Scanner>> scanner = jpaRepository.getResultList(cq);
			LOGGER.info("scanner={}", scanner);
			assertThat("Kein Scanner gefunden!", scanner.isPresent(), is(true));
			assertThat("Nicht genau ein Scanner gefunden; Treffer: "+scanner.get().size(), scanner.get(), hasSize(1));
			pruefeErgebnisDBOperation(scanner.get().get(0), SCANNER1_ID, SCANNER_1X);
		}
	}

}
