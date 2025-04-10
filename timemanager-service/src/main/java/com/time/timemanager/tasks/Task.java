package com.time.timemanager.tasks;

import com.time.timemanager.authentication.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "status", nullable = false)
    private String status; //TODO enum - IN_PROGRESS, COMPLETED

    @Column(name = "priority")
    private String priority; //TODO enum - LOW, MEDIUM, HIGH

    @Column(name = "category")
    private String category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
