package io.github.hansel.vetclinic.api.dto.request;

import io.github.hansel.vetclinic.api.entity.enums.Category;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OfferingRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,
        @NotBlank(message = "Description cannot be blank")
        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,
        @NotNull(message = "Category cannot be null")
        Category category,
        @DecimalMin(value = "0", message = "Price must be positive")
        BigDecimal price) {}
