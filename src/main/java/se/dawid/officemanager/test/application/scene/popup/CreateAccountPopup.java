    package se.dawid.officemanager.test.application.scene.popup;

    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.stage.Modality;
    import javafx.stage.Stage;
    import se.dawid.officemanager.test.controller.ServiceController;

    public class CreateAccountPopup {

        private final ServiceController serviceController;

        public CreateAccountPopup(ServiceController serviceController) {
            this.serviceController = serviceController;
        }

        public void display() {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Skapa ny användare");

            VBox layout = new VBox(10);
            layout.setStyle("-fx-padding: 20;");

            TextField usernameField = new TextField();
            usernameField.setPromptText("Användarnamn");

            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Lösenord");

            TextField firstNameField = new TextField();
            firstNameField.setPromptText("Namn");

            TextField lastNameField = new TextField();
            lastNameField.setPromptText("Efternamn");

            ComboBox<String> roleComboBox = new ComboBox<>();
            serviceController.getOccupationService().getAllOccupations().forEach(occupation -> {
                roleComboBox.getItems().add(occupation.getTitle());
            });


            HBox adminPrivilegeColumn = new HBox(10);
            Label adminPrivilegeLabel = new Label("Ge admin rättigheter?");

            CheckBox adminCheckBox = new CheckBox();

            adminPrivilegeColumn.getChildren().addAll(adminCheckBox, adminPrivilegeLabel);


            Button createButton = new Button("Create");
            createButton.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String role = roleComboBox.getValue();

                boolean adminPrivilege = adminCheckBox.isSelected();

                if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || role.isEmpty()) {
                    System.out.println("Alla fält är nödvändiga!");
                    return;
                }
                serviceController.createAccountIdentity(username, password, firstName, lastName, adminPrivilege, role);
                popupStage.close();
            });

            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(e -> popupStage.close());

            layout.getChildren().addAll(
                    new Label("Skapa användare:"),
                    usernameField,
                    passwordField,
                    firstNameField,
                    lastNameField,
                    roleComboBox,
                    adminPrivilegeColumn,
                    createButton,
                    cancelButton
            );

            Scene scene = new Scene(layout, 300, 400);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        }
    }
