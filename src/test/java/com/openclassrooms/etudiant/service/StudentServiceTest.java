package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void findAllReturnsStudents() {
        Student student = createStudent();
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<Student> result = studentService.findAll();

        assertThat(result).containsExactly(student);
    }

    @Test
    void findByIdReturnsStudentWhenItExists() {
        Student student = createStudent();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertThat(studentService.findById(1L)).isSameAs(student);
    }

    @Test
    void findByIdThrowsWhenStudentDoesNotExist() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.findById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student with id 99 not found");
    }

    @Test
    void findByStudentNumberReturnsStudentWhenItExists() {
        Student student = createStudent();
        when(studentRepository.findByStudentNumber("STU-2026-000001"))
                .thenReturn(Optional.of(student));

        assertThat(studentService.findByStudentNumber("STU-2026-000001"))
                .isSameAs(student);
    }

    @Test
    void findByStudentNumberThrowsWhenStudentDoesNotExist() {
        when(studentRepository.findByStudentNumber("UNKNOWN"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.findByStudentNumber("UNKNOWN"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student with number UNKNOWN not found");
    }

    @Test
    void createGeneratesStudentNumberAndClearsId() {
        Student student = createStudent();
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.create(student);

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getStudentNumber())
                .matches("STU-" + Year.now().getValue() + "-[0-9]{6}");
        assertThat(result).isSameAs(student);
    }

    @Test
    void updateChangesOnlyEditableFields() {
        Student existing = createStudent();
        Student changes = new Student();
        changes.setFirstName("Jane");
        changes.setLastName("Smith");
        changes.setEmail("jane.smith@example.com");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(existing)).thenReturn(existing);

        Student result = studentService.update(1L, changes);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getEmail()).isEqualTo("jane.smith@example.com");
        assertThat(result.getStudentNumber()).isEqualTo("STU-2026-000001");
        verify(studentRepository).save(existing);
    }

    @Test
    void deleteRemovesExistingStudent() {
        Student student = createStudent();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.delete(1L);

        verify(studentRepository).delete(student);
    }

    @Test
    void deleteDoesNotCallRepositoryDeleteWhenStudentDoesNotExist() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.delete(99L))
                .isInstanceOf(IllegalArgumentException.class);
        verify(studentRepository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    private Student createStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setStudentNumber("STU-2026-000001");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        return student;
    }
}
