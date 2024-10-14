package com.github.mbeier1406.howto.ausbildung.rechner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

import com.github.mbeier1406.howto.ausbildung.rechner.token.Token;

public class TokenFactory {

	public static Set<TokenInterface> getTokens() {
		Set<Class<?>> tokenClasses = new Reflections("com.github.mbeier1406.howto.ausbildung.rechner.token")
			.getTypesAnnotatedWith(Token.class);
		Set<TokenInterface> tokens = new HashSet<>();
		tokenClasses.forEach(tokenClass -> {
			try {
				tokens.add((TokenInterface) tokenClass.getConstructor().newInstance());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException("tokenClass="+tokenClass, e);
			}
		});
		return tokens;
	}

}
