package com.github.mbeier1406.howto.ausbildung.basic.impl;

import com.github.mbeier1406.howto.ausbildung.basic.Datatypes;

/**
 * Standardimplmentierung für {@linkplain Datatypes}.
 * @author mbeier
 */
public class DatatypesImpl implements Datatypes {

	/** {@inheritDoc} */
	@Override
	public short getMaxShort() {
		return Short.MAX_VALUE;
	}

	/** {@inheritDoc} */
	@Override
	public short getMinShort() {
		return Short.MIN_VALUE;
	}

	/** {@inheritDoc} */
	@Override
	public short intToShort(int i) {
		return (short) i;
	}

	/** {@inheritDoc} */
	@Override
	public char intToChar(int ch) {
		return (char) ch;
	}

}
