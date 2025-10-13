package com.egomaa.demo.employeeservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "positions")
@Setter @Getter
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(nullable = false, unique = true, length = 10)
    private String code;
}