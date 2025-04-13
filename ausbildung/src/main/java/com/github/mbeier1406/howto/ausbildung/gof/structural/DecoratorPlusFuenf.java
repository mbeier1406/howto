package com.github.mbeier1406.howto.ausbildung.gof.structural;

/**
 * Dieser Decorator addiert zum Ergebnis von
 * {@linkplain DecoratorComponent#getZahl()} fünf.
 */
public class DecoratorPlusFuenf extends Decorator implements DecoratorComponent {

	public DecoratorPlusFuenf(DecoratorComponent c) {
		super(c);
	}

	/** {@inheritDoc} */
	@Override
	public int getZahl() {
		return super.getZahl() + 5;
	}

}
