package com.time.timemanager.tasks;

import com.time.timemanager.authentication.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Column(name = "category")
    private Category category;

    @Column(name = "notification_before_due_date")
    private Boolean notificationBeforeDueDate;

    @Column(name = "notification_overdue")
    private Boolean notificationOverdue;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
