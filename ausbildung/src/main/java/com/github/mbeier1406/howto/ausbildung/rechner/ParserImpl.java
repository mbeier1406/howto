package com.github.mbeier1406.howto.ausbildung.rechner;

import java.util.List;

import com.github.mbeier1406.howto.ausbildung.rechner.token.GanzzahlToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.MinusToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.PlusToken;

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
			throw new ParserException();
		return result;
	}

	/** Liest einen mathematischen Ausdruck, d. h. eine Addition/Subtraktion von Faktoren ein */
	private double parseExpression() throws ParserException {
		double ergebnis = parseTerminal();
		while ( this.index < this.listOfTokens.size() ) {
			var token = this.listOfTokens.get(this.index++);
			if ( token instanceof PlusToken )
				ergebnis += parseTerminal();
			else if ( token instanceof MinusToken )
				ergebnis -= parseTerminal();
		}
		return ergebnis;
	}

	/** Liest ein Terminal des Ausdrucks (Zah, Klammer) ein */
	private double parseTerminal() throws ParserException {
		var token = this.listOfTokens.get(this.index++);
		if ( token instanceof GanzzahlToken )
			return (int) token.getValue().get();
		else
			throw new ParserException("Unerwartetes Token '"+token+"' an Index "+this.index+": Terminal (Ganzzahl) erwartet!");
	}

}
