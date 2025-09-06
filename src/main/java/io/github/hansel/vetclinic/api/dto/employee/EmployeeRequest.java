package io.github.hansel.vetclinic.api.dto.employee;

import io.github.hansel.vetclinic.api.entity.enums.Role;
import jakarta.validation.constraints.*;

public record EmployeeRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,
        @NotNull(message = "Role is required")
        Role role,
        @Size(max = 20, message = "CRMV must not exceed 20 characters")
        @Pattern(regexp = "^[0-9]{4,6}-[A-Z]{2}$", message = "CRMV must be in the format 12345-XX")
        String crmv,
        @NotBlank(message = "Phone cannot be blank")
        @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must have 11 digits")
        String phone,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        String email) {}
