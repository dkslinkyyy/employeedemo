module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    exports se.dawid.officemanager.test.application; // Allow access to the `App` class
}
