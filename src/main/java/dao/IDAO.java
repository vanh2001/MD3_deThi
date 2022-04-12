package dao;

import java.sql.SQLException;
import java.util.List;

public interface IDAO<T> {
    List<T> findAll();
    T findById (int id);
    void create(T t);
    boolean update(T t);
    boolean delete(int id);
}
