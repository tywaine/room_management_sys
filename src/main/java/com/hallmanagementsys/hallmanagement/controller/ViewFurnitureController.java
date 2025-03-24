package com.hallmanagementsys.hallmanagement.controller;

import com.hallmanagementsys.hallmanagement.dto.msg.FurnitureDeleteMessage;
import com.hallmanagementsys.hallmanagement.dto.msg.FurnitureUpdateMessage;
import com.hallmanagementsys.hallmanagement.enums.AdminMenuOptions;
import com.hallmanagementsys.hallmanagement.model.Furniture;
import com.hallmanagementsys.hallmanagement.model.Room;
import com.hallmanagementsys.hallmanagement.service.FurnitureService;
import com.hallmanagementsys.hallmanagement.util.MyAlert;
import com.hallmanagementsys.hallmanagement.viewFactory.ViewFactory;
import com.hallmanagementsys.hallmanagement.websocket.MyWebSocketClient;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
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
    public TableColumn<Furniture, Integer> columnFurnitureId;
    public TableColumn<Furniture, String> columnFurnitureType;
    public TableColumn<Furniture, String> columnFurnitureCondition;
    public TableColumn<Furniture, Void> columnEdit;
    public TableColumn<Furniture, Void> columnDelete;
    public Button btnAddFurniture;
    public TextField txtSearchRoom, txtSearchFurniture, txtRoomNumber;
    public ChoiceBox<String> choiceBoxCondition, choiceBoxType;
    public Button btnShowAllFurniture;

    private FilteredList<Room> filteredRoomList;
    private Room selectedRoom;
    private final ObservableList<Furniture> furnitureList = FXCollections.observableArrayList();
    private FilteredList<Furniture> filteredFurnitureList;

    public FurnitureService furnitureService = FurnitureService.getInstance();

    private static final String FURNITURE_TOPIC_UPDATE = "/topic/furnitureUpdates"; // Ensure this matches backend
    private static final String FURNITURE_TOPIC_DELETE = "/topic/furnitureDeletes";
    private final MyWebSocketClient webSocketClient = MyWebSocketClient.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Subscribe to WebSocket topics
        setupWebSocketSubscriptions();
        btnAddFurniture.setDisable(true);
        setupListView();

        ObservableList<String> furnitureTypeOptions = FXCollections.observableArrayList(
                "None",
                "Easy Chair",
                "Bed",
                "Mattress",
                "Closet",
                "Study Table",
                "Study Chair",
                "Wall",
                "Chest of Draws",
                "Window"
        );
        choiceBoxType.setItems(furnitureTypeOptions);
        choiceBoxType.setValue("None");
        choiceBoxType.valueProperty().addListener((observable, oldValue, newValue) -> validateFields());

        ObservableList<String> furnitureConditionOptions = FXCollections.observableArrayList( "None", "POOR", "GOOD", "EXCELLENT");
        choiceBoxCondition.setItems(furnitureConditionOptions);
        choiceBoxCondition.setValue("None");
        choiceBoxCondition.valueProperty().addListener((observable, oldValue, newValue) -> validateFields());

        columnFurnitureId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnFurnitureType.setCellValueFactory(cellData -> cellData.getValue().furnitureTypeProperty());
        columnFurnitureCondition.setCellValueFactory(cellData -> cellData.getValue().furnitureConditionProperty());

        filteredFurnitureList = new FilteredList<>(furnitureList, _ -> true);
        tableViewFurniture.setItems(filteredFurnitureList);
        txtSearchFurniture.textProperty().addListener((observable, oldValue, newValue) -> filterFurnitureList());

        setupEditColumn();
        setupDeleteColumn();
    }

    private void setupWebSocketSubscriptions() {
        // Subscribe to furniture updates
        webSocketClient.subscribe(FURNITURE_TOPIC_UPDATE, FurnitureUpdateMessage.class, message -> {
            Platform.runLater(() -> {
                // Handle furniture update message
                handleFurnitureUpdate(message);
            });
        });

        // Subscribe to furniture deletes
        webSocketClient.subscribe(FURNITURE_TOPIC_DELETE, FurnitureDeleteMessage.class, message -> {
            Platform.runLater(() -> {
                // Handle furniture delete message
                handleFurnitureDelete(message);
            });
        });
    }

    private void handleFurnitureUpdate(FurnitureUpdateMessage message) {
        System.out.println("Received furniture update: " + message);
        Furniture furniture = Furniture.getFurniture(message.getFurniture().getId());

        if(message.isStatusADD()){

            // Check and make sure furniture with the same id is not in the furnitureList
            // If it is not then furniture will be null
            if(furniture == null){
                Furniture newFurniture = Furniture.fromDTO(message.getFurniture());

                if(selectedRoom != null && selectedRoom.getID().equals(newFurniture.getRoomID())){
                    furnitureList.add(newFurniture);
                }
            }
            else{
                System.out.println("Furniture already exists");
            }
        }
        else{
            if(message.isStatusUPDATE()){

                // Furniture should be in the Furniture.getList() and not be null
                if(furniture != null){
                    furniture.setFurnitureCondition(message.getFurniture().getFurnitureCondition());
                }
                else{
                    System.out.println("Furniture does not exist");
                }
            }
        }

    }

    private void handleFurnitureDelete(FurnitureDeleteMessage message) {
        System.out.println("Received furniture delete: " + message);
        Furniture furniture = Furniture.getFurniture(message.getFurnitureId());

        if(furniture != null){
            Furniture.removeFurniture(furniture);

            if(selectedRoom != null && selectedRoom.getID().equals(furniture.getRoomID())){
                furnitureList.remove(furniture);
            }
        }
        else{
            System.out.println("Furniture was already deleted.");
        }
    }

    private void setupListView() {
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
        filteredRoomList = new FilteredList<>(Room.getList(), _ -> true);
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

            return room.getRoomNumber().contains(searchText);
        });
    }

    public void updateFurnitureList(Room selectedRoom) {
        if (selectedRoom != null) {
            furnitureList.setAll(selectedRoom.getFurnitureList());
            this.selectedRoom = selectedRoom;
            txtRoomNumber.setText(selectedRoom.getRoomNumber());
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

            return String.valueOf(furniture.getID()).contains(searchText)
                    || furniture.getFurnitureType().toUpperCase().contains(searchText)
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

    private void deleteFurniture(Furniture furniture) {
        if (MyAlert.confirmationDialogAlertIsYes("Delete Furniture?",
                "Are you sure you want to delete this Furniture type " + furniture.getFurnitureType() + ", with a "
                        + furniture.getFurnitureCondition() + " condition from room " + selectedRoom.getRoomNumber() + "?")) {

            if(furnitureService.deleteFurniture(furniture.getID())){
                Furniture.removeFurniture(furniture);
                furnitureList.remove(furniture);
                MyAlert.showAlert(Alert.AlertType.INFORMATION, "Deleted Furniture",
                        "Furniture type " + furniture.getFurnitureType() + ", in room " + selectedRoom.getRoomNumber() +
                                " with a " + furniture.getFurnitureCondition() + " condition" + " has been deleted!!");
            }
            else{
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error deleting Furniture",
                        "There was an error deleting Furniture type " + furniture.getFurnitureType() + ", with a "
                                + furniture.getFurnitureCondition() + " condition from room " + selectedRoom.getRoomNumber() + ".");
            }
        }
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

    private void editFurniture(Furniture furniture) {
        boolean saveClicked = ViewFactory.getInstance().showEditFurnitureDialog(furniture);

        if (saveClicked) {
            if(furnitureService.updateFurniture(furniture)){
                MyAlert.showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Furniture updated successfully!");
            }
            else{
                MyAlert.showAlert(Alert.AlertType.ERROR, "Unknown Error",
                        "Error updating Furniture in server.");
            }

            updateFurnitureList(selectedRoom);
        }
    }

    private void validateFields(){
        btnAddFurniture.setDisable(!allFieldsValidForAdd());
    }

    private boolean allFieldsValidForAdd(){
        if(choiceBoxType.getValue() == null || choiceBoxType.getValue().equals("None")){
            return false;
        }

        if(choiceBoxCondition.getValue() == null || choiceBoxCondition.getValue().equals("None")){
            return false;
        }

        String furnitureType = choiceBoxType.getValue();
        String furnitureCondition = choiceBoxCondition.getValue();
        return !furnitureType.isEmpty() && !furnitureCondition.isEmpty() && isRoomSelected();
    }

    private boolean isRoomSelected() {
        return selectedRoom != null;
    }

    public void addFurniture() {
        String furnitureType = choiceBoxType.getValue();
        String furnitureCondition = choiceBoxCondition.getValue();

        Furniture newFurniture = new Furniture(selectedRoom.getID(), furnitureType, furnitureCondition);
        Furniture furniture = furnitureService.createFurniture(newFurniture);

        if(furniture != null){
            furnitureList.add(furniture);
            MyAlert.showAlert(Alert.AlertType.INFORMATION, "Added Furniture",
                    "Furniture type " + furnitureType + " with a " + furnitureCondition +
                            " condition" + " has been added to room " + selectedRoom.getRoomNumber() + ".");
        }
        else{
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error Adding Furniture",
                    "Furniture type " + furnitureType + " with a " + furnitureCondition +
                            " condition" + " was not added to room " + selectedRoom.getRoomNumber() + "!!!");
        }
    }

    public void showAllFurnitureView() {
        ViewFactory.getInstance().getAdminSelectedMenuItem().set(AdminMenuOptions.ALL_FURNITURE);
    }
}
