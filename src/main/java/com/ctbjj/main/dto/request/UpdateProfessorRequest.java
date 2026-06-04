package com.ctbjj.main.dto.request;

import com.ctbjj.main.enums.Belt;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfessorRequest(
        @NotBlank(message = "Nome é obrigatório") String name,
        @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido") String email,
        Belt belt,
        int stripes,
        String bio,
        String photoUrl,
        boolean active,
        int displayOrder
) {}
