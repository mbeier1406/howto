package com.github.mbeier1406.howto.ausbildung.basic.impl;

import java.io.InputStream;
import java.util.Scanner;

import com.github.mbeier1406.howto.ausbildung.basic.IOFunctions;

/**
 * Standardimplmentierung für {@linkplain IOFunctions}.
 * @author mbeier
 */
public class IOFunctionsImpl implements IOFunctions {

	/** {@inheritDoc} */
	@Override
	public ExampleData readExampleData(InputStream in, String charsetName) {
		try ( Scanner scanner = new Scanner(in, charsetName); ) {
			System.out.print("str=");
			String str = scanner.nextLine();
			System.out.print("i=");
			int i = scanner.nextInt();
			System.out.print("f=");
			float f = scanner.nextFloat();
			return new ExampleData(str, i, f);
		}
	}

}
