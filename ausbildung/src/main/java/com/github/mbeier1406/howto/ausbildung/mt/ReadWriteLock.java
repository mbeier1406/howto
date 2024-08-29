package com.github.mbeier1406.howto.ausbildung.mt;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Zeigt die Verwendung eines Read/Write-Locks: ein Thread verändert Daten
 * einer geteilten Struktur, eine {@linkplain Map}, viele Threads lesen. Je nach verwendetem Lock
 * läuft die Anwendung unterschiedlich schnell durch.
 * @author mbeier
 */
public class ReadWriteLock {

	public static final Logger LOGGER = LogManager.getLogger(ReadWriteLock.class);

	/** Die Anzahl der Items in der geteilten Datenstruktur ist {@value} */
	public static final int NUM_ITEMS = 1000;

	/** Anazahl der lesenden Threads ist {@value} */
	public static final int NUM_READER_THREADS = 100;

	/**
	 * Der Key für die Map (geteilte Struktur) mit einem etwas
	 * aufwändigerem {@linkplain Comparator}, damit die Lese-Threads
	 * etwas Zeit verbrauchen.
	 */
	public static class Code implements Comparable<Code> {
		private final char type;
		private final int num;
		private final String info;
		public Code(char type, int num, String info) {
			super();
			this.type = type;
			this.num = num;
			this.info = info;
		}
		public char getType() {
			return type;
		}
		public int getNum() {
			return num;
		}
		public String getInfo() {
			return info;
		}
		@Override
		public String toString() {
			return "Code [type=" + type + ", num=" + num + ", info=" + info + "]";
		}
		@Override
		public int compareTo(Code o) {
			if ( o == null ) // verwendet nicht alle Felder
				return 1;
			else if ( this.type == o.getType() )
				return this.num - o.getNum();
			return this.type - o.getType();
		}
	}

	/** Damit die Keys in der Map (geteilte Struktur) verglichen werden können */
	public static class PersonComparator implements Comparator<Code> {
		@Override
		public int compare(Code o1, Code o2) {
			return o1.compareTo(o2);
		}
	}

	/** Von diesem/in dieses Objekt wird konkurrierend gelesen (mehrere Threads)/geschrieben (ein Thread) */
	private static final TreeMap<Code, Integer> SHARED_MAP = new TreeMap<>(new PersonComparator());

	/**
	 * Die Schnittstelle zu den Implementierungen, deren Threads die geteilten Struktur lesen und schreiben.
	 * {@linkplain CodeRepository#suchen(Code)} von den Lese-Threads, die andere vom Schreib-Thread.
	 */
	private static interface CodeRepository {
		public int suchen(Code code);
		public void entfernen(Code code);
		public void hinzufuegen(Code code);
	}

	/** Basis-Implementierung ohne Synchronisation */
	public abstract static class CodeRepositoryBasis implements CodeRepository {
		protected final TreeMap<Code, Integer> map;
		protected CodeRepositoryBasis(TreeMap<Code, Integer> map) {
			this.map = map;
		}
		@Override
		public int suchen(Code code) {
			Integer anz = null, diff = 0;
			while ( anz == null ) {
				var c = new Code(code.getType(), code.getNum()+diff, code.getInfo());
				if ( (anz=this.map.get(c)) == null ) {
					c = new Code(code.getType(), code.getNum()-diff, code.getInfo());;
					anz = this.map.get(c);
				}
				if ( diff++ > NUM_ITEMS )
					throw new RuntimeException("Kein Treffer für " + code);
			}
			return anz;
		}
		@Override
		public void entfernen(Code code) {
			op(code, f -> f-1);
		}
		@Override
		public void hinzufuegen(Code code) {
			op(code, f -> f+1);
		}
		private void op(Code code, Function<Integer, Integer> f) {
			Integer anz=this.map.get(code);
			if ( anz != null ) {
				this.map.put(code, f.apply(anz));
			}
		}
	}

	/** Diese Implementierung ist voll synchronisiert, langsam */
	public static class CodeRepositoryFullBlocking extends CodeRepositoryBasis implements CodeRepository {
		public CodeRepositoryFullBlocking(TreeMap<Code, Integer> map) {
			super(map);
		}
		@Override
		public synchronized int suchen(Code code) {
			return super.suchen(code);
		}
		@Override
		public synchronized void entfernen(Code code) {
			super.entfernen(code);
		}
		@Override
		public synchronized void hinzufuegen(Code code) {
			super.hinzufuegen(code);
		}
	}

	/** Diese Implementierung ist voll synchronisiert, langsam */
	public static class CodeRepositoryBlocking extends CodeRepositoryBasis implements CodeRepository {
		private Lock lock = new ReentrantLock();
		public CodeRepositoryBlocking(TreeMap<Code, Integer> map) {
			super(map);
		}
		@Override
		public int suchen(Code code) {
			lock.lock();
			try { return super.suchen(code); } finally { lock.unlock(); }
		}
		@Override
		public void entfernen(Code code) {
			lock.lock();
			try { super.entfernen(code); } finally { lock.unlock(); }
		}
		@Override
		public void hinzufuegen(Code code) {
			lock.lock();
			try { super.hinzufuegen(code); } finally { lock.unlock(); }
		}
	}

	/** Diese Implementierung ermöglicht paralleles Lesen, schnell */
	public static class CodeRepositoryNoneBlocking extends CodeRepositoryBasis implements CodeRepository {
		private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		private Lock readLock = lock.readLock(), writeLock = lock.writeLock();
		public CodeRepositoryNoneBlocking(TreeMap<Code, Integer> map) {
			super(map);
		}
		@Override
		public int suchen(Code code) {
			readLock.lock();
			try { return super.suchen(code); } finally { readLock.unlock(); }
		}
		@Override
		public void entfernen(Code code) {
			writeLock.lock();
			try { super.entfernen(code); } finally { writeLock.unlock(); }
		}
		@Override
		public void hinzufuegen(Code code) {
			writeLock.lock();
			try { super.hinzufuegen(code); } finally { writeLock.unlock(); }
		}
	}

	/** Aus dieser Klasse werden die Lese-Threads auf das geteilte Objekt generiert */
	public static class CodeRepositoryReader extends Thread {
		private CodeRepository codeRepository;
		private Random random = new Random();
		public CodeRepositoryReader(final CodeRepository codeRepository) {
			this.codeRepository = codeRepository;
		}
		@Override
		public void run() {
			LOGGER.trace("Starte Lesen {}...", Thread.currentThread());
			for ( int i=0; i < 100000; i++ )
				codeRepository.suchen(new Code((char) (random.nextInt(26)+65), random.nextInt(NUM_ITEMS), ""));
			LOGGER.trace("Fertig Lesen {}.", Thread.currentThread());
		}
	}

	/** Aus dieser Klasse wird der Schreib-Thread auf das geteilte Objekt generiert */
	public static class CodeRepositoryWriter extends Thread {
		private CodeRepository codeRepository;
		private Random random = new Random();
		public CodeRepositoryWriter(final CodeRepository codeRepository) {
			this.codeRepository = codeRepository;
		}
		@Override
		public void run() {
			LOGGER.trace("Starte Schreiben {}...", Thread.currentThread());
			final var code = new Code((char) (random.nextInt(26)+65), random.nextInt(NUM_ITEMS), "");
			for ( int i=0; i < 500000; i++ )
				if ( random.nextInt()%2 == 0 )
					codeRepository.hinzufuegen(code);
				else
					codeRepository.entfernen(code);
			LOGGER.trace("Fertig Schreiben {}.", Thread.currentThread());
		}
	}

	/** Initialisert das geteilte Objekt und misst die Laufzeit für die verschiedenen Implementierungen des {@linkplain CodeRepository} */
	public static void main(String[] args) throws InterruptedException {
		var random = new Random();
		var bytes = new byte[10];
		IntStream // Map, mit der getestet wird, mit etwas Daten füllen
			.range(0, NUM_ITEMS)
			.map(i -> { random.nextBytes(bytes); for (int j=0; j < bytes.length; j++) if ( bytes[j] < 97 ) bytes[j]=97; else if ( bytes[j]  > 122 ) bytes[j]=122; return i; })
			.mapToObj(i -> new Code((char) (random.nextInt(26)+65), i, new String(bytes, Charset.forName("UTF-8"))))
			.peek(LOGGER::trace)
			.forEach(c -> SHARED_MAP.put(c, random.nextInt(1000)));
		// final var codeRepository = new CodeRepositoryFullBlocking(SHARED_MAP);
		// final var codeRepository = new CodeRepositoryBlocking(SHARED_MAP);
		final var codeRepository = new CodeRepositoryNoneBlocking(SHARED_MAP);
		final var codeRepositoryWriter = new CodeRepositoryWriter(codeRepository);
		final var codeRepositoryReaderList = IntStream
				.range(0,  NUM_READER_THREADS)
				.mapToObj(i -> new CodeRepositoryReader(codeRepository))
				.collect(Collectors.toList());
		final var start = LocalDateTime.now();
		LOGGER.trace("Start: {}", start);
		codeRepositoryWriter.start();
		codeRepositoryReaderList.forEach(Thread::start);
		codeRepositoryWriter.join();
		codeRepositoryReaderList.forEach(t -> { try { t.join(); } catch (InterruptedException e) {} });
		LOGGER.trace("Ende: {} -> {}", start, LocalDateTime.now());
	}

}
