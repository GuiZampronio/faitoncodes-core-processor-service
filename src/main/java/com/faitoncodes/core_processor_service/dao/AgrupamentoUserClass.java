package com.faitoncodes.core_processor_service.dao;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "agrupamento_user_class")
@Table(name = "agrupamento_user_class")
@Getter
@Setter
@Builder
public class AgrupamentoUserClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_class_id")
    private Long userClassId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "class_id", nullable = false)
    private Long classId;
}

