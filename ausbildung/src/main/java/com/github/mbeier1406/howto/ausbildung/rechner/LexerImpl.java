package com.github.mbeier1406.howto.ausbildung.rechner;

import static com.github.mbeier1406.howto.ausbildung.rechner.Lexer.TokenTyp.GANZZAHL;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Die Standardimplementierung für den {@linkplain Lexer}.
 */
public class LexerImpl implements Lexer {

	public static Logger LOGGER = LogManager.getLogger(LexerImpl.class);

	/** {@inheritDoc} */
	@Override
	public List<Token> getTokens(String text) throws LexerException {
		final List<Token> listOfTokens = new ArrayList<>();
		try ( CloseableThreadContext.Instance ctx = CloseableThreadContext.put("text", text) ) {
			for ( int i=0; i < Objects.requireNonNull(text, "Text fehlt!").length(); i++ ) {
				char ch = text.charAt(i);
				LOGGER.trace("i={}; v={}", i, ch);
				if ( LIST_OF_BLANKS.contains(ch) ) continue;
				switch ( ch ) {
					case PLUS_SIGN -> i = getOperandOrNumber(text, i, listOfTokens, PLUS_TOKEN, 1);
					case MINUS_SIGN -> i = getOperandOrNumber(text, i, listOfTokens, MINUS_TOKEN, -1);
					default -> throw new LexerException("Ungültiges Zeichen '"+ch+"' an Position "+i+"!");
				}
			}
		}
		catch ( Exception e ) {
			throw new LexerException("text="+text+": "+e.getMessage(), e);
		}
		return listOfTokens;
	}

	/**
	 * Fügt zur Liste der bereits eingelesenen {@linkplain Token} zu einem vorhandenen {@value Lexer#PLUS_SIGN}
	 * oder {@value Lexer#MINUS_SIGN} hinzu, entweder:
	 * <ul>
	 * <li>Das {@linkplain Lexer#PLUS_TOKEN} oder {@linkplain Lexer#MINUS_TOKEN} falls nicht direkt eine Ziffer folgt</li>
	 * <li>Ein Token {@linkplain Lexer.TokenTyp#GANZZAHL} mit positivem oder negativem Wert</li>
	 * </ul>
	 * Liefert 
	 * @param text die Eingabeformel
	 * @param i der Index innerhalb der Formel, an dem der Operand für die Addition/Sbtraktion gerade gefunden worde
	 * @param listOfTokens die Liste der bereits eingelesenen Token, an die das neue angefügt wird
	 * @param t das einzufüende {@linkplain Lexer#PLUS_TOKEN} oder {@linkplain Lexer#MINUS_TOKEN}, falls keine Ziffer folgt
	 * @param multiplikator falls eine Ganzzahl gefunden wurde, der Faktor für die Berechnung des Gazzahlwerts (positiv/negativ)
	 * @return den Index vor dem Zeichen, an dem die Verarbeitung in {@linkplain #getTokens(String)} fortgesetzt werden muss
	 */
	public int getOperandOrNumber(String text, int i, final List<Token> listOfTokens, final Token t, int multiplikator) {
		if ( isDigitFollowing(text, i) ) { // nach + bzw. - folgt eine Ziffer -> Token ist Ganzzahl, kein Operand
			StringBuilder sb = new StringBuilder();
			while ( isDigitFollowing(text, ++i) )
				sb.append(text.charAt(i));
			sb.append(text.charAt(i));
			Integer z = Integer.parseInt(sb.toString());
			listOfTokens.add(new Token(GANZZAHL, z*multiplikator));
		}
		else
			listOfTokens.add(t); // Token ist + oder -
		return i;
	}

	/** Prüft, ob nach dem gerade verarbeiteten Zeichen noch eine Ziffer folgt */
	private boolean isDigitFollowing(String text, int i) {
		return i < text.length()-1 && text.charAt(++i) >= '0' && text.charAt(i) <= '9';
	}

}
