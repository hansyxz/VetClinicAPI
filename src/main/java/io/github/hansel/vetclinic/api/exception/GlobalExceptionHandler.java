package io.github.hansel.vetclinic.api.exception;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponse.FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "One or more fields are invalid",
                errors,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                Collections.emptyList(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                ex.getFieldErrors(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON Request",
                "Request body is invalid or incorrectly formatted",
                Collections.emptyList(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BusinessValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleBusinessValidation(BusinessValidationException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "Conflict with database constraints",
                ex.getFieldErrors(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldErrorResponse> errors = new ArrayList<>();

        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            String causeMessage = rootCause.getMessage().toLowerCase();
            if (causeMessage.contains("uk_user_phone")) {
                errors.add(new ErrorResponse.FieldErrorResponse("phone", "Phone already exists"));
            }
            if (causeMessage.contains("uk_user_email")) {
                errors.add(new ErrorResponse.FieldErrorResponse("email", "Email already exists"));
            }
        }

        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "Conflict with database constraints",
                errors,
                request.getRequestURI()
        );    }

    @ExceptionHandler(ForbiddenOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenOperation(ForbiddenOperationException ex, HttpServletRequest request) {
        return new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "Operation not allowed",
                ex.getFieldErrors(),
                request.getRequestURI()
        );
    }
}
