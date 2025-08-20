package io.github.hansel.vetclinic.api.dto.request;

import io.github.hansel.vetclinic.api.entity.enums.Gender;
import io.github.hansel.vetclinic.api.entity.enums.Species;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PetRequest(
        @NotNull
        Long ownerId,
        @NotBlank
        String name,
        @NotNull
        Integer age,
        BigDecimal weightKg,
        @NotNull
        Gender gender,
        @NotNull
        Species species,
        String otherSpecies,
        String breed,
        String notes) {
}
