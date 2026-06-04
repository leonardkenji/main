package com.ctbjj.main.dto.response;

import com.ctbjj.main.entity.Schedule;
import com.ctbjj.main.enums.Weekday;

import java.time.LocalTime;
import java.util.UUID;

public record ScheduleResponse(
        UUID id,
        ProfessorResponse professor,
        ClassTypeResponse classType,
        Weekday weekday,
        LocalTime startTime,
        LocalTime endTime,
        int maxCapacity,
        boolean active
) {
    public static ScheduleResponse from(Schedule s) {
        return new ScheduleResponse(
                s.getId(),
                ProfessorResponse.from(s.getProfessor()),
                ClassTypeResponse.from(s.getClassType()),
                s.getWeekday(),
                s.getStartTime(),
                s.getEndTime(),
                s.getMaxCapacity(),
                s.isActive()
        );
    }
}
