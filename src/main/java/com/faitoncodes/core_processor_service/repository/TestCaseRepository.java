package com.faitoncodes.core_processor_service.repository;

import com.faitoncodes.core_processor_service.dao.TestCase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends CrudRepository<TestCase, Long> {
    @Query(value = "DELETE * FROM public.testcase tc WHERE tc.exercise_id = ?1", nativeQuery = true)
    public void deleteAllTestCasesFromExercise(Long exerciseId);

    public List<TestCase> findByExerciseId(Long exerciseId);
}
