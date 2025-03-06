package com.hallmanagementsys.hallmanagement;

import com.hallmanagementsys.hallmanagement.model.Model;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Model.getInstance().loadStaticData(() ->
                Platform.runLater(() ->
                        Model.getInstance().getViewFactory().decideWhatToShow()
                )
        );
    }
}
