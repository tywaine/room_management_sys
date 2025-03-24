package com.hallmanagementsys.hallmanagement.controller.admin;

import com.hallmanagementsys.hallmanagement.enums.AdminMenuOptions;
import com.hallmanagementsys.hallmanagement.model.Occupant;
import com.hallmanagementsys.hallmanagement.viewFactory.ViewFactory;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AllOccupantsController implements Initializable {
    public TableView<Occupant> tableViewOccupants;
    public TableColumn<Occupant, String> columnRoomNumber;
    public TableColumn<Occupant, String> columnIdNumber;
    public TableColumn<Occupant, String> columnFirstName;
    public TableColumn<Occupant, String> columnLastName;
    public TableColumn<Occupant, String> columnEmail;
    public TableColumn<Occupant, String> columnPhoneNumber;
    public TableColumn<Occupant, String> columnDateAdded;
    public TextField txtSearch;
    public Button btnBack;

    private FilteredList<Occupant> filteredOccupantList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnRoomNumber.setCellValueFactory(cellData -> cellData.getValue().getRoomNumberProperty());
        columnIdNumber.setCellValueFactory(cellData -> cellData.getValue().idNumberProperty());
        columnFirstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        columnLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        columnEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        columnPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        columnDateAdded.setCellValueFactory(cellData -> cellData.getValue().dateAddedFormattedProperty());

        filteredOccupantList = new FilteredList<>(Occupant.getList(), _ -> true);
        tableViewOccupants.setItems(filteredOccupantList);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> filterOccupantList());
    }

    public void filterOccupantList(){
        String searchText = txtSearch.getText().toUpperCase().trim();

        filteredOccupantList.setPredicate(occupant -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return occupant.getRoomNumberProperty().get().contains(searchText)
                    || occupant.getIdNumber().contains(searchText)
                    || occupant.getFirstName().toUpperCase().contains(searchText)
                    || occupant.getLastName().toUpperCase().contains(searchText)
                    || occupant.getEmail().toUpperCase().contains(searchText)
                    || occupant.getPhoneNumber().toUpperCase().contains(searchText)
                    || occupant.dateAddedFormattedProperty().get().toUpperCase().contains(searchText);
        });
    }

    public void showManageOccupantsView() {
        ViewFactory.getInstance().getAdminSelectedMenuItem().set(AdminMenuOptions.OCCUPANTS);
    }
}
