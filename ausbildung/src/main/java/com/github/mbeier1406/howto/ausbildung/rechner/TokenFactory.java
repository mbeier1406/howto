package com.github.mbeier1406.howto.ausbildung.rechner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.github.mbeier1406.howto.ausbildung.rechner.token.GanzzahlToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.PlusToken;
import com.github.mbeier1406.howto.ausbildung.rechner.token.Token;

/**
 * Liefert die Liste aller bekannten Token mit den zugeordneten Symbolen.
 * Damit kann der {@linkplain Lexer} zum aktuell gefundenen Symbol (Zeichen)
 * aus der Map den zugehörigen Token mit dem zugehörigen Reader ermitteln:
 * <ul>
 * <li><b>+</b>: das {@linkplain PlusToken}</li>
 * <li><b>-</b>: das {@linkplain MinusToken}</li>
 * <li><b>0</b>: das {@linkplain GanzzahlToken}</li>
 * <li><b>1</b>: das {@linkplain GanzzahlToken}</li>
 * <li>...</li>
 * <li><b>9</b>: das {@linkplain GanzzahlToken}</li>
 * </ul>
 * @see Token
 */
public class TokenFactory {

	/** Ermitteln der {@linkplain Token} über {@linkplain Reflections} */
	public static Map<Character, TokenInterface> getTokens() {
		try {
			Set<Class<?>> tokenClasses = new Reflections("com.github.mbeier1406.howto.ausbildung.rechner.token")
				.getTypesAnnotatedWith(Token.class);
			final Map<Character, TokenInterface> tokens = new HashMap<>();
			tokenClasses.forEach(tokenClass -> {
				try {
					TokenInterface token = (TokenInterface) tokenClass.getConstructor().newInstance();
					for ( Character ch : token.getSymbols() )
						tokens.put(ch, token);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new RuntimeException("tokenClass="+tokenClass, e);
				}
			});
			return tokens;
		}
		catch ( Exception e ) {
			throw new RuntimeException("Token können nicht mehr Refelction ermittelt werden!", e);
		}
	}

}
