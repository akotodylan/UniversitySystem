package com.university;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Phase 8: The Main Application (LO3 - Scheduled Tasks)
 * Launches JavaFX UI and runs a ScheduledExecutorService every 5 seconds.
 */
public class App extends Application {

    private ScheduledExecutorService scheduler;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        Parent root = loader.load();
        MainController mainController = loader.getController();

        primaryStage.setTitle("University Management System (EDA)");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();

        // LO3: Schedule a background heartbeat task every 5 seconds
        scheduler = Executors.newScheduledThreadPool(1);
        Runnable heartbeat = () -> {
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            System.out.println("System Heartbeat: Checking for updates at " + time);

            // Safely update UI from background thread (Week 9)
            Platform.runLater(() ->
                    mainController.updateHeartbeat("System Heartbeat: Active (Last sync " + time + ")")
            );
        };

        scheduler.scheduleAtFixedRate(heartbeat, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
