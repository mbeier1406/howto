package com.github.mbeier1406.howto.ausbildung.aufgaben;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.howto.ausbildung.aufgaben.TuermeVonHanoi.Stein;
import com.github.mbeier1406.howto.ausbildung.aufgaben.TuermeVonHanoi.UnguetigeReiheException;
import com.github.mbeier1406.howto.ausbildung.aufgaben.TuermeVonHanoi.UnguetigeSpalteException;
import com.github.mbeier1406.howto.ausbildung.aufgaben.TuermeVonHanoi.UnguetigerZugException;

/**
 * Tests für die Klasse {@linkplain TuermeVonHanoi}.
 * @author mbeier
 */
public class TuermeVonHanoiTest {

	/** Das zu testende Objekt */
	public TuermeVonHanoi tuermeVonHanoi;

	/** Spielfeld für einen Test initialisieren */
	@BeforeEach
	public void init() throws UnguetigeSpalteException, UnguetigeReiheException {
		tuermeVonHanoi = new TuermeVonHanoi();
	}

	/** Manuelle Prüfung der Ausgabefunktion */
	@Test
	public void testePrint() throws UnguetigeSpalteException, UnguetigeReiheException {
		new TuermeVonHanoi(4, 5).print();
	}

	/** Stellt sicher, dass ungültige Felddimensionen die erwarteten Exceptions liefern */
	@Test
	public void erwarteExceptionBeiUngueltigerInitialisierung() {
		assertThrows(TuermeVonHanoi.UnguetigeSpalteException.class, () -> {	new TuermeVonHanoi(0, 1); });  // Anzahl Türme Null
		assertThrows(TuermeVonHanoi.UnguetigeSpalteException.class, () -> {	new TuermeVonHanoi(-1, 1); }); // Anzahl Türme kleiner Null
		assertThrows(TuermeVonHanoi.UnguetigeReiheException.class, () -> {	new TuermeVonHanoi(1, 0); });  // Höhe Türme Null
		assertThrows(TuermeVonHanoi.UnguetigeReiheException.class, () -> {	new TuermeVonHanoi(1, -1); }); // Höhe Türme kleiner Null
	}

	/** Stellt sicher, dass beim Holen des obersten Steins einer Spalte die richtige Exception erzeugt wird */
	@Test
	public void erwarteExceptionBeimErmittelnSteinInUngueltigerSpalte() {
		assertThrows(TuermeVonHanoi.UnguetigeSpalteException.class, () -> {	new TuermeVonHanoi().obersterSteinEinerSpalte(-1); }); // Index Spalte kleiner Null
		assertThrows(TuermeVonHanoi.UnguetigeSpalteException.class, () -> {
			TuermeVonHanoi t = new TuermeVonHanoi();
			t.obersterSteinEinerSpalte(t.getAnzahlTuerme()); }); // Index Spalte gößer als Anzahl der Türme
		assertThrows(TuermeVonHanoi.UnguetigeSpalteException.class, () -> {	new TuermeVonHanoi(2, 2).obersterSteinEinerSpalte(1); }); // Index eins enthält keinen Stein
	}

	/** Stellt sicher, dass der oberste Stein in der linkesten Spalte des initialiiserten Standardfelds den Wert 1 und die richtige Position 0/0 hat */
	@Test
	public void testeObersterSteinEinerSpalte() throws UnguetigeSpalteException {
		Stein obersterSteinEinerSpalte = tuermeVonHanoi.obersterSteinEinerSpalte(0);
		assertThat(obersterSteinEinerSpalte.getSpalte(), equalTo(0));
		assertThat(obersterSteinEinerSpalte.getReihe(), equalTo(0));
		assertThat(obersterSteinEinerSpalte.getWert(), equalTo(1));
	}

	/** Prüft, ob das Wegnehmens eines Steins aus einer Spalte nacheinander die richtigen Steine liefert */
	@Test
	public void testeObersterSteinEinerSpalteWegnehmen() throws UnguetigeSpalteException {
		assertThat(tuermeVonHanoi.steinAnPosition(0, 0).getWert(), equalTo(1));
		tuermeVonHanoi.oberstenSteinEinerSpalteWegnehmen(0);
		assertThat(tuermeVonHanoi.steinAnPosition(0, 0).getWert(), equalTo(0));
		assertThat(tuermeVonHanoi.steinAnPosition(0, 1).getWert(), equalTo(2));
		tuermeVonHanoi.oberstenSteinEinerSpalteWegnehmen(0);
		assertThat(tuermeVonHanoi.steinAnPosition(0, 1).getWert(), equalTo(0));
		assertThat(tuermeVonHanoi.steinAnPosition(0, 2).getWert(), equalTo(3));
		tuermeVonHanoi.oberstenSteinEinerSpalteWegnehmen(0);
		assertThat(tuermeVonHanoi.steinAnPosition(0, 2).getWert(), equalTo(0));
	}

	/** Stellt sicher, dass das Vergleichen zweier {@linkplain Stein}e funktioniert */
	@Test
	public void testeEqualsFuerKlasseZug() {
		TuermeVonHanoi.Zug zug1 = new TuermeVonHanoi.Zug(0, 1);
		TuermeVonHanoi.Zug zug2 = new TuermeVonHanoi.Zug(0, 1);
		assertThat(zug1, equalTo(zug2));
		zug2 = new TuermeVonHanoi.Zug(0, 2);
		assertThat(zug1, not(equalTo(zug2)));
	}

	/** Prüft, ob das ermittel möglicher Züge in einem kleinen Spielfluß funktioniert */
	@SuppressWarnings("serial")
	@Test
	public void testeAblauf() throws UnguetigeSpalteException, UnguetigeReiheException, UnguetigerZugException {
		TuermeVonHanoi.Zug[] moeglicheZuegeIst = tuermeVonHanoi.moeglicheZuege().toArray(new TuermeVonHanoi.Zug[0]);
		TuermeVonHanoi.Zug[] moeglicheZuegeSoll = new ArrayList<TuermeVonHanoi.Zug>() {{ add(new TuermeVonHanoi.Zug(0, 1)); add(new TuermeVonHanoi.Zug(0, 2)); }}.toArray(new TuermeVonHanoi.Zug[0]);
		assertThat(moeglicheZuegeIst, arrayContainingInAnyOrder(moeglicheZuegeSoll));
		tuermeVonHanoi.setze(0, 1);
		moeglicheZuegeIst = tuermeVonHanoi.moeglicheZuege().toArray(new TuermeVonHanoi.Zug[0]);
		moeglicheZuegeSoll = new ArrayList<TuermeVonHanoi.Zug>() {{ add(new TuermeVonHanoi.Zug(1, 0)); add(new TuermeVonHanoi.Zug(0, 2)); add(new TuermeVonHanoi.Zug(1, 2)); }}.toArray(new TuermeVonHanoi.Zug[0]);
		assertThat(moeglicheZuegeIst, arrayContainingInAnyOrder(moeglicheZuegeSoll));
		tuermeVonHanoi.setze(0, 2);
		moeglicheZuegeIst = tuermeVonHanoi.moeglicheZuege().toArray(new TuermeVonHanoi.Zug[0]);
		moeglicheZuegeSoll = new ArrayList<TuermeVonHanoi.Zug>() {{ add(new TuermeVonHanoi.Zug(1, 0)); add(new TuermeVonHanoi.Zug(2, 0)); add(new TuermeVonHanoi.Zug(1, 2)); }}.toArray(new TuermeVonHanoi.Zug[0]);
		assertThat(moeglicheZuegeIst, arrayContainingInAnyOrder(moeglicheZuegeSoll));
		tuermeVonHanoi.setze(1, 2);
		moeglicheZuegeIst = tuermeVonHanoi.moeglicheZuege().toArray(new TuermeVonHanoi.Zug[0]);
		moeglicheZuegeSoll = new ArrayList<TuermeVonHanoi.Zug>() {{ add(new TuermeVonHanoi.Zug(2, 1)); add(new TuermeVonHanoi.Zug(2, 0)); add(new TuermeVonHanoi.Zug(0, 1)); }}.toArray(new TuermeVonHanoi.Zug[0]);
		assertThat(moeglicheZuegeIst, arrayContainingInAnyOrder(moeglicheZuegeSoll));
		tuermeVonHanoi.setze(0, 1);
		moeglicheZuegeIst = tuermeVonHanoi.moeglicheZuege().toArray(new TuermeVonHanoi.Zug[0]);
		moeglicheZuegeSoll = new ArrayList<TuermeVonHanoi.Zug>() {{ add(new TuermeVonHanoi.Zug(1, 0)); add(new TuermeVonHanoi.Zug(2, 0)); add(new TuermeVonHanoi.Zug(2, 1)); }}.toArray(new TuermeVonHanoi.Zug[0]);
		assertThat(moeglicheZuegeIst, arrayContainingInAnyOrder(moeglicheZuegeSoll));
		tuermeVonHanoi.print();
		assertThat(tuermeVonHanoi.steinAnPosition(1, 2).getWert(), equalTo(3));
		assertThat(tuermeVonHanoi.steinAnPosition(2, 1).getWert(), equalTo(1));
		assertThat(tuermeVonHanoi.steinAnPosition(2, 2).getWert(), equalTo(2));
	}

	/** Für das Standardspielfeld gibt es eine Lösung */
	@SuppressWarnings("serial")
	@Test
	public void testeSpielMitLoesung() throws UnguetigeSpalteException, UnguetigerZugException, UnguetigeReiheException {
		assertTrue(tuermeVonHanoi.zuegeAusprobierenBisfertig(new ArrayList<>() {{ tuermeVonHanoi.getSpielfeldKopie(); }}));
	}

	/** Für ein Spielfeld mit zwei Türmen und drei Steinen gibt es keine Lösung */
	@Test
	public void testeSpielOhneLoesung() throws UnguetigeSpalteException, UnguetigerZugException, UnguetigeReiheException {
		assertFalse(new TuermeVonHanoi(2, 3).zuegeAusprobierenBisfertig(new ArrayList<>()));
	}

}
