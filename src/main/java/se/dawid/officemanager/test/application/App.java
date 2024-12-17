package se.dawid.officemanager.test.application;

import javafx.application.Application;
import javafx.stage.Stage;
import se.dawid.officemanager.test.utility.Authenticator;
import se.dawid.officemanager.test.application.scene.MainScene;
import se.dawid.officemanager.test.application.scene.manage.ManageAccountsScene;
import se.dawid.officemanager.test.application.scene.manage.ManageOccupationsScene;
import se.dawid.officemanager.test.application.scene.SceneManager;
import se.dawid.officemanager.test.application.scene.LoginScene;
import se.dawid.officemanager.test.controller.ServiceController;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("En cool CRUD applikation");

        SceneManager sceneManager = SceneManager.getInstance(primaryStage);

        ServiceController serviceController = new ServiceController();

        buildScenes(sceneManager, serviceController);

        sceneManager.initializeScenes(serviceController);


        sceneManager.switchScene("LoginScene");
        primaryStage.show();
    }

    private void buildScenes(SceneManager sceneManager, ServiceController serviceController) {
        new LoginScene(sceneManager, new Authenticator(serviceController));
        new MainScene(sceneManager);
        new ManageAccountsScene(sceneManager);
        new ManageOccupationsScene(sceneManager);
    }
}
