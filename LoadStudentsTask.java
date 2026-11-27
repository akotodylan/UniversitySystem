package com.university;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Phase 5: Background Database Task (Week 9 & Week 10 LO2 - Producer-Consumer Pattern)
 * Fetches students off the JavaFX Application Thread so the UI never freezes.
 */
public class LoadStudentsTask extends Task<ObservableList<Student>> {

    @Override
    protected ObservableList<Student> call() throws Exception {
        updateMessage("Connecting to MySQL...");
        ObservableList<Student> students = FXCollections.observableArrayList();
        String sql = "SELECT student_id, registration_number, first_name, last_name, email FROM students";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            updateMessage("Loading student records...");
            while (rs.next()) {
                if (isCancelled()) {
                    break;
                }
                Student student = new Student(
                        rs.getInt("student_id"),
                        rs.getString("registration_number"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email")
                );
                students.add(student);
            }
        }
        updateMessage("Loaded " + students.size() + " students.");
        return students;
    }
}
