package com.github.mbeier1406.howto.jse.jvm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.jse.jvm.JavaVersionImpl;
import com.github.mbeier1406.howto.jse.jvm.JavaVersionInterface;
import com.github.mbeier1406.howto.jse.jvm.JavaVersionStdImpl;


/**
 * Tests für das Interfave {@linkplain JavaVersionInterface} und die Implementierung {@linkplain JavaVersionStdImpl}.
 * @author mbeier
 */
public class JavaVersionTest {

	public static final Logger LOGGER = LogManager.getLogger(JavaVersionTest.class);

	/** Das zu testende Objekt in der Standard-Implementierung */
	public JavaVersionInterface javaVersionStd = new JavaVersionStdImpl();

	/** Das zu testende Objekt in der speziellen Implementierung */
	public JavaVersionInterface javaVersionSpez = new JavaVersionImpl();

	/** {@linkplain JavaVersionStdImpl} und {@linkplain JavaVersionImpl} müssen die gleiche Version liefern */
	@Test
	public void testeVersionString() {
		LOGGER.info("{}, {}", javaVersionStd.getVersion(), javaVersionSpez.getVersion());
		assertThat(javaVersionStd.getVersion(), equalTo(javaVersionSpez.getVersion()));
	}

	/** Prüfen, ob die Major/Minor/Patch-Number in allen Fällen korrekt ermittelt wird */
	@Test
	public void testeMajorNumber() {
		System.setProperty("java.version", "1.8.2_172"); // bis Java 8
		assertThat(javaVersionStd.getMajor(), equalTo(8));
		assertThat(javaVersionStd.getMinor().get(), equalTo(2));
		assertThat(javaVersionStd.getPatch().get(), equalTo(172));
		System.setProperty("java.version", "9.3.2"); // > Java 8
		assertThat(javaVersionStd.getMajor(), equalTo(9));
		assertThat(javaVersionStd.getMinor().get(), equalTo(3));
		assertThat(javaVersionStd.getPatch().get(), equalTo(2));
		System.setProperty("java.version", "16");	// OpenJDK
		assertThat(javaVersionStd.getMajor(), equalTo(16));
		assertThat(javaVersionStd.getMinor(), equalTo(Optional.empty()));
		assertThat(javaVersionStd.getPatch(), equalTo(Optional.empty()));
	}

}
