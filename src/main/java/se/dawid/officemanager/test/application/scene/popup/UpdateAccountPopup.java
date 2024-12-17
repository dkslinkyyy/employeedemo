package se.dawid.officemanager.test.application.scene.popup;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.dawid.officemanager.test.controller.ServiceController;
import se.dawid.officemanager.test.object.Account;
import se.dawid.officemanager.test.object.Employee;
import se.dawid.officemanager.test.object.Occupation;

public class UpdateAccountPopup {

    private final ServiceController serviceController;
    private final Stage stage;

    public UpdateAccountPopup(ServiceController serviceController) {
        this.serviceController = serviceController;
        this.stage = new Stage();
    }

    public void display() {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Uppdatera konto");
        showAccountSelectionScene();
        stage.showAndWait();
    }

    private void showAccountSelectionScene() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");

        ComboBox<String> accountComboBox = new ComboBox<>();
        accountComboBox.setPromptText("Välj konto att uppdatera");
        serviceController.getAccountService().getAllAccounts().forEach(account ->
                accountComboBox.getItems().add(account.getUsername())
        );

        Button updateButton = new Button("Uppdatera");
        Button cancelButton = new Button("Avbryt");

        updateButton.setOnAction(e -> {
            String selectedUsername = accountComboBox.getValue();
            if (selectedUsername == null || selectedUsername.isEmpty()) {
                showAlert("Fel", "Du måste välja ett konto.");
                return;
            }

            Account selectedAccount = serviceController.getAccountService().getAccountByUsername(selectedUsername);
            if (selectedAccount == null) {
                showAlert("Fel", "Kontot med användarnamn '" + selectedUsername + "' hittades inte.");
            } else {
                showEditAccountScene(selectedAccount);
            }
        });

        cancelButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(
                new Label("Välj ett konto att uppdatera:"),
                accountComboBox,
                new VBox(10, updateButton, cancelButton)
        );

        Scene selectionScene = new Scene(layout, 300, 200);
        stage.setScene(selectionScene);
    }

    private void showEditAccountScene(Account account) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");

        TextField usernameField = new TextField(account.getUsername());
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Ny lösenord (lämna tomt)");
        TextField firstNameField = new TextField(account.getEmployee().getFirstName());
        TextField lastNameField = new TextField(account.getEmployee().getLastName());
        CheckBox adminCheckBox = new CheckBox("Admin");
        adminCheckBox.setSelected(account.hasAdminPrivilege());

        Label roleLabel = new Label("Arbetsroll");
        ComboBox<String> roleComboBox = new ComboBox<>();

        serviceController.getOccupationService().getAllOccupations().forEach(occupation -> {
            roleComboBox.getItems().add(occupation.getTitle());
        });

        Button saveButton = new Button("Spara");
        Button cancelButton = new Button("Tillbaka");

        saveButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            boolean isAdmin = adminCheckBox.isSelected();
            String role = roleComboBox.getValue();

            if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                showAlert("Fel", "Användarnamn, Förnamn och Efternamn är obligatoriska fält.");
                return;
            }

            account.setUsername(username);
            if (!password.isEmpty()) {
                account.setPassword(password);
            }
            account.getEmployee().setFirstName(firstName);
            account.getEmployee().setLastName(lastName);
            account.setAdminPrivilege(isAdmin);

            Occupation occupation = serviceController.getOccupationService().getOccupationByTitle(role);
            if(occupation != null) {
                Employee employee = account.getEmployee();
                employee.setOccupation(occupation);
                serviceController.getEmployeeService().save(employee);
            }

            boolean success = serviceController.getAccountService().save(account);
            if (success) {
                showAlert("Lyckades", "Kontot har uppdaterats.");
                showAccountSelectionScene();
            } else {
                showAlert("Fel", "Det gick inte att uppdatera kontot.");
            }
        });

        cancelButton.setOnAction(e -> showAccountSelectionScene());

        layout.getChildren().addAll(
                new Label("Redigera konto:"),
                new Label("Användarnamn:"),
                usernameField,
                new Label("Lösenord (lämna tomt):"),
                passwordField,
                new Label("Förnamn:"),
                firstNameField,
                new Label("Efternamn:"),
                lastNameField,
                adminCheckBox,
                roleLabel,
                roleComboBox,
                new VBox(10, saveButton, cancelButton)
        );

        Scene editScene = new Scene(layout, 300, 400);
        stage.setScene(editScene);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
