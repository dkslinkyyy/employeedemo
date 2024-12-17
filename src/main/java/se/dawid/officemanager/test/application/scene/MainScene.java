package se.dawid.officemanager.test.application.scene;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class MainScene extends AbstractScene {


    public MainScene(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public Scene buildScene() {
        VBox layout = new VBox(10);

        Label welcomeLabel = new Label("Välkommen, " + getAccount().getEmployee().getFirstName()  +
                " " + getAccount().getEmployee().getLastName() + "!");

        Label occupationTitle = new Label(
                "Arbetsroll: " + getAccount().getEmployee().getOccupation().getTitle() + "!");
        Label occupationDescription = new Label(
                "Beskrivning: " + getAccount().getEmployee().getOccupation().getDescription() + "!");


        Label salaryLabel = new Label("Inkomst: " + getAccount().getEmployee().getOccupation().getSalary());


        Label adminStatus = new Label(getAccount().hasAdminPrivilege() ? "Du har admin rättigheter." : "");


        HBox navbar = (HBox) createNavbar();

        layout.setAlignment(Pos.TOP_CENTER);

        layout.getChildren().addAll(navbar, welcomeLabel, occupationTitle, occupationDescription, salaryLabel, adminStatus);
        return new Scene(layout, 600, 400);
    }


    @Override
    public String getName() {
        return "MainScene";
    }
}
