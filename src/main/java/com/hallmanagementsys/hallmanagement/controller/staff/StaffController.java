package com.hallmanagementsys.hallmanagement.controller.staff;

import com.hallmanagementsys.hallmanagement.viewFactory.ViewFactory;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffController implements Initializable {
    public BorderPane staffParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewFactory.getInstance().getStaffSelectedMenuItem().addListener(
                (observableValue, oldVal, newVal) -> {
                    switch(newVal){
                        case FURNITURE -> staffParent.setCenter(ViewFactory.getInstance().getViewFurnitureView());
                        case OCCUPANTS -> staffParent.setCenter(ViewFactory.getInstance().getManageOccupantsView());
                        case REPORT -> staffParent.setCenter(ViewFactory.getInstance().getGenerateReportView());
                        case ACCOUNT -> staffParent.setCenter(ViewFactory.getInstance().getAccountView());
                        case ALL_FURNITURE -> staffParent.setCenter(ViewFactory.getInstance().getAllFurnitureView());
                        default -> staffParent.setCenter(ViewFactory.getInstance().getDisplayRoomsView());
                    }
                }
        );
    }
}
