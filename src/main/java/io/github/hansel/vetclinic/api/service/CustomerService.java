package io.github.hansel.vetclinic.api.service;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import io.github.hansel.vetclinic.api.dto.request.CustomerRequest;
import io.github.hansel.vetclinic.api.dto.response.CustomerDetailResponse;
import io.github.hansel.vetclinic.api.dto.response.CustomerSummaryResponse;
import io.github.hansel.vetclinic.api.entity.Customer;
import io.github.hansel.vetclinic.api.exception.BusinessValidationException;
import io.github.hansel.vetclinic.api.exception.NotFoundException;
import io.github.hansel.vetclinic.api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    public CustomerRepository repository;

    public CustomerDetailResponse create(CustomerRequest request) {
        validateUniqueFieldsToCreate(request.phone(), request.email());

        var customer = new Customer(request);
        repository.save(customer);

        return new CustomerDetailResponse(customer);
    }

    public Page<CustomerSummaryResponse> findAllByActiveTrue(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(CustomerSummaryResponse::new);
    }

    public CustomerDetailResponse findByIdAndActiveTrue(Long id) {
        var customer = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + id));

        return new CustomerDetailResponse(customer);
    }

    public CustomerDetailResponse update(CustomerRequest request, Long id) {
        var customer = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + id));

        validateUniqueFieldsToUpdate(customer.getId(), request.phone(), request.email());
        customer.update(request);

        return new CustomerDetailResponse(customer);
    }

    public void deactivate(Long id) {
        var customer = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + id));

        customer.deactivate();
    }

    public void activate(Long id) {
        var customer = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + id));

        customer.activate();
    }

    public void validateUniqueFieldsToCreate(String phone, String email) {
        List<ErrorResponse.FieldErrorResponse> errors = new ArrayList<>();

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

    public void validateUniqueFieldsToUpdate(Long id, String phone, String email) {
        List<ErrorResponse.FieldErrorResponse> errors = new ArrayList<>();

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
}
