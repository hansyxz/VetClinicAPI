package io.github.hansel.vetclinic.api.entity;

import io.github.hansel.vetclinic.api.dto.request.EmployeeRequest;
import io.github.hansel.vetclinic.api.entity.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_employee_phone", columnNames = "phone"),
                @UniqueConstraint(name = "uk_employee_email", columnNames = "email")
        }
)
@Entity(name = "Employee")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Employee {
    private Long id;
    private String name;
    private Role role;
    private String crmv;
    private String phone;
    private String email;
    private boolean active;

    public Employee(EmployeeRequest dto) {
        name = dto.name();
        role = dto.role();
        crmv = dto.crmv();
        phone = dto.phone();
        email = dto.email();
    }
}
