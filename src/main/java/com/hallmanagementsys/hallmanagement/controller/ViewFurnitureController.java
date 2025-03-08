package com.hallmanagementsys.hallmanagement.controller;

import com.hallmanagementsys.hallmanagement.dto.FurnitureDTO;
import com.hallmanagementsys.hallmanagement.model.Furniture;
import com.hallmanagementsys.hallmanagement.model.Room;
import com.hallmanagementsys.hallmanagement.service.FurnitureService;
import com.hallmanagementsys.hallmanagement.util.MyAlert;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewFurnitureController implements Initializable {
    public ListView<Room> listViewRooms;
    public TableView<Furniture> tableViewFurniture;
    public TableColumn<Furniture, String> columnFurnitureType;
    public TableColumn<Furniture, String> columnFurnitureCondition;
    public TableColumn<Furniture, Void> columnEdit;
    public TableColumn<Furniture, Void> columnDelete;
    public Button btnAddFurniture;
    public TextField txtSearchRoom, txtSearchFurniture, txtFurnitureType;
    public Label lblTypeError;
    public ChoiceBox<String> choiceBoxCondition;

    private final ObservableList<Room> roomList = Room.getList();
    private FilteredList<Room> filteredRoomList;
    private Room selectedRoom;

    private final ObservableList<Furniture> furnitureList = FXCollections.observableArrayList();
    private FilteredList<Furniture> filteredFurnitureList;

    public FurnitureService furnitureService = FurnitureService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnAddFurniture.setDisable(true);
        setupListView();

        txtFurnitureType.textProperty().addListener((observable, oldValue, newValue) -> validateFields());

        ObservableList<String> staffCommandOptions = FXCollections.observableArrayList("POOR", "GOOD", "EXCELLENT");
        choiceBoxCondition.setItems(staffCommandOptions);
        choiceBoxCondition.setValue("POOR");

        columnFurnitureType.setCellValueFactory(cellData -> cellData.getValue().furnitureTypeProperty());
        columnFurnitureCondition.setCellValueFactory(cellData -> cellData.getValue().furnitureConditionProperty());

        filteredFurnitureList = new FilteredList<>(furnitureList, _ -> true);
        tableViewFurniture.setItems(filteredFurnitureList);
        txtSearchFurniture.textProperty().addListener((observable, oldValue, newValue) -> filterFurnitureList());

        setupEditColumn();
        setupDeleteColumn();
    }

    private void setupListView() {
        // Set up the cell factory to display room numbers
        listViewRooms.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);
                if (empty || room == null) {
                    setText(null);
                }
                else {
                    setText(room.getRoomNumber());
                }
            }
        });

        // Initialize filtered list for rooms
        filteredRoomList = new FilteredList<>(roomList, _ -> true);
        listViewRooms.setItems(filteredRoomList);

        // Add listener for room search
        txtSearchRoom.textProperty().addListener((observable, oldValue, newValue) -> filterRoomList());

        // Add selection listener to update furniture table when room is selected
        listViewRooms.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFurnitureList(newValue);
            }
        });
    }

    private void filterRoomList() {
        String searchText = txtSearchRoom.getText().toUpperCase().trim();

        filteredRoomList.setPredicate(room -> {
            if (searchText.isEmpty()) {
                return true;
            }
            return room.getRoomNumber().toUpperCase().contains(searchText);
        });
    }

    private void updateFurnitureList(Room selectedRoom) {
        if (selectedRoom != null) {
            furnitureList.setAll(selectedRoom.getFurnitureList());
            this.selectedRoom = selectedRoom;
            validateFields();
        }
        else{
            furnitureList.clear();
        }
    }

    public void filterFurnitureList(){
        String searchText = txtSearchFurniture.getText().toUpperCase().trim();

        filteredFurnitureList.setPredicate(furniture -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return furniture.getFurnitureType().toUpperCase().contains(searchText)
                    || furniture.getFurnitureCondition().contains(searchText);
        });
    }

    private void setupDeleteColumn() {
        columnDelete.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Furniture, Void> call(final TableColumn<Furniture, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button();

                    {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        deleteIcon.setSize("16");
                        deleteButton.setGraphic(deleteIcon);

                        deleteButton.setOnAction(event -> {
                            Furniture furniture = getTableView().getItems().get(getIndex());
                            deleteFurniture(furniture);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });
    }

    private void setupEditColumn() {
        columnEdit.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Furniture, Void> call(final TableColumn<Furniture, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button();

                    {
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
                        editIcon.setSize("16");
                        editButton.setGraphic(editIcon);

                        editButton.setOnAction(event -> {
                            Furniture furniture = getTableView().getItems().get(getIndex());
                            editFurniture(furniture);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(editButton);
                        }
                    }
                };
            }
        });
    }

    private void deleteFurniture(Furniture furniture) {
        if (MyAlert.confirmationDialogAlertIsYes("Delete Furniture?",
                "Are you sure you want to delete this Furniture: " + furniture.getFurnitureType() + ", with a "
                        + furniture.getFurnitureCondition() + " condition from room " + selectedRoom.getRoomNumber() + "?")) {

            if(furnitureService.deleteFurniture(furniture.getID())){
                Furniture.removeFurniture(furniture);
                MyAlert.showAlert(Alert.AlertType.INFORMATION, "Deleted Furniture",
                        "Furniture: " + furniture.getFurnitureType() + ", in room " + selectedRoom.getRoomNumber() +
                                " with a " + furniture.getFurnitureCondition() + " condition" + " has been added!");
            }
            else{
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error deleting Furniture",
                        "There was an error deleting Furniture: " + furniture.getFurnitureType() + ", with a "
                                + furniture.getFurnitureCondition() + " condition from room " + selectedRoom.getRoomNumber() + ".");
            }
        }
    }

    private void editFurniture(Furniture furniture) {

    }

    private void validateFields(){
        btnAddFurniture.setDisable(!allFieldsValidForAdd());
    }

    private boolean allFieldsValidForAdd(){
        if(txtFurnitureType.getText() == null){
            return false;
        }

        String furnitureType = txtFurnitureType.getText().trim();
        return !furnitureType.isEmpty() && isRoomSelected();
    }

    private boolean isRoomSelected() {
        return listViewRooms.getSelectionModel().getSelectedItem() != null;
    }

    private boolean isFurnitureSelected() {
        return tableViewFurniture.getSelectionModel().getSelectedItem() != null;
    }


    public void addFurniture() {
        String furnitureType = txtFurnitureType.getText().trim();
        String furnitureCondition = choiceBoxCondition.getValue();

        FurnitureDTO furnitureDTO = new FurnitureDTO(null, selectedRoom.getID(), furnitureType, furnitureCondition);
        Furniture furniture = furnitureService.createFurnitureAndRetrieve(furnitureDTO);

        if(furniture != null){
            furnitureList.add(furnitureService.createFurnitureAndRetrieve(furnitureDTO));
            txtFurnitureType.clear();
            MyAlert.showAlert(Alert.AlertType.INFORMATION, "Added Furniture",
                    "Furniture: " + furnitureType + " with a " + furnitureCondition +
                            " condition" + " has been added to room " + selectedRoom.getRoomNumber() + ".");
        }
        else{
            MyAlert.showAlert(Alert.AlertType.INFORMATION, "Error Adding Furniture",
                    "Furniture: " + furnitureType + " with a " + furnitureCondition +
                            " condition" + " was not added to room " + selectedRoom.getRoomNumber() + "!!!");
        }
    }
}
