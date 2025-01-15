package com.github.mbeier1406.howto.ausbildung.gof.creational;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain Singelton}.
 */
public class SingeltonTest {

	/** Anzahl der Threads, die das Singelton instantiieren, ist {@value} */
	public static final int ANZ_PROC = 100;

	/** Liste der Referenzen auf das Singelton erzeugt durch die {@value #ANZ_PROC} Threads müssen gleich sein */
	public Singelton[] singeltonList = new Singelton[ANZ_PROC];

	/** Initialisiert das {@linkplain Singelton} und speichert die Referenz in der Liste {@linkplain #singeltonList} */
	public static final class SingeltonInitializer extends Thread {
		final int index;
		final Singelton[] singeltonList;
		public SingeltonInitializer(int index, final Singelton[] singeltonList) {
			this.index = index;
			this.singeltonList = singeltonList;
		}
		@Override
		public void run() {
			// Jeweils abwechselnd die Initialiserer aufrufen
			this.singeltonList[this.index] = this.index%2 == 0 ? Singelton.getInstance(): Singelton.getInstance2();
		}
	}

	/** Startet parallel {@value SingeltonTest#ANZ_PROC} Threads zur Erzeugung des {@linkplain Singelton} und stellt sicher, dass es nur eine Instanz gibt */
	@Test
	public void testeInitialisierung() {
		var threadList = new SingeltonInitializer[singeltonList.length];
		IntStream.range(0, singeltonList.length).forEach(i -> threadList[i] = new SingeltonInitializer(i, singeltonList));
		IntStream.range(0, singeltonList.length).forEach(i -> threadList[i].start());
		IntStream.range(0, singeltonList.length).forEach(i -> {
			try {
				threadList[i].join(); // Warten, bis alle Threads fertig sind
			} catch (InterruptedException e) {
				throw new RuntimeException();
			}
		});
		Singelton s = this.singeltonList[0];
		for ( int i=1; i < this.singeltonList.length; i++ )
			assertThat(s == this.singeltonList[i], equalTo(true)); // alle initialisierten Singeltons haben die gleiche Referenz
		assertThat(Arrays.stream(singeltonList).peek(System.out::println).distinct().count(), equalTo(1L)); // Es gibt nur eine Instanz
		assertThat(singeltonList[0], not(equalTo(null))); // Referenzen sind auch gültig
	}

}
