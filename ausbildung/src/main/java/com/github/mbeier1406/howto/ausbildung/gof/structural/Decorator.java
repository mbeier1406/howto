package com.github.mbeier1406.howto.ausbildung.gof.structural;

/**
 * Diese Klasse bildet die Basis für alle Decorator-Klassen, die
 * dem zu dekorierenden Objekt {@linkplain DecoratorComponentImpl}
 * neue Funktionen hinzufügen. Die Klasse kann abstrakt sein, da nicht sie, sondern
 * nur die konkreten Decoratoren instanziiert werden.
 */
public abstract class Decorator implements DecoratorComponent {

	/** Das eigentliche objekt, das dekoriert werden soll und an das die Aufrufe delegiert werden */
	protected final DecoratorComponent DELEGATE;

	public Decorator(DecoratorComponent c) {
		this.DELEGATE = c;
	}

	/** {@inheritDoc} */
	@Override
	public int getZahl() {
		return this.DELEGATE.getZahl();
	}

}
