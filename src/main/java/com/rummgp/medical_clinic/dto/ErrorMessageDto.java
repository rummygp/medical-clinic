package com.rummgp.medical_clinic.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ErrorMessageDto {
    private LocalDate createdAt;
    private String message;
    private int status;
    private String error;
}
