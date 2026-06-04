package com.ctbjj.main.dto.response;

import com.ctbjj.main.entity.Student;
import com.ctbjj.main.enums.Belt;
import com.ctbjj.main.enums.EnrollmentStatus;
import com.ctbjj.main.enums.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record StudentResponse(
        UUID id,
        UUID userId,
        String name,
        String email,
        UserRole role,
        String phone,
        LocalDate birthDate,
        Belt belt,
        int stripes,
        EnrollmentStatus enrollmentStatus,
        UUID qrCodeToken,
        LocalDate enrollmentDate,
        String notes,
        LocalDateTime createdAt
) {
    public static StudentResponse from(Student s) {
        return new StudentResponse(
                s.getId(),
                s.getUser().getId(),
                s.getUser().getName(),
                s.getUser().getEmail(),
                s.getUser().getRole(),
                s.getPhone(),
                s.getBirthDate(),
                s.getBelt(),
                s.getStripes(),
                s.getEnrollmentStatus(),
                s.getQrCodeToken(),
                s.getEnrollmentDate(),
                s.getNotes(),
                s.getUser().getCreatedAt()
        );
    }
}
