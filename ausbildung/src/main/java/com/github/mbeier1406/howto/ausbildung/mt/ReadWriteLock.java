package com.github.mbeier1406.howto.ausbildung.mt;

import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadWriteLock {

	public static final Logger LOGGER = LogManager.getLogger(ReadWriteLock.class);

	public static final int NUM_ITEMS = 10;

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
			if ( o == null )
				return 1;
			else if ( this.type == o.getType() )
				return this.num - o.getNum();
			return this.type - o.getType();
		}
	}

	public static class PersonComparator implements Comparator<Code> {
		@Override
		public int compare(Code o1, Code o2) {
			return o1.compareTo(o2);
		}
	}

	/** Von diesem/in dieses Objekt wird konkurrierend gelesen (mehrere Threads)/geschrieben (ein Thread) */
	private static final TreeMap<Code, Integer> map = new TreeMap<>(new PersonComparator());


	private static interface CodeRepository {
		public int suchen(Code code);
		public void entfernen(Code code);
		public void hinzufuegen(Code code);
	}

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

	public static class CodeRepositoryBlocking extends CodeRepositoryBasis implements CodeRepository {
		private Lock lock = new ReentrantLock();
		public CodeRepositoryBlocking(TreeMap<Code, Integer> map) {
			super(map);
		}
		@Override
		public int suchen(Code code) {
			return super.suchen(code);
		}
		@Override
		public void entfernen(Code code) {
			super.entfernen(code);
		}
		@Override
		public void hinzufuegen(Code code) {
			super.hinzufuegen(code);
		}
	}

	public static void main(String[] args) {
		var random = new Random();
		var bytes = new byte[10];
		IntStream // Map, mit der getestet wird, mit etwas Daten füllen
			.range(0, NUM_ITEMS)
			.map(i -> { random.nextBytes(bytes); for (int j=0; j < bytes.length; j++) if ( bytes[j] < 97 ) bytes[j]=97; else if ( bytes[j]  > 122 ) bytes[j]=122; return i; })
			.mapToObj(i -> new Code((char) (random.nextInt(26)+65), i, new String(bytes, Charset.forName("UTF-8"))))
			.peek(LOGGER::trace)
			.forEach(c -> map.put(c, random.nextInt(1000)));
		final var codeRepository = new CodeRepositoryBlocking(map);
		Entry<Code, Integer> entry = map.entrySet().stream().findFirst().get();
		LOGGER.info("a={}", entry);
		codeRepository.hinzufuegen(entry.getKey());
		LOGGER.info("b={}", map.get(entry.getKey()));
		codeRepository.entfernen(entry.getKey());
		LOGGER.info("c={}", map.get(entry.getKey()));
	}

}
