package de.gmxhome.golkonda.howto.jpa;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

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
	 * Liefert den initialisierten EntityManager zum Beispiel für eine {@linkplain CriteriaQuery}.
	 * @return den EntityManager
	 */
	public EntityManager getEntityManager();

	/**
	 * Speichert einen Datensatz in der DB.
	 * @param entity das zu speichernde Objekt
	 * @return das gespeicherte Objekt
	 */
	public <S extends T> S save(S entity);

	/**
	 * Aktualisiert oder peichert einen Datensatz in der DB.
	 * @param entity das zu speichernde Objekt
	 * @return das gespeicherte Objekt
	 */
	public <S extends T> S merge(S entity);

	/**
	 * Lädt einen oder mehrere Datensätze aus der Datenbank.
	 * @param <S> Typ des zu ladenden Objekts, zum Beispiel {@linkplain Scanner}
	 * @param q die Datenbankabfrage
	 * @return ein Optional mit der Trefferliste
	 */
	public <S extends T> Optional<List<S>> getResultList(CriteriaQuery<S> q);

}
