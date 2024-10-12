package com.faitoncodes.core_processor_service.service;

import com.faitoncodes.core_processor_service.dao.AgrupamentoUserClass;
import com.faitoncodes.core_processor_service.dao.Class;
import com.faitoncodes.core_processor_service.dto.classes.ClassRegisterDTO;
import com.faitoncodes.core_processor_service.dto.classes.ClassesInfoDTO;
import com.faitoncodes.core_processor_service.repository.AgrupamentoUserClassRepository;
import com.faitoncodes.core_processor_service.repository.ClassRepository;
import com.faitoncodes.core_processor_service.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ClassesService {

    private static final String MIX_STRING = "abcdefghijklmnopqrstuvwxyz1234567890";

    private static final int MIX_STRING_LENGTH = MIX_STRING.length();

    @Autowired
    ClassRepository classRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AgrupamentoUserClassRepository agrupamentoRepository;


    public Class createClass(ClassRegisterDTO classRegisterDTO) {
        if(userRepository.getTipoUsuario(classRegisterDTO.getTeacher_id()) != 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de usuario informado não é um professor.");
        }

        String uniqueClassID = uniqueIDGenerate(System.currentTimeMillis());

        while (classRepository.existsByClassCode(uniqueClassID)) {
            uniqueClassID = uniqueIDGenerate(System.currentTimeMillis());
        }

        log.info("UniqueClassID: {}", uniqueClassID);

        Class newClass = Class.builder()
                .className(classRegisterDTO.getClassName())
                .announcement(classRegisterDTO.getAnnouncement() != null ? classRegisterDTO.getAnnouncement() : null)
                .classCode(uniqueClassID)
                .teacherId(classRegisterDTO.getTeacher_id())
                .build();

        Class newClassEntity = classRepository.save(newClass);

        AgrupamentoUserClass newAgrupamento = AgrupamentoUserClass.builder()
                .userId(classRegisterDTO.getTeacher_id())
                .classId(newClassEntity.getClassId())
                .build();

        agrupamentoRepository.save(newAgrupamento);
        return newClassEntity;
    }

    private String uniqueIDGenerate(final long base10){
        if (base10 == 0)
            return "0";

        long temp = base10;
        final StringBuilder sb = new StringBuilder();

        while (temp > 0) {
            if(sb.length() == 6) break;
            temp = fromBase10(temp, sb);
        }
        return sb.reverse().toString();
    }

    private Long fromBase10(final long base10, final StringBuilder sb){
        final int rem = (int) (base10 % MIX_STRING_LENGTH);
        sb.append(MIX_STRING.charAt(rem));
        return base10 / MIX_STRING_LENGTH;
    }

    public AgrupamentoUserClass linkUserToClass(Long userId, String classCode) {
        Optional<Class> classFound = classRepository.findByClassCode(classCode);

        if(classFound.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Codigo de classe inexistente.");
        }
        if (!userRepository.existsByUserId(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID inexistente.");
        }

        AgrupamentoUserClass agrupamentoUserClass = AgrupamentoUserClass.builder()
                .classId(classFound.get().getClassId())
                .userId(userId)
                .build();

        return agrupamentoRepository.save(agrupamentoUserClass);
    }

    public List<ClassesInfoDTO> getClassesFromUser(Long userId) {
        List<ClassesInfoDTO> listClassesInfo = new ArrayList<>();

        List<AgrupamentoUserClass> listAgrupamento = agrupamentoRepository.findByUserId(userId);

        listAgrupamento.forEach(agrupamento -> {
            Optional<Class> optionalClassFromUser = classRepository.findById(agrupamento.getClassId());
            Class classFromUser;
            if(optionalClassFromUser.isPresent()){
                classFromUser = optionalClassFromUser.get();
                ClassesInfoDTO classInfo = ClassesInfoDTO.builder()
                        .classId(classFromUser.getClassId())
                        .className(classFromUser.getClassName())
                        .announcement(classFromUser.getAnnouncement())
                        .classCode(classFromUser.getClassCode())
                        .teacher_id(classFromUser.getTeacherId())
                        .teacherName(userRepository.getTeacherName(classFromUser.getTeacherId()))
                        .build();
                
                listClassesInfo.add(classInfo);
            } else{
                log.info("Class de agrupamento nao existe. Class Id: {}", agrupamento.getClassId());
                agrupamentoRepository.deleteById(agrupamento.getUserClassId());
            }
        });

        return listClassesInfo;
    }
}
