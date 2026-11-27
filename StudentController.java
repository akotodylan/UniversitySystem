package com.university;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Phase 7: The Controller (Week 8 Bindings, Week 9 Tasks, Week 10 LO2 & LO4)
 */
public class StudentController {

    @FXML private TextField regField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> colReg;
    @FXML private TableColumn<Student, String> colFirst;
    @FXML private TableColumn<Student, String> colLast;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private Label statusLabel;
    @FXML private Button saveButton;
    @FXML private ProgressIndicator loadingSpinner;

    private final ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Bind TableColumns to Observable Student Properties (Week 8)
        colReg.setCellValueFactory(cellData -> cellData.getValue().regNumberProperty());
        colFirst.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        colLast.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        studentTable.setItems(studentList);

        // 2. Reactive Form Validation Binding (Week 8)
        // Save button is automatically disabled if any required field is empty
        saveButton.disableProperty().bind(
                regField.textProperty().isEmpty()
                        .or(firstNameField.textProperty().isEmpty())
                        .or(lastNameField.textProperty().isEmpty())
        );

        // 3. Initial background load (Week 9 & 10)
        loadStudents();
    }

    @FXML
    private void handleSaveStudent() {
        String reg = regField.getText().trim();
        String fName = firstNameField.getText().trim();
        String lName = lastNameField.getText().trim();

        Task<Void> saveTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String sql = "INSERT INTO students (registration_number, first_name, last_name, email) VALUES (?, ?, ?, ?)";
                try (Connection conn = DatabaseUtil.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, reg);
                    stmt.setString(2, fName);
                    stmt.setString(3, lName);
                    stmt.setString(4, reg.toLowerCase() + "@university.edu");
                    stmt.executeUpdate();
                }
                return null;
            }
        };

        loadingSpinner.visibleProperty().bind(saveTask.runningProperty());

        saveTask.setOnSucceeded(e -> {
            statusLabel.setText("Student " + reg + " saved successfully!");
            regField.clear();
            firstNameField.clear();
            lastNameField.clear();

            // Fire Custom Event (Week 7 LO4) so parent containers can react
            saveButton.fireEvent(new StudentSavedEvent(reg));
            loadStudents();
        });

        saveTask.setOnFailed(e -> {
            Throwable ex = saveTask.getException();
            statusLabel.setText("Error saving student: " + (ex != null ? ex.getMessage() : "Unknown error"));
        });

        new Thread(saveTask).start();
    }

    @FXML
    private void handleRefresh() {
        loadStudents();
    }

    private void loadStudents() {
        LoadStudentsTask task = new LoadStudentsTask();
        loadingSpinner.visibleProperty().bind(task.runningProperty());

        task.setOnSucceeded(e -> {
            studentList.setAll(task.getValue());
            statusLabel.setText("Loaded " + studentList.size() + " students from database.");
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            statusLabel.setText("DB Load Failed: " + (ex != null ? ex.getMessage() : "Check MySQL connection"));
        });

        new Thread(task).start();
    }
}
