package com.ctbjj.main.service;

import com.ctbjj.main.dto.request.CreateProfessorRequest;
import com.ctbjj.main.dto.request.UpdateProfessorRequest;
import com.ctbjj.main.dto.response.ProfessorResponse;
import com.ctbjj.main.entity.Professor;
import com.ctbjj.main.entity.User;
import com.ctbjj.main.enums.UserRole;
import com.ctbjj.main.exception.BusinessException;
import com.ctbjj.main.exception.ResourceNotFoundException;
import com.ctbjj.main.repository.ProfessorRepository;
import com.ctbjj.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<ProfessorResponse> findAll() {
        return professorRepository.findAllWithUserOrderByDisplayOrder().stream()
                .map(ProfessorResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProfessorResponse findById(UUID id) {
        return ProfessorResponse.from(getProfessorOrThrow(id));
    }

    public ProfessorResponse create(CreateProfessorRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new BusinessException("Email já cadastrado: " + req.email());
        }

        User user = User.builder()
                .name(req.name())
                .email(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(UserRole.PROFESSOR)
                .build();

        Professor professor = Professor.builder()
                .user(user)
                .belt(req.belt())
                .stripes(req.stripes())
                .bio(req.bio())
                .photoUrl(req.photoUrl())
                .active(req.active())
                .displayOrder(req.displayOrder())
                .build();

        return ProfessorResponse.from(professorRepository.save(professor));
    }

    public ProfessorResponse update(UUID id, UpdateProfessorRequest req) {
        Professor professor = getProfessorOrThrow(id);

        if (!professor.getUser().getEmail().equals(req.email()) && userRepository.existsByEmail(req.email())) {
            throw new BusinessException("Email já cadastrado: " + req.email());
        }

        professor.getUser().setName(req.name());
        professor.getUser().setEmail(req.email());
        professor.setBelt(req.belt());
        professor.setStripes(req.stripes());
        professor.setBio(req.bio());
        professor.setPhotoUrl(req.photoUrl());
        professor.setActive(req.active());
        professor.setDisplayOrder(req.displayOrder());

        return ProfessorResponse.from(professorRepository.save(professor));
    }

    public void delete(UUID id) {
        if (!professorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Professor não encontrado: " + id);
        }
        professorRepository.deleteById(id);
    }

    private Professor getProfessorOrThrow(UUID id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado: " + id));
    }
}
