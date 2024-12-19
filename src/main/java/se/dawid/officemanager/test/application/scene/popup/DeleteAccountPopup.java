package se.dawid.officemanager.test.application.scene.popup;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.dawid.officemanager.test.controller.ServiceController;
import se.dawid.officemanager.test.object.Account;

public class DeleteAccountPopup {

    private final ServiceController serviceController;

    public DeleteAccountPopup(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    public void display() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Ta bort konto");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");

        ComboBox<String> accountComboBox = new ComboBox<>();
        accountComboBox.setPromptText("Välj konto att ta bort");
        serviceController.getAccountService().getAllAccounts().forEach(account ->
                accountComboBox.getItems().add(account.getUsername())
        );

        TextField usernameField = new TextField();
        usernameField.setPromptText("Eller ange användarnamn");

        Button deleteButton = new Button("Ta bort");
        Button cancelButton = new Button("Avbryt");

        deleteButton.setOnAction(e -> {
            String selectedUsername = accountComboBox.getValue();
            String manualUsername = usernameField.getText();
            String usernameToDelete = selectedUsername != null ? selectedUsername : manualUsername;

            if (usernameToDelete == null || usernameToDelete.isEmpty()) {
                showAlert("Fel", "Du måste välja eller ange ett användarnamn.");
                return;
            }

            Account accountToDelete = serviceController.getAccountService().getAccountByUsername(usernameToDelete);
            if (accountToDelete == null) {
                showAlert("Fel", "Kontot med användarnamn '" + usernameToDelete + "' hittades inte.");
            } else {
                boolean success = serviceController.getAccountService().delete(accountToDelete);
                if (success) {
                    showAlert("Lyckades", "Kontot har tagits bort.");
                    popupStage.close();
                } else {
                    showAlert("Fel", "Det gick inte att ta bort kontot.");
                }
            }
        });

        cancelButton.setOnAction(e -> popupStage.close());

        HBox buttons = new HBox(10, deleteButton, cancelButton);
        layout.getChildren().addAll(
                new Label("Ta bort konto:"),
                accountComboBox,
                new Label("Eller skriv in användarnamn:"),
                usernameField,
                buttons
        );

        Scene scene = new Scene(layout, 300, 250);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
