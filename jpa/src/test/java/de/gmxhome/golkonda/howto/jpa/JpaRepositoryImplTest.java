package de.gmxhome.golkonda.howto.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.StringContains.containsString;

import java.sql.SQLIntegrityConstraintViolationException;

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

	public static final Logger LOGGER = LogManager.getLogger(JpaRepositoryImpl.class);

	/**
	 * Einen {@linkplain Scanner}-Datensatz in der Datenbank speichern. Falls er schon vorhanden ist,
	 * Fehler ignorieren.
	 * @throws Exception bei technischen Fehlern
	 */
	@Test
	public void testeSave() throws Exception {
		try ( JpaRepository<Scanner, Long> jpaRepository = new JpaRepositoryImpl<>(Scanner.class); ) {
			Scanner scanner = jpaRepository.save(new Scanner(1, "Scanner 1"));
			LOGGER.info("scanner={}", scanner);
			assertThat(scanner, allOf(
					hasProperty("id", notNullValue()),
					hasProperty("scannerId", equalTo(1)),
					hasProperty("bezeichnung", equalTo("Scanner 1"))));
		}
		catch ( Exception e ) {
			// Nur Fehler für doppelte Einträge sind in Ordnung
			assertThat(e.getCause().getCause(), instanceOf(SQLIntegrityConstraintViolationException.class));
			assertThat(e.getCause().getLocalizedMessage(), containsString("Duplicate entry"));
		}
	}

}
