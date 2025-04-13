package com.github.mbeier1406.howto.ausbildung.gof.structural;

/**
 * Dieser Decorator multipilziert das Ergebnis von
 * {@linkplain DecoratorComponent#getZahl()} mit zwei.
 */
public class DecoratorMalZwei extends Decorator implements DecoratorComponent {

	public DecoratorMalZwei(DecoratorComponent c) {
		super(c);
	}

	/** {@inheritDoc} */
	@Override
	public int getZahl() {
		return super.getZahl() * 2;
	}

}
