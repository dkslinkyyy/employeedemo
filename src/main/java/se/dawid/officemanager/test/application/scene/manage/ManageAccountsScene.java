package se.dawid.officemanager.test.application.scene.manage;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import se.dawid.officemanager.test.application.scene.AbstractScene;
import se.dawid.officemanager.test.application.scene.SceneManager;
import se.dawid.officemanager.test.application.scene.popup.CreateAccountPopup;
import se.dawid.officemanager.test.application.scene.popup.DeleteAccountPopup;
import se.dawid.officemanager.test.application.scene.popup.UpdateAccountPopup;
import se.dawid.officemanager.test.object.Account;

import java.util.List;

public class ManageAccountsScene extends AbstractScene {


    public ManageAccountsScene(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public Scene buildScene() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 20;");

        HBox navbar = createNavbar();
        VBox chipsContainer = new VBox(10);
        chipsContainer.setStyle("-fx-padding: 10; -fx-border-color: lightgray;");

        List<Account> accounts = getServiceController().getAccountService().getAllAccounts();
        accounts.forEach(account -> createChip(account, chipsContainer));

        Button createUserButton = new Button("Skapa användare");
        createUserButton.setOnAction(e -> {
            CreateAccountPopup createUserScene = new CreateAccountPopup(getServiceController());
            createUserScene.display();
        });

        Button updateUserButton = new Button("Uppdatera användare");
        updateUserButton.setOnAction(e -> {
            UpdateAccountPopup updateAccountScene = new UpdateAccountPopup(getServiceController());
            updateAccountScene.display();
        });

        Button deleteUserButton = new Button("Radera användare");
        deleteUserButton.setOnAction(e -> {
            DeleteAccountPopup deleteAccountScene = new DeleteAccountPopup(getServiceController());
            deleteAccountScene.display();
        });

        HBox footer = new HBox(10);
        footer.setAlignment(Pos.BOTTOM_RIGHT);
        footer.getChildren().addAll(updateUserButton, deleteUserButton, createUserButton);

        layout.getChildren().addAll(navbar, chipsContainer, footer);

        return new Scene(layout, 600, 400);
    }

    @Override
    public String getName() {
        return "ManageAccountsScene";
    }

    private void createChip(Account account, VBox parentLayout) {
        HBox chip = new HBox(10);
        chip.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");

        Label accountUsername = new Label(account.getUsername());
        Label fullName = new Label(account.getEmployee().getFirstName() + " " + account.getEmployee().getLastName());
        Label occupationTitle = new Label(account.getEmployee().getOccupation().getTitle());

        HBox.setHgrow(accountUsername, Priority.ALWAYS);
        HBox.setHgrow(fullName, Priority.ALWAYS);
        HBox.setHgrow(occupationTitle, Priority.ALWAYS);

        chip.setAlignment(Pos.CENTER_LEFT);
        accountUsername.setMaxWidth(Double.MAX_VALUE);
        fullName.setMaxWidth(Double.MAX_VALUE);
        occupationTitle.setMaxWidth(Double.MAX_VALUE);

        chip.getChildren().addAll(accountUsername, fullName, occupationTitle);
        parentLayout.getChildren().add(chip);
    }
}
