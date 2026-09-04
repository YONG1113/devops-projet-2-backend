package com.openclassrooms.etudiant.entities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class StudentTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validStudentHasNoValidationErrors() {
        Student student = validStudent();

        assertThat(validator.validate(student)).isEmpty();
    }

    @Test
    void blankRequiredFieldsAreRejected() {
        Student student = validStudent();
        student.setFirstName(" ");
        student.setLastName("");
        student.setEmail(null);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactlyInAnyOrder("firstName", "lastName", "email");
    }

    private Student validStudent() {
        Student student = new Student();
        student.setStudentNumber("STU-2026-000001");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        return student;
    }
}
