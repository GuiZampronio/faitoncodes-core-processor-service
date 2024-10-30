package com.faitoncodes.core_processor_service.repository;

import com.faitoncodes.core_processor_service.dao.TestCase;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends CrudRepository<TestCase, Long> {
    @Query(value = "DELETE FROM public.testcase tc WHERE tc.exercise_id = :id", nativeQuery = true)
    public void deleteAllTestCasesFromExercise(@Param("id")Long exerciseId);

    public List<TestCase> findByExerciseId(Long exerciseId);

    public boolean existsByExerciseId(Long exerciseId);

    @Transactional
    public void deleteByExerciseId(Long exerciseId);
}
