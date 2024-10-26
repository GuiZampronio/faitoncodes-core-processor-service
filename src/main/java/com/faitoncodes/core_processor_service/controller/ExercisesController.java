package com.faitoncodes.core_processor_service.controller;

import com.faitoncodes.core_processor_service.dao.Exercise;
import com.faitoncodes.core_processor_service.dto.exercises.ExerciseDTO;
import com.faitoncodes.core_processor_service.dto.exercises.ExerciseInfoDTO;
import com.faitoncodes.core_processor_service.service.ExercisesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercise")
@Log4j2
@SecurityRequirement(name = "bearerAuth")
public class ExercisesController {
    @Autowired
    ExercisesService exercisesService;


    @PostMapping
    public ResponseEntity<Exercise> createExercise(@Valid @RequestBody ExerciseDTO exerciseDTO){
        Exercise exerciseCreated = exercisesService.createExercise(exerciseDTO);
        return ResponseEntity.ok(exerciseCreated);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseInfoDTO>> getExercisesFromClass(@RequestParam Long classId){
        List<ExerciseInfoDTO> listExercises = exercisesService.getExercisesFromClass(classId);
        return ResponseEntity.ok(listExercises);
    }

    @PutMapping
    public ResponseEntity<Exercise> updateExercise(@Valid @RequestBody ExerciseDTO exerciseDTO, @RequestParam Long exerciseId){
        Exercise exerciseUpdated = exercisesService.updateExercise(exerciseDTO, exerciseId);
        return ResponseEntity.ok(exerciseUpdated);
    }

}
