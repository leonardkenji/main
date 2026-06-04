package com.ctbjj.main.dto.response;

import com.ctbjj.main.entity.Professor;
import com.ctbjj.main.enums.Belt;

import java.util.UUID;

public record ProfessorResponse(
        UUID id,
        UUID userId,
        String name,
        String email,
        Belt belt,
        int stripes,
        String bio,
        String photoUrl,
        boolean active,
        int displayOrder
) {
    public static ProfessorResponse from(Professor p) {
        return new ProfessorResponse(
                p.getId(),
                p.getUser().getId(),
                p.getUser().getName(),
                p.getUser().getEmail(),
                p.getBelt(),
                p.getStripes(),
                p.getBio(),
                p.getPhotoUrl(),
                p.isActive(),
                p.getDisplayOrder()
        );
    }
}
