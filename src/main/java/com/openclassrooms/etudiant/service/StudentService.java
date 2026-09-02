package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        Optional<Student> student = studentRepository.findById(id);

        if (student.isPresent()) {
            return student.get();
        } else {
            throw new IllegalArgumentException(
                    "Student with id " + id + " not found");
        }
    }

    public Student findByStudentNumber(String studentNumber) {
        Optional<Student> student = studentRepository.findByStudentNumber(studentNumber);

        if (student.isPresent()) {
            return student.get();
        } else {
            throw new IllegalArgumentException(
                    "Student with number " + studentNumber + " not found");
        }
    }

    public Student create(Student student) {
        student.setId(null);
        student.setStudentNumber(generateStudentNumber());
        return studentRepository.save(student);
    }

    public Student update(Long id, Student student) {
        Student existingStudent = findById(id);
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        return studentRepository.save(existingStudent);
    }

    public void delete(Long id) {
        Student student = findById(id);
        studentRepository.delete(student);
    }

    private String generateStudentNumber() {
        int randomNumber = ThreadLocalRandom.current()
                .nextInt(0, 1_000_000);

        return "STU-"
                + Year.now().getValue()
                + "-"
                + String.format("%06d", randomNumber);
    }
}
