package com.faitoncodes.core_processor_service.dto.classes;

import lombok.Data;

@Data
public class ClassRegisterDTO {
    private String className;

    private String announcement;

    private Long teacher_id;
}
