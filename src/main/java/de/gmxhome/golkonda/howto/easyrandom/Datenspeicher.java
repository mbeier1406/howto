package de.gmxhome.golkonda.howto.easyrandom;

/**
 * Dieses Objekt soll mittels <a href="https://github.com/j-easy/easy-random">Easy Random</a> mit verschiedenen
 * Daten gefüllt werden. Dabei kommen sowohl einfache Datentypen als auch komplexe Objekte zum Einsatz.
 * Dabei sollen die Werte nicht völlig zufällig generiert werden, sondern sich in definierten Bereichen befinden.
 * Das Objekt hat ansonsten keine weitere Logik. Der Test befindet sich in {@code DatenspeicherTest}.<p/>
 * <u>Hinweis</u>: zum Sertzen der Felder werden keine <em>Setter</em> benötigt, obwohl diese als <code>private</code>
 * definiert sind, da die API mit Reflections arbeitet!
 * @author mbeier
 */
public class Datenspeicher {

	/** Eine ganze Zahl zum Test von NOT-NULL */
	private Integer ganzeZahl;

	/** Eine Zeichenkette zum Test von Längen */
	private String zeichenKette;

	/** Diese Zeichenkette soll explizit nicht gesetzt werden */
	private String nichtBenutzteZeichenKette;

	/** Das Datum soll so generiert werden, dass es in der Zukunft liegt */
	private DatumsSpeicher datumsSpeicher;

	public Integer getGanzeZahl() {
		return ganzeZahl;
	}

	public String getZeichenKette() {
		return zeichenKette;
	}

	public String getNichtBenutzteZeichenKette() {
		return nichtBenutzteZeichenKette;
	}

	public DatumsSpeicher getDatumsSpeicher() {
		return datumsSpeicher;
	}

	@Override
	public String toString() {
		return "Datenspeicher [ganzeZahl=" + ganzeZahl + ", zeichenKette=" + zeichenKette
				+ ", nichtBenutzteZeichenKette=" + nichtBenutzteZeichenKette + ", datumsSpeicher=" + datumsSpeicher
				+ "]";
	}

}
