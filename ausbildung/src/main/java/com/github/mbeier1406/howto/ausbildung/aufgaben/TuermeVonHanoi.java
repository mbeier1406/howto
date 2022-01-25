package com.github.mbeier1406.howto.ausbildung.aufgaben;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * <h3>Übung zur Demonstration rekursiver Funktionen</h3>
 * Als Beispiel für einen rekursiven Algorithmus wird die Lösung des Problem der <a href="https://de.wikipedia.org/wiki/T%C3%BCrme_von_Hanoi">
 * Türme von Hanoi</a> gewählt. Der Algorithmus löst das Problem durch einfaches Ausprobieren alle möglichen Züge, bis die Zielstellung
 * erreicht ist. Das einfache programm hat folgende Einschränkungen:
 * <ul>
 * <li>Es findet keine Wertung bzw. Priorisierung der Züge statt, um den schnellsten Weg zu finden.</li>
 * <li>Es wird nur ein Lösungsweg gefunden und dann abgebrochen, mehrere Wege werden nicht ausprobiert.</li> 
 * <li>Es existiert keine graphische Darstellung, es erfolgt lediglich eine symbolische Ausgabe mit Zahlenwerten für die Ordnung eines Steins.</li>
 * </ul>
 * <p>Der Einfachheit halber sind Klassen für benötigte Datenstrukturen, Exceptions usw. als innere Klassen implementiert.</p>
 * Das Spielfeld ist als zweidimensionales Integer-Feld implementiert, wobei eine <b>0</b> gleichbedeutend mit einem leeren Feld, und
 * eine Zahl von <b>1</b> bis <b>n</b> einen Stein mit "<i>Breite</i>" seines Werts. Im Standardfall hat ein Spielfeld entsprechend zu Beginn
 * folgende Werte (alle Steine sind auf der linken Seite angeordnet, die "<i>Breite</i>" nimmt nach unten zu:
 * <pre><code>
 * 1 0 0
 * 2 0 0
 * 3 0 0
 * <code></pre>
 * Die Zielstellung sieht entsprechend so aus:
 * <pre><code>
 * 0 0 1
 * 0 0 2
 * 0 0 3
 * <code></pre>
 * Ein Stein darf nie auf einen mit geringerer Breite gesetzt werden, entsprechend müssen in jeder Spalte die Steine "<i>nach unten</i>" hin größere
 * Werte haben. Die Indizes werden bei den Türmen (Spalten) von links nach rechts von {@code 0..n} und die Anzahl der Steine je Turm von oben nach unten
 * mit {@code 0..m} gezählt, wobei <b>n</b> ungleich <b>m</b> sein kann, wobei es dann Konstellationen geben kann, für die es Keine Lösung gibt (zum Beispiel zwei
 * Spalten bei Türmen mit drei Steinen).
 * @author mbeier
 */
public class TuermeVonHanoi {

	/**
	 * Diese Klasse repräsentiert einen Stein mit seinen Koordinaten Reihe und Spalte sowie seinem Wert.
	 * Beim Wert handelt es sich um eine Zahl von 1 bis n (siehe {@linkplain TuermeVonHanoi#anzahlEtagen}). 
	 */
	public static class Stein extends Point {
		private static final long serialVersionUID = 3961344364639141008L;
		private int wert;
		public Stein(int i, int j, int wert) {
			super(i, j);
			this.wert = wert;
		}
		public int getSpalte() {
			return (int) super.getX();
		}
		public int getReihe() {
			return (int) super.getY();
		}
		public int getWert() {
			return wert;
		}
		@Override
		public String toString() {
			return "Stein [getSpalte()=" + getSpalte() + ", getReihe()=" + getReihe() + ", getWert()=" + getWert() + "]";
		}
	}

	/**
	 * Diese Klasse definiert einen Spielzug, d. h. das Setzen eines Steins von einer Spalte in eine
	 * andere. Entsprechend muss die neue Spalte leer sein, oder der dort vorhandene Stein muss einen höheren Wert haben.
	 * @see TuermeVonHanoi#gueltigerZug
	 */
	public static class Zug {
		private int spalteVon, spalteNach;
		public Zug(int spalteVon, int spalteNach) {
			super();
			this.spalteVon = spalteVon;
			this.spalteNach = spalteNach;
		}
		public int getSpalteVon() {
			return spalteVon;
		}
		public int getSpalteNach() {
			return spalteNach;
		}
		@Override
		public boolean equals(Object other) {
			if ( !(other instanceof Zug) )
				return false;
			return ((Zug) other).getSpalteVon() == getSpalteVon() &&
					((Zug) other).getSpalteNach() == getSpalteNach();
		}
		@Override
		public String toString() {
			return "Zug [spalteVon=" + spalteVon + ", spalteNach=" + spalteNach + "]";
		}
	}

	/**
	 * Definiert die Ausnahmebedingung, dass ein Stein in eine ungültige
	 * Spalte (Turm) gesetzt werden soll.
	 * @see TuermeVonHanoi#gueltigeSpalte
	 */
	@SuppressWarnings("serial")
	public static class UnguetigeSpalteException extends Exception{
		public UnguetigeSpalteException() { super(); };
		public UnguetigeSpalteException(int spalte) {
			super("Ungültige Spalte für Turm: "+spalte);
		}
		public UnguetigeSpalteException(String msg) {
			super(msg);
		}
		public UnguetigeSpalteException(Exception e) {
			super(e);
		}
	}

	/**
	 * Definiert die Ausnahmebedingung, dass ein ungültiges Spielfeld defieniert wurde.
	 * @see TuermeVonHanoi#TuermeVonHanoi(int, int)
	 */
	@SuppressWarnings("serial")
	public static class UnguetigeReiheException extends Exception{
		public UnguetigeReiheException() { super(); };
		public UnguetigeReiheException(int reihe) {
			super("Ungültige Reihe: "+reihe);
		}
		public UnguetigeReiheException(String msg) {
			super(msg);
		}
		public UnguetigeReiheException(Exception e) {
			super(e);
		}
	}

	/**
	 * Definiert die Ausnahmebedingung, dass ein ungültiger Zug durchgeführt wurde.
	 * @see TuermeVonHanoi#gueltigerZug
	 */
	@SuppressWarnings("serial")
	public static class UnguetigerZugException extends Exception{
		public UnguetigerZugException() { super(); };
		public UnguetigerZugException(Stein stein, int spalteNeu) {
			super("Ungültige Zug: "+stein+" nach Spalte "+spalteNeu);
		}
		public UnguetigerZugException(String msg) {
			super(msg);
		}
		public UnguetigerZugException(Exception e) {
			super(e);
		}
	}

	/** Defniert das Interface zu einem Service, der die Dimensionen eines Spielfelds prüft */
	@FunctionalInterface
	public interface GueltigesSpielfeld {
		public void pruefeSpielfeldDimensionen(int anzahlTuerme, int anzahlSpalten) throws UnguetigeSpalteException, UnguetigeReiheException;
	}

	/** Service prüft, dass die angegebenen Dimensionen größer Null sind. */
	private GueltigesSpielfeld gueltigesSpielfeld = ( anzahlTuerme, anzahlEtagen ) -> {
		if ( anzahlTuerme <= 0 )
			throw new UnguetigeSpalteException(anzahlTuerme);
		if ( anzahlEtagen <= 0 )
			throw new UnguetigeReiheException(anzahlEtagen);
	};

	/** Defniert das Interface zu einem Service, der die Gültigkeoit einer Spalte (Index eines Turms) prüft */
	@FunctionalInterface
	public interface GueltigeSpalte {
		public void pruefeSpalte(int spalte, int anzahlTuerme) throws UnguetigeSpalteException;
	}

	/**	Service prüft, dass die gewählte Spalte zwischen {@code 0} und <code>{@linkplain #anzahlTuerme}-1</code> liegt */
	private GueltigeSpalte gueltigeSpalte = ( spalte, anzahlTuerme ) -> {
		if ( spalte < 0 || spalte >= anzahlTuerme )
			throw new UnguetigeSpalteException(spalte);
	};

	/** Defniert das Interface zu einem Service, der die Gültigkeoit eines Zugs prüft */
	@FunctionalInterface
	public interface GueltigerZug {
		public void pruefeZug(int[][] spielfeld, Stein stein, int spalte, int reihe) throws UnguetigerZugException;
	}

	/** Service prüft, ob die Spalte im Ziel leer ist, oder der dort vorhandene, oberste Stein einen größeren Wert als der zu setzende hat */
	private GueltigerZug gueltigerZug = ( int[][] spielfeld, Stein stein, int spalte, int j /* reihe */ ) -> {
		if ( j == 0 ) throw new IllegalArgumentException(stein + "; spalte="+spalte+"; Reihe="+j);
		try {
			if ( j < getAnzahlEtagen()-1 && steinAnPosition(spielfeld, spalte, j).getWert() <= stein.getWert() )
				throw new UnguetigerZugException(stein, spalte);
		} catch (UnguetigeSpalteException e) {
			throw new IllegalArgumentException(e);
		}
	};

	/** In der Standarddefinition hat das Spiel {@value} Türme (Spalten) */
	private static final int ANZAHL_TUERME = 3;

	/** In der Standarddefinition hat das Spiel {@value} Etagen/Steine je Turm (Reihen) */
	private static final int ANZAHL_ETAGEN = 3;

	/** Das Spielfeld */
	private int[][] tuerme;

	/** Die für das Spiel gewählte Anzahl von Türmen */
	private int anzahlTuerme;
	/** Die für das Spiel gewählte Anzahl von Steinen je Turm */
	private int anzahlEtagen;

	/** @see #anzahlTuerme */
	public int getAnzahlTuerme() {
		return anzahlTuerme;
	}

	/** @see #anzahlEtagen */
	public int getAnzahlEtagen() {
		return anzahlEtagen;
	}

	/** Erzeugt eine Spielfeldkopie */
	public int[][] getSpielfeldKopie() {
		return Arrays.stream(tuerme).map(int[]::clone).toArray(int[][]::new);
	}

	/** Erzeugt eine Spielfeldkopie zum Testen von Zügen */
	public int[][] getSpielfeldKopie(int[][] spielfeld) {
		return Arrays.stream(spielfeld).map(int[]::clone).toArray(int[][]::new);
	}

	/**
	 * Im Standard wird ein Spielfeld der Dimension {@value #ANZAHL_TUERME} Türme und {@value #ANZAHL_ETAGEN} Steien je Turm erzeugt
	 * @see TuermeVonHanoi#TuermeVonHanoi(int, int)
	 */
	public TuermeVonHanoi() throws UnguetigeSpalteException, UnguetigeReiheException {
		this(ANZAHL_TUERME, ANZAHL_ETAGEN);
	}

	/** Erzeugt ein Spielfeld mit den geforderten Dimensionen und initialisiert es. */
	public TuermeVonHanoi(int anzahlTuerme, int anzahlEtagen) throws UnguetigeSpalteException, UnguetigeReiheException {
		gueltigesSpielfeld.pruefeSpielfeldDimensionen(anzahlTuerme, anzahlEtagen);
		this.anzahlTuerme = anzahlTuerme;
		this.anzahlEtagen = anzahlEtagen;
		init();
	}

	/** Initialisiert das Spielfeld, so dass der Turm zunächsrt in der linkesten Spalte steht und die weiteren Spalten leer sind */
	private void init() {
		tuerme = new int[anzahlTuerme][];
		for ( int i=0; i < anzahlTuerme; i++ ) {
			tuerme[i] = new int[anzahlEtagen];
			for ( int j=0; j < anzahlEtagen; j++ )
				tuerme[i][j] = i == 0 ? j+1 : 0;
		}
	}

	/**
	 * Gibt das Spielfeld als Text auf der Konsole aus, wobei leere Felder als
	 * Leerzeichen, und eine Stein als sein Wert angegeben wird. Beispiel:
	 * <pre><code>
	 * 
	 * 1
	 * 2   3
	 * </code></pre>
	 * In diesem Feld sind zwei Steine im ersten, und einer im dritten Turm platziert.
	 * Normalerweise gehört diese Funktion nicht in die Klasse, zur Darstellung sollte
	 * es ein Marshaller-Package mit Klassen für verschiedene graphischen Ausgaben geben.
	 * In dieser Methode wird das Standard-Spielfeld {@linkplain #tuerme} angezeigt.
	 * @see #print(int[][])
	 */
	public void print() {
		print(tuerme);
	}

	/**
	 * Zeigt das übergebene Spiel an.
	 * @param spielfeld das anzuzeigende Spielfeld
	 * @see #print()
	 */
	public void print(int[][] spielfeld) {
		for ( int j=0; j < anzahlEtagen; j++ ) {
				for ( int i=0; i < anzahlTuerme; i++ )
					System.out.print((spielfeld[i][j] == 0 ? " " : spielfeld[i][j]) + " ");
				System.out.println();
		}
		System.out.println(StringUtils.repeat('=', anzahlTuerme*2)+"\n");
	}

	/**
	 * Liefert den {@linkplain Stein} an einer vorgegebene Stelle des Spielfelds.
	 * <p><b>ACHTUNG</b>: falls sich an der  Position kein Stein befindet, enthält
	 * der zurückgegebene Stein den Wert 0 und ist somit ungültig!
	 * @param spalte der Turm von 0..{@linkplain #anzahlTuerme}-1
	 * @param reihe die Etage von 0..{@linkplain #anzahlEtagen}-1
	 * @return den Stein an der Stelle
	 * @throws UnguetigeSpalteException wie in {@linkplain #gueltigeSpalte} definiert
	 */
	public Stein steinAnPosition(int spalte, int reihe) throws UnguetigeSpalteException {
		return steinAnPosition(tuerme, spalte, reihe);
	}

	/** @see #steinAnPosition(int, int) */
	public Stein steinAnPosition(int[][] spielfeld, int spalte, int reihe) throws UnguetigeSpalteException {
		if ( spalte < 0 || spalte >= anzahlTuerme )
			throw new UnguetigeSpalteException(spalte);
		if ( reihe < 0 || reihe >= anzahlEtagen )
			throw new UnguetigeSpalteException(spalte);
		return new Stein(spalte, reihe, spielfeld[spalte][reihe]);
	}

	/**
	 * Prüft, on das Spiel zu Ende ist. Das ist der Fall, wenn sie Steine auf der rechten Seite des
	 * Spielfelds (Index Turm {@linkplain #anzahlTuerme}-1) mit absteigendem Wert angeordnet sind.
	 */
	public boolean fertig() {
		return fertig(tuerme);
	}

	/** @see #fertig() */
	public boolean fertig(int[][] spielfeld) {
		for ( int i=0; i < anzahlEtagen; i++ )
			if ( spielfeld[anzahlTuerme-1][i] != i+1 )
				return false;
		return true;
	}

	/**
	 * Liefert den obersten Stein einer Spalte, also der, der für einen Zug verwendet werdne kann
	 * @param spalte der Turm, von dem ein Stein genommen werden soll
	 * @return den obersten Stein, wenn einer vorhanden ist
	 * @throws UnguetigeSpalteException siehe {@linkplain #gueltigeSpalte} oder wenn kein Stein vorhanden ist
	 */
	public Stein obersterSteinEinerSpalte(int spalte) throws UnguetigeSpalteException {
		return obersterSteinEinerSpalte(tuerme, spalte);
	}

	/** @see #oberstenSteinEinerSpalte(int) */
	public Stein obersterSteinEinerSpalte(int[][] spielfeld, int spalte) throws UnguetigeSpalteException {
		gueltigeSpalte.pruefeSpalte(spalte, anzahlTuerme);
		for ( int j=0; j < anzahlEtagen; j++ )
			if ( spielfeld[spalte][j] != 0 )
				return new Stein(spalte, j, spielfeld[spalte][j]);
		throw new UnguetigeSpalteException("Keine Steine in Turm-Spalte "+spalte);
	}

	/**
	 * Entfernt den oberstenStein einer Spalte. Das entsprechende Feld enthält danach den Wert 0.
	 * @param spalte der Turm, von dem ein Stein genommen werden soll
	 * @return den obersten Stein, wenn einer vorhanden ist
	 * @throws UnguetigeSpalteException siehe {@linkplain #gueltigeSpalte} oder wenn kein Stein vorhanden ist
	 */
	public Stein oberstenSteinEinerSpalteWegnehmen(int spalte) throws UnguetigeSpalteException {
		return oberstenSteinEinerSpalteWegnehmen(tuerme, spalte);
	}

	/** @see #oberstenSteinEinerSpalteWegnehmen(int) */
	public Stein oberstenSteinEinerSpalteWegnehmen(int[][] spielfeld, int spalte) throws UnguetigeSpalteException {
		Stein stein = obersterSteinEinerSpalte(spielfeld, spalte);
		spielfeld[stein.getSpalte()][stein.getReihe()] = 0;
		return stein;
		
	}

	/**
	 * Setzt einen Stein in eine Spalte
	 * @param stein der zu setzende Stein
	 * @param spalte der Turm, auf den der Stein gelegt wird
	 * @return die neue Definition des Steins mit der aktuellen Position
	 * @throws UnguetigeSpalteException siehe {@linkplain #gueltigeSpalte}
	 * @throws UnguetigerZugException siehe {@linkplain #gueltigerZug}
	 */
	public Stein steinInEineSpalteSetzen(Stein stein, int spalte) throws UnguetigeSpalteException, UnguetigerZugException {
		return steinInEineSpalteSetzen(tuerme, stein, spalte);
	}

	/** @see #steinInEineSpalteSetzen(Stein, int) */
	public Stein steinInEineSpalteSetzen(int[][] spielfeld, Stein stein, int spalte) throws UnguetigeSpalteException, UnguetigerZugException {
		gueltigeSpalte.pruefeSpalte(spalte,anzahlTuerme);
		int j=0; for ( ; j < anzahlEtagen && spielfeld[spalte][j] == 0; j++ );
		gueltigerZug.pruefeZug(spielfeld, stein, spalte, j);
		spielfeld[spalte][j-1] = stein.getWert();
		return new Stein(spalte, j-1, stein.getWert());
	}

	/**
	 * Ein Spielzug: Stein von einem Turm auf einen anderen (möglicherweise leeren) legen.
	 * @param spalteVon der Turm, von dem der oberste Stein genommen wird
	 * @param spalteNach der Turm, auf den der Stein gelegt wird
	 * @return die neue Definition des Steins mit der aktuellen Position
	 * @throws UnguetigeSpalteException siehe {@linkplain #gueltigeSpalte}
	 * @throws UnguetigerZugException siehe {@linkplain #gueltigerZug}
	 */
	public Stein setze(int spalteVon, int spalteNach) throws UnguetigeSpalteException, UnguetigerZugException {
		return setze(tuerme, spalteVon, spalteNach);
	}

	/** @see #setze(int, int) */
	public Stein setze(int[][] spielfeld, int spalteVon, int spalteNach) throws UnguetigeSpalteException, UnguetigerZugException {
		Stein stein = oberstenSteinEinerSpalteWegnehmen(spielfeld, spalteVon);
		stein = steinInEineSpalteSetzen(spielfeld, stein, spalteNach);
		return stein;
	}

	/**
	 * Liefert alle prinzipiell möglichen Spielzüge zur aktuellen Spielsituation in {@linkplain #tuerme}.
	 * Es wird hier noch nicht berücksichtigt, ob der Spielzug zu einer Situation führen würde,
	 * die bereits vorhanden gewesen ist (also zu einem Zyklus führen könnte).
	 * @return Liste der möglichen Züge
	 * @throws UnguetigeSpalteException
	 */
	public List<Zug> moeglicheZuege() throws UnguetigeSpalteException {
		return moeglicheZuege(tuerme);
	}

	/** @see #moeglicheZuege() */
	public List<Zug> moeglicheZuege(int[][] spielfeld) throws UnguetigeSpalteException {
		List<Zug> moeglicheZuege = new ArrayList<>();
		for ( int i=0; i < anzahlTuerme; i++ )
			if ( spielfeld[i][anzahlEtagen-1] != 0 ) {
				Stein stein = obersterSteinEinerSpalte(spielfeld, i);
				for ( int i1=0; i1 < anzahlTuerme; i1++ ) {
					if ( i1 != i ) {
						Stein stein1 = null;
						if ( spielfeld[i1][anzahlEtagen-1] != 0 )
							stein1 = obersterSteinEinerSpalte(spielfeld, i1);
						if ( stein1 == null || stein.wert < stein1.wert )
							moeglicheZuege.add(new Zug(i, i1));
					}
				}
			}
		return moeglicheZuege;
	}

	/**
	 * Durchführung des Spiels. Es wird:
	 * <ol>
	 * <li>Die aktuelle Spielsituation ausgegeben</li>
	 * <li>Geprüft, ob das Spiel zu Ende ist</li>
	 * <li>Alle zur Zeit möglichen Spielzüge ermittelt</li>
	 * <li>Aus doieser Liste alle Züge entfernt, die zu eine Spielsituation führen würden,
	 * die bereits vorhanden war um eine Endlosschleife zu vermeiden</li>
	 * <li>Falls dann keine Züge übrig bleiben, wird dieser Zweig der Rekursion erfolglos
	 * abgebrochen (liefert <b>false</b> zurück)</li>
	 * <li>Alle möglichen Spielzüge werdne nacheinander ausprobiert, wobei eine Spielfeldkopie
	 * in der Liste der bereits gehabten Spielstände eingetragen wird, der Zug durchgeführt wird,
	 * und die Funktion rekursiv mit neuem Spielstand und erweiterter Liste bekannter Spielstände
	 * ausgeführt wird. Liefert der Aufruf <b>false</b> zurück (führt nicht zum Ziel), so wird
	 * der Zug zurückgenommen und der bekannte Spielstand entfernt</li>
	 * <li>Wenn alle ausprübierten, möglichen Spielzüge nicht zum Ziel geführt haben,
	 * wird <b>false</b> zurückgegeben</li>
	 * </ol>
	 * @param bisherigeSpielstaende Liste der Spielstäne ist zu Beginn des Spiels der Stand mit dem Turm links
	 * @return <b>true</b>, falls es für das Spiel eine Lösung gibt, sonst <b>false</b>
	 * @throws UnguetigeSpalteException siehe {@linkplain #gueltigeSpalte}
	 * @throws UnguetigerZugException siehe {@linkplain #gueltigerZug}
	 */
	public boolean zuegeAusprobierenBisfertig(List<int[][]> bisherigeSpielstaende) throws UnguetigeSpalteException, UnguetigerZugException {
		print();
		if ( fertig(tuerme) )
			return true;
		List<Zug> moeglicheZuege = moeglicheZuege(tuerme);
		List<Zug> moeglicheNeueZuege = new ArrayList<>();
		for ( Zug zug : moeglicheZuege ) {
			int[][]tuermeClone = getSpielfeldKopie(tuerme);
			setze(tuermeClone, zug.getSpalteVon(), zug.getSpalteNach());
			boolean neueSpielSituation = bisherigeSpielstaende.stream().filter(s -> Arrays.deepEquals(s, tuermeClone)).findAny().isEmpty();
			if ( neueSpielSituation )
				moeglicheNeueZuege.add(zug);
				
		}
		if ( moeglicheNeueZuege.isEmpty() )
			return false;
		for ( Zug zug : moeglicheNeueZuege ) {
			setze(zug.getSpalteVon(), zug.getSpalteNach());
			int[][] spielfeldKopie = getSpielfeldKopie(tuerme);
			bisherigeSpielstaende.add(spielfeldKopie);
			boolean fertig = zuegeAusprobierenBisfertig(bisherigeSpielstaende);
			if ( !fertig ) {
				if ( !bisherigeSpielstaende.remove(spielfeldKopie) )
					throw new IllegalArgumentException();
				setze(zug.getSpalteNach(), zug.getSpalteVon());
			}
			else return true;
		}
		return false;
	}

}
