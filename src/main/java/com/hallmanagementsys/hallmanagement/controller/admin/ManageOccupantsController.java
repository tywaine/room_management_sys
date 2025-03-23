package com.hallmanagementsys.hallmanagement.controller.admin;

import com.hallmanagementsys.hallmanagement.dto.msg.OccupantDeleteMessage;
import com.hallmanagementsys.hallmanagement.dto.msg.OccupantUpdateMessage;
import com.hallmanagementsys.hallmanagement.model.Occupant;
import com.hallmanagementsys.hallmanagement.model.Room;
import com.hallmanagementsys.hallmanagement.service.OccupantService;
import com.hallmanagementsys.hallmanagement.util.MyAlert;
import com.hallmanagementsys.hallmanagement.viewFactory.ViewFactory;
import com.hallmanagementsys.hallmanagement.websocket.MyWebSocketClient;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ManageOccupantsController implements Initializable {
    public ListView<Room> listViewRooms;
    public TableView<Occupant> tableViewOccupants;
    public TableColumn<Occupant, String> columnIdNumber;
    public TableColumn<Occupant, String> columnFirstName;
    public TableColumn<Occupant, String> columnLastName;
    public TableColumn<Occupant, String> columnEmail;
    public TableColumn<Occupant, String> columnPhoneNumber;
    public TableColumn<Occupant, String> columnDateAdded;
    public TableColumn<Occupant, Void> columnEdit;
    public TableColumn<Occupant, Void> columnDelete;
    public TextField txtRoomNumber, txtIdNumber, txtFirstName, txtLastName, txtEmail, txtPhoneNumber;
    public TextField txtSearchRoom;
    public Label lblIdNumberError, lblFirstNameError, lblLastNameError, lblEmailError, lblPhoneNumberError;
    public Label lblMaxOccupants;
    public Button btnAddOccupant;

    private FilteredList<Room> filteredRoomList;
    private Room selectedRoom;

    public OccupantService occupantService = OccupantService.getInstance();

    private static final String OCCUPANT_TOPIC_UPDATE = "/topic/occupantUpdates"; // Ensure this matches backend
    private static final String OCCUPANT_TOPIC_DELETE = "/topic/occupantDeletes";
    private final MyWebSocketClient webSocketClient = MyWebSocketClient.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupWebSocketSubscriptions();
        btnAddOccupant.setDisable(true);
        setupListView();

        txtIdNumber.textProperty().addListener((observable, oldValue, newValue) -> handleIdNumberError());
        txtFirstName.textProperty().addListener((observable, oldValue, newValue) -> handleFirstNameError());
        txtLastName.textProperty().addListener((observable, oldValue, newValue) -> handleLastNameError());
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> handleEmailError());
        txtPhoneNumber.textProperty().addListener((observable, oldValue, newValue) -> handlePhoneNumberError());

        columnIdNumber.setCellValueFactory(cellData -> cellData.getValue().idNumberProperty());
        columnFirstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        columnLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        columnEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        columnPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        columnDateAdded.setCellValueFactory(cellData -> cellData.getValue().dateAddedFormattedProperty());

        tableViewOccupants.setItems(FXCollections.observableArrayList());

        setupEditColumn();
        setupDeleteColumn();
    }

    private void setupWebSocketSubscriptions() {
        // Subscribe to occupant updates
        webSocketClient.subscribe(OCCUPANT_TOPIC_UPDATE, OccupantUpdateMessage.class, message -> {
            Platform.runLater(() -> {
                // Handle occupant update message
                handleOccupantUpdate(message);
            });
        });

        // Subscribe to occupant deletes
        webSocketClient.subscribe(OCCUPANT_TOPIC_DELETE, OccupantDeleteMessage.class, message -> {
            Platform.runLater(() -> {
                // Handle occupant delete message
                handleOccupantDelete(message);
            });
        });
    }

    private void handleOccupantUpdate(OccupantUpdateMessage message) {
        System.out.println("Received occupant update: " + message);
        Occupant occupant = Occupant.getOccupant(message.getOccupant().getId());

        if(message.isStatusADD()){

            // Check and make sure occupant with the same id is not in the occupantList
            // If it is not then occupant will be null
            if(occupant == null){
                Occupant newOccupant = Occupant.fromDTO(message.getOccupant());

                /*
                if(selectedRoom != null && selectedRoom.getID().equals(newOccupant.getRoomID())){
                    occupantList.add(newOccupant);
                }

                 */
            }
            else{
                System.out.println("Occupant already exists");
            }
        }
        else{
            if(message.isStatusUPDATE()){

                // Occupant should be in the Occupant.getList() and not be null
                if(occupant != null){
                    occupant.setIdNumber(message.getOccupant().getIdNumber());
                    occupant.setFirstName(message.getOccupant().getFirstName());
                    occupant.setLastName(message.getOccupant().getLastName());
                    occupant.setEmail(message.getOccupant().getEmail());
                    occupant.setPhoneNumber(message.getOccupant().getPhoneNumber());
                }
                else{
                    System.out.println("Occupant does not exist");
                }
            }
        }

    }

    private void handleOccupantDelete(OccupantDeleteMessage message) {
        System.out.println("Received occupant delete: " + message);
        Occupant occupant = Occupant.getOccupant(message.getOccupantId());

        if(occupant != null){
            Occupant.removeOccupant(occupant);

            /*
            if(selectedRoom != null && selectedRoom.getID().equals(occupant.getRoomID())){
                occupantList.remove(occupant);
            }

             */
        }
        else{
            System.out.println("Occupant was already deleted.");
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
                updateOccupantList(newValue);
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

    public void updateOccupantList(Room selectedRoom) {
        if (selectedRoom != null) {
            tableViewOccupants.setItems(selectedRoom.getOccupantList());
            this.selectedRoom = selectedRoom;
            lblMaxOccupants.setText("Max Occupants: " + selectedRoom.getMaxOccupants());
            txtRoomNumber.setText(selectedRoom.getRoomNumber());
            validateFields();
        }
        else{
            tableViewOccupants.setItems(FXCollections.observableArrayList());
        }
    }

    private void setupDeleteColumn() {
        columnDelete.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Occupant, Void> call(final TableColumn<Occupant, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button();

                    {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        deleteIcon.setSize("14");
                        deleteButton.setGraphic(deleteIcon);

                        deleteButton.setOnAction(event -> {
                            Occupant occupant = getTableView().getItems().get(getIndex());
                            deleteOccupant(occupant);
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

    private void deleteOccupant(Occupant occupant) {
        if (MyAlert.confirmationDialogAlertIsYes("Delete Occupant?",
                "Are you sure you want to delete this occupant with id number " + occupant.getIdNumber()
                        + " from room " + selectedRoom.getRoomNumber() + "?")) {

            if(occupantService.deleteOccupant(occupant.getID())){
                Occupant.removeOccupant(occupant);
                //occupantList.remove(occupant);
                MyAlert.showAlert(Alert.AlertType.INFORMATION, "Deleted Occupant",
                        "Occupant with id Number " + occupant.getIdNumber() + " in room " + selectedRoom.getRoomNumber()
                                + " has been deleted!!");

                validateFields();
            }
            else{
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error deleting Occupant",
                        "There was an error deleting Occupant with id number " + occupant.getIdNumber()
                                + " from room " + selectedRoom.getRoomNumber() + ".");
            }
        }
    }

    private void setupEditColumn() {
        columnEdit.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Occupant, Void> call(final TableColumn<Occupant, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button();

                    {
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
                        editIcon.setSize("14");
                        editButton.setGraphic(editIcon);

                        editButton.setOnAction(event -> {
                            Occupant occupant = getTableView().getItems().get(getIndex());
                            editOccupant(occupant);
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

    private void editOccupant(Occupant occupant) {
        boolean saveClicked = ViewFactory.getInstance().showEditOccupantDialog(occupant);

        if (saveClicked) {
            if(occupantService.updateOccupant(occupant)){
                MyAlert.showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Occupant updated successfully!");
            }
            else{
                MyAlert.showAlert(Alert.AlertType.ERROR, "Unknown Error",
                        "Error updating Occupant in server.");
            }

            updateOccupantList(selectedRoom);
        }
    }

    private void handleIdNumberError(){
        String idNumber = txtIdNumber.getText().trim();

        if(!idNumber.isEmpty()){
            if(Occupant.isValidIdNumber(idNumber)){
                lblIdNumberError.setText("");
            }
            else{
                lblIdNumberError.setText("Id Number should be 9 numbers");
            }
        }
        else{
            lblIdNumberError.setText("");
        }

        validateFields();
    }

    private void handleFirstNameError(){
        handleNameError(txtFirstName, lblFirstNameError);
    }

    private void handleLastNameError(){
        handleNameError(txtLastName, lblLastNameError);
    }

    private void handleNameError(TextField txtName, Label lblNameError) {
        String firstName = txtName.getText().trim();

        if(!firstName.isEmpty()){
            if(Occupant.isValidName(firstName)){
                lblNameError.setText("");
            }
            else{
                if(firstName.length() > 50){
                    lblNameError.setText("Name should be less than 50 characters");
                }
                else{
                    lblNameError.setText("Name should only contain letters");
                }
            }
        }
        else{
            lblNameError.setText("");
        }

        validateFields();
    }

    private void handleEmailError(){
        String email = txtEmail.getText().trim();

        if(!email.isEmpty()){
            if(Occupant.isValidEmail(email)){
                lblEmailError.setText("");
            }
            else{
                if(email.length() > 100){
                    lblEmailError.setText("Email length is too long");
                }
                else{
                    lblEmailError.setText("Not a valid email address");
                }
            }
        }
        else{
            lblEmailError.setText("");
        }

        validateFields();
    }

    private void handlePhoneNumberError(){
        String phoneNumber = txtPhoneNumber.getText().trim();

        if(!phoneNumber.isEmpty()){
            if(Occupant.isValidPhoneNumber(phoneNumber)){
                lblPhoneNumberError.setText("");
            }
            else{
                lblPhoneNumberError.setText("Not a valid phone number");
            }
        }
        else{
            lblPhoneNumberError.setText("");
        }

        validateFields();
    }

    private void validateFields(){
        btnAddOccupant.setDisable(!allFieldsValidForAdd());
    }

    private boolean allFieldsValidForAdd(){
        if(txtIdNumber.getText().isEmpty() || txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() ||
            txtEmail.getText().isEmpty() || txtPhoneNumber.getText().isEmpty() || !isRoomSelected()){
            return false;
        }

        if(tableViewOccupants.getItems().size() == selectedRoom.getMaxOccupants()){
            return false;
        }

        return lblIdNumberError.getText().isEmpty() && lblFirstNameError.getText().isEmpty() && lblLastNameError.getText().isEmpty()
                && lblEmailError.getText().isEmpty() && lblPhoneNumberError.getText().isEmpty();
    }

    private boolean isRoomSelected() {
        return selectedRoom != null;
    }

    private void clearFields() {
        txtIdNumber.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhoneNumber.setText("");
    }

    public void addOccupant() {
        String idNumber = txtIdNumber.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phoneNumber = txtPhoneNumber.getText().trim();

        if(Occupant.idNumberExist(idNumber)){
            Occupant occupant = Occupant.getOccupantByIdNumber(idNumber);
            String room = Room.getRoom(occupant.getRoomID()).getRoomNumber();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Occupant already Exists",
                    "Occupant with Id number " + idNumber + " already exists in room " + room + "!");

            return;
        }

        if(Occupant.emailExist(email)){
            Occupant occupant = Occupant.getOccupantByEmail(email);
            String room = Room.getRoom(occupant.getRoomID()).getRoomNumber();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Occupant already Exists",
                    "Occupant with email " + email + " already exists in room " + room + "!");

            return;
        }

        if(Occupant.phoneNumberExist(phoneNumber)){
            Occupant occupant = Occupant.getOccupantByPhoneNumber(phoneNumber);
            String room = Room.getRoom(occupant.getRoomID()).getRoomNumber();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Occupant already Exists",
                    "Occupant with phone number " + phoneNumber + " already exists in room " + room + "!");

            return;
        }

        Occupant newOccupant = new Occupant(firstName, lastName, idNumber, email, phoneNumber, selectedRoom.getID(), LocalDateTime.now());
        Occupant occupant = occupantService.createOccupant(newOccupant);

        if(occupant != null){
            //occupantList.add(occupant);
            clearFields();
            MyAlert.showAlert(Alert.AlertType.INFORMATION, "Added Occupant",
                    "Successfully added occupant to room number " + selectedRoom.getRoomNumber() + "!");
        }
        else{
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error Adding Occupant",
                    "Occupant was not added to room number " + selectedRoom.getRoomNumber() + "!!!");
        }
    }
}
