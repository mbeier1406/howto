package de.gmxhome.golkonda.howto.jse.jvm;

/**
 * Speziell Implementierung für {@linkplain JavaVersionInterface}, die eine eigene
 * Implementierung von {@linkplain #getVersion()} verwendet.
 * @author mbeier
 * @see JavaVersionStdImpl
 */
public class JavaVersionImpl extends JavaVersionStdImpl implements JavaVersionInterface {

	/** {@inheritDoc} */
	@Override
	public String getVersion() {
		return Runtime.version()
				.version()
				.stream()
				.map(Object::toString)
				.reduce("", (versionAlsString, teilVersionsNummer) -> {
					return versionAlsString.length() == 0 ?
							teilVersionsNummer : // Erste Stelle der Version
								versionAlsString + (
										versionAlsString.startsWith("1") && versionAlsString.matches("\\d\\.\\d\\.\\d") ?
												"_" : // JDK bis Java 1.8 verwendet X.Y.Z_A als hier "_"
													"." // Normales Trennzeichen zwischen den Versionsnummern ist "."
											) +
								teilVersionsNummer;
				});
	}

}
