package io.github.hansel.vetclinic.api.dto.response;

import io.github.hansel.vetclinic.api.entity.Employee;
import io.github.hansel.vetclinic.api.entity.enums.Role;

public record EmployeeDetailResponse (Long id, String name, Role role,
                                      String crmv, String phone, String email) {
    public EmployeeDetailResponse(Employee employee) {
            this(employee.getId(), employee.getName(), employee.getRole(), employee.getCrmv(),
                    employee.getPhone(), employee.getEmail());
        }
    }
