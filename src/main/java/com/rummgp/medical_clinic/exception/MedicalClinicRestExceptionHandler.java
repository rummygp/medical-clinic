package com.rummgp.medical_clinic.exception;

import com.rummgp.medical_clinic.dto.ErrorMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class MedicalClinicRestExceptionHandler {

    @ExceptionHandler(value = ClinicException.class)
    protected ResponseEntity<ErrorMessageDto> handleMedicalClinicException(ClinicException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(
                ErrorMessageDto.builder()
                        .createdAt(LocalDate.now())
                        .message(ex.getMessage())
                        .error(ex.getHttpStatus().getReasonPhrase())
                        .status(ex.getHttpStatus().value())
                        .build()
        );
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorMessageDto> handleGlobalException(Exception ex) {
        return ResponseEntity.status(500).body(
                ErrorMessageDto.builder()
                        .createdAt(LocalDate.now())
                        .message(ex.getMessage())
                        .error("Unknown Error")
                        .status(500)
                        .build()
        );
    }
}
