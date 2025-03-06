package com.hallmanagementsys.hallmanagement.controller.staff;

import com.hallmanagementsys.hallmanagement.model.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffController implements Initializable {
    public BorderPane staffParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getStaffSelectedMenuItem().addListener(
                (observableValue, oldVal, newVal) -> {
                    switch(newVal){
                        case FURNITURE -> staffParent.setCenter(Model.getInstance().getViewFactory().getViewFurnitureView());
                        case OCCUPANTS -> staffParent.setCenter(Model.getInstance().getViewFactory().getManageOccupantsView());
                        case REPORT -> staffParent.setCenter(Model.getInstance().getViewFactory().getGenerateReportView());
                        case ACCOUNT -> staffParent.setCenter(Model.getInstance().getViewFactory().getAccountView());
                        default -> staffParent.setCenter(Model.getInstance().getViewFactory().getDisplayRoomsView());
                    }
                }
        );
    }
}
