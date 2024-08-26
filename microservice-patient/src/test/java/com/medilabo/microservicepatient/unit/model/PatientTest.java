package com.medilabo.microservicepatient.unit.model;

import com.medilabo.microservicepatient.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatientTest {

    @Test
    void genderEnumContainsMale() {
        assertTrue(EnumSet.allOf(Patient.Gender.class).contains(Patient.Gender.M));
    }

    @Test
    void genderEnumContainsFemale() {
        assertTrue(EnumSet.allOf(Patient.Gender.class).contains(Patient.Gender.F));
    }

    @Test
    void genderEnumDoesNotContainOtherValues() {
        assertEquals(2, EnumSet.allOf(Patient.Gender.class).size());
    }
}
