package com.openclassrooms.etudiant.repository;

import com.openclassrooms.etudiant.entities.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.docker.compose.enabled=false")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class StudentRepositoryTest {

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void findByEmailReturnsStudent() {
        Student saved = studentRepository.saveAndFlush(student());

        Optional<Student> result = studentRepository.findByEmail("john.doe@example.com");

        assertThat(result).contains(saved);
    }

    @Test
    void findByStudentNumberReturnsStudent() {
        Student saved = studentRepository.saveAndFlush(student());

        Optional<Student> result = studentRepository.findByStudentNumber("STU-2026-000001");

        assertThat(result).contains(saved);
    }

    @Test
    void findMethodsReturnEmptyForUnknownValues() {
        assertThat(studentRepository.findByEmail("unknown@example.com")).isEmpty();
        assertThat(studentRepository.findByStudentNumber("UNKNOWN")).isEmpty();
    }

    private Student student() {
        Student student = new Student();
        student.setStudentNumber("STU-2026-000001");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        return student;
    }
}
