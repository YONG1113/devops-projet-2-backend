package com.openclassrooms.etudiant.controller;

import com.openclassrooms.etudiant.dto.StudentDTO;
import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.mapper.StudentDtoMapper;
import com.openclassrooms.etudiant.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentService studentService;
    @Mock
    private StudentDtoMapper studentDtoMapper;
    @InjectMocks
    private StudentController studentController;

    private Student student;
    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setStudentNumber("STU-2026-000001");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");

        studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setStudentNumber("STU-2026-000001");
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Doe");
        studentDTO.setEmail("john.doe@example.com");
    }

    @Test
    void findAllReturnsStudentDtos() {
        when(studentService.findAll()).thenReturn(List.of(student));
        when(studentDtoMapper.toDtoList(List.of(student))).thenReturn(List.of(studentDTO));

        ResponseEntity<List<StudentDTO>> response = studentController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(studentDTO);
    }

    @Test
    void findByIdReturnsStudentDto() {
        when(studentService.findById(1L)).thenReturn(student);
        when(studentDtoMapper.toDto(student)).thenReturn(studentDTO);

        ResponseEntity<StudentDTO> response = studentController.findById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(studentDTO);
    }

    @Test
    void createReturnsCreatedStudent() {
        when(studentDtoMapper.toEntity(studentDTO)).thenReturn(student);
        when(studentService.create(student)).thenReturn(student);
        when(studentDtoMapper.toDto(student)).thenReturn(studentDTO);

        ResponseEntity<StudentDTO> response = studentController.create(studentDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(studentDTO);
        verify(studentService).create(student);
    }

    @Test
    void updateReturnsUpdatedStudent() {
        when(studentDtoMapper.toEntity(studentDTO)).thenReturn(student);
        when(studentService.update(1L, student)).thenReturn(student);
        when(studentDtoMapper.toDto(student)).thenReturn(studentDTO);

        ResponseEntity<StudentDTO> response = studentController.update(1L, studentDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(studentDTO);
        verify(studentService).update(1L, student);
    }

    @Test
    void deleteReturnsNoContent() {
        ResponseEntity<Void> response = studentController.delete(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(studentService).delete(1L);
    }
}
