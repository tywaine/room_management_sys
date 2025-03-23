package com.hallmanagementsys.hallmanagement.controller;

import com.hallmanagementsys.hallmanagement.model.Furniture;
import com.hallmanagementsys.hallmanagement.model.Model;
import com.hallmanagementsys.hallmanagement.model.Room;
import com.hallmanagementsys.hallmanagement.service.FurnitureService;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditFurnitureController implements Initializable {
    public Label lblFurnitureId;
    public TextField txtRoomNumber;
    public TextField txtFurnitureType;
    public ChoiceBox<String> choiceBoxCondition;
    public Button btnSave;
    public Button btnCancel;

    private Furniture furniture;
    private boolean saveClicked = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSave.setDisable(true);
        choiceBoxCondition.setItems(FXCollections.observableArrayList("POOR", "GOOD", "EXCELLENT"));

        txtFurnitureType.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        choiceBoxCondition.valueProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }

    public void setFurniture(Furniture furniture) {
        this.furniture = furniture;

        // Pre-fill the fields with current values
        lblFurnitureId.setText("Furniture ID: " + furniture.getID());
        txtRoomNumber.setText(Room.getRoom(furniture.getRoomID()).getRoomNumber());
        txtFurnitureType.setText(furniture.getFurnitureType());
        choiceBoxCondition.setValue(furniture.getFurnitureCondition());
    }

    private void validateFields(){
        btnSave.setDisable(!allFieldsValidForUpdate());
    }

    private boolean allFieldsValidForUpdate(){
        if(txtFurnitureType.getText() == null || choiceBoxCondition.getValue() == null){
            return false;
        }

        String furnitureType = furniture.getFurnitureType();
        String furnitureCondition = furniture.getFurnitureCondition();

        if (txtFurnitureType.getText().equals(furnitureType) && choiceBoxCondition.getValue().equals(furnitureCondition)) {
            System.out.println("No fields have been changed.");
            return false;
        }

        return !furnitureType.isEmpty();
    }

    public void handleSave() {
        furniture.setFurnitureCondition(choiceBoxCondition.getValue());
        saveClicked = true;
        closeDialog();
    }

    public void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }
}
