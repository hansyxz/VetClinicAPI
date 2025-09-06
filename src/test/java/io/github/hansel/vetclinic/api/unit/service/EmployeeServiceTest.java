package io.github.hansel.vetclinic.api.unit.service;

import io.github.hansel.vetclinic.api.dto.employee.EmployeeRequest;
import io.github.hansel.vetclinic.api.dto.employee.EmployeeDetailResponse;
import io.github.hansel.vetclinic.api.entity.Employee;
import io.github.hansel.vetclinic.api.entity.enums.Role;
import io.github.hansel.vetclinic.api.exception.BusinessValidationException;
import io.github.hansel.vetclinic.api.exception.NotFoundException;
import io.github.hansel.vetclinic.api.repository.EmployeeRepository;
import io.github.hansel.vetclinic.api.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    private EmployeeRequest request;
    private Employee employee;

    @BeforeEach
    void setUp() {
        request = new EmployeeRequest("Ana Silva", Role.VET, "12345-XX", "11911111111", "ana.silva@example.com");
        employee = spy(new Employee(request));
    }

    @Test
    void shouldCreateEmployee() {
        when(repository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDetailResponse response = service.create(request);

        assertEquals("Ana Silva", response.name());
        verify(repository).save(any(Employee.class));
    }

    @Test
    void shouldThrowWhenCreatingWithDuplicatePhone() {
        when(repository.existsByPhone(request.phone())).thenReturn(true);

        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> service.create(request));

        assertEquals("Phone already exists", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenCreatingWithDuplicateEmail() {
        when(repository.existsByEmail(request.email())).thenReturn(true);

        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> service.create(request));

        assertEquals("Email already exists", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenCreatingWithCrmvForNonVet() {
        EmployeeRequest requestNonVet = new EmployeeRequest("Ana Silva", Role.GROOMER, "12345-XX", "11911111111", "ana.silva@example.com");

        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> service.create(requestNonVet));

        assertEquals("CRMV is only allowed for employees with role VET", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenCreatingWithDuplicateCrmv() {
        when(repository.existsByCrmv("12345-XX")).thenReturn(true);

        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> service.create(request));

        assertEquals("CRMV already exists", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldFindEmployeeById() {
        when(repository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(employee));

        EmployeeDetailResponse response = service.findByIdAndActiveTrue(1L);

        assertEquals("Ana Silva", response.name());
    }

    @Test
    void shouldThrowWhenEmployeeNotFoundById() {
        when(repository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findByIdAndActiveTrue(1L));

        assertEquals("Employee not found with id 1", exception.getMessage());
    }

    @Test
    void shouldUpdateEmployee() {
        when(repository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(employee));

        EmployeeDetailResponse response = service.update(request, 1L);

        assertNotNull(response);
        verify(repository).findByIdAndActiveTrue(1L);
        verify(employee).update(any(EmployeeRequest.class));
    }

    @Test
    void shouldThrowWhenUpdatingCrmv() {
        when(repository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(employee));

        EmployeeRequest requestUpdate = new EmployeeRequest("Ana Silva", Role.VET, "12345-YY", "11911111111", "ana@example.com");

        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> service.update(requestUpdate, 1L));

        assertEquals("CRMV cannot be changed", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenUpdatingRole() {
        EmployeeRequest requestUpdate = new EmployeeRequest("Ana Silva", Role.GROOMER, "12345-XX", "11911111111", "ana@example.com");

        BusinessValidationException exception = assertThrows(BusinessValidationException.class, () -> service.validateRoleAndCrmvUpdate(employee, requestUpdate));

        assertEquals("Role cannot be changed", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldDeactivateEmployee() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        service.deactivate(1L);

        verify(employee).deactivate();
    }

    @Test
    void shouldActivateEmployee() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        service.deactivate(1L);
        service.activate(1L);

        verify(employee).activate();
    }
}
