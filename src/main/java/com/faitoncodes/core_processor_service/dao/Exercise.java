package com.faitoncodes.core_processor_service.dao;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity(name = "exercise")
@Table(name = "exercise")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "due_date", nullable = false)
    private ZonedDateTime dueDate;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "deletion_date", nullable = true)
    private LocalDateTime deletionDate;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedDate;

    @Column(name = "class_id", nullable = false)
    private Long classId;
}
