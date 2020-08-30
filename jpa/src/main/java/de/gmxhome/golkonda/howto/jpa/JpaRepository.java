package de.gmxhome.golkonda.howto.jpa;

import de.gmxhome.golkonda.howto.jpa.model.Scanner;

/**
 * Definiert die Schnittstelle zum Repository der Datenbank für die Scanner-Objekte.
 * @author mbeier
 * @param <T> Der Typ der zu speichernde Objekte
 * @param <ID> Der Typ der Primary Keys der zu speichernde Objekte
 * @see Scanner
 */
public interface JpaRepository<T, ID> extends AutoCloseable {

	/**
	 * Speichert einen Datensatz in der DB.
	 * @param entity das zu speichernde Objekt
	 * @return das gespeicherte Objekt
	 */
	public <S extends T> S save(S entity);

}
