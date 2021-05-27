package de.gmxhome.golkonda.howto.jse.jvm;

import java.util.List;

/**
 * Dieser Provider stellt die Liste der Versionsnummern (siehe {@linkplain JavaVersionInterface#getVersion()}
 * aus der Laufzeitumgebung bereit: {@linkplain Runtime#version()}.
 * @author mbeier
 * @see VersionProvider
 */
public class RuntimeVersionProvider {

	/**
	 * Liefert die Versionsnummern Major, Minor, Patch als liste
	 * aus der Laufzeitumgebung.
	 * @return Liste der Versionsnummern
	 */
	public static List<Integer> version() {
		return Runtime.version().version();
	}

}
