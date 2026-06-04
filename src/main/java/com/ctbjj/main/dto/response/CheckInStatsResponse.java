package com.ctbjj.main.dto.response;

import java.util.List;
import java.util.UUID;

public record CheckInStatsResponse(
        int totalClasses,
        int totalMinutes,
        List<ClassTypeStat> byClassType
) {
    public record ClassTypeStat(
            UUID classTypeId,
            String classTypeName,
            String colorHex,
            int totalClasses,
            int totalMinutes
    ) {}
}
