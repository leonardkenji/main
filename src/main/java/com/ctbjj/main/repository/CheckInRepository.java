package com.ctbjj.main.repository;

import com.ctbjj.main.entity.CheckIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, UUID> {

    @Query("SELECT c FROM CheckIn c WHERE c.student.id = :studentId AND c.checkedOutAt IS NULL AND c.checkedInAt >= :startOfDay")
    Optional<CheckIn> findOpenCheckinToday(@Param("studentId") UUID studentId, @Param("startOfDay") LocalDateTime startOfDay);

    List<CheckIn> findByCheckedOutAtIsNullAndCheckedInAtBefore(LocalDateTime cutoff);

    @Query("SELECT c FROM CheckIn c LEFT JOIN FETCH c.schedule s LEFT JOIN FETCH s.classType WHERE c.student.id = :studentId ORDER BY c.checkedInAt DESC")
    Page<CheckIn> findHistoryByStudentId(@Param("studentId") UUID studentId, Pageable pageable);

    @Query("SELECT c FROM CheckIn c LEFT JOIN FETCH c.schedule s LEFT JOIN FETCH s.classType WHERE c.student.id = :studentId")
    List<CheckIn> findAllByStudentId(@Param("studentId") UUID studentId);
}
