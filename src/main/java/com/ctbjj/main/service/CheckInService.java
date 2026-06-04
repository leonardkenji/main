package com.ctbjj.main.service;

import com.ctbjj.main.dto.request.ManualCheckInRequest;
import com.ctbjj.main.dto.request.QrCheckInRequest;
import com.ctbjj.main.dto.response.CheckInResponse;
import com.ctbjj.main.dto.response.CheckInStatsResponse;
import com.ctbjj.main.dto.response.CheckInStatsResponse.ClassTypeStat;
import com.ctbjj.main.entity.CheckIn;
import com.ctbjj.main.entity.Student;
import com.ctbjj.main.enums.CheckInMethod;
import com.ctbjj.main.enums.EnrollmentStatus;
import com.ctbjj.main.exception.BusinessException;
import com.ctbjj.main.exception.ResourceNotFoundException;
import com.ctbjj.main.repository.CheckInRepository;
import com.ctbjj.main.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final StudentRepository studentRepository;

    public CheckInResponse checkInByQr(QrCheckInRequest req) {
        Student student = studentRepository.findByQrCodeToken(req.token())
                .orElseThrow(() -> new ResourceNotFoundException("Token QR inválido"));
        return performCheckIn(student, CheckInMethod.QR_CODE, false);
    }

    public CheckInResponse checkInManual(ManualCheckInRequest req) {
        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado: " + req.studentId()));
        return performCheckIn(student, CheckInMethod.MANUAL, true);
    }

    private CheckInResponse performCheckIn(Student student, CheckInMethod method, boolean adminOverride) {
        validate(student);

        CheckIn checkIn = CheckIn.builder()
                .student(student)
                .checkInMethod(method)
                .checkedInAt(LocalDateTime.now())
                .adminOverride(adminOverride)
                .build();

        return CheckInResponse.from(checkInRepository.save(checkIn));
    }

    private void validate(Student student) {
        EnrollmentStatus status = student.getEnrollmentStatus();
        if (status != EnrollmentStatus.ACTIVE && status != EnrollmentStatus.TRIAL) {
            throw new BusinessException("Check-in não permitido para alunos com status: " + status);
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        checkInRepository.findOpenCheckinToday(student.getId(), startOfDay).ifPresent(c -> {
            throw new BusinessException("Aluno já possui um check-in aberto hoje");
        });
    }

    public CheckInResponse checkout(UUID checkInId) {
        CheckIn checkIn = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in não encontrado: " + checkInId));

        if (checkIn.getCheckedOutAt() != null) {
            throw new BusinessException("Check-out já realizado para este check-in");
        }

        LocalDateTime now = LocalDateTime.now();
        checkIn.setCheckedOutAt(now);
        checkIn.setDurationMinutes((int) Duration.between(checkIn.getCheckedInAt(), now).toMinutes());

        return CheckInResponse.from(checkInRepository.save(checkIn));
    }

    @Transactional(readOnly = true)
    public Page<CheckInResponse> getHistory(UUID studentId, Pageable pageable) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Aluno não encontrado: " + studentId);
        }
        return checkInRepository.findHistoryByStudentId(studentId, pageable).map(CheckInResponse::from);
    }

    @Transactional(readOnly = true)
    public CheckInStatsResponse getStats(UUID studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Aluno não encontrado: " + studentId);
        }

        List<CheckIn> checkins = checkInRepository.findAllByStudentId(studentId);

        record Accumulator(String name, String colorHex, int classes, int minutes) {}

        Map<UUID, Accumulator> map = new LinkedHashMap<>();

        int totalClasses = 0;
        int totalMinutes = 0;

        for (CheckIn c : checkins) {
            totalClasses++;
            int minutes = resolveMinutes(c);
            totalMinutes += minutes;

            if (c.getSchedule() != null) {
                UUID ctId = c.getSchedule().getClassType().getId();
                String ctName = c.getSchedule().getClassType().getName();
                String colorHex = c.getSchedule().getClassType().getColorHex();
                Accumulator acc = map.getOrDefault(ctId, new Accumulator(ctName, colorHex, 0, 0));
                map.put(ctId, new Accumulator(ctName, colorHex, acc.classes() + 1, acc.minutes() + minutes));
            } else {
                UUID noScheduleKey = new UUID(0L, 0L);
                Accumulator acc = map.getOrDefault(noScheduleKey, new Accumulator("Sem categoria", "#808080", 0, 0));
                map.put(noScheduleKey, new Accumulator("Sem categoria", "#808080", acc.classes() + 1, acc.minutes() + minutes));
            }
        }

        List<ClassTypeStat> byClassType = map.entrySet().stream()
                .map(e -> new ClassTypeStat(
                        e.getKey().equals(new UUID(0L, 0L)) ? null : e.getKey(),
                        e.getValue().name(),
                        e.getValue().colorHex(),
                        e.getValue().classes(),
                        e.getValue().minutes()
                ))
                .toList();

        return new CheckInStatsResponse(totalClasses, totalMinutes, byClassType);
    }

    private int resolveMinutes(CheckIn c) {
        if (c.getSchedule() != null) {
            return (int) Duration.between(c.getSchedule().getStartTime(), c.getSchedule().getEndTime()).toMinutes();
        }
        return c.getDurationMinutes() != null ? c.getDurationMinutes() : 0;
    }

    @Transactional
    public void autoCheckout() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(60);
        List<CheckIn> expired = checkInRepository.findByCheckedOutAtIsNullAndCheckedInAtBefore(cutoff);

        for (CheckIn c : expired) {
            LocalDateTime now = LocalDateTime.now();
            c.setCheckedOutAt(now);
            c.setDurationMinutes((int) Duration.between(c.getCheckedInAt(), now).toMinutes());
        }

        checkInRepository.saveAll(expired);
    }
}
