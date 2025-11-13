package com.cursor.product.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response structure for API exceptions.
 * <p>
 * This class represents a standardized error response format that is returned
 * when exceptions occur in the application. It includes timestamp, HTTP status,
 * error type, message, request path, and optional validation errors.
 * </p>
 *
 * @author Cursor Product Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code of the error.
     */
    private int status;

    /**
     * Error type or category.
     */
    private String error;

    /**
     * Detailed error message.
     */
    private String message;

    /**
     * Request path where the error occurred.
     */
    private String path;

    /**
     * List of validation errors (if applicable).
     */
    private List<String> validationErrors;

    /**
     * Constructs an ErrorResponse without validation errors.
     *
     * @param timestamp the timestamp when the error occurred
     * @param status the HTTP status code
     * @param error the error type
     * @param message the error message
     * @param path the request path
     */
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}

