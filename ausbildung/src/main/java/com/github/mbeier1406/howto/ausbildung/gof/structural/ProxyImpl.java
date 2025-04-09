package com.github.mbeier1406.howto.ausbildung.gof.structural;

/**
 * Diese Klasse bildet die Implementierung, die proxyfiziert wird,
 * d. h. an die der Proxy die Aufrufe delegiert.
 */
public class ProxyImpl implements ProxyInterface {

	/** {@inheritDoc} */
	@Override
	public void method() {
		// hier die Implementierung einfügen
		System.out.println("Aufuruf Proxy Implementierung!");
	}

}
