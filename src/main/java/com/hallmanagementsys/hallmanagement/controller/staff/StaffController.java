package com.hallmanagementsys.hallmanagement.controller.staff;

import com.hallmanagementsys.hallmanagement.enums.StaffMenuOptions;
import com.hallmanagementsys.hallmanagement.viewFactory.ViewFactory;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StaffController implements Initializable {
    public BorderPane staffParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewFactory.getInstance().getStaffSelectedMenuItem().addListener(
                (observableValue, oldVal, newVal) -> {
                    if (Objects.requireNonNull(newVal) == StaffMenuOptions.ALL_FURNITURE) {
                        staffParent.setCenter(ViewFactory.getInstance().getAllFurnitureView());
                    }
                    else {
                        staffParent.setCenter(ViewFactory.getInstance().getViewFurnitureView());
                    }
                }
        );
    }
}
