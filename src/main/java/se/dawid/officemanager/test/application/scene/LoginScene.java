package se.dawid.officemanager.test.application.scene;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import se.dawid.officemanager.test.utility.Authenticator;
import se.dawid.officemanager.test.object.Account;

public class LoginScene extends AbstractScene {

    private final Authenticator authenticator;

    public LoginScene(SceneManager sceneManager, Authenticator authenticator) {
        super(sceneManager);
        this.authenticator = authenticator;
    }

    @Override
    public Scene buildScene() {
        VBox layout = new VBox(10);
        TextField usernameField = new TextField();

        usernameField.setPromptText("Användarnamn");

        usernameField.setMinWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Lösenord");
        passwordField.setMinWidth(250);

        Label errorLabel = new Label();
        errorLabel.setMinWidth(200);

        Button loginButton = new Button("Logga in");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            Account account = authenticator.authenticate(username, password);
            if (account != null) {
                MainScene mainScene = (MainScene) getSceneManager().getScene("MainScene");
                mainScene.setAccount(account);

                getSceneManager().switchScene("MainScene");
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        });



        layout.getChildren().addAll(usernameField, passwordField, loginButton, errorLabel);
        return new Scene(layout, 400, 300);
    }


    @Override
    public String getName() {
        return "LoginScene";
    }
}
