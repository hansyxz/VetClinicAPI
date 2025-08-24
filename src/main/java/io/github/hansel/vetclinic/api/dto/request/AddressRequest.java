package io.github.hansel.vetclinic.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressRequest(
        @NotBlank(message = "Street cannot be blank")
        String street,
        @NotBlank(message = "Number cannot be blank")
        String number,
        @NotBlank(message = "Neighborhood cannot be blank")
        String neighborhood,
        @NotBlank(message = "PostalCode cannot be blank")
        @Pattern(regexp = "\\d{8}", message = "PostalCode must have exactly 8 digits")
        String postalCode,
        String complement) {}
