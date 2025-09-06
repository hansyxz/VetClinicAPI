package io.github.hansel.vetclinic.api.dto.response;

import io.github.hansel.vetclinic.api.entity.Employee;
import io.github.hansel.vetclinic.api.entity.enums.Role;

public record EmployeeSummaryResponse(Long id, String name, Role role) {
    public EmployeeSummaryResponse(Employee employee) {
        this(employee.getId(), employee.getName(), employee.getRole());
    }
}
