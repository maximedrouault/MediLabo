package com.medilabo.microservicepatient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@DynamicUpdate
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @NotBlank
    private String lastName;

    @Column(length = 100, nullable = false)
    @NotBlank
    private String firstName;

    @Column(nullable = false)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 1, nullable = false)
    @NotNull
    private Gender gender;

    public enum Gender {
        M, // MALE
        F  // FEMALE
    }

    private String address;

    @Column(length = 25)
    private String telephoneNumber;
}