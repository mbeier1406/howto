package com.github.mbeier1406.howto.ausbildung.mt;

import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadWriteLock {

	public static final Logger LOGGER = LogManager.getLogger(ReadWriteLock.class);

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
				return this.num -o.getNum();
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
		public Code suchen(Code code);
		public void entfernen(Code code);
		public void hinzufuegen(Code code);
	}

	public static class CodeRepositoryBlocking implements CodeRepository {
		private final TreeMap<Code, Integer> map;
		private Lock lock = new ReentrantLock();
		public CodeRepositoryBlocking(TreeMap<Code, Integer> map) {
			super();
			this.map = map;
		}
		@Override
		public Code suchen(Code code) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void entfernen(Code code) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void hinzufuegen(Code code) {
			// TODO Auto-generated method stub
			
		}
	}

	public static void main(String[] args) {
		var random = new Random();
		var bytes = new byte[10];
		IntStream // Map, mit der getestet wird, mit etwas Daten füllen
			.range(0, 10000)
			.map(i -> { random.nextBytes(bytes); for (int j=0; j < bytes.length; j++) if ( bytes[j] < 97 ) bytes[j]=97; else if ( bytes[j]  > 122 ) bytes[j]=122; return i; })
			.mapToObj(i -> new Code((char) (random.nextInt(26)+65), i, new String(bytes, Charset.forName("UTF-8"))))
			.peek(LOGGER::info)
			.forEach(c -> map.put(c, random.nextInt(1000)));
		LOGGER.info("a={}", map.size());
	}

}
