package com.ctbjj.main.repository;

import com.ctbjj.main.entity.ClassType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClassTypeRepository extends JpaRepository<ClassType, UUID> {
}
