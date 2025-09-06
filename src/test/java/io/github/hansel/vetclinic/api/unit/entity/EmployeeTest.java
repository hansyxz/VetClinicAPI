package io.github.hansel.vetclinic.api.unit.entity;

import io.github.hansel.vetclinic.api.dto.employee.EmployeeRequest;
import io.github.hansel.vetclinic.api.entity.Employee;
import io.github.hansel.vetclinic.api.entity.enums.Role;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    private EmployeeRequest request;
    private Employee employee;

    @BeforeEach
    void setUp() {
        request = new EmployeeRequest("Ana Silva", Role.VET, "12345-XX", "11911111111", "ana.silva@example.com");
        employee = new Employee(request);
    }

    @Test
    void shouldCreateEmployeeWithDTO() {
        assertEquals("Ana Silva", employee.getName());
        assertEquals(Role.VET, employee.getRole());
        assertEquals("12345-XX", employee.getCrmv());
        assertEquals("11911111111", employee.getPhone());
        assertEquals("ana.silva@example.com", employee.getEmail());
        assertTrue(employee.isActive());
    }

    @Test
    void shouldUpdateEmployeePartially() {
        EmployeeRequest updateRequest = new EmployeeRequest(null, null, null, "11922222222", "ana@example.com");
        employee.update(updateRequest);

        assertEquals("Ana Silva", employee.getName()); // unchanged
        assertEquals("11922222222", employee.getPhone()); // updated
        assertEquals("ana@example.com", employee.getEmail()); // updated
    }

    @Test
    void shouldNotUpdateRoleOrCrmv() {
        EmployeeRequest updateRequest = new EmployeeRequest("Ana Silva", Role.GROOMER, "99999-YY", null, null);
        employee.update(updateRequest);

        assertEquals(Role.VET, employee.getRole()); // unchanged
        assertEquals("12345-XX", employee.getCrmv()); // unchanged
    }

    @Test
    void shouldDeactivateAndActivateEmployee() {
        employee.deactivate();
        assertFalse(employee.isActive());

        employee.activate();
        assertTrue(employee.isActive());
    }

    @Test
    void shouldThrowWhenDeactivatingInactiveCustomer() {
        employee.deactivate();

        BadRequestException exception = assertThrows(BadRequestException.class, employee::deactivate);
        assertEquals("Employee is already inactive", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenActivatingActiveCustomer() {
        BadRequestException exception = assertThrows(BadRequestException.class, employee::activate);
        assertEquals("Employee is already active", exception.getFieldErrors().get(0).message());
    }
}
