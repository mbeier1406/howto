 package com.github.mbeier1406.howto.ausbildung.gof.creational;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Diese Factory erstellt Kopien der {@linkplain Prototype}n.
 * @see PrototypeImpl1
 * @see PrototypeImpl2
 */
public class PrototypeRegistry {

	/** Erstellt die {@linkplain Map} mit den {@linkplain Prototype}n und deren Schlüsselwörtern */
	@SuppressWarnings({ "serial" })
	private static final Map<String, Prototype> PROTOTYPES = new HashMap<>() {{
		put("impl1", new PrototypeImpl1());
		put("impl2", new PrototypeImpl2());
	}};

	/**
	 * Erstellt die Kopie eines {@linkplain Prototype}n über dessen Keyword mit Hilfe
	 * der {@linkplain Prototype#clone()}-Methode.
	 * @param key Schlüsselwort aus der Map {@linkplain #PROTOTYPES}
	 * @return Eine <b>Kopie</b> des Prototypen wie zB {@linkplain PrototypeImpl1}
	 */
	public static Prototype getPrototype(String key) {
		return Optional.ofNullable(PROTOTYPES.get(key)).orElseThrow(IllegalArgumentException::new).clone();
	}

}
