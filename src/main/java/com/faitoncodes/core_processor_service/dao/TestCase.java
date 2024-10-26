package com.faitoncodes.core_processor_service.dao;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "testcase")
@Table(name = "testcase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exercise_id", nullable = false)
    private Long exerciseId;

    @Column(name = "input", nullable = false)
    private String input;

    @Column(name = "expected_output", nullable = false)
    private String expectedOutput;
}
