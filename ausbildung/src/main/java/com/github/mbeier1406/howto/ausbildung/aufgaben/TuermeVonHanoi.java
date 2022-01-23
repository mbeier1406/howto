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

	@FunctionalInterface
	public interface GueltigesSpielfeld {
		public void pruefeSpielfeldDimensionen(int anzahlTuerme, int anzahlSpalten) throws UnguetigeSpalteException, UnguetigeReiheException;
	}
	private GueltigesSpielfeld gueltigesSpielfeld = ( anzahlTuerme, anzahlEtagen ) -> {
		if ( anzahlTuerme <= 0 )
			throw new UnguetigeSpalteException(anzahlTuerme);
		if ( anzahlEtagen <= 0 )
			throw new UnguetigeReiheException(anzahlEtagen);
	};

	@FunctionalInterface
	public interface GueltigeSpalte {
		public void pruefeSpalte(int spalte, int anzahlTuerme) throws UnguetigeSpalteException;
	}
	private GueltigeSpalte gueltigeSpalte = ( spalte, anzahlTuerme ) -> {
		if ( spalte < 0 || spalte >= anzahlTuerme )
			throw new UnguetigeSpalteException(spalte);
	};

	@FunctionalInterface
	public interface GueltigerZug {
		public void pruefeZug(int[][] spielfeld, Stein stein, int spalte, int reihe) throws UnguetigerZugException;
	}
	private GueltigerZug gueltigerZug = ( int[][] spielfeld, Stein stein, int spalte, int j /* reihe */ ) -> {
		if ( j == 0 ) throw new IllegalArgumentException(stein + "; spalte="+spalte+"; Reihe="+j);
		try {
			if ( j < getAnzahlEtagen()-1 && steinAnPosition(spielfeld, spalte, j).getWert() <= stein.getWert() )
				throw new UnguetigerZugException(stein, spalte);
		} catch (UnguetigeSpalteException e) {
			throw new IllegalArgumentException(e);
		}
	};

	private static final int ANZAHL_TUERME = 3;

	private static final int ANZAHL_ETAGEN = 3;

	private int[][] tuerme;

	private int anzahlTuerme;
	private int anzahlEtagen;

	public int[][] getSpielfeldKopie(int[][] spielfeld) {
		return Arrays.stream(spielfeld).map(int[]::clone).toArray(int[][]::new);
	}

	public int getAnzahlTuerme() {
		return anzahlTuerme;
	}

	public int getAnzahlEtagen() {
		return anzahlEtagen;
	}

	public TuermeVonHanoi() throws UnguetigeSpalteException, UnguetigeReiheException {
		this(ANZAHL_TUERME, ANZAHL_ETAGEN);
	}

	public TuermeVonHanoi(int anzahlTuerme, int anzahlEtagen) throws UnguetigeSpalteException, UnguetigeReiheException {
		gueltigesSpielfeld.pruefeSpielfeldDimensionen(anzahlTuerme, anzahlEtagen);
		this.anzahlTuerme = anzahlTuerme;
		this.anzahlEtagen = anzahlEtagen;
		init();
	}

	private void init() {
		tuerme = new int[anzahlTuerme][];
		for ( int i=0; i < anzahlTuerme; i++ ) {
			tuerme[i] = new int[anzahlEtagen];
			for ( int j=0; j < anzahlEtagen; j++ )
				tuerme[i][j] = i == 0 ? j+1 : 0;
		}
	}

	public void print() {
		print(tuerme);
	}

	public void print(int[][] spielfeld) {
		for ( int j=0; j < anzahlEtagen; j++ ) {
				for ( int i=0; i < anzahlTuerme; i++ )
					System.out.print((spielfeld[i][j] == 0 ? " " : spielfeld[i][j]) + " ");
				System.out.println();
		}
		System.out.println(StringUtils.repeat('=', anzahlTuerme*2)+"\n");
	}

	public Stein steinAnPosition(int spalte, int reihe) throws UnguetigeSpalteException {
		return steinAnPosition(tuerme, spalte, reihe);
	}

	public Stein steinAnPosition(int[][] spielfeld, int spalte, int reihe) throws UnguetigeSpalteException {
		if ( spalte < 0 || spalte >= anzahlTuerme )
			throw new UnguetigeSpalteException(spalte);
		if ( reihe < 0 || reihe >= anzahlEtagen )
			throw new UnguetigeSpalteException(spalte);
		return new Stein(spalte, reihe, spielfeld[spalte][reihe]);
	}

	public boolean fertig() {
		return fertig(tuerme);
	}

	public boolean fertig(int[][] spielfeld) {
		for ( int i=0; i < anzahlEtagen; i++ )
			if ( spielfeld[anzahlTuerme-1][i] != i+1 )
				return false;
		return true;
	}

	public Stein obersterSteinEinerSpalte(int spalte) throws UnguetigeSpalteException {
		return obersterSteinEinerSpalte(tuerme, spalte);
	}

	public Stein obersterSteinEinerSpalte(int[][] spielfeld, int spalte) throws UnguetigeSpalteException {
		gueltigeSpalte.pruefeSpalte(spalte, anzahlTuerme);
		for ( int j=0; j < anzahlEtagen; j++ )
			if ( spielfeld[spalte][j] != 0 )
				return new Stein(spalte, j, spielfeld[spalte][j]);
		throw new UnguetigeSpalteException("Keine Steine in Turm-Spalte "+spalte);
	}

	public Stein oberstenSteinEinerSpalteWegnehmen(int spalte) throws UnguetigeSpalteException {
		return oberstenSteinEinerSpalteWegnehmen(tuerme, spalte);
	}

	public Stein oberstenSteinEinerSpalteWegnehmen(int[][] spielfeld, int spalte) throws UnguetigeSpalteException {
		Stein stein = obersterSteinEinerSpalte(spielfeld, spalte);
		spielfeld[stein.getSpalte()][stein.getReihe()] = 0;
		return stein;
		
	}

	public Stein steinInEineSpalteSetzen(Stein stein, int spalte) throws UnguetigeSpalteException, UnguetigerZugException {
		return steinInEineSpalteSetzen(tuerme, stein, spalte);
	}

	public Stein steinInEineSpalteSetzen(int[][] spielfeld, Stein stein, int spalte) throws UnguetigeSpalteException, UnguetigerZugException {
		gueltigeSpalte.pruefeSpalte(spalte,anzahlTuerme);
		int j=0; for ( ; j < anzahlEtagen && spielfeld[spalte][j] == 0; j++ );
		gueltigerZug.pruefeZug(spielfeld, stein, spalte, j);
		spielfeld[spalte][j-1] = stein.getWert();
		return new Stein(spalte, j-1, stein.getWert());
	}

	public Stein setze(int spalteVon, int spalteNach) throws UnguetigeSpalteException, UnguetigerZugException {
		return setze(tuerme, spalteVon, spalteNach);
	}

	public Stein setze(int[][] spielfeld, int spalteVon, int spalteNach) throws UnguetigeSpalteException, UnguetigerZugException {
		Stein stein = oberstenSteinEinerSpalteWegnehmen(spielfeld, spalteVon);
		stein = steinInEineSpalteSetzen(spielfeld, stein, spalteNach);
		return stein;
	}

	public List<Zug> moeglicheZuege() throws UnguetigeSpalteException {
		return moeglicheZuege(tuerme);
	}

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
			int[][] spielfeldKopie = getSpielfeldKopie(tuerme);
			bisherigeSpielstaende.add(spielfeldKopie);
			setze(zug.getSpalteVon(), zug.getSpalteNach());
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
