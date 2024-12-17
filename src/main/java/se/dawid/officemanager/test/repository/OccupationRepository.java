package se.dawid.officemanager.test.repository;

import se.dawid.officemanager.test.controller.DatabaseController;
import se.dawid.officemanager.test.utility.QueryBuilder;
import se.dawid.officemanager.test.object.Occupation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OccupationRepository implements Repository<Occupation> {

    private static final String TABLE_NAME = "Occupation";

    @Override
    public Occupation findByIdentifier(String identifier) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .select("roleID", "title", "description", "salary")
                .from(TABLE_NAME)
                .where("title = ? OR roleID = ?");

        try {
            return DatabaseController.getInstance().executeSelectQuery(
                    queryBuilder,
                    resultSet -> {
                        try {
                            if (resultSet.next()) {
                                return mapRowToModel(resultSet);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    },
                    identifier, identifier
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error finding occupation by identifier", e);
        }
    }

    @Override
    public Occupation findByID(int id) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .select("roleID", "title", "description", "salary")
                .from(TABLE_NAME)
                .where("roleID = ?");

        try {
            return DatabaseController.getInstance().executeSelectQuery(
                    queryBuilder,
                    resultSet -> {
                        try {
                            if (resultSet.next()) {
                                return mapRowToModel(resultSet);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    },
                    id
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error finding occupation by ID", e);
        }
    }

    @Override
    public List<Occupation> findAll() {
        QueryBuilder queryBuilder = new QueryBuilder()
                .select("roleID", "title", "description", "salary")
                .from(TABLE_NAME);

        try {
            return DatabaseController.getInstance().executeSelectQuery(
                    queryBuilder,
                    resultSet -> {
                        List<Occupation> occupations = new ArrayList<>();
                        while (true) {
                            try {
                                if (!resultSet.next()) break;
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                occupations.add(mapRowToModel(resultSet));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return occupations;
                    }
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error finding all occupations", e);
        }
    }

    @Override
    public boolean create(Occupation occupation) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .insertInto(TABLE_NAME, "title", "description", "salary")
                .values("?", "?", "?");

        try {
            int rowsAffected = DatabaseController.getInstance().executeUpdateQuery(
                    queryBuilder,
                    occupation.getTitle(),
                    occupation.getDescription(),
                    occupation.getSalary()
            );
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error creating new occupation", e);
        }
    }

    @Override
    public boolean delete(Occupation occupation) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .deleteFrom(TABLE_NAME)
                .where("roleID = ?");

        try {
            int rowsAffected = DatabaseController.getInstance().executeUpdateQuery(
                    queryBuilder,
                    occupation.getId()
            );
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error deleting occupation", e);
        }
    }

    @Override
    public boolean update(Occupation occupation) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .update(TABLE_NAME)
                .set("title", "?")
                .set("description"," ?")
                .set( "salary", "?")
                .where("roleID = ?");

        try {
            int rowsAffected = DatabaseController.getInstance().executeUpdateQuery(
                    queryBuilder,
                    occupation.getTitle(),
                    occupation.getDescription(),
                    occupation.getSalary(),
                    occupation.getId()
            );
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error updating occupation", e);
        }
    }

    @Override
    public Occupation mapRowToModel(ResultSet resultSet) throws SQLException {
        Integer occupationID = resultSet.getObject("roleID") != null ? resultSet.getInt("roleID") : null;
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        Integer salary = resultSet.getInt("salary");

        return new Occupation(occupationID, title, description, salary);
    }
}
