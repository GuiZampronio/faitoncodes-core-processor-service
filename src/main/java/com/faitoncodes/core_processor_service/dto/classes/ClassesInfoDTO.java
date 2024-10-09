package com.faitoncodes.core_processor_service.dto.classes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassesInfoDTO {

    private Long classId;

    private String className;

    private String announcement;

    private String classCode;

    private Long teacher_id;

    private String teacherName;
}
