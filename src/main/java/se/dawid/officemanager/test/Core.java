package se.dawid.officemanager.test;

import javafx.application.Application;
import se.dawid.officemanager.test.application.App;
import se.dawid.officemanager.test.controller.DatabaseController;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class Core {

    public static void main(String[] args) {
        try {
            Properties properties = loadProperties();

            DatabaseController.getInstance(properties).establishConnection();

            Application.launch(App.class, args);

        } catch (IOException e) {
            throw new RuntimeException("Failed to start the application", e);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Core.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("application.properties file not found");
            }
            properties.load(input);
        }
        return properties;
    }

}
