package io.github.hansel.vetclinic.api.service;

import io.github.hansel.vetclinic.api.dto.request.CustomerRequest;
import io.github.hansel.vetclinic.api.dto.response.CustomerDetailResponse;
import io.github.hansel.vetclinic.api.dto.response.CustomerSummaryResponse;
import io.github.hansel.vetclinic.api.entity.Customer;
import io.github.hansel.vetclinic.api.exception.NotFoundException;
import io.github.hansel.vetclinic.api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    public CustomerRepository repository;

    public CustomerDetailResponse create(CustomerRequest request) {
        var customer = new Customer(request);
        repository.save(customer);

        return new CustomerDetailResponse(customer);
    }

    public Page<CustomerSummaryResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(CustomerSummaryResponse::new);
    }

    public CustomerDetailResponse findById(Long id) {
        var customer = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + id));

        return new CustomerDetailResponse(customer);
    }
}
