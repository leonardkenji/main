package com.ctbjj.main.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ManualCheckInRequest(
        @NotNull(message = "studentId é obrigatório") UUID studentId
) {}
