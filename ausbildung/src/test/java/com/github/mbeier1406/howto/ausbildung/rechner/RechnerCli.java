package com.github.mbeier1406.howto.ausbildung.rechner;

import java.util.Scanner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import com.github.mbeier1406.howto.ausbildung.rechner.Lexer.LexerException;
import com.github.mbeier1406.howto.ausbildung.rechner.Parser.ParserException;

/**
 * Bietet eine eingeschränkte Taschenrechnerfunktion über eine Texteingabe.
 * Beispiel:
 * <pre><code>
 * > 1 + 1
 * 2.0
 * </code></pre><p/>
 * Das Erebnis der Formal wird immer als unformartierte Fließkommazahl ausgegeben.
 * @see Lexer
 * @see Parser
 */
public class RechnerCli {

	/** Liest solange mathematische Formeln ein und wertet sie aus, bis eine Zeile mit 'e' eingegeben wird */
	public static void main(String[] args) {
		Configurator.setLevel("com.github.mbeier1406.howto.ausbildung.rechner", Level.ERROR);
		Lexer lexer = new LexerImpl();
		Parser parser = new ParserImpl();
		try ( final var scanner = new Scanner(System.in) ) {
			System.out.println("Taschenrechner, bitte Formel eingeben ('e' für \"Ende\").");
			while ( true ) {
				System.out.print("> ");
				String formel = scanner.nextLine();
				if ( formel.length() == 0 ) continue;
				if ( formel.equalsIgnoreCase("e") ) break;
				try {
					System.out.println(parser.evaluate(lexer.getTokens(formel)));
				} catch (ParserException | LexerException e) {
					System.out.println("Fehler: " + e.getMessage());
				}
			}
			System.out.println("Fertig.");
		}
	}

}
