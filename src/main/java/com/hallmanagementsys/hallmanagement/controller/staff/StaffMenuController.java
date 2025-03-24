package com.hallmanagementsys.hallmanagement.controller.staff;

import com.hallmanagementsys.hallmanagement.controller.LoginController;
import com.hallmanagementsys.hallmanagement.enums.StaffMenuOptions;
import com.hallmanagementsys.hallmanagement.model.Model;
import com.hallmanagementsys.hallmanagement.viewFactory.ViewFactory;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffMenuController implements Initializable {
    public Button btnFurniture, btnSignOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        addListeners();
        btnFurniture.getStyleClass().add("button-selected");
    }

    private void setIcons() {
        btnFurniture.setGraphic(createFontIcon(FontAwesomeIcon.BED, 18)); // Furniture
        btnSignOut.setGraphic(createFontIcon(FontAwesomeIcon.SIGN_OUT, 14)); // Sign Out
    }

    private FontAwesomeIconView createFontIcon(FontAwesomeIcon iconType, int size) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconType);
        icon.setGlyphSize(size);
        icon.getStyleClass().add("icon");
        return icon;
    }

    private void addListeners() {
        addButtonListener(btnFurniture, StaffMenuOptions.FURNITURE);
        btnSignOut.setOnAction(event -> onSignOut());
    }

    private void addButtonListener(Button button, StaffMenuOptions menuOption) {
        button.setOnAction(event -> {
            clearButtonStyles();
            button.getStyleClass().add("button-selected");
            ViewFactory.getInstance().getStaffSelectedMenuItem().set(menuOption);
        });
    }

    private void clearButtonStyles() {
        btnFurniture.getStyleClass().remove("button-selected");
    }

    private void onSignOut(){
        Stage stage = (Stage) btnFurniture.getScene().getWindow();
        ViewFactory.getInstance().closeStage(stage);
        LoginController.removeCredentials();
        Model.getInstance().emptyData();
        ViewFactory.getInstance().resetStaffViews();
        ViewFactory.getInstance().showLoginWindow();
    }
}
