package io.github.hansel.vetclinic.api.dto.request;

import io.github.hansel.vetclinic.api.entity.enums.Category;
import jakarta.validation.constraints.*;

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
        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Price format invalid")
        BigDecimal price) {}
