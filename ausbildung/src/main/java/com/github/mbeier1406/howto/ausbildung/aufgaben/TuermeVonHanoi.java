package com.github.mbeier1406.howto.ausbildung.aufgaben;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TuermeVonHanoi {

	public static final Logger LOGGER = LogManager.getLogger(TuermeVonHanoi.class);

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
		public void pruefeSpalte(int spalte) throws UnguetigeSpalteException;
	}
	private GueltigeSpalte gueltigeSpalte = ( spalte ) -> {
		if ( spalte < 0 || spalte >= ANZAHL_TUERME )
			throw new UnguetigeSpalteException(spalte);
	};

	@FunctionalInterface
	public interface GueltigerZug {
		public void pruefeZug(Stein stein, int spalte, int reihe) throws UnguetigerZugException;
	}
	private GueltigerZug gueltigerZug = ( Stein stein, int spalte, int j /* reihe */ ) -> {
		if ( j == 0 ) throw new IllegalArgumentException(stein + "; spalte="+spalte+"; Reihe="+j);
		if ( j < getAnzahlEtagen()-1 && steinAnPosition(spalte, j).getWert() > stein.getWert() )
			throw new UnguetigerZugException(stein, spalte);
	};

	private static final int ANZAHL_TUERME = 3;

	private static final int ANZAHL_ETAGEN = 3;

	private final int[][] tuerme = new int[ANZAHL_TUERME][];

	private int anzahlTuerme;
	private int anzahlEtagen;

	public int getAnzahlTuerme() {
		return anzahlTuerme;
	}

	public int getAnzahlEtagen() {
		return anzahlEtagen;
	}

	public Stein steinAnPosition(int spalte, int reihe) {
		return new Stein(spalte, reihe, tuerme[spalte][reihe]);
	}

	public TuermeVonHanoi() throws UnguetigeSpalteException, UnguetigeReiheException {
		this(ANZAHL_TUERME, ANZAHL_ETAGEN);
	}

	public TuermeVonHanoi(int anzahlTuerme, int anzahlEtagen) throws UnguetigeSpalteException, UnguetigeReiheException {
		gueltigesSpielfeld.pruefeSpielfeldDimensionen(anzahlTuerme, anzahlEtagen);
		this.anzahlTuerme = anzahlTuerme;
		this.anzahlEtagen = anzahlEtagen;
		init();
		print();
	}

	private void init() {
		for ( int i=0; i < anzahlTuerme; i++ ) {
			tuerme[i] = new int[anzahlEtagen];
			for ( int j=0; j < anzahlEtagen; j++ )
				tuerme[i][j] = i == 0 ? j+1 : 0;
		}
	}

	public void print() {
		for ( int j=0; j < anzahlEtagen; j++ ) {
				for ( int i=0; i < anzahlTuerme; i++ )
					System.out.print((tuerme[i][j] == 0 ? " " : tuerme[i][j]) + " ");
				System.out.println();
		}
		System.out.println(StringUtils.repeat('=', anzahlTuerme*2)+"\n");
	}

	public boolean fertig() {
		for ( int i=0; i < anzahlEtagen; i++ )
			if ( tuerme[anzahlTuerme-1][i] != i+1 )
				return false;
		return true;
	}

	public Stein obersterSteinEinerSpalte(int spalte) throws UnguetigeSpalteException {
		gueltigeSpalte.pruefeSpalte(spalte);
		for ( int j=0; j < anzahlEtagen; j++ )
			if ( tuerme[spalte][j] != 0 )
				return new Stein(spalte, j, tuerme[spalte][j]);
		throw new UnguetigeSpalteException("Keine Steine in Turm-Spalte "+spalte);
	}

	public Stein oberstenSteinEinerSpalteWegnehmen(int spalte) throws UnguetigeSpalteException {
		Stein stein = obersterSteinEinerSpalte(spalte);
		tuerme[stein.getSpalte()][stein.getReihe()] = 0;
		return stein;
		
	}

	public Stein steinInEineSpalteSetzen(Stein stein, int spalte) throws UnguetigeSpalteException, UnguetigerZugException {
		gueltigeSpalte.pruefeSpalte(spalte);
		int j=0; for ( ; j < anzahlEtagen && tuerme[spalte][j] == 0; j++ );
		gueltigerZug.pruefeZug(stein, spalte, j);
		tuerme[spalte][j-1] = stein.getWert();
		return new Stein(spalte, j-1, stein.getWert());
	}

	public Stein setze(int spalteVon, int spalteNach) throws UnguetigeSpalteException, UnguetigerZugException {
		Stein stein = oberstenSteinEinerSpalteWegnehmen(spalteVon);
		stein = steinInEineSpalteSetzen(stein, spalteNach);
		return stein;
	}

	public List<Zug> moeglicheZuege() throws UnguetigeSpalteException {
		List<Zug> moeglicheZuege = new ArrayList<>();
		for ( int i=0; i < anzahlTuerme; i++ )
			if ( tuerme[i][anzahlEtagen-1] != 0 ) {
				Stein stein = obersterSteinEinerSpalte(i);
				for ( int i1=0; i1 < anzahlTuerme; i1++ ) {
					if ( i1 != i ) {
						Stein stein1 = null;
						if ( tuerme[i1][anzahlEtagen-1] != 0 )
							stein1 = obersterSteinEinerSpalte(i1);
						if ( stein1 == null || stein.wert < stein1.wert )
							moeglicheZuege.add(new Zug(i, i1));
					}
				}
			}
		return moeglicheZuege;
	}

}
