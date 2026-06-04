package com.ctbjj.main.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record QrCheckInRequest(
        @NotNull(message = "Token é obrigatório") UUID token
) {}
