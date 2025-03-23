package com.hallmanagementsys.hallmanagement.controller.admin;

import com.hallmanagementsys.hallmanagement.controller.LoginController;
import com.hallmanagementsys.hallmanagement.enums.AdminMenuOptions;
import com.hallmanagementsys.hallmanagement.model.Model;
import com.hallmanagementsys.hallmanagement.viewFactory.ViewFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button btnOccupants, btnFurniture, btnRooms, btnReport,
            btnAccount, btnSignOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        addListeners();
        btnRooms.getStyleClass().add("button-selected");
    }

    private void setIcons() {
        btnRooms.setGraphic(createFontIcon(FontAwesomeIcon.HOME, 18)); // Room Management
        btnFurniture.setGraphic(createFontIcon(FontAwesomeIcon.BED, 18)); // Furniture
        btnOccupants.setGraphic(createFontIcon(FontAwesomeIcon.USERS, 18)); // Occupants
        btnReport.setGraphic(createFontIcon(FontAwesomeIcon.FILE_TEXT, 18)); // Reports
        btnAccount.setGraphic(createFontIcon(FontAwesomeIcon.USER_CIRCLE, 18)); // Account Settings
        btnSignOut.setGraphic(createFontIcon(FontAwesomeIcon.SIGN_OUT, 14)); // Sign Out
    }

    private FontAwesomeIconView createFontIcon(FontAwesomeIcon iconType, int size) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconType);
        icon.setGlyphSize(size);
        icon.getStyleClass().add("icon");
        return icon;
    }

    private void addListeners() {
        addButtonListener(btnOccupants, AdminMenuOptions.OCCUPANTS);
        addButtonListener(btnFurniture, AdminMenuOptions.FURNITURE);
        addButtonListener(btnRooms, AdminMenuOptions.ROOMS);
        addButtonListener(btnReport, AdminMenuOptions.REPORT);
        addButtonListener(btnAccount, AdminMenuOptions.ACCOUNT);
        btnSignOut.setOnAction(event -> onSignOut());
    }

    private void addButtonListener(Button button, AdminMenuOptions menuOption) {
        button.setOnAction(event -> {
            clearButtonStyles();
            button.getStyleClass().add("button-selected");
            ViewFactory.getInstance().getAdminSelectedMenuItem().set(menuOption);
        });
    }

    private void clearButtonStyles() {
        btnOccupants.getStyleClass().remove("button-selected");
        btnFurniture.getStyleClass().remove("button-selected");
        btnRooms.getStyleClass().remove("button-selected");
        btnReport.getStyleClass().remove("button-selected");
        btnAccount.getStyleClass().remove("button-selected");
    }

    private void onSignOut(){
        Stage stage = (Stage) btnOccupants.getScene().getWindow();
        ViewFactory.getInstance().closeStage(stage);
        LoginController.removeCredentials();
        Model.getInstance().emptyData();
        ViewFactory.getInstance().resetAdminViews();
        ViewFactory.getInstance().showLoginWindow();
    }
}
