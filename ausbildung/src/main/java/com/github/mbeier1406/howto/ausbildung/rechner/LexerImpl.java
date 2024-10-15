package com.github.mbeier1406.howto.ausbildung.rechner;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.howto.ausbildung.rechner.token.GanzzahlToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.MinusToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.PlusToken;


/**
 * Die Standardimplementierung für den {@linkplain Lexer}.
 */
public class LexerImpl implements Lexer {

	public static final Logger LOGGER = LogManager.getLogger(LexerImpl.class);

	/** Alle bekannten Token laden */
	private Map<Character, TokenInterface> tokens = TokenFactory.getTokens();

	
	/** {@inheritDoc} */
	@Override
	public List<TokenInterface> getTokens(String text) throws LexerException {
		final List<TokenInterface> listOfTokens = new ArrayList<>();
		/*
		 * Damit aus den Token '+' gefolgt '123' die Zahl 123 bzw. aus '-' gefolgt von '123'
		 * die Zahl -123 wird, muss bei einem eingelehsenen Ganzzahltoken geprüft werden,
		 * ob ein Plsu-/Minustoken ohne Leerzeichen dazwischen das vorhergende Token war.
		 * Entsprechend hier merken, ob Leerzeichen gelesen wurden.
		 */
		boolean leerZeichenGelesen = false;
		try ( CloseableThreadContext.Instance ctx = CloseableThreadContext.put("text", text) ) {
			for ( int i=0; i < requireNonNull(text, "text").length(); ) {
				char ch = text.charAt(i);
				LOGGER.trace("i={}; v='{}'", i, ch);
				if ( LIST_OF_BLANKS.contains(ch) ) {
					// Falls als nächstes eine Ganzzahl gelesen wird, ggf. vorher gelesenen +/- ignorieren
					leerZeichenGelesen = true;
					i++; // nächstes Symbol einlesen
					continue;
				}
				final var token = requireNonNull(tokens.get(ch), "Unbekanntes Symbol '"+ch+"'; index="+i);
				LOGGER.trace("token={}", token);
				final var value = token.read(text.substring(i));
				LOGGER.trace("value={}", value);
				i += value.length(); // Stelle nach dem Token, ab der weiter gelesen werden muss
				var neuesToken = value.token(); // wir gehen davon aus, dass das gelesen Token nicht korrigiert werden muss
				if ( value.token() instanceof GanzzahlToken && !leerZeichenGelesen && listOfTokens.size() > 0 ) {
					var lastToken = listOfTokens.get(listOfTokens.size()-1); // prüfen, ob das vorhergehende Token ein + oder - war
					Integer vorzeichen = null; // wenn != null, dann Vorzeichen auf +/1 setzen
					if ( lastToken instanceof MinusToken ) vorzeichen = -1;
					else if ( lastToken instanceof PlusToken ) vorzeichen = 1;
					if ( vorzeichen != null ) {
						// letztes Token löschen (gehört zur aktuellen Ganzzahl) und Wert der Ganzzahl neu berechnen
						listOfTokens.remove(listOfTokens.size()-1);
						neuesToken = new GanzzahlToken(((Integer) value.token().getValue().get()) * vorzeichen);
					}
				}
				listOfTokens.add(neuesToken);
				leerZeichenGelesen = false; // da ein Token hinzugefügt wurde
			}
		}
		catch ( Exception e ) {
			throw new LexerException("text='"+text+"': "+e.getMessage(), e);
		}
		return listOfTokens;
	}

}
