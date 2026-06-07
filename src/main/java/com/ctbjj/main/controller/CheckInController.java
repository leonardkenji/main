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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/checkins")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping("/qr")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR', 'STUDENT')")
    public ResponseEntity<CheckInResponse> checkInByQr(@Valid @RequestBody QrCheckInRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checkInService.checkInByQr(req));
    }

    @PostMapping("/manual")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<CheckInResponse> checkInManual(@Valid @RequestBody ManualCheckInRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checkInService.checkInManual(req));
    }

    @PostMapping("/{id}/checkout")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<CheckInResponse> checkout(@PathVariable UUID id) {
        return ResponseEntity.ok(checkInService.checkout(id));
    }

    @GetMapping("/student/{studentId}/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR', 'STUDENT')")
    public ResponseEntity<CheckInStatsResponse> getStats(@PathVariable UUID studentId) {
        return ResponseEntity.ok(checkInService.getStats(studentId));
    }

    @GetMapping("/student/{studentId}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR', 'STUDENT')")
    public ResponseEntity<Page<CheckInResponse>> getHistory(
            @PathVariable UUID studentId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(checkInService.getHistory(studentId, pageable));
    }
}
