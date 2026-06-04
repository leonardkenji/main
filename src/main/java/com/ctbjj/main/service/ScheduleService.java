package com.ctbjj.main.service;

import com.ctbjj.main.dto.request.CreateScheduleRequest;
import com.ctbjj.main.dto.request.UpdateScheduleRequest;
import com.ctbjj.main.dto.response.ScheduleResponse;
import com.ctbjj.main.entity.ClassType;
import com.ctbjj.main.entity.Professor;
import com.ctbjj.main.entity.Schedule;
import com.ctbjj.main.enums.Weekday;
import com.ctbjj.main.exception.ResourceNotFoundException;
import com.ctbjj.main.repository.ClassTypeRepository;
import com.ctbjj.main.repository.ProfessorRepository;
import com.ctbjj.main.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ProfessorRepository professorRepository;
    private final ClassTypeRepository classTypeRepository;

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findAll() {
        return scheduleRepository.findAllWithRelations().stream().map(ScheduleResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ScheduleResponse findById(UUID id) {
        return ScheduleResponse.from(getOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findToday() {
        Weekday today = Weekday.from(LocalDate.now().getDayOfWeek());
        return scheduleRepository.findByWeekdayAndActiveTrue(today).stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    public ScheduleResponse create(CreateScheduleRequest req) {
        Professor professor = professorRepository.findById(req.professorId())
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado: " + req.professorId()));
        ClassType classType = classTypeRepository.findById(req.classTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de aula não encontrado: " + req.classTypeId()));

        Schedule schedule = Schedule.builder()
                .professor(professor)
                .classType(classType)
                .weekday(req.weekday())
                .startTime(req.startTime())
                .endTime(req.endTime())
                .maxCapacity(req.maxCapacity())
                .active(req.active())
                .build();

        return ScheduleResponse.from(scheduleRepository.save(schedule));
    }

    public ScheduleResponse update(UUID id, UpdateScheduleRequest req) {
        Schedule schedule = getOrThrow(id);

        Professor professor = professorRepository.findById(req.professorId())
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado: " + req.professorId()));
        ClassType classType = classTypeRepository.findById(req.classTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de aula não encontrado: " + req.classTypeId()));

        schedule.setProfessor(professor);
        schedule.setClassType(classType);
        schedule.setWeekday(req.weekday());
        schedule.setStartTime(req.startTime());
        schedule.setEndTime(req.endTime());
        schedule.setMaxCapacity(req.maxCapacity());
        schedule.setActive(req.active());

        return ScheduleResponse.from(scheduleRepository.save(schedule));
    }

    public void delete(UUID id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Horário não encontrado: " + id);
        }
        scheduleRepository.deleteById(id);
    }

    private Schedule getOrThrow(UUID id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horário não encontrado: " + id));
    }
}
