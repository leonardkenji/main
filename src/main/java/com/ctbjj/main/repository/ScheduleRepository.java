package com.ctbjj.main.repository;

import com.ctbjj.main.entity.Schedule;
import com.ctbjj.main.enums.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    @Query("SELECT s FROM Schedule s JOIN FETCH s.professor p JOIN FETCH p.user JOIN FETCH s.classType WHERE s.weekday = :weekday AND s.active = true ORDER BY s.startTime")
    List<Schedule> findByWeekdayAndActiveTrue(@Param("weekday") Weekday weekday);

    @Query("SELECT s FROM Schedule s JOIN FETCH s.professor p JOIN FETCH p.user JOIN FETCH s.classType ORDER BY s.weekday, s.startTime")
    List<Schedule> findAllWithRelations();
}
