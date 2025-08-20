package io.github.hansel.vetclinic.api.dto.request;

import io.github.hansel.vetclinic.api.entity.common.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        @NotBlank
        String name,
        @NotBlank
        String phone,
        @NotBlank
        String email,
        @NotNull @Valid
        Address address) {
}
