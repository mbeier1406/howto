package com.github.mbeier1406.howto.ausbildung.rechner;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.howto.ausbildung.rechner.token.CommaToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.DezimalToken;
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
		boolean kannVorzeichenSein = false;
		/*
		 * Wenn ein Komma (",") gelesen wurde (und geprüft wurde, dass sich direkt davor eine Ganzzahl
		 * befand, dann muss danach auch eine Ganzzahl (der Dezimalanteil) folgen. Die drei Token
		 * werden dann durch ein DezimalToken ersetzt. Das wird sich hier gemerkt.
		 */
		boolean parseDezimalZahl = false;
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
				if ( parseDezimalZahl && !(value.token() instanceof GanzzahlToken) )
					/* Wir haben zuvor ein Komma (',') gelesen, also wird jetzt der Dezimalanteil 0-9... erwartet */
					throw new LexerException("Nach dem Komma wird der Dezimalanteil erwartet!");
				else if ( parseDezimalZahl && value.token() instanceof GanzzahlToken ) {
					/*
					 * Die Dezimalzahl wurde an dieser Stelle erfolgreich eingelesen. Sie besteht hier jetzt aus
					 * drei Token: Ganzzahl, Komma, Ganzzahl. Die beiden ersten wurden bereits eingelesen und
					 * befinden sich daher schon in der Liste. Also:
					 * 1. Markierung für das Einlesen einer Dezimalzahl löschen
					 * 2. Die lezten beiden Token in der Liste (Ganzzahl und Komma) löschen
					 * 3. Neues Dezimaltoken erzeugen und einfügen
					 */
					parseDezimalZahl = false;
					double wert = Double.parseDouble(
							String.valueOf(listOfTokens.get(listOfTokens.size()-2).getValue().get())+"."+
							String.valueOf(value.token().getValue().get()));
					listOfTokens.remove(listOfTokens.size()-1);
					listOfTokens.remove(listOfTokens.size()-1);
					neuesToken = new DezimalToken(wert);
				}
				else if ( value.token() instanceof GanzzahlToken && !leerZeichenGelesen && listOfTokens.size() > 0 && kannVorzeichenSein ) {
					/*
					 * Wir haben eine Zahl der keine Leerstelle aber mindestens ein Token (welches ein + oder - sein kann)
					 * vorangestellt ist, und vor diesem Token befand sich mindestens eine Leerstelle.
					 * Es kann sich also bei dem zuvor gelesenen Token um das Vorzeichen dieser Zahl handeln
					 */
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
				else if ( value.token() instanceof PlusToken || value.token() instanceof MinusToken ) {
					/*
					 * Hier wird berechnet, ob es sich bei dem gerade gelesenen +/- Token um das Vorzeichen einer
					 * ggf. noch folgenden Zahl handeln kann. Dies ist der Fall, wenn
					 * 1. es sich um das erste Token der Formel handelt ("+123 + 4");
					 * 2. zuvor ein +/- Token ohne trennendes Leerzeichen gelesen wurde ("++123");
					 * 3. zuvor mindestens eine Leerstelle gelesen wurde ("1 + 1"),
					 */
					if ( listOfTokens.size() == 0 )
						/* Als erstes Token in der Formel kann es sich bei diesem +/- um das Vorzeichen einer noch folgenden Zahl sein */
						kannVorzeichenSein = true;
					else if ( listOfTokens.size() > 0 && !leerZeichenGelesen ) {
						/*
						 * Wenn zvor bereits ein + oder - eingelesen wurde, ohne dass Leerstellen gefolgt sind,
						 * kann es sich bei diesem +/- um ein Vorzeichen handeln:
						 * ++3 wird dann zu '+' und '3'. 
						 */
						var lastToken = listOfTokens.get(listOfTokens.size()-1); // prüfen, ob das vorhergehende Token ein + oder - war
						if ( lastToken instanceof MinusToken || lastToken instanceof PlusToken )
							kannVorzeichenSein = true;
					}
					else
						/* 
						 * Wenn wir zuvor ein Leerzeichen geesen haben, kann es sich bei diesem +/- um das Vorzeichen
						 * einer Zahl handeln, das also merken falls gleich danach eine Zahl eingelesen wird
						 */
						kannVorzeichenSein = leerZeichenGelesen;
				}
				else if ( value.token() instanceof CommaToken ) {
					if ( leerZeichenGelesen )
						throw new LexerException("Leerstellen vor dem Komma nicht erlaubt!");
					if ( !(listOfTokens.size() > 0 && listOfTokens.get(listOfTokens.size()-1) instanceof GanzzahlToken) )
						throw new LexerException("Dem Komma muss eine Ganzzahl vorangehen!");
					parseDezimalZahl = true;
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
