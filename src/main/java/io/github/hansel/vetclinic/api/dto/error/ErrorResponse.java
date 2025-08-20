package io.github.hansel.vetclinic.api.dto.error;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path) {}
