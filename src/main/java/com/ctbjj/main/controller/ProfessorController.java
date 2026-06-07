package com.ctbjj.main.controller;

import com.ctbjj.main.dto.request.CreateProfessorRequest;
import com.ctbjj.main.dto.request.UpdateProfessorRequest;
import com.ctbjj.main.dto.response.ProfessorResponse;
import com.ctbjj.main.service.ProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/professors")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<ProfessorResponse>> findAll() {
        return ResponseEntity.ok(professorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(professorService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfessorResponse> create(@Valid @RequestBody CreateProfessorRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.create(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfessorResponse> update(@PathVariable UUID id,
                                                     @Valid @RequestBody UpdateProfessorRequest req) {
        return ResponseEntity.ok(professorService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        professorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
