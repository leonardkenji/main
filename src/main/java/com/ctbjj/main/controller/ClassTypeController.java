package com.ctbjj.main.controller;

import com.ctbjj.main.dto.request.CreateClassTypeRequest;
import com.ctbjj.main.dto.request.UpdateClassTypeRequest;
import com.ctbjj.main.dto.response.ClassTypeResponse;
import com.ctbjj.main.service.ClassTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/class-types")
@RequiredArgsConstructor
public class ClassTypeController {

    private final ClassTypeService classTypeService;

    @GetMapping
    public ResponseEntity<List<ClassTypeResponse>> findAll() {
        return ResponseEntity.ok(classTypeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassTypeResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(classTypeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClassTypeResponse> create(@Valid @RequestBody CreateClassTypeRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classTypeService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassTypeResponse> update(@PathVariable UUID id,
                                                     @Valid @RequestBody UpdateClassTypeRequest req) {
        return ResponseEntity.ok(classTypeService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        classTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
