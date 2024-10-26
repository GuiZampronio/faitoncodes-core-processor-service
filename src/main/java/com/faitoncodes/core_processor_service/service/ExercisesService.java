package com.faitoncodes.core_processor_service.service;

import com.faitoncodes.core_processor_service.dao.Class;
import com.faitoncodes.core_processor_service.dao.Exercise;
import com.faitoncodes.core_processor_service.dao.TestCase;
import com.faitoncodes.core_processor_service.dto.exercises.ExerciseDTO;
import com.faitoncodes.core_processor_service.dto.exercises.ExerciseInfoDTO;
import com.faitoncodes.core_processor_service.repository.ClassRepository;
import com.faitoncodes.core_processor_service.repository.ExerciseRepository;
import com.faitoncodes.core_processor_service.repository.TestCaseRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.Response;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ExercisesService {
    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    ClassRepository classRepository;

    DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    TestCaseRepository testCaseRepository;

    public Exercise createExercise(ExerciseDTO exerciseDTO) {
        if(!classRepository.existsById(exerciseDTO.getClassId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da classe não existe para criar o exercício");
        }
        if(!classRepository.existsByTeacherId(exerciseDTO.getTeacherId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de professor informado não é dono da turma.");
        }

        ZonedDateTime dueTimeExercise = LocalDateTime.parse(exerciseDTO.getDueDate(), dataFormatter).atZone(ZoneOffset.UTC);

        Exercise newExercise = Exercise.builder()
                .title(exerciseDTO.getTitle())
                .description(exerciseDTO.getDescription() != null? exerciseDTO.getDescription() : null)
                .dueDate(dueTimeExercise)
                .creationDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .classId(exerciseDTO.getClassId())
                .build();

        try{
            newExercise = exerciseRepository.save(newExercise);
            List<TestCase> extractedTestCases = extractListOfTestCases(exerciseDTO.getTestCases(), newExercise.getId());

            if(!extractedTestCases.isEmpty()){
                extractedTestCases.forEach(testCase -> {
                    testCaseRepository.save(testCase);
                });
            }

            return newExercise;
        }catch (Exception e){
            log.error(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao salvar novo exercicio.");
        }


    }

    public List<ExerciseInfoDTO> getExercisesFromClass(Long classId) {
        List<ExerciseInfoDTO> exerciseInfoDTOList = new ArrayList<>();
        List<Exercise> exercisesList = exerciseRepository.findByClassId(classId);

        exercisesList.forEach(exercise -> {
            if(exercise.getDeletionDate() == null){
                exerciseInfoDTOList.add(ExerciseInfoDTO.builder()
                        .id(exercise.getId())
                        .title(exercise.getTitle())
                        .description(exercise.getDescription())
                        .testCases(getTestCasesFromExercise(exercise.getId()))
                        .dueDate(exercise.getDueDate())
                        .updatedDate(exercise.getUpdatedDate())
                        .build()
                );
            }
        });

        return exerciseInfoDTOList;
    }

    private List<TestCase> extractListOfTestCases(String testCasesString, Long exerciseId) {
        if(testCasesString.isBlank()){
            return List.of();
        }

        // TODO Validar se tiver algo de errado com a string, pensar o que deve ser feito nesse caso, parar todo o processamento ou somente ignorar o que estiver errado.
        List<String> testCaseStringList = Arrays.asList(testCasesString.split("/"));
        List<TestCase> listTestCases = new ArrayList<>();
        testCaseStringList.forEach(testCase -> {
            String[] inputAndOutput = testCase.split(";");
            TestCase newTestCase = TestCase.builder()
                    .exerciseId(exerciseId)
                    .input(StringUtils.deleteWhitespace(inputAndOutput[0]))
                    .expectedOutput(StringUtils.deleteWhitespace(inputAndOutput[1]))
                    .build();
            listTestCases.add(newTestCase);
        });

        return listTestCases;

    }


    public Exercise updateExercise(ExerciseDTO exerciseDTO, Long exerciseId) {
        if(exerciseId == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do exercício não informado");
        }

        Optional<Exercise> actualExercise = exerciseRepository.findById(exerciseId);

        if(actualExercise.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exercício não encontrado.");
        }
        if(!classRepository.existsById(exerciseDTO.getClassId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da classe não existe para criar o exercício");
        }
        if(!classRepository.existsByTeacherId(exerciseDTO.getTeacherId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de professor informado não é dono da turma.");
        }

        ModelMapper modelMapper = new ModelMapper();
        Exercise updatedExercise = actualExercise.get();
        if(exerciseDTO.getDueDate() != null){
            ZonedDateTime dueTimeExercise = LocalDateTime.parse(exerciseDTO.getDueDate(), dataFormatter).atZone(ZoneOffset.UTC);
            updatedExercise.setDueDate(dueTimeExercise);
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(exerciseDTO, updatedExercise);

        try{
            updatedExercise = exerciseRepository.save(updatedExercise);
            testCaseRepository.deleteAllTestCasesFromExercise(exerciseId);

            List<TestCase> extractedTestCases = extractListOfTestCases(exerciseDTO.getTestCases() != null? exerciseDTO.getTestCases() : "", updatedExercise.getId());


            if(!extractedTestCases.isEmpty()){
                extractedTestCases.forEach(testCase -> {
                    testCaseRepository.save(testCase);
                });
            }


            return updatedExercise;
        }catch (Exception e){
            log.error(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao salvar novo exercicio.");
        }

    }

    private String getTestCasesFromExercise(Long exerciseId) {
        List<TestCase> listTestCaseEntity = testCaseRepository.findByExerciseId(exerciseId);
        StringBuilder stringFormatted = new StringBuilder();
        listTestCaseEntity.forEach(testCase -> {
            stringFormatted.append(testCase.getInput());
            stringFormatted.append(";");
            stringFormatted.append(testCase.getExpectedOutput());
            stringFormatted.append("/");
        });

        return stringFormatted.toString();
    }
}
