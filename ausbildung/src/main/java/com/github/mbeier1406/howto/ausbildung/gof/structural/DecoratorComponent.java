package com.github.mbeier1406.howto.ausbildung.gof.structural;

/**
 * GoF - Structural Pattern: Deorator<p/>
 * Das Interface definiert den Service, anhnad desse das Decorator Pattern
 * demonstriert werden soll. Dieses bietet die Möglichkeit, einem Objekt dynamisch
 * neue Funktionen hinzuzfügen ohne dessen Struktur zu verändern oder (durch deren
 * flexible Kombination) eine Vielzahl von abgeleiteten Klassen erzeugen zu müssen.
 * @see /howto-ausbildung/src/main/resources/com/github/mbeier1406/howto/ausbildung/gof/structural/Decorator.png 
 */
public interface DecoratorComponent {

	/** Diese Methode soll die Eigenschaft des zu dekorierendne Objektes enthalten */
	public int getZahl();

}
