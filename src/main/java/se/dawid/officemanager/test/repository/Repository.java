package se.dawid.officemanager.test.repository;

import java.sql.SQLException;
import java.util.List;

public interface Repository<E>{

    E findByIdentifier(String identifier);
    E findByID(int id);
    List<E> findAll();
    boolean create(E account);
    boolean delete(E account);
    boolean update(E account);

    E mapRowToModel(java.sql.ResultSet resultSet) throws SQLException;
}
