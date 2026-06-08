package com.ctbjj.main.repository;

import com.ctbjj.main.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByQrCodeToken(UUID qrCodeToken);
    Optional<Student> findByUser_Email(String email);

    @Query("SELECT s FROM Student s JOIN s.user u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> searchByName(@Param("name") String name);

    @Query("SELECT s FROM Student s JOIN FETCH s.user")
    Page<Student> findAllWithUser(Pageable pageable);
}
