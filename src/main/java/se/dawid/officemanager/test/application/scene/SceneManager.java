package se.dawid.officemanager.test.application.scene;

import javafx.stage.Stage;
import se.dawid.officemanager.test.controller.ServiceController;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {

    private static SceneManager instance; // Singleton instance
    private final Stage primaryStage;
    private final List<AbstractScene> scenes;

    private SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.scenes = new ArrayList<>();
    }

    // Public method to get the singleton instance
    public static SceneManager getInstance(Stage primaryStage) {
        if (instance == null) {
            synchronized (SceneManager.class) {
                if (instance == null) {
                    instance = new SceneManager(primaryStage); // Create instance if it doesn't exist
                }
            }
        }
        return instance;
    }

    public void addScene(AbstractScene scene) {
        if (instance == null) {
            throw new IllegalStateException("SceneManager instance is not initialized.");
        }
        instance.scenes.add(scene);
    }

    public AbstractScene getScene(String sceneName) {
        for (AbstractScene scene : scenes) {
            if (scene.getName().equals(sceneName)) {
                return scene;
            }
        }
        throw new IllegalArgumentException("Scene with name '" + sceneName + "' not found.");
    }

    public void switchScene(String name) {
        AbstractScene abstractScene = getScene(name);
        primaryStage.setScene(abstractScene.buildScene());
    }

    public void initializeScenes(ServiceController serviceController) {
        for (AbstractScene scene : scenes) {
            scene.setServiceController(serviceController);
        }
    }
}
