package com.example.student.service;

import com.example.student.domain.StudentDto;
import com.example.student.entity.Student;
import com.example.student.exception.StudentNotFoundException;
import com.example.student.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public StudentDto getStudentById(Long id) {
        logger.debug("Fetching student with id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Student not found with id: {}", id);
                    return new StudentNotFoundException("Student not found with id: " + id);
                });
        return convertToDto(student);
    }
    
    public StudentDto createStudent(StudentDto studentDto) {
        logger.debug("Creating new student with email: {}", studentDto.getEmail());
        Student student = convertToEntity(studentDto);
        Student savedStudent = studentRepository.save(student);
        logger.info("Student created successfully with id: {}", savedStudent.getId());
        return convertToDto(savedStudent);
    }
    
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        logger.debug("Updating student with id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Student not found with id: {}", id);
                    return new StudentNotFoundException("Student not found with id: " + id);
                });
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setAge(studentDto.getAge());
        Student updatedStudent = studentRepository.save(student);
        logger.info("Student updated successfully with id: {}", id);
        return convertToDto(updatedStudent);
    }
    
    public void deleteStudent(Long id) {
        logger.debug("Deleting student with id: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Student not found with id: {}", id);
                    return new StudentNotFoundException("Student not found with id: " + id);
                });
        studentRepository.delete(student);
        logger.info("Student deleted successfully with id: {}", id);
    }
    
    private StudentDto convertToDto(Student student) {
        return new StudentDto(student.getId(), student.getName(), student.getEmail(), student.getAge());
    }
    
    private Student convertToEntity(StudentDto studentDto) {
        return new Student(studentDto.getId(), studentDto.getName(), studentDto.getEmail(), studentDto.getAge());
    }
}

