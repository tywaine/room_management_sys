package com.hallmanagementsys.hallmanagement.controller.admin;

import com.hallmanagementsys.hallmanagement.viewFactory.ViewFactory;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane adminParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewFactory.getInstance().getAdminSelectedMenuItem().addListener(
                (observableValue, oldVal, newVal) -> {
                    switch(newVal){
                        case FURNITURE -> adminParent.setCenter(ViewFactory.getInstance().getViewFurnitureView());
                        case OCCUPANTS -> adminParent.setCenter(ViewFactory.getInstance().getManageOccupantsView());
                        case REPORT -> adminParent.setCenter(ViewFactory.getInstance().getGenerateReportView());
                        case ACCOUNT -> adminParent.setCenter(ViewFactory.getInstance().getAccountView());
                        default -> adminParent.setCenter(ViewFactory.getInstance().getDisplayRoomsView());
                    }
                }
        );
    }
}
