package io.github.hansel.vetclinic.api.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank(message = "Street cannot be blank")
        @Size(max = 100, message = "Street must not exceed 100 characters")
        String street,
        @NotBlank(message = "Number cannot be blank")
        @Size(max = 10, message = "Number must not exceed 10 characters")
        String number,
        @NotBlank(message = "Neighborhood cannot be blank")
        @Size(max = 100, message = "Neighborhood must not exceed 100 characters")
        String neighborhood,
        @NotBlank(message = "PostalCode cannot be blank")
        @Pattern(regexp = "\\d{8}", message = "PostalCode must have exactly 8 digits")
        String postalCode,
        @Size(max = 100, message = "Complement must not exceed 100 characters")
        String complement) {}
