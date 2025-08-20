package io.github.hansel.vetclinic.api.dto.response;

import io.github.hansel.vetclinic.api.entity.Customer;

import java.util.List;

public record CustomerDetailResponse(Long id, String name, String phone, String email, AddressResponse address, List<PetResponse> pets) {
    public CustomerDetailResponse(Customer customer) {
        this(customer.getId(), customer.getName(), customer.getPhone(), customer.getEmail(),
                new AddressResponse(customer.getAddress()),
                customer.getPets().stream().map(PetResponse::new).toList());
    }
}
