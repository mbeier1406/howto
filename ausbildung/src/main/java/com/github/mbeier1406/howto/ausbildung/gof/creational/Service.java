package com.github.mbeier1406.howto.ausbildung.gof.creational;

/**
 * GoF - Creational Pattern: Factory<p/>
 * Erstellt <u>neue Objekte</u> (im gegensatz zu {@linkplain PrototypeRegistry} ohne
 * Kenntnis der konkreten Klasse. Bestehnder Code muss nicht geändert werden, wenn
 * neue Implementierungen des Servcies bereitgestelt werden. Dient der Entkopplung
 * der Obkekterzeugung und des nutzenden Codes.
 */
public interface Service {

	/** Zugriff auf die Verarbeitungslogik der Implementierungen. */
	public void method();

}
