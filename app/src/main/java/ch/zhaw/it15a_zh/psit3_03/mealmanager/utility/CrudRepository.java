package ch.zhaw.it15a_zh.psit3_03.mealmanager.utility;

import java.util.ArrayList;

/**
 * Interface for the five different CRUD related database operations.
 *
 * @param <T> The class, which implements this interface.
 */
public interface CrudRepository<T> {
    T insert(T entity);

    ArrayList<T> findAll();

    T findOneByID(int tID);

    int update(T newEntity);

    int delete(int tID);
}
