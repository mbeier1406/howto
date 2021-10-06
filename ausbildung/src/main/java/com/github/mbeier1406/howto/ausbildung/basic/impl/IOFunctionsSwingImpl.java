package com.github.mbeier1406.howto.ausbildung.basic.impl;

import static javax.swing.JOptionPane.showInputDialog;

import java.io.InputStream;

/**
 * Beispielimplementierung mit Verwendung von Swing-Komponenten.
 * @author mbeier
 */
public class IOFunctionsSwingImpl extends IOFunctionsImpl {

	/** {@inheritDoc} */
	@Override
	public ExampleData readExampleData(InputStream in, String charsetName) {
		String str = showInputDialog("String");
		int i = Integer.parseInt(showInputDialog("Int"));
		float f = Float.parseFloat(showInputDialog("float"));
		return new ExampleData(str, i, f);
	}

}
