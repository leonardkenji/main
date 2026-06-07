package com.ctbjj.main.service;

import com.ctbjj.main.dto.request.CreateUserRequest;
import com.ctbjj.main.dto.response.UserResponse;
import com.ctbjj.main.entity.Professor;
import com.ctbjj.main.entity.Student;
import com.ctbjj.main.entity.User;
import com.ctbjj.main.enums.EnrollmentStatus;
import com.ctbjj.main.exception.BusinessException;
import com.ctbjj.main.exception.ResourceNotFoundException;
import com.ctbjj.main.repository.ProfessorRepository;
import com.ctbjj.main.repository.StudentRepository;
import com.ctbjj.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    public UserResponse findById(UUID id) {
        return userRepository.findById(id)
                .map(UserResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    public UserResponse findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + email));
    }

    @Transactional
    public UserResponse create(CreateUserRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new BusinessException("Email já cadastrado: " + req.email());
        }

        User user = User.builder()
                .name(req.name())
                .email(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(req.role())
                .build();

        switch (req.role()) {
            case STUDENT -> {
                Student student = Student.builder()
                        .user(user)
                        .stripes(0)
                        .enrollmentStatus(EnrollmentStatus.ACTIVE)
                        .qrCodeToken(UUID.randomUUID())
                        .build();
                studentRepository.save(student);
            }
            case PROFESSOR -> {
                Professor professor = Professor.builder()
                        .user(user)
                        .stripes(0)
                        .active(true)
                        .displayOrder(0)
                        .build();
                professorRepository.save(professor);
            }
            case ADMIN -> userRepository.save(user);
        }

        return UserResponse.from(user);
    }
}
