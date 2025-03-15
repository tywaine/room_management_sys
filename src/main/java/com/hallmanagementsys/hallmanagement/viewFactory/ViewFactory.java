package com.hallmanagementsys.hallmanagement.viewFactory;

import com.hallmanagementsys.hallmanagement.controller.EditFurnitureController;
import com.hallmanagementsys.hallmanagement.controller.LoginController;
import com.hallmanagementsys.hallmanagement.controller.admin.AdminController;
import com.hallmanagementsys.hallmanagement.controller.staff.StaffController;
import com.hallmanagementsys.hallmanagement.enums.AdminMenuOptions;
import com.hallmanagementsys.hallmanagement.enums.StaffMenuOptions;
import com.hallmanagementsys.hallmanagement.model.Furniture;
import com.hallmanagementsys.hallmanagement.model.Model;
import com.hallmanagementsys.hallmanagement.service.FurnitureService;
import com.hallmanagementsys.hallmanagement.service.UserService;
import com.hallmanagementsys.hallmanagement.util.MyAlert;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class ViewFactory {
    //Admin Views
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
    private AnchorPane displayRoomsView;
    private AnchorPane generateReportView;
    private AnchorPane manageOccupantsView;

    //Staff View
    private final ObjectProperty<StaffMenuOptions> staffSelectedMenuItem;

    //Shared Views
    private AnchorPane accountView;
    private AnchorPane viewFurnitureView;

    public ViewFactory(){
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
        this.staffSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    public ObjectProperty<StaffMenuOptions> getStaffSelectedMenuItem(){
        return staffSelectedMenuItem;
    }

    // Admin View getters
    public AnchorPane getDisplayRoomsView() {
        if(displayRoomsView == null){
            try{
                displayRoomsView = new FXMLLoader(getClass().getResource("/view/admin/displayRoomsView.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return displayRoomsView;
    }

    public AnchorPane getGenerateReportView() {
        if(generateReportView == null){
            try{
                generateReportView = new FXMLLoader(getClass().getResource("/view/admin/generateReportView.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return generateReportView;
    }

    public AnchorPane getManageOccupantsView() {
        if(manageOccupantsView == null){
            try{
                manageOccupantsView = new FXMLLoader(getClass().getResource("/view/admin/manageOccupantsView.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return manageOccupantsView;
    }

    // Shared View getters
    public AnchorPane getAccountView() {
        if(accountView == null){
            try{
                accountView = new FXMLLoader(getClass().getResource("/view/account.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return accountView;
    }

    public AnchorPane getViewFurnitureView() {
        if(viewFurnitureView == null){
            try{
                viewFurnitureView = new FXMLLoader(getClass().getResource("/view/viewFurnitureView.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return viewFurnitureView;
    }

    public void resetAdminViews(){
        displayRoomsView = null;
        viewFurnitureView = null;
        manageOccupantsView = null;
        generateReportView = null;
        accountView = null;
    }

    public void resetStaffViews(){
        displayRoomsView = null;
        viewFurnitureView = null;
        manageOccupantsView = null;
        generateReportView = null;
        accountView = null;
    }

    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
    }

    public void showStaffWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/staff/staff.fxml"));
        StaffController staffController = new StaffController();
        loader.setController(staffController);
        createStage(loader);
    }

    public void showSignUpWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/signUpView.fxml"));
        createStage(loader, "Sign Up");
        MyAlert.showAlert(Alert.AlertType.INFORMATION, "First time creating a User account",
                """
                        Since there is no user registered in the system you will create an Account.
                        Make sure to remember the details.
                        You will need them to login. THERE IS NO RECOVERY SYSTEM (As of now)
                        """);
    }

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        createStage(loader);
    }

    public void decideWhatToShow(){
        if(UserService.getInstance().doesAdminExist()){
            loginWindow();
        }
        else{
            LoginController.removeCredentials();
            showSignUpWindow();
        }
    }

    public void loginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        LoginController loginController;

        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Room Inventory Management");
            stage.setResizable(false);

            stage.setOnCloseRequest(event -> {
                onExit();
            });

            loginController = loader.getController();
            loginController.shouldShow();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showEditFurnitureDialog(Furniture furniture) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editFurnitureView.fxml"));
            Scene scene = new Scene(loader.load());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Furniture");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            EditFurnitureController controller = loader.getController();
            controller.setFurniture(furniture);

            dialogStage.showAndWait();
            return controller.isSaveClicked();
        }
        catch (IOException e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error",
                    "Unexpected error occurred. Please try again later.");
            return false;
        }
    }

    private void createStage(FXMLLoader loader){
        try{
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Room Inventory Management");
            stage.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

            stage.setOnCloseRequest(event -> {
                onExit();
            });

            stage.show();
            stage.setResizable(false);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createStage(FXMLLoader loader, String title){
        try{
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);

            stage.setOnCloseRequest(event -> {
                onExit();
            });

            stage.show();
            stage.setResizable(false);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void onExit() {
        System.out.println("Application is closing...");
        Model.getInstance().getWebSocketClient().disconnect();
    }

    public void closeStage(Stage stage){
        stage.close();
    }
}