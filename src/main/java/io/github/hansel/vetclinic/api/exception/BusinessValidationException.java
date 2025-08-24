package io.github.hansel.vetclinic.api.exception;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class BusinessValidationException extends RuntimeException {
    private final List<ErrorResponse.FieldErrorResponse> fieldErrors;

    public BusinessValidationException(List<ErrorResponse.FieldErrorResponse> fieldErrors) {
        super("Business validation failed");
        this.fieldErrors = fieldErrors;
    }
}