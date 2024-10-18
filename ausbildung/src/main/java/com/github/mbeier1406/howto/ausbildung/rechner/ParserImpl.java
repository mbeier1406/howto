package com.github.mbeier1406.howto.ausbildung.rechner;

import java.util.List;

import com.github.mbeier1406.howto.ausbildung.rechner.token.DezimalToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.DivisionToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.GanzzahlToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.KlammeraufToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.KlammerzuToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.MinusToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.PeriodToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.PlusToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.SinusToken;

/**
 * Standardimplementierung des Parsers mittels Einlesen von Terminals (z. B. Zahlen),
 * Fakotren (Multiplkation, Division) und des mathematischen Ausdrucks (Addition
 * und Suptraktion). Noch offen, weitere Funktionen wie Wurzel usw.
 */
public class ParserImpl implements Parser {

	 /**  Index in der {@linkplain #listOfTokens Liste der Tokens}, der geade geparsed wird */
	private int index = 0;

	/** Die Liste der Tokens, die die mathematische Formel bilden */
	private List<TokenInterface> listOfTokens;

	/** {@inheritDoc} */
	@Override
	public double evaluate(final List<TokenInterface> listOfTokens) throws ParserException {
		this.listOfTokens = listOfTokens;
		this.index = 0;
		double result = parseExpression();
		if ( this.index != listOfTokens.size() )
			throw new ParserException("Unerwartetes Token an Index "+this.index+": "+this.listOfTokens.get(this.index));
		return result;
	}

	/** Liest einen mathematischen Ausdruck, d. h. eine Addition/Subtraktion von Faktoren ein */
	private double parseExpression() throws ParserException {
		double ergebnis = parseTerm();
		while ( this.index < this.listOfTokens.size() ) {
			var token = this.listOfTokens.get(this.index++);
			if ( token instanceof PlusToken )
				ergebnis += parseTerm();
			else if ( token instanceof MinusToken )
				ergebnis -= parseTerm();
			else {
				this.index--; // Zeiger in der Tokenliste zurücksetzen
				break;
			}
		}
		return ergebnis;
	}

	/** Liest einen mathematischen Term, d. h. eine Multiplikation/Division von Faktoren ein */
	private double parseTerm() throws ParserException {
		double ergebnis = parseFactor();
		while ( this.index < this.listOfTokens.size() ) {
			var token = this.listOfTokens.get(this.index++);
			if ( token instanceof PeriodToken )
				ergebnis *= parseFactor();
			else if ( token instanceof DivisionToken )
				ergebnis /= parseFactor();
			else {
				this.index--; // Zeiger in der Tokenliste zurücksetzen
				break;
			}
		}
		return ergebnis;
	}

	/** Liest ein Terminal/Faktor des Ausdrucks (Zahl, Klammer) ein */
	private double parseFactor() throws ParserException {
		var token = this.listOfTokens.get(this.index++);
		if ( token instanceof GanzzahlToken  )
			return (int) token.getValue().get();
		else if ( token instanceof DezimalToken )
			return (double) token.getValue().get();
		else if ( token instanceof KlammeraufToken ) {
			nextToken(false, "Ausdruck nach '(' erwartet");
			double ergebnis = parseExpression();
			token = nextToken(true, "')' erwartet");
			if ( !(token instanceof KlammerzuToken) )
				throw new ParserException("')' erwartet (Index "+this.index+"), gefunden: '"+token+"'!");
			return ergebnis;
		}
		else if ( token instanceof SinusToken ) {
			token = nextToken(true, "'(' nach 'sin' erwartet");
			if ( !(token instanceof KlammeraufToken) )
				throw new ParserException("'(' nach 'sin' erwartet (Index "+this.index+"), gefunden: '"+token+"'!");
			double ergebnis = parseExpression();
			token = nextToken(true, "')' erwartet");
			if ( !(token instanceof KlammerzuToken) )
				throw new ParserException("')' erwartet nach 'sin' (Index "+this.index+"), gefunden: '"+token+"'!");
			return Math.sin(Math.toRadians(ergebnis));
		}
		else
			throw new ParserException("Unerwartetes Token '"+token+"' an Index "+this.index+": Terminal (Ganzzahl) erwartet!");
	}

	/** prüft, ob noch Token vorhanden sind und liefert ggf. den nächsten */
	private TokenInterface nextToken(boolean einlesen, String fehlerText) throws ParserException {
		if ( this.listOfTokens.size() <= this.index )
			throw new ParserException(fehlerText+" (Index "+(this.index+1)+")!");
		return einlesen ? this.listOfTokens.get(this.index++) : null;
	}

}
