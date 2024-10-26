package com.faitoncodes.core_processor_service.repository;

import com.faitoncodes.core_processor_service.dao.Class;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends CrudRepository<Class, Long> {
    boolean existsByClassCode(String classCode);

    boolean existsByTeacherId(Long teacherId);

    Optional<Class> findByClassCode(String classCode);
}
