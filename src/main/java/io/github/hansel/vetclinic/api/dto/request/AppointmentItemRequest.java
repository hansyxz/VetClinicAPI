package io.github.hansel.vetclinic.api.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AppointmentItemRequest(
        @NotNull(message = "Appointment ID cannot be null")
        @Positive(message = "Appointment ID must be positive")
        Long appointmentId,
        @NotNull(message = "Offering ID cannot be null")
        @Positive(message = "Offering ID must be positive")
        Long offeringId,
        @NotNull(message = "Employee ID cannot be null")
        @Positive(message = "Employee ID must be positive")
        Long employeeId,
        @Min(value = 1, message = "Amount must be at least 1")
        @Max(value = 10, message = "Amount cannot exceed 10")
        int amount,
        @NotNull(message = "Unit price cannot be null")
        @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Unit price format invalid")
        BigDecimal unitPrice) {}
