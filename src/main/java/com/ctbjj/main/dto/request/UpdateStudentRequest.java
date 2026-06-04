package com.ctbjj.main.dto.request;

import com.ctbjj.main.enums.Belt;
import com.ctbjj.main.enums.EnrollmentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateStudentRequest(
        @NotBlank(message = "Nome é obrigatório") String name,
        @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido") String email,
        String phone,
        LocalDate birthDate,
        Belt belt,
        int stripes,
        @NotNull(message = "Status de matrícula é obrigatório") EnrollmentStatus enrollmentStatus,
        LocalDate enrollmentDate,
        String notes
) {}
