package com.example.student.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Unit Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName("Should handle StudentNotFoundException and return 404 status")
    void shouldHandleStudentNotFoundException() {
        // Arrange
        String errorMessage = "Student not found with id: 999";
        StudentNotFoundException exception = new StudentNotFoundException(errorMessage);

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleStudentNotFoundException(exception);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Student Not Found");
        assertThat(response.getBody().get("message")).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException and return 400 status with validation errors")
    void shouldHandleMethodArgumentNotValidException() throws Exception {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("studentDto", "name", "Name is required and cannot be blank"));
        fieldErrors.add(new FieldError("studentDto", "email", "Email must be a valid email address"));
        fieldErrors.add(new FieldError("studentDto", "age", "Age must be at least 1"));
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(3);
        assertThat(response.getBody().get("name")).isEqualTo("Name is required and cannot be blank");
        assertThat(response.getBody().get("email")).isEqualTo("Email must be a valid email address");
        assertThat(response.getBody().get("age")).isEqualTo("Age must be at least 1");
    }

    @Test
    @DisplayName("Should handle generic Exception and return 500 status with generic message")
    void shouldHandleGenericException() {
        // Arrange
        String errorMessage = "Unexpected error occurred";
        Exception exception = new RuntimeException(errorMessage);

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleGenericException(exception);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Internal Server Error");
        // Security: Generic exception messages should not expose internal details
        assertThat(response.getBody().get("message")).isEqualTo("An internal error occurred. Please contact support.");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with empty field errors")
    void shouldHandleMethodArgumentNotValidExceptionWithEmptyErrors() throws Exception {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }
}

