package com.openclassrooms.etudiant.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StudentDTOTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validStudentDtoHasNoValidationErrors() {
        assertThat(validator.validate(validDto())).isEmpty();
    }

    @Test
    void blankRequiredFieldsAreRejected() {
        StudentDTO dto = new StudentDTO();

        assertThat(validator.validate(dto))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactlyInAnyOrder("firstName", "lastName", "email");
    }

    @Test
    void invalidEmailIsRejected() {
        StudentDTO dto = validDto();
        dto.setEmail("not-an-email");

        assertThat(validator.validate(dto))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("email");
    }

    private StudentDTO validDto() {
        StudentDTO dto = new StudentDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        return dto;
    }
}
