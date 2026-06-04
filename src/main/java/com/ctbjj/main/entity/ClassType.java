package com.ctbjj.main.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "class_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "color_hex")
    private String colorHex;
}
