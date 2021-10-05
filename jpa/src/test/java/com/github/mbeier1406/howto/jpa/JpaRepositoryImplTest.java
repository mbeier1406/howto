package com.github.mbeier1406.howto.jpa;

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
import org.junit.Ignore;
import org.junit.Test;

import com.github.mbeier1406.howto.jpa.JpaRepository;
import com.github.mbeier1406.howto.jpa.JpaRepositoryImpl;
import com.github.mbeier1406.howto.jpa.model.Scanner;

/**
 * Tests für die Klasse {@linkplain JpaRepositoryImpl}.
 * @author mbeier
 * @see JpaRepository
 */
@Ignore
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
	 * Einen {@linkplain Scanner}-Datensatz in der Datenbank speichern, danach aktualisieren und löschen.
	 * Falls er schon vorhanden ist, Fehler ignorieren.
	 * @throws Exception bei technischen Fehlern
	 */
	@Test
	public void testeWorkflow() throws Exception {
		try ( JpaRepository<Scanner, Long> jpaRepository = new JpaRepositoryImpl<>(Scanner.class); ) {
			int scannerId = SCANNER1_ID;
			String scannerBezeichnung1 = "Scanner 1", scannerBezeichnung2 = SCANNER_1X;
			Scanner scanner = new Scanner(scannerId, scannerBezeichnung1);
			try {
				pruefeErgebnisDBOperation(jpaRepository.save(scanner), scannerId, scannerBezeichnung1);
			}
			catch ( Exception e ) {
				// Nur Fehler für doppelte Einträge sind in Ordnung
				assertThat(e.getCause().getCause(), instanceOf(SQLIntegrityConstraintViolationException.class));
				assertThat(e.getCause().getLocalizedMessage(), containsString("Duplicate entry"));
				CriteriaBuilder cb = jpaRepository.getEntityManager().getCriteriaBuilder();
				CriteriaQuery<Scanner> cq = (CriteriaQuery<Scanner>) cb.createQuery(Scanner.class);
				Root<Scanner> root = (Root<Scanner>) cq.from(Scanner.class);
				cq.select(root).where(cb.equal(root.get("bezeichnung"), SCANNER_1X));
				Optional<List<Scanner>> scannerList = jpaRepository.getResultList(cq);
				LOGGER.info("scanner={}", scanner);
				assertThat("Kein Scanner gefunden!", scannerList.isPresent(), is(true));
				assertThat("Nicht genau ein Scanner gefunden; Treffer: "+scannerList.get().size(), scannerList.get(), hasSize(1));
				pruefeErgebnisDBOperation(scannerList.get().get(0), SCANNER1_ID, SCANNER_1X);
				scanner = scannerList.get().get(0);
			}
			scanner.setBezeichnung(scannerBezeichnung2);
			pruefeErgebnisDBOperation(jpaRepository.merge(scanner), scannerId, scannerBezeichnung2);
			pruefeErgebnisDBOperation(jpaRepository.findById(scanner.getId()).get(), scannerId, scannerBezeichnung2);
			jpaRepository.delete(scanner);
		}
	}

}
