package com.ctbjj.main.service;

import com.ctbjj.main.dto.request.CreateStudentRequest;
import com.ctbjj.main.dto.request.UpdateStudentRequest;
import com.ctbjj.main.dto.request.UpdateStudentStatusRequest;
import com.ctbjj.main.dto.response.StudentResponse;
import com.ctbjj.main.entity.Student;
import com.ctbjj.main.entity.User;
import com.ctbjj.main.enums.UserRole;
import com.ctbjj.main.exception.BusinessException;
import com.ctbjj.main.exception.ResourceNotFoundException;
import com.ctbjj.main.repository.StudentRepository;
import com.ctbjj.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<StudentResponse> findAll(Pageable pageable) {
        return studentRepository.findAllWithUser(pageable).map(StudentResponse::from);
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(UUID id) {
        return StudentResponse.from(getStudentOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> searchByName(String name) {
        return studentRepository.searchByName(name).stream()
                .map(StudentResponse::from)
                .toList();
    }

    public StudentResponse create(CreateStudentRequest req) {
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
                .phone(req.phone())
                .birthDate(req.birthDate())
                .belt(req.belt())
                .stripes(req.stripes())
                .enrollmentStatus(req.enrollmentStatus())
                .qrCodeToken(UUID.randomUUID())
                .enrollmentDate(req.enrollmentDate())
                .notes(req.notes())
                .build();

        return StudentResponse.from(studentRepository.save(student));
    }

    public StudentResponse update(UUID id, UpdateStudentRequest req) {
        Student student = getStudentOrThrow(id);

        if (!student.getUser().getEmail().equals(req.email()) && userRepository.existsByEmail(req.email())) {
            throw new BusinessException("Email já cadastrado: " + req.email());
        }

        student.getUser().setName(req.name());
        student.getUser().setEmail(req.email());
        student.setPhone(req.phone());
        student.setBirthDate(req.birthDate());
        student.setBelt(req.belt());
        student.setStripes(req.stripes());
        student.setEnrollmentStatus(req.enrollmentStatus());
        student.setEnrollmentDate(req.enrollmentDate());
        student.setNotes(req.notes());

        return StudentResponse.from(studentRepository.save(student));
    }

    public StudentResponse updateStatus(UUID id, UpdateStudentStatusRequest req) {
        Student student = getStudentOrThrow(id);
        student.setEnrollmentStatus(req.status());
        return StudentResponse.from(studentRepository.save(student));
    }

    public void delete(UUID id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aluno não encontrado: " + id);
        }
        studentRepository.deleteById(id);
    }

    private Student getStudentOrThrow(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public StudentResponse findMe(String email) {
        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));
        return StudentResponse.from(student);
    }
}
