package com.faitoncodes.core_processor_service.repository;

import com.faitoncodes.core_processor_service.dao.Exercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
    List<Exercise> findByClassId(Long classId);
}
