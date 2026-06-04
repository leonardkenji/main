package com.ctbjj.main.service;

import com.ctbjj.main.dto.request.CreateClassTypeRequest;
import com.ctbjj.main.dto.request.UpdateClassTypeRequest;
import com.ctbjj.main.dto.response.ClassTypeResponse;
import com.ctbjj.main.entity.ClassType;
import com.ctbjj.main.exception.ResourceNotFoundException;
import com.ctbjj.main.repository.ClassTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassTypeService {

    private final ClassTypeRepository classTypeRepository;

    @Transactional(readOnly = true)
    public List<ClassTypeResponse> findAll() {
        return classTypeRepository.findAll().stream().map(ClassTypeResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ClassTypeResponse findById(UUID id) {
        return ClassTypeResponse.from(getOrThrow(id));
    }

    public ClassTypeResponse create(CreateClassTypeRequest req) {
        ClassType ct = ClassType.builder()
                .name(req.name())
                .description(req.description())
                .colorHex(req.colorHex())
                .build();
        return ClassTypeResponse.from(classTypeRepository.save(ct));
    }

    public ClassTypeResponse update(UUID id, UpdateClassTypeRequest req) {
        ClassType ct = getOrThrow(id);
        ct.setName(req.name());
        ct.setDescription(req.description());
        ct.setColorHex(req.colorHex());
        return ClassTypeResponse.from(classTypeRepository.save(ct));
    }

    public void delete(UUID id) {
        if (!classTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de aula não encontrado: " + id);
        }
        classTypeRepository.deleteById(id);
    }

    private ClassType getOrThrow(UUID id) {
        return classTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de aula não encontrado: " + id));
    }
}
