package se.dawid.officemanager.test.repository;

import se.dawid.officemanager.test.controller.DatabaseController;
import se.dawid.officemanager.test.utility.QueryBuilder;
import se.dawid.officemanager.test.object.Account;
import se.dawid.officemanager.test.object.Employee;
import se.dawid.officemanager.test.object.Occupation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository implements Repository<Account> {

    private static final String TABLE_NAME = "Account";

    @Override
    public Account findByIdentifier(String username) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .select(
                        "a.accountID",
                        "a.username",
                        "a.password",
                        "a.adminPrivilege",
                        "a.employeeID",
                        "e.employeeID AS employee_employeeID",
                        "e.firstName",
                        "e.lastName",
                        "o.roleID",
                        "o.title AS occupation_title",
                        "o.description AS occupation_description",
                        "o.salary")
                .from("Account a")
                .join(" Left", "Employee e", "a.employeeID = e.employeeID")
                .join(" Left", "Occupation o", "e.roleID = o.roleID")
                .where("a.username = ?");

        try {
            return DatabaseController.getInstance().executeSelectQuery(
                    queryBuilder,
                    resultSet -> {
                        try {
                            if (resultSet.next()) {
                                // Map the result to Account, Employee, and Occupation
                                return mapRowToModel(resultSet);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException("Error mapping result set", e);
                        }
                        return null;
                    },
                    username
            );
        } catch (SQLException  | ClassNotFoundException e) {
            throw new RuntimeException("Error finding account by username", e);
        }
    }

    @Override
    public Account findByID(int id) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .select("accountID", "username", "password", "adminPrivilege", "employeeID")
                .from(TABLE_NAME)
                .where("accountID = ?");

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
            throw new RuntimeException("Error finding account by ID", e);
        }
    }

    @Override
    public List<Account> findAll() {
        QueryBuilder queryBuilder = new QueryBuilder()
                .select(
                        "a.accountID",
                        "a.username",
                        "a.password",
                        "a.adminPrivilege",
                        "a.employeeID",
                        "e.employeeID AS employee_employeeID",
                        "e.firstName",
                        "e.lastName",
                        "o.roleID",
                        "o.title AS occupation_title",
                        "o.description AS occupation_description",
                        "o.salary")
                .from("Account a")
                .join(" Left", "Employee e", "a.employeeID = e.employeeID")
                .join(" Left", "Occupation o", "e.roleID = o.roleID");

        try {
            return DatabaseController.getInstance().executeSelectQuery(
                    queryBuilder,
                    resultSet -> {
                        List<Account> accounts = new ArrayList<>();
                        try {
                            while (resultSet.next()) {
                                accounts.add(mapRowToModel(resultSet));
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException("Error mapping result set", e);
                        }
                        return accounts;
                    }
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error finding all accounts", e);
        }
    }

    @Override
    public boolean create(Account account) {
        if (account.getEmployee() == null || account.getEmployee().getEmployeeID() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null when creating an account");
        }

        QueryBuilder queryBuilder = new QueryBuilder()
                .insertInto(TABLE_NAME, "username", "password", "adminPrivilege", "employeeID")
                .values("?", "?", "?", "?");

        try {
            int rowsAffected = DatabaseController.getInstance().executeUpdateQuery(
                    queryBuilder,
                    account.getUsername(),
                    account.getPassword(),
                    account.hasAdminPrivilege() ? 1 : 0,
                    account.getEmployee().getEmployeeID()
            );
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error creating account", e);
        }
    }


    @Override
    public boolean delete(Account account) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .deleteFrom(TABLE_NAME)
                .where("accountID = ?");

        try {
            int rowsAffected = DatabaseController.getInstance().executeUpdateQuery(
                    queryBuilder,
                    account.getId()
            );
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error deleting account", e);
        }
    }

    @Override
    public boolean update(Account account) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .update(TABLE_NAME)
                .set("username", "?")
                .set("password", "?")
                .set("adminPrivilege", "?")
                .set("employeeID", "?")
                .where("accountID = ?");

        try {
            int rowsAffected = DatabaseController.getInstance().executeUpdateQuery(
                    queryBuilder,
                    account.getUsername(),
                    account.getPassword(),
                    account.hasAdminPrivilege() ? 1 : 0,
                    account.getEmployeeId(),
                    account.getId()
            );
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error updating account", e);
        }
    }


    @Override
    public Account mapRowToModel(ResultSet resultSet) throws SQLException {

        int accountId = resultSet.getInt("accountID");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        boolean adminPrivilege = resultSet.getInt("adminPrivilege") == 1;

        int employeeID = resultSet.getInt("employeeID");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");

        Integer occupationID = resultSet.getObject("roleID") != null ? resultSet.getInt("roleID") : null;
        String title = resultSet.getString("occupation_title");
        String description = resultSet.getString("occupation_description");
        Integer salary = resultSet.getInt("salary");

        Employee employee = new Employee(employeeID, firstName, lastName, new Occupation(occupationID, title, description,salary));
        return new Account(accountId, username, password, adminPrivilege, employee);
    }

}
