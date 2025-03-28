package com.hallmanagementsys.hallmanagement.model;

import com.hallmanagementsys.hallmanagement.service.*;
import com.hallmanagementsys.hallmanagement.websocket.MyWebSocketClient;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class Model {
    private static Model model;
    private User user;

    private Model(){
        MyWebSocketClient.getInstance().connect();
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

    public boolean isCurrentUserAdmin(){
        if(user != null){
            return user.isAdmin();
        }

        return false;
    }

    // Load static data into a separate thread
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

                if(isCurrentUserAdmin()){
                    OccupantService.getInstance().fetchOccupants();
                }

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

    // Empty data from the different static lists, maps that are associated to the model classes
    public void emptyData(){
        Furniture.emptyFurniture();
        Occupant.emptyOccupants();
        Room.emptyFurnitureAndOccupants();
    }
}
