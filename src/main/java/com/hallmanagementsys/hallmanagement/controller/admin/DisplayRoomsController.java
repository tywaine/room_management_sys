package com.hallmanagementsys.hallmanagement.controller.admin;

import com.hallmanagementsys.hallmanagement.model.Room;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DisplayRoomsController implements Initializable {
    public TableView<Room> tableViewRooms;
    public TableColumn<Room, String> columnRoomNumber;
    public TableColumn<Room, Character> columnBlock;
    public TableColumn<Room, Integer> columnFloor;
    public TableColumn<Room, Integer> columnMaxOccupants;
    public TableColumn<Room, Integer> columnCurrentOccupants;
    public TableColumn<Room, Integer> columnTotalFurniture;
    public TextField txtSearch;

    private final ObservableList<Room> roomList = Room.getList();
    private FilteredList<Room> filteredRoomList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnRoomNumber.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty());
        columnBlock.setCellValueFactory(cellData -> cellData.getValue().getBlockNameProperty());
        columnFloor.setCellValueFactory(cellData -> cellData.getValue().floorProperty().asObject());
        columnMaxOccupants.setCellValueFactory(cellData -> cellData.getValue().maxOccupantsProperty().asObject());
        columnCurrentOccupants.setCellValueFactory(cellData -> cellData.getValue().currentOccupantsProperty().asObject());
        columnTotalFurniture.setCellValueFactory(cellData -> cellData.getValue().totalFurnitureProperty().asObject());

        filteredRoomList = new FilteredList<>(roomList, p -> true);
        tableViewRooms.setItems(filteredRoomList);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> filterRoomList());
    }


    public void filterRoomList(){
        String searchText = txtSearch.getText().toLowerCase().trim();

        filteredRoomList.setPredicate(room -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return room.getRoomNumber().contains(searchText)
                    || room.getBlockNameProperty().get() == searchText.toUpperCase().charAt(0)
                    || String.valueOf(room.getFloor()).toLowerCase().contains(searchText)
                    || String.valueOf(room.getMaxOccupants()).contains(searchText)
                    || String.valueOf(room.getCurrentOccupants()).contains(searchText)
                    || String.valueOf(room.getTotalFurniture()).contains(searchText);
        });
    }
}
