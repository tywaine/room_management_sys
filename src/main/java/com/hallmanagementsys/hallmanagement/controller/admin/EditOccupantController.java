package com.hallmanagementsys.hallmanagement.controller.admin;

import com.hallmanagementsys.hallmanagement.model.Occupant;
import com.hallmanagementsys.hallmanagement.model.Room;
import com.hallmanagementsys.hallmanagement.util.MyAlert;
import com.hallmanagementsys.hallmanagement.viewFactory.ViewFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditOccupantController implements Initializable {
    public TextField txtRoomNumber, txtIdNumber, txtFirstName, txtLastName, txtEmail, txtPhoneNumber;
    public Label lblIdNumberError, lblFirstNameError, lblLastNameError, lblEmailError, lblPhoneNumberError;

    public Button btnSave;
    public Button btnCancel;

    private Occupant occupant;
    private boolean saveClicked = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSave.setDisable(true);

        txtIdNumber.textProperty().addListener((observable, oldValue, newValue) -> handleIdNumberError());
        txtFirstName.textProperty().addListener((observable, oldValue, newValue) -> handleFirstNameError());
        txtLastName.textProperty().addListener((observable, oldValue, newValue) -> handleLastNameError());
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> handleEmailError());
        txtPhoneNumber.textProperty().addListener((observable, oldValue, newValue) -> handlePhoneNumberError());
    }

    public void setOccupant(Occupant occupant) {
        this.occupant = occupant;

        // Pre-fill the fields with current values
        txtRoomNumber.setText(Room.getRoom(occupant.getRoomID()).getRoomNumber());
        txtIdNumber.setText(occupant.getIdNumber());
        txtFirstName.setText(occupant.getFirstName());
        txtLastName.setText(occupant.getLastName());
        txtEmail.setText(occupant.getEmail());
        txtPhoneNumber.setText(occupant.getPhoneNumber());
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
        btnSave.setDisable(!allFieldsValidForUpdate());
    }

    private boolean allFieldsValidForUpdate(){
        if(txtIdNumber.getText().isEmpty() || txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty()
                || txtEmail.getText().isEmpty() || txtPhoneNumber.getText().isEmpty()){
            return false;
        }

        String idNumber = txtIdNumber.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phoneNumber = txtPhoneNumber.getText().trim();

        if (occupant.getIdNumber().equals(idNumber) && occupant.getFirstName().equals(firstName)
                && occupant.getLastName().equals(lastName) && occupant.getEmail().equals(email)
                && occupant.getPhoneNumber().equals(phoneNumber)){
            System.out.println("No fields have been changed.");
            return false;
        }

        return true;
    }

    public void handleSave() {
        String idNumber = txtIdNumber.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phoneNumber = txtPhoneNumber.getText().trim();

        if(Occupant.idNumberExist(idNumber)){
            Occupant occupantByIdNumber = Occupant.getOccupantByIdNumber(idNumber);

            if(!occupant.equals(occupantByIdNumber)){
                String room = Room.getRoom(occupantByIdNumber.getRoomID()).getRoomNumber();
                MyAlert.showAlert(Alert.AlertType.ERROR, "Occupant already Exists",
                        "Occupant with Id number " + idNumber + " already exists in room " + room + "!");

                return;
            }
        }

        if(Occupant.emailExist(email)){
            Occupant occupantByEmail = Occupant.getOccupantByEmail(email);

            if(!occupant.equals(occupantByEmail)){
                String room = Room.getRoom(occupantByEmail.getRoomID()).getRoomNumber();
                MyAlert.showAlert(Alert.AlertType.ERROR, "Occupant already Exists",
                        "Occupant with email " + email + " already exists in room " + room + "!");

                return;
            }
        }

        if(Occupant.phoneNumberExist(phoneNumber)){
            Occupant occupantByPhoneNumber = Occupant.getOccupantByPhoneNumber(phoneNumber);

            if(!occupant.equals(occupantByPhoneNumber)){
                String room = Room.getRoom(occupantByPhoneNumber.getRoomID()).getRoomNumber();
                MyAlert.showAlert(Alert.AlertType.ERROR, "Occupant already Exists",
                        "Occupant with phone number " + phoneNumber + " already exists in room " + room + "!");

                return;
            }
        }

        occupant.update(firstName, lastName, idNumber, email, phoneNumber, null);
        saveClicked = true;
        closeDialog();
    }

    public void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        ViewFactory.getInstance().closeStage(stage);
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }
}
