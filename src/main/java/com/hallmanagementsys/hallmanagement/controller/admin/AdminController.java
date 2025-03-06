package com.hallmanagementsys.hallmanagement.controller.admin;

import com.hallmanagementsys.hallmanagement.model.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane adminParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener(
                (observableValue, oldVal, newVal) -> {
                    switch(newVal){
                        case FURNITURE -> adminParent.setCenter(Model.getInstance().getViewFactory().getViewFurnitureView());
                        case OCCUPANTS -> adminParent.setCenter(Model.getInstance().getViewFactory().getManageOccupantsView());
                        case REPORT -> adminParent.setCenter(Model.getInstance().getViewFactory().getGenerateReportView());
                        case ACCOUNT -> adminParent.setCenter(Model.getInstance().getViewFactory().getAccountView());
                        default -> adminParent.setCenter(Model.getInstance().getViewFactory().getDisplayRoomsView());
                    }
                }
        );
    }
}
