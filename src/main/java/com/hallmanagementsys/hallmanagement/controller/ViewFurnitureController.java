package com.hallmanagementsys.hallmanagement.controller;

import com.hallmanagementsys.hallmanagement.dto.FurnitureDTO;
import com.hallmanagementsys.hallmanagement.dto.msg.FurnitureDeleteMessage;
import com.hallmanagementsys.hallmanagement.dto.msg.FurnitureUpdateMessage;
import com.hallmanagementsys.hallmanagement.model.Furniture;
import com.hallmanagementsys.hallmanagement.model.Model;
import com.hallmanagementsys.hallmanagement.model.Room;
import com.hallmanagementsys.hallmanagement.service.FurnitureService;
import com.hallmanagementsys.hallmanagement.util.Json;
import com.hallmanagementsys.hallmanagement.util.MyAlert;
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

    private static final String FURNITURE_TOPIC_UPDATE = "/topic/furnitureUpdates"; // Ensure this matches backend
    private static final String FURNITURE_TOPIC_DELETE = "/topic/furnitureDeletes";

    private final MyWebSocketClient webSocketClient = Model.getInstance().getWebSocketClient();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Subscribe to WebSocket topics
        setupWebSocketSubscriptions();
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

    private void setupWebSocketSubscriptions() {
        // Subscribe to furniture updates
        webSocketClient.subscribe("/topic/furnitureUpdates", FurnitureUpdateMessage.class, message -> {
            Platform.runLater(() -> {
                // Handle furniture update message
                handleFurnitureUpdate(message);
            });
        });

        // Subscribe to furniture deletes
        webSocketClient.subscribe("/topic/furnitureDeletes", FurnitureDeleteMessage.class, message -> {
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

            if(furniture == null){
                Furniture newFurniture = Furniture.fromDTO(message.getFurniture());
                furnitureList.add(newFurniture);
            }
            else{
                System.out.println("Furniture already exists");
            }
        }
        else{
            if(message.isStatusUPDATE()){

                if(furniture != null){
                    furniture.setFurnitureCondition(message.getFurniture().getFurnitureCondition());
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
            furnitureList.remove(furniture);
        }
        else{
            System.out.println("Furniture was already deleted.");
        }
    }

    public void sendFurnitureUpdate(FurnitureUpdateMessage message) {
        webSocketClient.sendMessage("/furnitureUpdates", message);
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
            return room.getRoomNumber().contains(searchText);
        });
    }

    public void updateFurnitureList(Room selectedRoom) {
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
        boolean success = Model.getInstance().getViewFactory().showEditFurnitureDialog(furniture);

        if (success) {
            MyAlert.showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Furniture updated successfully!");
            updateFurnitureList(selectedRoom);
        }
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
            txtFurnitureType.clear();
            furnitureList.add(furniture);
            MyAlert.showAlert(Alert.AlertType.INFORMATION, "Added Furniture",
                    "Furniture type " + furnitureType + " with a " + furnitureCondition +
                            " condition" + " has been added to room " + selectedRoom.getRoomNumber() + ".");
        }
        else{
            MyAlert.showAlert(Alert.AlertType.INFORMATION, "Error Adding Furniture",
                    "Furniture type " + furnitureType + " with a " + furnitureCondition +
                            " condition" + " was not added to room " + selectedRoom.getRoomNumber() + "!!!");
        }
    }
}
