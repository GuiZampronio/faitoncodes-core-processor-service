package com.faitoncodes.core_processor_service.controller;

import com.faitoncodes.core_processor_service.dao.AgrupamentoUserClass;
import com.faitoncodes.core_processor_service.dao.Class;
import com.faitoncodes.core_processor_service.dto.classes.ClassRegisterDTO;
import com.faitoncodes.core_processor_service.dto.classes.ClassesInfoDTO;
import com.faitoncodes.core_processor_service.service.ClassesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/class")
@Log4j2
@SecurityRequirement(name = "bearerAuth")
public class ClassesController {
    @Autowired
    ClassesService classesService;

    @PostMapping("/createClass")
    public ResponseEntity<Class> createNewClass(@RequestBody ClassRegisterDTO classRegisterDTO){
        try{
            Class newClass = classesService.createClass(classRegisterDTO);
            return ResponseEntity.ok(newClass);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao criar nova turma!", e);
        }
    }

    @PostMapping("/linkClass")
    public ResponseEntity<AgrupamentoUserClass> linkUserToClass(@RequestParam Long userId, @RequestParam String classCode){

        try{
            AgrupamentoUserClass newAgrupamento = classesService.linkUserToClass(userId, classCode);
            return ResponseEntity.ok(newAgrupamento);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falhou");
        }

    }

    @GetMapping("/getClasses")
    public ResponseEntity<List<ClassesInfoDTO>> getAllClassesFromUser(@RequestParam Long userId){
        List<ClassesInfoDTO> classesInfos = classesService.getClassesFromUser(userId);
        return ResponseEntity.ok(classesInfos);
    }


}
