package com.faitoncodes.core_processor_service.exception;

import com.faitoncodes.core_processor_service.dto.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDTO> handleBadRequestException(ResponseStatusException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getStatusCode(), ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(errorDTO);
    }
}
