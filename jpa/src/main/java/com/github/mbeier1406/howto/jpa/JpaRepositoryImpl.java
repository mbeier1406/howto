package com.github.mbeier1406.howto.jpa;

import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.howto.jpa.model.Scanner;

/**
 * Standard JPA-Implementierung für das Scanner {@linkplain JpaRepository}.
 * Definition <i>transaction-type</i> der Persistence Unit muss {@code RESOURCE_LOCAL} sein.
 * @author mbeier
 * @param <T> Der Typ der zu speichernde Objekte
 * @param <ID> Der Typ der Primary Keys der zu speichernde Objekte
 * @see Scanner
 */
public class JpaRepositoryImpl<T, ID> implements JpaRepository<T, ID> {

	public static final Logger LOGGER = LogManager.getLogger(JpaRepositoryImpl.class);

    private static final String PERSISTENCE_UNIT_NAME = "scannerdb";
    private static EntityManagerFactory factory;
	private final Class<T> idTypeClass;
	protected EntityManager em;

	public JpaRepositoryImpl(Class<T> idTypeClass) {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
		this.idTypeClass = idTypeClass;
	}

	/**
	 * {@inheritDoc}
	 * <p>Schließt den {@linkplain EntityManager} {@linkplain #em}</p>
	 */
	@Override
	public void close() throws Exception {
		try {
			em.clear();
			em.close();
		}
		catch ( Exception e ) {
			LOGGER.error("", e);
			throw e;
		}
	}

	/** {@inheritDoc} */
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	/** {@inheritDoc} */
	@Override
	public <S extends T> S save(S entity) {
		try ( final CloseableThreadContext.Instance ctc = put("entity", entity.toString()) ) {
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			return entity;
		}
		catch ( Throwable t ) {
			LOGGER.error("entity={}", entity, t);
			throw t;
		}
	}

	/** {@inheritDoc} */
	@Override
	public <S extends T> S merge(S entity) {
		try ( final CloseableThreadContext.Instance ctc = put("entity", entity.toString()) ) {
			em.getTransaction().begin();
			em.merge(entity);
			em.getTransaction().commit();
			return entity;
		}
		catch ( Throwable t ) {
			LOGGER.error("entity={}", entity, t);
			throw t;
		}
	}

	/** {@inheritDoc} */
	@Override
	public <S extends T> void delete(S s) {
		try ( final CloseableThreadContext.Instance ctc = put("s", s.toString()) ) {
			em.getTransaction().begin();
			em.remove(s);
			em.getTransaction().commit();
		}
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public <S extends T> Optional<S> findById(ID id) {
		try ( final CloseableThreadContext.Instance ctc = put("id", id.toString()) ) {
			return Optional.ofNullable((S) em.find(idTypeClass, id));
		}
	}

	/** {@inheritDoc} */
	@Override
	public <S extends T> Optional<List<S>> getResultList(CriteriaQuery<S> q) {
		try {
			return Optional.ofNullable(em.createQuery(q).getResultList());
		} catch (RuntimeException e) {
			LOGGER.debug("", e);
			return Optional.empty();
		}
	}

}
