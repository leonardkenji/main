package com.ctbjj.main.dto.response;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID id,
        String name,
        String email,
        String role
) {}
