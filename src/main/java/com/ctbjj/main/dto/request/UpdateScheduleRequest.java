package com.ctbjj.main.dto.request;

import com.ctbjj.main.enums.Weekday;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

public record UpdateScheduleRequest(
        @NotNull(message = "Professor é obrigatório") UUID professorId,
        @NotNull(message = "Tipo de aula é obrigatório") UUID classTypeId,
        @NotNull(message = "Dia da semana é obrigatório") Weekday weekday,
        @NotNull(message = "Hora de início é obrigatória") LocalTime startTime,
        @NotNull(message = "Hora de término é obrigatória") LocalTime endTime,
        int maxCapacity,
        boolean active
) {}
