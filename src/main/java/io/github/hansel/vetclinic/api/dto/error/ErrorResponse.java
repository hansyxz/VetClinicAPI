package io.github.hansel.vetclinic.api.dto.error;

import java.util.List;

public record ErrorResponse(
        int status,
        String title,
        String detail,
        List<FieldErrorResponse> errors,
        String path
) {
    public static record FieldErrorResponse(String field, String message) {}
}
