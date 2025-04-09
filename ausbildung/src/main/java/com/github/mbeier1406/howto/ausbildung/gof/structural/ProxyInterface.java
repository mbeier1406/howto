package com.github.mbeier1406.howto.ausbildung.gof.structural;

/**
 * GoF - Structural Pattern: Proxy<p/>
 * Dieses Interface wirde von der eigentlichen Implementierung
 * in {@linkplain ProxyImpl} und vom {@linkplain ProxyMetrics} verwendet.
 * Die enthaltene Methode wird vom Proxy an die Implemsntierung
 * delegiert.
 * @see /howto-ausbildung/src/main/resources/com/github/mbeier1406/howto/ausbildung/gof/structural/Proxy.png
 */
public interface ProxyInterface {

	/** Die Methode, die Proxyfiziert wird */
	public void method();

}
