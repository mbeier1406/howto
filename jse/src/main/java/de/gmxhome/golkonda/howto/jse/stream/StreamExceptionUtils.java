package de.gmxhome.golkonda.howto.jse.stream;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Vereinfachtes Exceptionhandling für {@linkplain Stream}s.
 * Mapping von checked Exceptions auf {@linkplain RuntimeException}s.
 * <p>Beispiel:</p>
 * <pre><code>
 *	// Eine Methode, die eine Exception erzeugen kann
 * 	public int f(Integer x) throws Exception {
 *		if ( null == x ) throw new Exception();
 *		return x+1;
 *	}
 *
 *	// wandelt die checked Exceptio in eine RuntimeException um
 *	int sum = INT_STREAM
 *		.map(wrapEx1(this::f))
 *		.reduce(0, (a, b) -> a+b);
 *</code></pre>
 * @author mbeier
 */
public class StreamExceptionUtils {

	/**
	 * Für Methoden mit einem Parameter, die eine {@linkplain Exception} werfen können
	 * und innerhalb eines {@linkplain Stream}s aufgerufen werden.
	 * @author mbeier
	 * @param <P> der Typ des Parameters der Methode
	 * @param <R> der Typ des Rückgabewertes der Methode
	 */
	@FunctionalInterface
	public interface Function1 <P, R> {
		public R apply(P p) throws Exception;
	}

	/**
	 * Stream-Funktionen wie zum Beispiel {@linkplain Stream#map(Function)}, die eine {@linkplain Function}
	 * als Parameter erwarten und eine Methode <b>mit einem Parameter</b> aufrufen, die checked Exceptions werfen kann, können diese
	 * Methode verwenden, um diese in eine {@linkplain RuntimeException} umzuwandeln um das Exceptionhandling zu vermeiden.
	 * @param <P> der Typ des Parameters der aufzurufenden Methode
	 * @param <R> der Typ des Rückgabewertes der aufzurufenden Methode
	 * @param x die aufzurufende Methode
	 * @return das Ergebnis des Aufrufs vom typ <b>&lt;R></b>
	 * @see Function1
	 */
	public static <P, R> Function<P, R> wrapEx1(Function1<P, R> x) {
		return p -> {
			try {
				return x.apply(p);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

}
