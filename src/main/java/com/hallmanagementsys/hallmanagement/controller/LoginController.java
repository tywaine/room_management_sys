package com.hallmanagementsys.hallmanagement.controller;

import com.hallmanagementsys.hallmanagement.enums.PreferenceKeys;
import com.hallmanagementsys.hallmanagement.model.*;
import com.hallmanagementsys.hallmanagement.service.UserService;
import com.hallmanagementsys.hallmanagement.util.MyAlert;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LoginController implements Initializable {
    public Button btnLogin, btnVisible;
    public TextField txtUsername, txtVisiblePassword;
    public CheckBox chkSaveCredentials;
    public Label lblError;
    public PasswordField pwdPassword;
    public ProgressBar progressBar;

    private final FontAwesomeIconView visibleIcon = createFontIcon(FontAwesomeIcon.EYE);
    private final FontAwesomeIconView invisibleIcon = createFontIcon(FontAwesomeIcon.EYE_SLASH);
    private static final Preferences preferences = Preferences.userNodeForPackage(LoginController.class);

    private final UserService userService = UserService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtVisiblePassword.setManaged(false);
        txtVisiblePassword.setVisible(false);
        txtVisiblePassword.textProperty().bindBidirectional(pwdPassword.textProperty());
        btnVisible.setOnAction(event -> togglePasswordVisibility());
        btnVisible.setGraphic(visibleIcon);
        progressBar.setVisible(false);
    }

    private FontAwesomeIconView createFontIcon(FontAwesomeIcon iconType) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconType);
        icon.setGlyphSize(18); // Adjust size as needed
        icon.getStyleClass().add("icon");
        return icon;
    }

    public void login(){
        String username = txtUsername.getText();
        String password = (pwdPassword.isVisible()) ? pwdPassword.getText() : txtVisiblePassword.getText();

        if(username.isEmpty() || password.isEmpty()){
            lblError.setText("Text field/s cannot be empty");
        }

        User user = userService.sendLoginRequest(username, password);

        if(user != null){
            Model.getInstance().setCurrentUser(user);
            lblError.setText("");
            // Show ProgressBar
            progressBar.setVisible(true);

            // Load data asynchronously
            Model.getInstance().loadDataAsync(progress -> Platform.runLater(() -> progressBar.setProgress(progress)), () -> {
                // Hide ProgressBar & Show Dashboard
                Platform.runLater(() -> {
                    progressBar.setVisible(false);
                    Stage stage = (Stage) lblError.getScene().getWindow();
                    showCorrectWindow(username, password, stage, user.getRole());
                });
            });
        }
        else{
            lblError.setText("Invalid username or password");
        }
    }

    private void togglePasswordVisibility() {
        if (txtVisiblePassword.isVisible()) {
            btnVisible.setGraphic(visibleIcon);

            txtVisiblePassword.setManaged(false);
            txtVisiblePassword.setVisible(false);
            pwdPassword.setManaged(true);
            pwdPassword.setVisible(true);
        }
        else {
            btnVisible.setGraphic(invisibleIcon);

            txtVisiblePassword.setManaged(true);
            txtVisiblePassword.setVisible(true);
            pwdPassword.setManaged(false);
            pwdPassword.setVisible(false);
        }
    }

    public static void removeCredentials(){
        preferences.put(PreferenceKeys.USER_USERNAME.getKey(), "");
        preferences.put(PreferenceKeys.USER_PASSWORD.getKey(), "");
    }

    public void shouldShow() {
        String username = preferences.get(PreferenceKeys.USER_USERNAME.getKey(), "");
        String password = preferences.get(PreferenceKeys.USER_PASSWORD.getKey(), "");
        Stage stage = (Stage) lblError.getScene().getWindow();

        User user = userService.sendLoginRequest(username, password);

        if(user != null){
            Model.getInstance().setCurrentUser(user);

            Model.getInstance().loadDataAsync(progress -> Platform.runLater(() -> progressBar.setProgress(progress)), () -> {
                Platform.runLater(() -> {
                    showCorrectWindow(username, password, stage, user.getRole());
                });
            });
        }
        else{
            removeCredentials();
            stage.show();
        }
    }

    private void showCorrectWindow(String email, String password, Stage stage, String role) {
        Model.getInstance().getViewFactory().closeStage(stage);

        if ("ADMIN".equals(role)) {
            saveCredentials(email, password);
            Model.getInstance().getViewFactory().showAdminWindow();
        }
        else if ("STAFF".equals(role)) {
            saveCredentials(email, password);
            Model.getInstance().getViewFactory().showStaffWindow();
        }
        else {
            MyAlert.showAlert(Alert.AlertType.ERROR, "There is no User other than Admin and Staff", "How did this happen?????");
            System.exit(1);
        }


    }

    private void saveCredentials(String email, String password) {
        if (chkSaveCredentials.isSelected()) {
            preferences.put(PreferenceKeys.USER_USERNAME.getKey(), email);
            preferences.put(PreferenceKeys.USER_PASSWORD.getKey(), password);
        }
    }
}
