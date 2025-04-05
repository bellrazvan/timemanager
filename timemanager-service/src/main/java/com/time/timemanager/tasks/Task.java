package com.time.timemanager.tasks;

import com.time.timemanager.authentication.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(nullable = false)
    private String status; //TODO enum - IN_PROGRESS, COMPLETED

    private String priority; //TODO enum - LOW, MEDIUM, HIGH

    @Column(name = "category")
    private String category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Task() {}

    public Task(String title, String description, LocalDate dueDate, String status, String priority, String category) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
        this.category = category;
    }
}
