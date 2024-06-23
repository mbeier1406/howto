package com.github.mbeier1406.howto.ausbildung.mt;

import java.util.stream.Stream;

/**
 * Demonstriert das Data Race-Problem: solange die Programmlogik nicht verletzt wird,
 * kann die Ausführung von Code in der Reihenfolge geändert werden. In den Imlementierungen
 * von {@linkplain Container#increment()} gibt es zwischen der Inkremntierung von x und y
 * keinen Zusammenhang, weshalb y größer als x sein kann obwohl das textuell nicht möglich
 * sein sollte.<p/>
 * Abhilfe schafft das Schlüsselwort <b>volatile</b>, das die Einhaltung der Reihenfolge für
 * die Änderungen erzwingt. Die Ausführung des Threads dauert entsprechend länger.
 * @author mbeier
 */
public class DataRace {

	/** Führt die Inkrementierung und die Prüfung für die zwei Implementierungen durch */
	public static void main(String[] args) {
		Stream.of(new StandardContainer(), new VolatileContainer()).forEach(c -> {
			System.out.println("c="+c.getClass().getName());
			new Thread(new Runnable() {
				@Override
				public void run() {
					for ( int i=0; i < Integer.MAX_VALUE; i++ )
						c.increment();
					System.out.println(String.format("%s: increment() fertig.", c.getClass().getName()));
				}
			}).start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					for ( int i=0; i < Integer.MAX_VALUE; i++ )
						c.check();
					System.out.println(String.format("%s: check() fertig.", c.getClass().getName()));
				}
			}).start();
		});
	}

	/** Inkremntierung und Prüfung der Invariante x muss größer oder gleich y sein */
	public static interface Container {
		public void increment();
		public void check();
	}

	/** Bei der Ausführung kann die Reihenfolge in {@linkplain #increment()} geändert werden: Invariate verletzt */
	public static class StandardContainer implements Container {
		int x=0, y=0;
		@Override
		public void increment() {
			x++;
			y++;
		}
		@Override
		public void check() {
			if ( y > x )
				System.out.println(String.format("%s: Fehler: x=%d, y=%d", getClass().getName(), x, y));
		}
	}

	/** Bei der Ausführung kann die Reihenfolge in {@linkplain #increment()} <u>nicht</u> geändert werden: Invariate nicht verletzt */
	public static class VolatileContainer implements Container {
		volatile int x=0, y=0;
		@Override
		public void increment() {
			x++;
			y++;
		}
		@Override
		public void check() {
			if ( y > x )
				System.out.println(String.format("%s: Fehler: x=%d, y=%d", getClass().getName(), x, y));
		}
	}

}
