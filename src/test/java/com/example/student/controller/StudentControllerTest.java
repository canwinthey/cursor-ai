package com.example.student.controller;

import com.example.student.domain.StudentDto;
import com.example.student.exception.GlobalExceptionHandler;
import com.example.student.exception.StudentNotFoundException;
import com.example.student.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentController Unit Tests")
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private StudentDto studentDto1;
    private StudentDto studentDto2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        studentDto1 = new StudentDto(1L, "John Doe", "john.doe@example.com", 20);
        studentDto2 = new StudentDto(2L, "Jane Smith", "jane.smith@example.com", 22);
    }

    @Test
    @DisplayName("Should return all students with status 200 when GET /api/students is called")
    void shouldReturnAllStudents() throws Exception {
        // Arrange
        List<StudentDto> students = Arrays.asList(studentDto1, studentDto2);
        when(studentService.getAllStudents()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("Should return student with status 200 when GET /api/students/{id} is called with valid id")
    void shouldReturnStudentById() throws Exception {
        // Arrange
        Long studentId = 1L;
        when(studentService.getStudentById(studentId)).thenReturn(studentDto1);

        // Act & Assert
        mockMvc.perform(get("/api/students/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(20));

        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    @DisplayName("Should return status 404 when GET /api/students/{id} is called with invalid id")
    void shouldReturnNotFoundWhenGetStudentByIdWithInvalidId() throws Exception {
        // Arrange
        Long invalidId = 999L;
        when(studentService.getStudentById(invalidId))
                .thenThrow(new StudentNotFoundException("Student not found with id: " + invalidId));

        // Act & Assert
        mockMvc.perform(get("/api/students/{id}", invalidId))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).getStudentById(invalidId);
    }

    @Test
    @DisplayName("Should create student with status 201 when POST /api/students is called")
    void shouldCreateStudent() throws Exception {
        // Arrange
        StudentDto newStudentDto = new StudentDto(null, "Bob Johnson", "bob.johnson@example.com", 19);
        StudentDto createdStudentDto = new StudentDto(3L, "Bob Johnson", "bob.johnson@example.com", 19);
        when(studentService.createStudent(any(StudentDto.class))).thenReturn(createdStudentDto);

        // Act & Assert
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudentDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Bob Johnson"))
                .andExpect(jsonPath("$.email").value("bob.johnson@example.com"))
                .andExpect(jsonPath("$.age").value(19));

        verify(studentService, times(1)).createStudent(any(StudentDto.class));
    }

    @Test
    @DisplayName("Should update student with status 200 when PUT /api/students/{id} is called")
    void shouldUpdateStudent() throws Exception {
        // Arrange
        Long studentId = 1L;
        StudentDto updatedDto = new StudentDto(1L, "John Updated", "john.updated@example.com", 25);
        when(studentService.updateStudent(eq(studentId), any(StudentDto.class))).thenReturn(updatedDto);

        // Act & Assert
        mockMvc.perform(put("/api/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"))
                .andExpect(jsonPath("$.age").value(25));

        verify(studentService, times(1)).updateStudent(eq(studentId), any(StudentDto.class));
    }

    @Test
    @DisplayName("Should return status 404 when PUT /api/students/{id} is called with invalid id")
    void shouldReturnNotFoundWhenUpdateStudentWithInvalidId() throws Exception {
        // Arrange
        Long invalidId = 999L;
        StudentDto updatedDto = new StudentDto(999L, "Updated Name", "updated@example.com", 30);
        when(studentService.updateStudent(eq(invalidId), any(StudentDto.class)))
                .thenThrow(new StudentNotFoundException("Student not found with id: " + invalidId));

        // Act & Assert
        mockMvc.perform(put("/api/students/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).updateStudent(eq(invalidId), any(StudentDto.class));
    }

    @Test
    @DisplayName("Should delete student with status 204 when DELETE /api/students/{id} is called")
    void shouldDeleteStudent() throws Exception {
        // Arrange
        Long studentId = 1L;
        doNothing().when(studentService).deleteStudent(studentId);

        // Act & Assert
        mockMvc.perform(delete("/api/students/{id}", studentId))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).deleteStudent(studentId);
    }

    @Test
    @DisplayName("Should return status 404 when DELETE /api/students/{id} is called with invalid id")
    void shouldReturnNotFoundWhenDeleteStudentWithInvalidId() throws Exception {
        // Arrange
        Long invalidId = 999L;
        doThrow(new StudentNotFoundException("Student not found with id: " + invalidId))
                .when(studentService).deleteStudent(invalidId);

        // Act & Assert
        mockMvc.perform(delete("/api/students/{id}", invalidId))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).deleteStudent(invalidId);
    }
}

