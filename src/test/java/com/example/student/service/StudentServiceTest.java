package com.example.student.service;

import com.example.student.domain.StudentDto;
import com.example.student.entity.Student;
import com.example.student.exception.StudentNotFoundException;
import com.example.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Unit Tests")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;
    private StudentDto studentDto1;
    private StudentDto studentDto2;

    @BeforeEach
    void setUp() {
        student1 = new Student(1L, "John Doe", "john.doe@example.com", 20);
        student2 = new Student(2L, "Jane Smith", "jane.smith@example.com", 22);

        studentDto1 = new StudentDto(1L, "John Doe", "john.doe@example.com", 20);
        studentDto2 = new StudentDto(2L, "Jane Smith", "jane.smith@example.com", 22);
    }

    @Test
    @DisplayName("Should return all students when getAllStudents is called")
    void shouldReturnAllStudents() {
        // Arrange
        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(students);

        // Act
        List<StudentDto> result = studentService.getAllStudents();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        assertThat(result.get(1).getName()).isEqualTo("Jane Smith");
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no students exist")
    void shouldReturnEmptyListWhenNoStudentsExist() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(List.of());

        // Act
        List<StudentDto> result = studentService.getAllStudents();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return student when getStudentById is called with valid id")
    void shouldReturnStudentWhenGetStudentByIdWithValidId() {
        // Arrange
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));

        // Act
        StudentDto result = studentService.getStudentById(studentId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getAge()).isEqualTo(20);
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    @DisplayName("Should throw StudentNotFoundException when getStudentById is called with invalid id")
    void shouldThrowExceptionWhenGetStudentByIdWithInvalidId() {
        // Arrange
        Long invalidId = 999L;
        when(studentRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.getStudentById(invalidId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student not found with id: " + invalidId);
        verify(studentRepository, times(1)).findById(invalidId);
    }

    @Test
    @DisplayName("Should create and return student when createStudent is called")
    void shouldCreateAndReturnStudent() {
        // Arrange
        StudentDto newStudentDto = new StudentDto(null, "Bob Johnson", "bob.johnson@example.com", 19);
        Student savedStudent = new Student(3L, "Bob Johnson", "bob.johnson@example.com", 19);
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        // Act
        StudentDto result = studentService.createStudent(newStudentDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Bob Johnson");
        assertThat(result.getEmail()).isEqualTo("bob.johnson@example.com");
        assertThat(result.getAge()).isEqualTo(19);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should update and return student when updateStudent is called with valid id")
    void shouldUpdateAndReturnStudentWhenUpdateStudentWithValidId() {
        // Arrange
        Long studentId = 1L;
        StudentDto updatedDto = new StudentDto(1L, "John Updated", "john.updated@example.com", 25);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        StudentDto result = studentService.updateStudent(studentId, updatedDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Updated");
        assertThat(result.getEmail()).isEqualTo("john.updated@example.com");
        assertThat(result.getAge()).isEqualTo(25);
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw StudentNotFoundException when updateStudent is called with invalid id")
    void shouldThrowExceptionWhenUpdateStudentWithInvalidId() {
        // Arrange
        Long invalidId = 999L;
        StudentDto updatedDto = new StudentDto(999L, "Updated Name", "updated@example.com", 30);
        when(studentRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.updateStudent(invalidId, updatedDto))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student not found with id: " + invalidId);
        verify(studentRepository, times(1)).findById(invalidId);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should delete student when deleteStudent is called with valid id")
    void shouldDeleteStudentWhenDeleteStudentWithValidId() {
        // Arrange
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));
        doNothing().when(studentRepository).delete(student1);

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).delete(student1);
    }

    @Test
    @DisplayName("Should throw StudentNotFoundException when deleteStudent is called with invalid id")
    void shouldThrowExceptionWhenDeleteStudentWithInvalidId() {
        // Arrange
        Long invalidId = 999L;
        when(studentRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> studentService.deleteStudent(invalidId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student not found with id: " + invalidId);
        verify(studentRepository, times(1)).findById(invalidId);
        verify(studentRepository, never()).delete(any(Student.class));
    }
}

