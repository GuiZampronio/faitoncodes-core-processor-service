package com.faitoncodes.core_processor_service.dto.exercises;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Builder
public class ExerciseInfoDTO {
    private Long id;

    private String title;

    private String description;

    private String testCases;

    private ZonedDateTime dueDate;

    private LocalDateTime updatedDate;

}
