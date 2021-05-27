package de.gmxhome.golkonda.howto.jse.jvm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain JavaVersionImpl}.
 * @author mbeier
 */
public class JavaVersionImplTest {

	public static final Logger LOGGER = LogManager.getLogger();

	/** Definiert einen {@linkplain VersionProvider}, der nur die Major liefert */
	@VersionProvider(provider = JavaVersionMajor.class)
	public static class JavaVersionMajor extends JavaVersionImpl implements JavaVersionInterface {
		public static List<Integer> version() {
			return Arrays.asList(15);
		}
	}

	/** Definiert einen {@linkplain VersionProvider}, der die Major und Minor liefert */
	@VersionProvider(provider = JavaVersionMajorUndMinor.class)
	public static class JavaVersionMajorUndMinor extends JavaVersionImpl implements JavaVersionInterface {
		public static List<Integer> version() {
			return Arrays.asList(16, 1);
		}
	}

	/** Definiert einen {@linkplain VersionProvider}, der die Major, Minor und den Patch liefert */
	@VersionProvider(provider = JavaVersionMajorUndMinorUndPatch.class)
	public static class JavaVersionMajorUndMinorUndPatch extends JavaVersionImpl implements JavaVersionInterface {
		public static List<Integer> version() {
			return Arrays.asList(13, 1, 134);
		}
	}

	/** Definiert einen {@linkplain VersionProvider} im Format bis Java9 */
	@VersionProvider(provider = JavaVersionBisJava9.class)
	public static class JavaVersionBisJava9 extends JavaVersionImpl implements JavaVersionInterface {
		public static List<Integer> version() {
			return Arrays.asList(1, 8, 0, 172);
		}
	}

	/** Provider für die Major-Number */
	public JavaVersionInterface javaVersionMajor = new JavaVersionMajor();

	/** Provider für die Major und Minor-Number */
	public JavaVersionInterface javaVersionMajorUndMinor = new JavaVersionMajorUndMinor();

	/** Provider für die Major und Minor-Number und Pacth-Level */
	public JavaVersionInterface javaVersionMajorUndMinorUndPatch = new JavaVersionMajorUndMinorUndPatch();

	/** Provider für die Version bis Java 9 */
	public JavaVersionInterface javaVersionBisJava9 = new JavaVersionBisJava9();

	/** Prüft, ab die Versionsnummer korrekt berechnet wird: nur Major */
	@Test
	public void testJavaVersionMajor() {
		String version = javaVersionMajor.getVersion();
		LOGGER.info("Version: {}", version);
		assertThat(version, equalTo("15"));
	}

	/** Prüft, ab die Versionsnummer korrekt berechnet wird: Format Major.Minor */
	@Test
	public void testJavaVersionMajorUndMinor() {
		String version = javaVersionMajorUndMinor.getVersion();
		LOGGER.info("Version: {}", version);
		assertThat(version, equalTo("16.1"));
	}

	/** Prüft, ab die Versionsnummer korrekt berechnet wird: Format Major.Minor.Patch */
	@Test
	public void testJavaVersionMajorUndMinorUndPatch() {
		String version = javaVersionMajorUndMinorUndPatch.getVersion();
		LOGGER.info("Version: {}", version);
		assertThat(version, equalTo("13.1.134"));
	}

	/** Prüft, ab die Versionsnummer korrekt berechnet wird: Format Major.Minor.Patch */
	@Test
	public void testJavaVersionBisJava9() {
		String version = javaVersionBisJava9.getVersion();
		LOGGER.info("Version: {}", version);
		assertThat(version, equalTo("1.8.0_172"));
	}

}
