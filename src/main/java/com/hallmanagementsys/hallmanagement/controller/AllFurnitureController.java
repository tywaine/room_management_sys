package com.hallmanagementsys.hallmanagement.controller;

import com.hallmanagementsys.hallmanagement.enums.AdminMenuOptions;
import com.hallmanagementsys.hallmanagement.model.Furniture;
import com.hallmanagementsys.hallmanagement.viewFactory.ViewFactory;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AllFurnitureController implements Initializable {
    public TableView<Furniture> tableViewFurniture;
    public TableColumn<Furniture, String> columnRoomNumber;
    public TableColumn<Furniture, Integer> columnFurnitureId;
    public TableColumn<Furniture, String> columnFurnitureType;
    public TableColumn<Furniture, String> columnFurnitureCondition;
    public TextField txtSearch;
    public Button btnBack;

    private FilteredList<Furniture> filteredFurnitureList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnRoomNumber.setCellValueFactory(cellData -> cellData.getValue().getRoomNumberProperty());
        columnFurnitureId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnFurnitureType.setCellValueFactory(cellData -> cellData.getValue().furnitureTypeProperty());
        columnFurnitureCondition.setCellValueFactory(cellData -> cellData.getValue().furnitureConditionProperty());

        filteredFurnitureList = new FilteredList<>(Furniture.getList(), _ -> true);
        tableViewFurniture.setItems(filteredFurnitureList);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> filterFurnitureList());
    }

    public void filterFurnitureList(){
        String searchText = txtSearch.getText().toUpperCase().trim();

        filteredFurnitureList.setPredicate(furniture -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return furniture.getRoomNumberProperty().get().contains(searchText)
                    || String.valueOf(furniture.getID()).contains(searchText)
                    || furniture.getFurnitureType().toUpperCase().contains(searchText)
                    || furniture.getFurnitureCondition().contains(searchText);
        });
    }

    public void showViewFurnitureView() {
        ViewFactory.getInstance().getAdminSelectedMenuItem().set(AdminMenuOptions.FURNITURE);
    }
}
