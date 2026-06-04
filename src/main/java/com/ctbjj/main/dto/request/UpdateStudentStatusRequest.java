package com.ctbjj.main.dto.request;

import com.ctbjj.main.enums.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStudentStatusRequest(
        @NotNull(message = "Status é obrigatório") EnrollmentStatus status
) {}
