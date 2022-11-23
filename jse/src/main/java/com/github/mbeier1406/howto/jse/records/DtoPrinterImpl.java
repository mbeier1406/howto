package com.github.mbeier1406.howto.jse.records;

import static java.util.Objects.requireNonNull;

/**
 * Demonstriert die Eigenschaften von Records.
 * Records können Interfaces implemntieren.
 * @author mbeier
 */
public record DtoPrinterImpl(String str, Integer num) implements DtoPrinter {

	/**	Records können nested records (statisch) enthalten */
	public static record NestedStaticRecord() { }

	/**	Records können nested records (Instanz) enthalten */
	public record NestedRecord() { }

	/** Records können statische Variablen enthalten */
	private static final Integer NUM2;

	/** Records können statische Methoden enthalten */
	private static final String getStrFormatted(String str) {
		return str +  " ";	
	}

	/** Records können statische Initializer enthalten */
	static {
		NUM2 = 3;
	}

	/** Konstruktoren können überschrieben werden */
	public DtoPrinterImpl {
		if ( num == null )
			throw new IllegalArgumentException("num ist NULL");
	}

	/** {@inheritDoc} */
	@Override
	public String printFormatted() {
		return getStrFormatted(str) + (requireNonNull(num, "num ist NULL") + NUM2);
	}

}
