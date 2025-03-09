package com.hallmanagementsys.hallmanagement.model;

import com.hallmanagementsys.hallmanagement.service.*;
import com.hallmanagementsys.hallmanagement.view.ViewFactory;
import com.hallmanagementsys.hallmanagement.websocket.MyWebSocketClient;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;
    private User user;
    private MyWebSocketClient webSocketClient = new MyWebSocketClient();

    private Model(){
        this.viewFactory = new ViewFactory();
        webSocketClient.connect();
    }

    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }

        return model;
    }

    public User getCurrentUser() {
        return user;
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    public ViewFactory getViewFactory(){
        return viewFactory;
    }

    public MyWebSocketClient getWebSocketClient() {
        return webSocketClient;
    }

    public void loadStaticData(Runnable onComplete){
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                double steps = 2.0; // Number of tasks
                updateProgress(0, steps);

                BlockService.getInstance().fetchBlocks();
                updateProgress(1, steps);

                RoomService.getInstance().fetchRooms();
                updateProgress(2, steps);

                return null;
            }
        };

        task.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                if (onComplete != null) {
                    onComplete.run();
                }
            });
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> System.err.println("Data loading failed: " + task.getException().getMessage()));
        });

        new Thread(task).start();
    }

    // Load Data in a Separate Thread
    public void loadDataAsync(Consumer<Double> onProgress, Runnable onComplete) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                double steps = 2.0; // Number of tasks
                updateProgress(0, steps);

                FurnitureService.getInstance().fetchFurniture();
                updateProgress(1, steps);

                OccupantService.getInstance().fetchOccupants();
                updateProgress(2, steps);

                return null;
            }
        };

        task.progressProperty().addListener((obs, oldProgress, newProgress) -> {
            if (onProgress != null) {
                onProgress.accept(newProgress.doubleValue());
            }
        });

        task.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                if (onComplete != null) {
                    onComplete.run();
                }
            });
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> System.err.println("Data loading failed: " + task.getException().getMessage()));
        });

        new Thread(task).start();
    }

    public void emptyData(){
        User.emptyStaff();
        Furniture.emptyFurniture();
        Occupant.emptyOccupants();
    }
}
