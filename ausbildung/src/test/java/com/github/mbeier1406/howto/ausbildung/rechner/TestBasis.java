package com.github.mbeier1406.howto.ausbildung.rechner;

import com.github.mbeier1406.howto.ausbildung.rechner.token.CommaToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.DezimalToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.DivisionToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.GanzzahlToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.KlammeraufToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.KlammerzuToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.MinusToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.PeriodToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.PlusToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.SinusToken;

/** Definiert ein paar gemeinsame Konstandten für die Tests */
public abstract class TestBasis {

	protected static final TokenInterface PLUS = new PlusToken();
	protected static final TokenInterface MINUS = new MinusToken();
	protected static final TokenInterface PERIOD = new PeriodToken();
	protected static final TokenInterface DIVISION = new DivisionToken();
	protected static final TokenInterface COMMA = new CommaToken();
	protected static final TokenInterface KLAMMERAUF = new KlammeraufToken();
	protected static final TokenInterface KLAMMERZU = new KlammerzuToken();
	protected static final TokenInterface SINUS = new SinusToken();
	protected static final TokenInterface NULL = new GanzzahlToken(0);
	protected static final TokenInterface EINS = new GanzzahlToken(1);
	protected static final TokenInterface DREI = new GanzzahlToken(3);
	protected static final TokenInterface SECHS = new GanzzahlToken(6);
	protected static final TokenInterface MINUS_ZWEI = new GanzzahlToken(-2);
	protected static final TokenInterface Z90 = new GanzzahlToken(90);
	protected static final TokenInterface Z123 = new GanzzahlToken(123);
	protected static final TokenInterface D0_123 = new DezimalToken(0.123);

}
