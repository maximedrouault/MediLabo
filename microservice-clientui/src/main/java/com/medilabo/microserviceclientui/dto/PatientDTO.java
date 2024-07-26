package com.medilabo.microserviceclientui.dto;


import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientDTO {

    private Long id;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    private Gender gender;

    public enum Gender {
        M, // MALE
        F  // FEMALE
    }

    private String address;
    private String telephoneNumber;
}
