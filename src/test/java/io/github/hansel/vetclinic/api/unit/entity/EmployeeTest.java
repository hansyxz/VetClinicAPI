package io.github.hansel.vetclinic.api.unit.entity;

import io.github.hansel.vetclinic.api.dto.request.EmployeeRequest;
import io.github.hansel.vetclinic.api.entity.Employee;
import io.github.hansel.vetclinic.api.entity.enums.Role;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import io.github.hansel.vetclinic.api.exception.BusinessValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    @Test
    void shouldCreateEmployeeWithDTO() {
        EmployeeRequest dto = new EmployeeRequest(
                "Ana Silva",
                Role.VET,
                "12345-XX",
                "11911111111",
                "ana.silva@example.com"
        );
        Employee employee = new Employee(dto);

        assertEquals("Ana Silva", employee.getName());
        assertEquals(Role.VET, employee.getRole());
        assertEquals("12345-XX", employee.getCrmv());
        assertEquals("11911111111", employee.getPhone());
        assertEquals("ana.silva@example.com", employee.getEmail());
        assertTrue(employee.isActive());
    }

    @Test
    void shouldThrowWhenNonVetHasCrmv() {
        EmployeeRequest invalidDto = new EmployeeRequest(
                "Ana Silva",
                Role.GROOMER,
                "12345-XX",
                "11911111111",
                "ana.silva@example.com"
        );

        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> new Employee(invalidDto));
        assertEquals("CRMV is only allowed for employees with role VET", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldUpdateEmployeePartially() {
        EmployeeRequest dto = new EmployeeRequest("Ana Silva", Role.VET, "12345-XX", "11911111111", "ana.silva@example.com");
        Employee employee = new Employee(dto);

        EmployeeRequest updateDto = new EmployeeRequest(
                null,
                null,
                null,
                "11922222222",
                "ana@example.com"
        );
        employee.update(updateDto);

        assertEquals("Ana Silva", employee.getName()); // unchanged
        assertEquals(Role.VET, employee.getRole()); // unchanged
        assertEquals("12345-XX", employee.getCrmv()); // unchanged
        assertEquals("11922222222", employee.getPhone()); // updated
        assertEquals("ana@example.com", employee.getEmail()); // updated
    }

    @Test
    void shouldNotUpdateRoleOrCrmv() {
        EmployeeRequest dto = new EmployeeRequest("Ana Silva", Role.VET, "12345-XX", "11911111111", "ana.silva@example.com");
        Employee employee = new Employee(dto);

        EmployeeRequest updateDto = new EmployeeRequest("Ana Silva", Role.GROOMER, "99999-YY", null, null);
        employee.update(updateDto);

        assertEquals(Role.VET, employee.getRole()); // unchanged
        assertEquals("12345-XX", employee.getCrmv()); // unchanged
    }

    @Test
    void shouldDeactivateAndActivateEmployee() {
        EmployeeRequest dto = new EmployeeRequest("Ana Silva", Role.VET, "12345-XX", "11911111111", "ana.silva@example.com");
        Employee employee = new Employee(dto);

        employee.deactivate();
        assertFalse(employee.isActive());

        employee.activate();
        assertTrue(employee.isActive());
    }

    @Test
    void shouldThrowWhenDeactivatingInactiveCustomer() {
        EmployeeRequest dto = new EmployeeRequest("Ana Silva", Role.VET, "12345-XX", "11911111111", "ana.silva@example.com");
        Employee employee = new Employee(dto);
        employee.deactivate();

        BadRequestException exception = assertThrows(BadRequestException.class, employee::deactivate);
        assertEquals("Employee is already inactive", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenActivatingActiveCustomer() {
        EmployeeRequest dto = new EmployeeRequest("Ana Silva", Role.VET, "12345-XX", "11911111111", "ana.silva@example.com");
        Employee employee = new Employee(dto);

        BadRequestException exception = assertThrows(BadRequestException.class, employee::activate);
        assertEquals("Employee is already active", exception.getFieldErrors().get(0).message());
    }
}
