package com.ctbjj.main.dto.response;

import com.ctbjj.main.entity.ClassType;

import java.util.UUID;

public record ClassTypeResponse(
        UUID id,
        String name,
        String description,
        String colorHex
) {
    public static ClassTypeResponse from(ClassType ct) {
        return new ClassTypeResponse(ct.getId(), ct.getName(), ct.getDescription(), ct.getColorHex());
    }
}
