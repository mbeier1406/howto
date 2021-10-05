package com.github.mbeier1406.howto.jse.jvm;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Speziell Implementierung für {@linkplain JavaVersionInterface}, die eine eigene
 * Implementierung von {@linkplain #getVersion()} verwendet. Die Klasse, die die Liste
 * der Versionsnummern bereitstellt ({@linkplain JavaVersionInterface#getVersion()}),
 * wird in der Annotation {@linkplain VersionProvider} definiert.
 * @author mbeier
 * @see JavaVersionStdImpl
 */
@VersionProvider
public class JavaVersionImpl extends JavaVersionStdImpl implements JavaVersionInterface {

	public static final Logger LOGGER = LogManager.getLogger(JavaVersionImpl.class);

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public String getVersion() throws RuntimeException {
		Class<?> provider = null;
		try {
			provider = requireNonNull(getClass().getAnnotation(VersionProvider.class), "Klasse benötigt eine @VersionProvider Annotation").provider();
			LOGGER.trace("provider={}", provider);
			Method method = provider.getDeclaredMethod("version");
			LOGGER.trace("method={}", method);
			return ((List<Integer>) method.invoke(null))
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
		} catch (SecurityException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("provider="+provider, e);
		}
	}

}
