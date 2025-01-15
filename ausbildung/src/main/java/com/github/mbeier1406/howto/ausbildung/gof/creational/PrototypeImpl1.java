 package com.github.mbeier1406.howto.ausbildung.gof.creational;

import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

/**
 * Erste Implementierung für das Interface {@linkplain Prototype}.
 */
@SuppressWarnings("serial")
public class PrototypeImpl1 implements Prototype, Serializable {

	/** {@inheritDoc} */
	@Override
	public void method() {
		// Hier die Verarbeitungslogik einfügen
	}

	/** {@inheritDoc} */
	@Override
	public Prototype clone() {
		/* Kopie des Objektes mittels "deep cloning" erstellen */
		return (Prototype) SerializationUtils.clone(this);
	}

}
