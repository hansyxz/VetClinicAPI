package io.github.hansel.vetclinic.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record CustomerRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,
        @NotBlank(message = "Phone cannot be blank")
        @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must have 10 or 11 digits")
        String phone,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,
        @NotNull(message = "Address is required")
        @Valid
        AddressRequest address) {}