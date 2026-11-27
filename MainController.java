package com.university;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Phase 9: Main Navigation Controller (Week 6 FXML, Week 7 Custom Event Listener)
 */
public class MainController {

    @FXML private BorderPane rootPane;
    @FXML private StackPane centerPane;
    @FXML private Label heartbeatLabel;
    @FXML private Label eventLogLabel;

    @FXML
    public void initialize() {
        showDashboard();

        // Listen for custom StudentSavedEvent bubbling up from StudentView (Week 7 LO4)
        rootPane.addEventHandler(StudentSavedEvent.STUDENT_SAVED, event -> {
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            eventLogLabel.setText("Last Event: Student " + event.getStudentRegNumber() + " added at " + time);
        });
    }

    public void updateHeartbeat(String message) {
        heartbeatLabel.setText(message);
    }

    @FXML
    private void showDashboard() {
        VBox dashboard = new VBox(12);
        dashboard.setStyle("-fx-alignment: center; -fx-padding: 30;");
        Label title = new Label("Welcome to the University Management System");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label subtitle = new Label("Event-Driven Architecture • JavaFX 21 • MySQL • Async Tasks");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        dashboard.getChildren().addAll(title, subtitle);
        centerPane.getChildren().setAll(dashboard);
    }

    @FXML
    private void showStudents() {
        try {
            Parent studentView = FXMLLoader.load(getClass().getResource("StudentView.fxml"));
            centerPane.getChildren().setAll(studentView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
