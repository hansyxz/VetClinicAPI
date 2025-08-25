package io.github.hansel.vetclinic.api.exception;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class ForbiddenOperationException extends RuntimeException {
    private final List<ErrorResponse.FieldErrorResponse> fieldErrors;

    public ForbiddenOperationException(List<ErrorResponse.FieldErrorResponse> fieldErrors) {
        super("Forbidden operation");
        this.fieldErrors = fieldErrors;
    }
}