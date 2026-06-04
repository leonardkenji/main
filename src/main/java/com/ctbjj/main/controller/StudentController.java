package com.ctbjj.main.controller;

import com.ctbjj.main.dto.request.CreateStudentRequest;
import com.ctbjj.main.dto.request.UpdateStudentRequest;
import com.ctbjj.main.dto.request.UpdateStudentStatusRequest;
import com.ctbjj.main.dto.response.StudentResponse;
import com.ctbjj.main.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<Page<StudentResponse>> findAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(studentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> search(@RequestParam String name) {
        return ResponseEntity.ok(studentService.searchByName(name));
    }

    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody CreateStudentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(@PathVariable UUID id,
                                                   @Valid @RequestBody UpdateStudentRequest req) {
        return ResponseEntity.ok(studentService.update(id, req));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<StudentResponse> updateStatus(@PathVariable UUID id,
                                                         @Valid @RequestBody UpdateStudentStatusRequest req) {
        return ResponseEntity.ok(studentService.updateStatus(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
