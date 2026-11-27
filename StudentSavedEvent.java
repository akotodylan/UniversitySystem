package com.university;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Week 7 (LO4) & Week 10 (LO4): Custom Event for decoupled communication.
 * Fired when a new student is saved to MySQL so other UI components can react.
 */
public class StudentSavedEvent extends Event {

    public static final EventType<StudentSavedEvent> STUDENT_SAVED =
            new EventType<>(Event.ANY, "STUDENT_SAVED");

    private final String studentRegNumber;

    public StudentSavedEvent(String studentRegNumber) {
        super(STUDENT_SAVED);
        this.studentRegNumber = studentRegNumber;
    }

    public String getStudentRegNumber() {
        return studentRegNumber;
    }
}
