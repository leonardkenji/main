package com.ctbjj.main.controller;

import com.ctbjj.main.dto.request.ManualCheckInRequest;
import com.ctbjj.main.dto.request.QrCheckInRequest;
import com.ctbjj.main.dto.response.CheckInResponse;
import com.ctbjj.main.dto.response.CheckInStatsResponse;
import com.ctbjj.main.service.CheckInService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/checkins")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping("/qr")
    public ResponseEntity<CheckInResponse> checkInByQr(@Valid @RequestBody QrCheckInRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checkInService.checkInByQr(req));
    }

    @PostMapping("/manual")
    public ResponseEntity<CheckInResponse> checkInManual(@Valid @RequestBody ManualCheckInRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checkInService.checkInManual(req));
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<CheckInResponse> checkout(@PathVariable UUID id) {
        return ResponseEntity.ok(checkInService.checkout(id));
    }

    @GetMapping("/student/{studentId}/stats")
    public ResponseEntity<CheckInStatsResponse> getStats(@PathVariable UUID studentId) {
        return ResponseEntity.ok(checkInService.getStats(studentId));
    }

    @GetMapping("/student/{studentId}/history")
    public ResponseEntity<Page<CheckInResponse>> getHistory(
            @PathVariable UUID studentId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(checkInService.getHistory(studentId, pageable));
    }
}
