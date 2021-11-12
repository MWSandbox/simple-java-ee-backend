package com.mdevoc.persistence;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class BaseDAO<T extends BaseEntity> {

    private static final String FIND_ALL_QUERY_TEMPLATE = "SELECT model FROM %s model";

    private Class<T> modelClass;

    @Inject
    private EntityManager em;

    public BaseDAO() {
        modelClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public List<T> findAll() {
        try {
            String findAllQuery = String.format(FIND_ALL_QUERY_TEMPLATE, modelClass.getSimpleName());
            TypedQuery<T> query = em.createQuery(findAllQuery, modelClass);
            return query.getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        }
    }

    public Optional<T> findById(long id) {
        return Optional.ofNullable(em.find(modelClass, id));
    }

    public void insert(T model) {
        model.setId(null);
        em.persist(model);
    }

    public void update(T model) throws IllegalArgumentException {
        em.merge(model);
    }

    public void deleteById(long id) throws IllegalArgumentException, NoSuchElementException {
        Optional<T> modelToDelete = findById(id);
        if (!modelToDelete.isPresent()) {
            throw new NoSuchElementException("Could not find entity to delete");
        }
        em.remove(modelToDelete.get());
    }

    public void deleteAll() throws IllegalArgumentException, NoSuchElementException {
        Collection<T> modelsToDelete = findAll();
        modelsToDelete.stream()
                .map(T::getId)
                .forEach(this::deleteById);
    }

    public EntityManager getEm() {
        return em;
    }
}
