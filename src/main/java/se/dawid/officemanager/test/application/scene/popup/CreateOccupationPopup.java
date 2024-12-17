package se.dawid.officemanager.test.application.scene.popup;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.dawid.officemanager.test.controller.ServiceController;
import se.dawid.officemanager.test.object.Occupation;

public class CreateOccupationPopup {

    private final ServiceController serviceController;

    public CreateOccupationPopup(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    public void display() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Skapa ny arbetsroll");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");

        TextField titleField = new TextField();
        titleField.setPromptText("Rolltitel");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Beskrivning");
        descriptionField.setWrapText(true);

        TextField salaryField = new TextField();
        salaryField.setPromptText("Inkomst");

        Button createButton = new Button("Create");
        createButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();

            if (title.isEmpty() || description.isEmpty()) {
                System.out.println("Alla fält är nödvändiga!");
                return;
            }

            serviceController.getOccupationService().createOccupation(new Occupation(titleField.getText(), descriptionField.getText(), Integer.parseInt(salaryField.getText())));
            popupStage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> popupStage.close());

        layout.getChildren().addAll(
                new Label("Skapa arbetsroll:"),
                titleField,
                descriptionField,
                salaryField,
                createButton,
                cancelButton
        );

        Scene scene = new Scene(layout, 300, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
}
