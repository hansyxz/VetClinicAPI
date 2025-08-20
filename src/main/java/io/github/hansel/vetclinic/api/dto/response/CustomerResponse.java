package io.github.hansel.vetclinic.api.dto.response;

import io.github.hansel.vetclinic.api.entity.Customer;
import io.github.hansel.vetclinic.api.entity.common.Address;

public record CustomerResponse(Long id, String name, String phone, String email, Address address) {
    public CustomerResponse(Customer customer) {
        this(customer.getId(), customer.getName(), customer.getPhone(), customer.getEmail(), customer.getAddress());
    }
}
