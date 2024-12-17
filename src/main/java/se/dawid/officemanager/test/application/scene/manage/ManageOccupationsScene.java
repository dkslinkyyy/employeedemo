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
import se.dawid.officemanager.test.application.scene.popup.CreateOccupationPopup;
import se.dawid.officemanager.test.object.Occupation;

import java.util.List;

public class ManageOccupationsScene extends AbstractScene {


    public ManageOccupationsScene(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public Scene buildScene() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 20;");

        HBox navbar = createNavbar();
        VBox occupationsContainer = new VBox(10);
        occupationsContainer.setStyle("-fx-padding: 10; -fx-border-color: lightgray;");

        List<Occupation> occupations = getServiceController().getOccupationService().getAllOccupations();
        occupations.forEach(occupation -> createChip(occupation, occupationsContainer));

        Button createOccupationButton = new Button("Skapa yrke");
        createOccupationButton.setOnAction(e -> {
            CreateOccupationPopup createOccupationScene = new CreateOccupationPopup(getServiceController());
            createOccupationScene.display();
        });



        HBox footer = new HBox(10);
        footer.setAlignment(Pos.BOTTOM_RIGHT);
        footer.getChildren().add(createOccupationButton);

        layout.getChildren().addAll(navbar, occupationsContainer, footer);

        return new Scene(layout, 600, 400);
    }

    @Override
    public String getName() {
        return "ManageOccupationsScene";
    }

    private void createChip(Occupation occupation, VBox parentLayout) {
        HBox chip = new HBox(10);
        chip.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");

        Label occupationTitle = new Label(occupation.getTitle());
        Label occupationDescription = new Label(occupation.getDescription());
        Label occupationSalary = new Label(String.valueOf(occupation.getSalary()));

        HBox.setHgrow(occupationTitle, Priority.ALWAYS);
        HBox.setHgrow(occupationDescription, Priority.ALWAYS);
        HBox.setHgrow(occupationSalary, Priority.ALWAYS);

        chip.setAlignment(Pos.CENTER_LEFT);
        occupationTitle.setMaxWidth(Double.MAX_VALUE);
        occupationDescription.setMaxWidth(Double.MAX_VALUE);
        occupationSalary.setMaxWidth(Double.MAX_VALUE);

        chip.getChildren().addAll(occupationTitle, occupationDescription, occupationSalary);
        parentLayout.getChildren().add(chip);
    }
}
