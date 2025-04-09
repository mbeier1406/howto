package com.github.mbeier1406.howto.ausbildung.gof.creational;

/**
 * GoF - Creational Pattern: Singelton<p/>
 * Stellt sicher, dass es nur eine Instanz dieser Klasse gibt und stellt
 * eine globale Methode für den Zugriff bereit, der das Objekt <i>lazy</i>
 * (also nur nach Bedarf) erzeugt.<p/>
 * Aufruf:
 * <pre><code>
 * Singelton singelton = Singelton.getInstance();
 * </code></pre>
 * @see /howto-ausbildung/src/main/resources/com/github/mbeier1406/howto/ausbildung/gof/creational/Singelton.png
 */
public class Singelton {

	/** Die Instanz der Klasse ist <i>static</i>, damit es nur eine geben kann */
	private static Singelton instance = null;

	/** Konstruktor muss privat sein, damit keine Instanz "von außen" erzeugt werdne kann */
	private Singelton() {
		// Initialisierung hier...
	}

	/**
	 * Die Methode muss statisch sein, da ja von außen das Objekt nicht erzeugt werden kann.
	 * Sie <b>muss</b> in multi-threaded Umgebungen synchronisiert werden, damit nicht doch
	 * zwei Objekte der Klasse durch Nebenläufigkeitseffekte gibt. Als Monito dient hier
	 * {@linkplain Singelton#getClass()}.
	 * @return die Instanz der Klasse
	 * @see #getInstance2()
	 */
	public static synchronized Singelton getInstance() {
		if ( instance == null )
			instance = new Singelton();
		return instance;
	}

	/**
	 * Analog {@linkplain #getInstance()}, allerdings entfällt der Aufwand für
	 * die Synchronisation, wenn die Initialisierung einmalig erfolgt ist.
	 * @return die Instanz der Klasse
	 */
	public static synchronized Singelton getInstance2() {
		if ( instance == null ) {
			synchronized ( Singelton.class ) {
				if ( instance == null )
					instance = new Singelton();
			}
		}
		return instance;
	}

	public void method1() {
		// Logik hier...
		// instance.<...>
	}

}
