package se.dawid.officemanager.test.application.scene;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import se.dawid.officemanager.test.application.scene.manage.ManageAccountsScene;
import se.dawid.officemanager.test.application.scene.manage.ManageOccupationsScene;
import se.dawid.officemanager.test.controller.ServiceController;
import se.dawid.officemanager.test.object.Account;

public abstract class AbstractScene {

    private ServiceController serviceController;
    private SceneManager sceneManager;
    private Account account;

    public AbstractScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        sceneManager.addScene(this);
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public void setServiceController(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    public ServiceController getServiceController() {
        return serviceController;
    }


    public void setAccount(Account account) {
        this.account = account;
    }

    protected Account getAccount() {
        return account;
    }

    public abstract Scene buildScene();

    public abstract String getName();

    protected HBox createNavbar() {
        if (account == null) {
            throw new IllegalStateException("Account not set");
        }
        HBox navbar = new HBox(10);
        Button accountButton = new Button("Ditt konto");
        accountButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        accountButton.setOnAction(e -> sceneManager.switchScene("MainScene"));

        Button logoutButton = new Button("Logga ut");
        logoutButton.setOnAction(e -> {
            sceneManager.switchScene("LoginScene");
            setAccount(null);
        });

        Button manageUsersButton = new Button("Hantera användare");
        manageUsersButton.setOnAction(e -> {
            ManageAccountsScene manageAccountsScene = (ManageAccountsScene) sceneManager.getScene("ManageAccountsScene");
            manageAccountsScene.setAccount(account);
            sceneManager.switchScene("ManageAccountsScene");
        });
        Button manageRolesButton = new Button("Hantera arbetsroller");

        manageRolesButton.setOnAction(e -> {
            ManageOccupationsScene manageOccupationsScene = (ManageOccupationsScene) sceneManager.getScene("ManageOccupationsScene");
            manageOccupationsScene.setAccount(account);
           sceneManager.switchScene("ManageOccupationsScene");
        });

        if (account != null && account.hasAdminPrivilege()) {
            navbar.getChildren().addAll(accountButton, manageUsersButton, manageRolesButton, logoutButton);
        } else {
            navbar.getChildren().addAll(accountButton, logoutButton);
        }

        return navbar;
    }
}
