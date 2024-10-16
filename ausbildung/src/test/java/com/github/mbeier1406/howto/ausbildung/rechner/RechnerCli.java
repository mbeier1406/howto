package com.github.mbeier1406.howto.ausbildung.rechner;

import java.util.Scanner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import com.github.mbeier1406.howto.ausbildung.rechner.Lexer.LexerException;
import com.github.mbeier1406.howto.ausbildung.rechner.Parser.ParserException;

/**
 * Bietet eine eingeschränkte Taschenrechnerfunktion über eine Texteingabe.
 * @see Lexer
 * @see Parser
 */
public class RechnerCli {

	public static void main(String[] args) {
		Configurator.setLevel("com.github.mbeier1406.howto.ausbildung.rechner", Level.ERROR);
		Lexer lexer = new LexerImpl();
		Parser parser = new ParserImpl();
		try ( final var scanner = new Scanner(System.in) ) {
			System.out.println("Taschenrechner, bitte Formel eingeben.");
			while ( true ) {
				System.out.print("> ");
				String formel = scanner.nextLine();
				if ( formel.length() == 0 ) break;
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
