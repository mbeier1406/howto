package com.github.mbeier1406.howto.jse.jvm;

import java.util.Optional;

/**
 * Standard-Implementierung für {@linkplain JavaVersionInterface}.
 * Verwendet die Interface-Implementierung von {@linkplain #getVersion()}.
 * @author mbeier
 */
public class JavaVersionStdImpl implements JavaVersionInterface {

	/** {@inheritDoc} */
	@Override
	public int getMajor() throws NumberFormatException {
		return getVersion().startsWith("1.") ?
				Integer.parseInt(getVersion().replaceAll("^\\d+\\.(\\d+).*$", "$1")) :
					Integer.parseInt(getVersion().replaceAll("^(\\d+).*$", "$1"));
	}

	/** {@inheritDoc} */
	@Override
	public Optional<Integer> getMinor() throws NumberFormatException {
		return getVersion().startsWith("1.") ?
				Optional.of(Integer.parseInt(getVersion().replaceAll("^1\\.\\d+\\.(\\d+).*$", "$1"))) : // Bis Java 8
					( getVersion().matches("^\\d+\\.\\d+.*$") ?
							Optional.of(Integer.parseInt(getVersion().replaceAll("^\\d+\\.(\\d+).*$", "$1"))) : // > Java 8
								Optional.empty() ); // OpenJDK
	}

	/** {@inheritDoc} */
	@Override
	public Optional<Integer> getPatch() throws NumberFormatException {
		return getVersion().startsWith("1.") ?
				Optional.of(Integer.parseInt(getVersion().replaceAll("^1\\.\\d+\\.\\d+_(\\d+).*$", "$1"))) : // Bis Java 8
							( getVersion().matches("^\\d+\\.\\d+\\.\\d+.*$") ?
									Optional.of(Integer.parseInt(getVersion().replaceAll("^\\d+\\.\\d+\\.(\\d+).*$", "$1"))) : // > Java 8
										Optional.empty() ); // OpenJDK
	}

}
