package io.github.hansel.vetclinic.api.service;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import io.github.hansel.vetclinic.api.dto.employee.EmployeeRequest;
import io.github.hansel.vetclinic.api.dto.employee.EmployeeDetailResponse;
import io.github.hansel.vetclinic.api.dto.employee.EmployeeSummaryResponse;
import io.github.hansel.vetclinic.api.entity.Employee;
import io.github.hansel.vetclinic.api.entity.enums.Role;
import io.github.hansel.vetclinic.api.exception.BusinessValidationException;
import io.github.hansel.vetclinic.api.exception.NotFoundException;
import io.github.hansel.vetclinic.api.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    public EmployeeRepository repository;

    public EmployeeDetailResponse create(EmployeeRequest request) {
        validateFieldsToCreate(request.role(), request.crmv(), request.phone(), request.email());

        var eployee = new Employee(request);
        repository.save(eployee);

        return new EmployeeDetailResponse(eployee);
    }

    public Page<EmployeeSummaryResponse> findAllByActiveTrue(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(EmployeeSummaryResponse::new);
    }

    public EmployeeDetailResponse findByIdAndActiveTrue(Long id) {
        var employee = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id " + id));

        return new EmployeeDetailResponse(employee);
    }

    public EmployeeDetailResponse update(EmployeeRequest request, Long id) {
        var employee = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id " + id));

        validateUniqueFieldsToUpdate(employee.getId(), request.crmv(), request.phone(), request.email());
        validateRoleAndCrmvUpdate(employee, request);
        employee.update(request);

        return new EmployeeDetailResponse(employee);
    }

    public void deactivate(Long id) {
        var employee = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id " + id));

        employee.deactivate();
    }

    public void activate(Long id) {
        var employee = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id " + id));

        employee.activate();
    }

    public void validateFieldsToCreate(Role role, String crmv, String phone, String email) {
        List<ErrorResponse.FieldErrorResponse> errors = new ArrayList<>();

        if (role != Role.VET && crmv != null) {
            errors.add(new ErrorResponse.FieldErrorResponse("crmv", "CRMV is only allowed for employees with role VET"));
        }
        if (repository.existsByCrmv(crmv)) {
            errors.add(new ErrorResponse.FieldErrorResponse("crmv", "CRMV already exists"));
        }
        if (repository.existsByPhone(phone)) {
            errors.add(new ErrorResponse.FieldErrorResponse("phone", "Phone already exists"));
        }
        if (repository.existsByEmail(email)) {
            errors.add(new ErrorResponse.FieldErrorResponse("email", "Email already exists"));
        }
        if (!errors.isEmpty()) {
            throw new BusinessValidationException(errors);
        }
    }

    public void validateUniqueFieldsToUpdate(Long id, String crmv, String phone, String email) {
        List<ErrorResponse.FieldErrorResponse> errors = new ArrayList<>();

        repository.findByCrmv(crmv).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                errors.add(new ErrorResponse.FieldErrorResponse("crmv", "CRMV already exists"));
            }
        });
        repository.findByPhone(phone).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                errors.add(new ErrorResponse.FieldErrorResponse("phone", "Phone already exists"));
            }
        });
        repository.findByEmail(email).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                errors.add(new ErrorResponse.FieldErrorResponse("email", "Email already exists"));
            }
        });
        if (!errors.isEmpty()) {
            throw new BusinessValidationException(errors);
        }
    }

    public void validateRoleAndCrmvUpdate(Employee employee, EmployeeRequest request) {
        List<ErrorResponse.FieldErrorResponse> errors = new ArrayList<>();

        if (request.role() != null && !request.role().equals(employee.getRole())) {
            errors.add(new ErrorResponse.FieldErrorResponse("role", "Role cannot be changed"));
        }
        if (request.role() == Role.VET && request.crmv() != null && !request.crmv().equals(employee.getCrmv())) {
            errors.add(new ErrorResponse.FieldErrorResponse("crmv", "CRMV cannot be changed"));
        }
        if (!errors.isEmpty()) {
            throw new BusinessValidationException(errors);
        }
    }
}
