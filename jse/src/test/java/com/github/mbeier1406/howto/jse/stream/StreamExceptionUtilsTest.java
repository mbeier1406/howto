package com.github.mbeier1406.howto.jse.stream;

import static com.github.mbeier1406.howto.jse.stream.StreamExceptionUtils.wrapException;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.util.StreamUtils;
import org.junit.Test;

import com.github.mbeier1406.howto.jse.stream.StreamExceptionUtils.CheckedFunction;


/**
 * Tests für die Klasse {@linkplain StreamUtils}.
 * @author mbeier
 */
public class StreamExceptionUtilsTest {

	public static final Logger LOGGER = LogManager.getLogger(StreamExceptionUtilsTest.class);

	/** Testdaten für Streams */
	public static final Stream<Integer> INT_STREAM = Stream.of(1, 2, 3, null, 5, 6);

	/**	Testmethode für {@linkplain CheckedFunction} */
	public int f(Integer x) throws Exception {
		LOGGER.info("x={}", x);
		if ( null == x ) throw new Exception();
		return x+1;
	}

	/** Testet, ob {@linkplain StreamExceptionUtils#wrapEx1} eine {@linkplain RuntimeException} erzeugt */
	@Test(expected = RuntimeException.class)
	public void testeExceptionWrapperMitEinemParameter() {
		int sum = INT_STREAM
				.peek(LOGGER::info)
				.map(wrapException(this::f))
				.reduce(0, (a, b) -> a+b);
		LOGGER.info("sum={}", sum);
	}

}
