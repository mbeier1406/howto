package de.gmxhome.golkonda.howto.jse.jvm;

import java.util.Optional;

/**
 * Liefert die Java Runtime-Version:
 * <ol>
 * <li>Bis einschließlich Java 8 im Format {@code 1.X.Y_Z}</li>
 * <li>Ab Java 9 im Format {@code X.Y.Z} (<i>semantic versioning</i>)</li>
 * <li>OpenJDK verwendet ggf. auch andere Versionschemata, z. B. nur eine Versionsnummer</li>
 * </ol>
 * Mit <b>X</b> als major number, <b>Y</b> als minor number, und <b>Z</b> als patchlevel.
 * @author mbeier
 * @see <a href="https://semver.org/lang/de/">Semantic Versioning</a>
 */
public interface JavaVersionInterface {

	/** Property-Key für die Java-Version ist {@value} */
	public static final String JAVA_VERSION_PROPERTY = "java.version";

	/**
	 * Liefert die Version der JVM.
	 * Standardmäßig aus der System-Property {@value #JAVA_VERSION_PROPERTY}.
	 * Format ist (je nach Oracle oder OpenjDK) <code>X[.Y[.Z[_N]]]</code>
	 * @return Die Version als Zeichenkette
	 */
	public default String getVersion() {
		return System.getProperty(JAVA_VERSION_PROPERTY);
	}

	/**
	 * Major-Number, bis Java 8 Stelle zwei, sonst eins.
	 * @return die Major-Number
	 * @throws NumberFormatException falls {@linkplain #getVersion()} nicht das erwartete Format liefert
	 */
	public int getMajor() throws NumberFormatException;

	/**
	 * Minor-Number, bis Java 8 Stelle drei, sonst zwei.
	 * @return die Major-Number
	 * @throws NumberFormatException falls {@linkplain #getVersion()} nicht das erwartete Format liefert
	 */
	public Optional<Integer> getMinor() ;

	/**
	 * Patch level, bis Java 8 Stelle vier, sonst drei.
	 * @return die Major-Number
	 * @throws NumberFormatException falls {@linkplain #getVersion()} nicht das erwartete Format liefert
	 */
	public Optional<Integer> getPatch() throws NumberFormatException;

}
