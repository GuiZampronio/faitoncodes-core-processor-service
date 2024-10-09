package com.faitoncodes.core_processor_service.dao;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "class")
@Table(name = "class")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long classId;

    @Column(name = "class_name", nullable = false, length = 100)
    private String className;

    @Column(name = "announcement")
    private String announcement;

    @Column(name = "class_code", nullable = false, unique = true, length = 6)
    private String classCode;

    @Column(name = "teacher_id", nullable = false, unique = true)
    private Long teacherId;

}
