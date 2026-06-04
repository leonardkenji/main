package com.ctbjj.main.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateClassTypeRequest(
        @NotBlank(message = "Nome é obrigatório") String name,
        String description,
        String colorHex
) {}
