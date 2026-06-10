package com.ctbjj.main.config;

import com.ctbjj.main.entity.Professor;
import com.ctbjj.main.entity.Student;
import com.ctbjj.main.entity.User;
import com.ctbjj.main.enums.Belt;
import com.ctbjj.main.enums.EnrollmentStatus;
import com.ctbjj.main.enums.UserRole;
import com.ctbjj.main.repository.ProfessorRepository;
import com.ctbjj.main.repository.StudentRepository;
import com.ctbjj.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Seed ignorado: banco já possui dados.");
            return;
        }

        seedProfessors();
        seedStudents();
        log.info("Seed concluído: 2 professores e 10 alunos criados.");
    }

    private void seedProfessors() {
        record ProfData(String name, String email, Belt belt, int stripes, String bio, int order) {}

        List<ProfData> profs = List.of(
            new ProfData("Carlos Silva",  "carlos.silva@ctbjj.com",  Belt.BLACK,  1, "Faixa preta com mais de 15 anos de experiência no BJJ.", 1),
            new ProfData("Marcos Pereira","marcos.pereira@ctbjj.com", Belt.BROWN, 3, "Especialista em jogo de guarda e competidor nacional.", 2)
        );

        for (ProfData p : profs) {
            User user = User.builder()
                    .name(p.name())
                    .email(p.email())
                    .passwordHash(passwordEncoder.encode("senha123"))
                    .role(UserRole.PROFESSOR)
                    .build();

            Professor professor = Professor.builder()
                    .user(user)
                    .belt(p.belt())
                    .stripes(p.stripes())
                    .bio(p.bio())
                    .active(true)
                    .displayOrder(p.order())
                    .build();

            professorRepository.save(professor);
        }
    }

    private void seedStudents() {
        record StudentData(String name, String email, String phone, LocalDate birthDate,
                           Belt belt, int stripes, EnrollmentStatus status, LocalDate enrollmentDate) {}

        List<StudentData> students = List.of(
            new StudentData("Ana Costa",      "ana.costa@email.com",      "(11) 91234-0001", LocalDate.of(1998,  3, 15), Belt.WHITE,  2, EnrollmentStatus.ACTIVE,   LocalDate.of(2024, 1, 10)),
            new StudentData("Bruno Oliveira", "bruno.oliveira@email.com", "(11) 91234-0002", LocalDate.of(1995,  7, 22), Belt.BLUE,   1, EnrollmentStatus.ACTIVE,   LocalDate.of(2023, 6,  5)),
            new StudentData("Carla Mendes",   "carla.mendes@email.com",   "(11) 91234-0003", LocalDate.of(2000, 11,  8), Belt.WHITE,  0, EnrollmentStatus.TRIAL,    LocalDate.of(2025, 5,  1)),
            new StudentData("Diego Souza",    "diego.souza@email.com",    "(11) 91234-0004", LocalDate.of(1992,  4, 30), Belt.PURPLE, 2, EnrollmentStatus.ACTIVE,   LocalDate.of(2022, 3, 20)),
            new StudentData("Elena Ferreira", "elena.ferreira@email.com", "(11) 91234-0005", LocalDate.of(2001,  9, 12), Belt.WHITE,  3, EnrollmentStatus.ACTIVE,   LocalDate.of(2024, 8, 14)),
            new StudentData("Felipe Lima",    "felipe.lima@email.com",    "(11) 91234-0006", LocalDate.of(1990,  1, 25), Belt.BLUE,   3, EnrollmentStatus.ACTIVE,   LocalDate.of(2021, 2, 28)),
            new StudentData("Gabriela Rocha", "gabriela.rocha@email.com", "(11) 91234-0007", LocalDate.of(1997,  6,  3), Belt.BLUE,   0, EnrollmentStatus.INACTIVE, LocalDate.of(2023, 9,  7)),
            new StudentData("Henrique Nunes", "henrique.nunes@email.com", "(11) 91234-0008", LocalDate.of(2003,  2, 18), Belt.WHITE,  1, EnrollmentStatus.ACTIVE,   LocalDate.of(2025, 1, 15)),
            new StudentData("Isabela Castro", "isabela.castro@email.com", "(11) 91234-0009", LocalDate.of(1994, 12,  5), Belt.BROWN,  1, EnrollmentStatus.ACTIVE,   LocalDate.of(2020, 7, 10)),
            new StudentData("João Barbosa",   "joao.barbosa@email.com",   "(11) 91234-0010", LocalDate.of(1999,  8, 27), Belt.WHITE,  0, EnrollmentStatus.SUSPENDED,LocalDate.of(2024, 4, 22))
        );

        for (StudentData s : students) {
            User user = User.builder()
                    .name(s.name())
                    .email(s.email())
                    .passwordHash(passwordEncoder.encode("senha123"))
                    .role(UserRole.STUDENT)
                    .build();

            Student student = Student.builder()
                    .user(user)
                    .phone(s.phone())
                    .birthDate(s.birthDate())
                    .belt(s.belt())
                    .stripes(s.stripes())
                    .enrollmentStatus(s.status())
                    .qrCodeToken(UUID.randomUUID())
                    .enrollmentDate(s.enrollmentDate())
                    .build();

            studentRepository.save(student);
        }
    }
}
