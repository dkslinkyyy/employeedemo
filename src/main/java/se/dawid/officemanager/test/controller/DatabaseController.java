package se.dawid.officemanager.test.controller;

import se.dawid.officemanager.test.utility.QueryBuilder;

import java.sql.*;
import java.util.Properties;
import java.util.function.Function;

public class DatabaseController {

    private static DatabaseController instance;
    private final Properties dbProperties;
    private Connection connection;

    private DatabaseController(Properties dbProperties) {
        this.dbProperties = dbProperties;
    }

    public static synchronized DatabaseController getInstance(Properties dbProperties) {
        if (instance == null) {
            instance = new DatabaseController(dbProperties);
        }
        return instance;
    }

    public static synchronized DatabaseController getInstance() {
        if (instance == null || instance.connection == null) {
            throw new IllegalStateException("DatabaseService not initialized correctly.");
        }
        return instance;
    }


    public Connection getConnection() {
        return connection;
    }

    public void establishConnection() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            String url = dbProperties.getProperty("db.url");
            String username = dbProperties.getProperty("db.username");
            String password = dbProperties.getProperty("db.password");
            String driver = dbProperties.getProperty("db.driver-class-name");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established.");
        }
    }

    private void validateConnection() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            establishConnection();
        }
    }

    public <T> T executeSelectQuery(QueryBuilder queryBuilder, Function<ResultSet, T> resultHandler, Object... params) throws SQLException, ClassNotFoundException {
        validateConnection();
        String sql = queryBuilder.build();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, params);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultHandler.apply(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing select query", e);
        }
    }

    public int executeUpdateQuery(QueryBuilder queryBuilder, Object... params) throws SQLException, ClassNotFoundException {
        validateConnection();
        String sql = queryBuilder.build();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, params);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing update query", e);
        }
    }

    private void setParameters(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    public void close() {
        closeResource(connection);
        connection = null;
    }

    private static void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                System.err.println("Error closing resource: " + e.getMessage());
            }
        }
    }
}
