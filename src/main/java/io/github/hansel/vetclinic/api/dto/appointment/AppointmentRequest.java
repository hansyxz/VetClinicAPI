package io.github.hansel.vetclinic.api.dto.appointment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotNull(message = "Date/time cannot be null")
        LocalDateTime dateTime,
        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String notes,
        boolean pickupRequired,
        @NotNull(message = "Pet ID cannot be null")
        @Positive(message = "Pet ID must be positive")
        Long petId,
        @NotNull(message = "Customer ID cannot be null")
        @Positive(message = "Customer ID must be positive")
        Long customerId) {}
