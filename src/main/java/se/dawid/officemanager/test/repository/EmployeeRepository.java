package se.dawid.officemanager.test.repository;

import se.dawid.officemanager.test.controller.DatabaseController;
import se.dawid.officemanager.test.utility.QueryBuilder;
import se.dawid.officemanager.test.object.Employee;
import se.dawid.officemanager.test.object.Occupation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository implements Repository<Employee> {

    private static final String TABLE_NAME = "Employee";

    @Override
    public Employee findByIdentifier(String identifier) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .select(
                        "e.employeeID",
                        "e.firstName",
                        "e.lastName",
                        "e.roleID",
                        "o.title AS occupation_title",
                        "o.description AS occupation_description",
                        "o.salary")
                .from("Employee e")
                .join(" Left", "Occupation o", "e.roleID = o.roleID")
                .where("e.firstName = ?");

        try {
            return DatabaseController.getInstance().executeSelectQuery(
                    queryBuilder,
                    resultSet -> {
                        try {
                            if (resultSet.next()) {
                                return mapRowToModel(resultSet);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException("Error mapping result set", e);
                        }
                        return null;
                    },
                    identifier
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error finding employee by identifier", e);
        }
    }

    @Override
    public Employee findByID(int id) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .select("employeeID", "firstName", "lastName", "roleID")
                .from(TABLE_NAME)
                .where("employeeID = ?");

        try {
            return DatabaseController.getInstance().executeSelectQuery(
                    queryBuilder,
                    resultSet -> {
                        try {
                            if (resultSet.next()) {
                                return mapRowToModel(resultSet);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException("Error mapping result set", e);
                        }
                        return null;
                    },
                    id
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error finding employee by ID", e);
        }
    }

    @Override
    public List<Employee> findAll() {
        QueryBuilder queryBuilder = new QueryBuilder()
                .select(
                        "e.employeeID",
                        "e.firstName",
                        "e.lastName",
                        "e.roleID",
                        "o.title AS occupation_title",
                        "o.description AS occupation_description",
                        "o.salary")
                .from("Employee e")
                .join(" Left", "Occupation o", "e.roleID = o.roleID");

        try {
            return DatabaseController.getInstance().executeSelectQuery(
                    queryBuilder,
                    resultSet -> {
                        List<Employee> employees = new ArrayList<>();
                        try {
                            while (resultSet.next()) {
                                employees.add(mapRowToModel(resultSet));
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException("Error mapping result set", e);
                        }
                        return employees;
                    }
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error finding all employees", e);
        }
    }

    @Override
    public boolean create(Employee employee) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .insertInto(TABLE_NAME, "firstName", "lastName", "roleID")
                .values("?", "?", "?");

        try {
            int rowsAffected = DatabaseController.getInstance().executeUpdateQuery(
                    queryBuilder,
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getOccupation().getId()
            );
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error creating employee", e);
        }
    }

    @Override
    public boolean delete(Employee employee) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .deleteFrom(TABLE_NAME)
                .where("employeeID = ?");

        try {
            int rowsAffected = DatabaseController.getInstance().executeUpdateQuery(
                    queryBuilder,
                    employee.getEmployeeID()
            );
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error deleting employee", e);
        }
    }

    @Override
    public boolean update(Employee employee) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .update(TABLE_NAME)
                .set("firstName", "?")
                .set("lastName", "?")
                .set("roleID", "?")
                .where("employeeID = ?");

        try {
            int rowsAffected = DatabaseController.getInstance().executeUpdateQuery(
                    queryBuilder,
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getOccupation().getId(),
                    employee.getEmployeeID()
            );
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error updating employee", e);
        }
    }

    @Override
    public Employee mapRowToModel(ResultSet resultSet) throws SQLException {
        int employeeID = resultSet.getInt("employeeID");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");

        // Map Occupation details
        Integer occupationID = resultSet.getObject("roleID") != null ? resultSet.getInt("roleID") : null;
        String title = resultSet.getString("occupation_title");
        String description = resultSet.getString("occupation_description");
        Integer salary = resultSet.getInt("salary");

        // Create and return Employee object
        Occupation occupation = new Occupation(occupationID, title, description, salary);
        return new Employee(employeeID, firstName, lastName, occupation);
    }

}
