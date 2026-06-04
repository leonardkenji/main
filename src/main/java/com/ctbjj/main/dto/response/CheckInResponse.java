package com.ctbjj.main.dto.response;

import com.ctbjj.main.entity.CheckIn;
import com.ctbjj.main.enums.CheckInMethod;

import java.time.LocalDateTime;
import java.util.UUID;

public record CheckInResponse(
        UUID id,
        UUID studentId,
        String studentName,
        UUID scheduleId,
        String classTypeName,
        CheckInMethod checkInMethod,
        LocalDateTime checkedInAt,
        LocalDateTime checkedOutAt,
        Integer durationMinutes,
        boolean adminOverride
) {
    public static CheckInResponse from(CheckIn c) {
        UUID scheduleId = c.getSchedule() != null ? c.getSchedule().getId() : null;
        String classTypeName = c.getSchedule() != null ? c.getSchedule().getClassType().getName() : null;
        return new CheckInResponse(
                c.getId(),
                c.getStudent().getId(),
                c.getStudent().getUser().getName(),
                scheduleId,
                classTypeName,
                c.getCheckInMethod(),
                c.getCheckedInAt(),
                c.getCheckedOutAt(),
                c.getDurationMinutes(),
                c.isAdminOverride()
        );
    }
}
