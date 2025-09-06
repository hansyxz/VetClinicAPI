package io.github.hansel.vetclinic.api.entity;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import io.github.hansel.vetclinic.api.dto.request.EmployeeRequest;
import io.github.hansel.vetclinic.api.entity.enums.Role;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(length = 20)
    private String crmv;

    @Column(nullable = false, length = 11, unique = true)
    private String phone;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean active;

    public Employee(EmployeeRequest dto) {
        this.name = dto.name();
        this.role = dto.role();
        this.crmv = dto.crmv();
        this.phone = dto.phone();
        this.email = dto.email();
        this.active = true;
    }

    public void update(EmployeeRequest dto) {
        this.name = dto.name() != null ? dto.name() : name;
        this.phone = dto.phone() != null ? dto.phone() : phone;
        this.email = dto.email() != null ? dto.email() : email;
    }

    public void deactivate() {
        if (!active) {
            throw new BadRequestException(
                    List.of(new ErrorResponse.FieldErrorResponse("active", "Employee is already inactive"))
            );
        }
        active = false;
    }

    public void activate() {
        if (active) {
            throw new BadRequestException(
                    List.of(new ErrorResponse.FieldErrorResponse("active", "Employee is already active"))
            );
        }
        active = true;
    }
}
