package se.dawid.officemanager.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.dawid.officemanager.test.controller.DatabaseController;
import se.dawid.officemanager.test.controller.ServiceController;
import se.dawid.officemanager.test.object.Account;
import se.dawid.officemanager.test.object.Employee;
import se.dawid.officemanager.test.object.Occupation;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceControllerTest {

    private DatabaseController dbController;
    private ServiceController serviceController;

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Core.class
                .getClassLoader()
                .getResourceAsStream("application2.properties")) {
            if (input == null) {
                throw new IOException("application2.properties file not found");
            }
            properties.load(input);
        }
        return properties;
    }

    @BeforeEach
    public void setUp() throws Exception {
        dbController = DatabaseController.getInstance(loadProperties());
        System.out.println("Connecting to db " + loadProperties().getProperty("db.url"));
        serviceController = new ServiceController();
        dbController.establishConnection();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection conn = dbController.getConnection()) {
            String dropAccountTable = "DROP TABLE IF EXISTS Account";
            String dropEmployeeTable = "DROP TABLE IF EXISTS Employee";
            String dropOccupationTable = "DROP TABLE IF EXISTS Occupation";
            try (PreparedStatement stmt1 = conn.prepareStatement(dropAccountTable);
                 PreparedStatement stmt2 = conn.prepareStatement(dropEmployeeTable);
                 PreparedStatement stmt3 = conn.prepareStatement(dropOccupationTable)) {
                stmt1.execute();
                stmt2.execute();
                stmt3.execute();
            }
        }
        dbController.close();
    }

    @Test
    public void testInsertAccountIdentity() throws SQLException, ClassNotFoundException {
        Occupation occupation = new Occupation("Tekniker", "tekniker beskrivning", 100);
        Employee employee = new Employee("Bob", "bobsson", occupation);

        boolean employeeCreated = serviceController.getEmployeeService().create(employee);
        if (!employeeCreated) {
            throw new RuntimeException("Failed to create employee.");
        }

        employee = serviceController.getEmployeeService().findByName("Bob");
        Account account = new Account("bob1", "abc123", false, employee);
        boolean accountCreated = serviceController.getAccountService().create(account);

        assertTrue(accountCreated);
        assertNotNull(employee);
        assertNotNull(account.getEmployee());
        assertNotNull(account.getEmployee().getOccupation());
        assertEquals("bob1", account.getUsername());
    }


    @Test
    public void testFindAccountByUsername() throws SQLException, ClassNotFoundException {
        Occupation occupation = new Occupation("Designer", "designer beskrivningg", 130);
        Employee employee = new Employee("Eva", "eveson", occupation);
        serviceController.getEmployeeService().create(employee);

        employee = serviceController.getEmployeeService().findByName("Eva");
        Account account = new Account("eva1", "abc123", false, employee);
        serviceController.getAccountService().create(account);

        Account foundAccount = serviceController.getAccountService().getAccountByUsername("eva1");

        assertNotNull(foundAccount);
        assertEquals("eva1", foundAccount.getUsername());
    }
}
