package com.medilabo.mpatient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
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