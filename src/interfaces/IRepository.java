package interfaces;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {

    void add(T item);
    Optional<T> findById(int id);
    Optional<T> findByName(String name);
    List<T> getAll();
    void getAllCustom();
    void showAllWithIndex();
    void update(T entity);
    boolean removeById(int id);

}