package com.ctbjj.main.controller;

import com.ctbjj.main.dto.request.CreateScheduleRequest;
import com.ctbjj.main.dto.request.UpdateScheduleRequest;
import com.ctbjj.main.dto.response.ScheduleResponse;
import com.ctbjj.main.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findAll() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    @GetMapping("/today")
    public ResponseEntity<List<ScheduleResponse>> findToday() {
        return ResponseEntity.ok(scheduleService.findToday());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(scheduleService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<ScheduleResponse> create(@Valid @RequestBody CreateScheduleRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.create(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<ScheduleResponse> update(@PathVariable UUID id,
                                                    @Valid @RequestBody UpdateScheduleRequest req) {
        return ResponseEntity.ok(scheduleService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
