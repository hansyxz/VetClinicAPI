package io.github.hansel.vetclinic.api.dto.request;

import io.github.hansel.vetclinic.api.entity.enums.Gender;
import io.github.hansel.vetclinic.api.entity.enums.Species;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PetRequest(
        @NotNull(message = "Owner ID cannot be null")
        @Positive(message = "Owner ID must be a positive number")
        Long ownerId,
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,
        @NotNull(message = "Age cannot be null")
        @Min(value = 0, message = "Age must be at least 0")
        @Max(value = 120, message = "Age cannot exceed 120 years")
        Integer age,
        @DecimalMin(value = "0.1", message = "Weight must be at least 0.1 kg")
        @DecimalMax(value = "100.0", message = "Weight cannot exceed 100 kg")
        BigDecimal weightKg,
        @NotNull(message = "Gender cannot be null")
        Gender gender,
        @NotNull(message = "Species cannot be null")
        Species species,
        @Size(max = 100, message = "Other species must not exceed 100 characters")
        String otherSpecies,
        @NotNull(message = "Breed cannot be null")
        @Size(max = 100, message = "Breed must not exceed 100 characters")
        String breed,
        @Size(max = 300, message = "Notes must not exceed 300 characters")
        String notes) {}
