package com.github.mbeier1406.howto.ausbildung.gof.structural;

import lombok.Getter;

/**
 * Diese Proxy-Implementierung misst die Laufzeit eines Aufrufs
 * der Methode {@linkplain ProxyInterface#method()} der eigentlichen
 * Implementierung. Es wird nur der Wert des letzten Aufrufs gespeichert.
 * Demonstriert das Proxy-Pattern. Andere Proxyies könnten den Zugriff auf
 * die eigentlich Implementierung regeln (Security Proxy) etc.
 * Initialisierung des DELEGATE hier lazy. Für die Fälle, dass das Erzeugen
 * des Ojektes aufwändig ist (wird dann nur bei Bedarf erzeugt).
 */
@Getter
public class ProxyMetrics implements ProxyInterface {

	/** Die eigentliche Implementierung des Interface {@linkplain ProxyInterface} */
	private ProxyInterface DELEGATE = null;

	/** Laufzeit des letzten {@linkplain ProxyInterface#method()}-Aufrufs in ms. */
	private long laufzeitLetzterAufruf = 0;

	/**
	 * {@inheritDoc}<p/>
	 * Misst die Laufzeit der Implementierung.
	 */
	@Override
	public void method() {
		if ( this.DELEGATE == null ) // Lazy Initialisierung
			this.DELEGATE = new ProxyImpl();
		long start = System.currentTimeMillis();
		this.DELEGATE.method();
		this.laufzeitLetzterAufruf = System.currentTimeMillis()-start;
	}

}
