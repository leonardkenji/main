package com.ctbjj.main.service;

import com.ctbjj.main.dto.request.LoginRequest;
import com.ctbjj.main.dto.request.RegisterRequest;
import com.ctbjj.main.dto.response.AuthResponse;
import com.ctbjj.main.entity.Student;
import com.ctbjj.main.entity.User;
import com.ctbjj.main.enums.EnrollmentStatus;
import com.ctbjj.main.enums.UserRole;
import com.ctbjj.main.exception.BusinessException;
import com.ctbjj.main.repository.StudentRepository;
import com.ctbjj.main.repository.UserRepository;
import com.ctbjj.main.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new BusinessException("Email já cadastrado: " + req.email());
        }

        User user = User.builder()
                .name(req.name())
                .email(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(UserRole.STUDENT)
                .build();

        Student student = Student.builder()
                .user(user)
                .stripes(0)
                .enrollmentStatus(EnrollmentStatus.ACTIVE)
                .qrCodeToken(UUID.randomUUID())
                .build();

        studentRepository.save(student);

        String token = jwtUtil.generate(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        String token = jwtUtil.generate(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
