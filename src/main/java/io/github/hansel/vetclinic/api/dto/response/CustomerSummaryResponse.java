package io.github.hansel.vetclinic.api.dto.response;

import io.github.hansel.vetclinic.api.entity.Customer;

public record CustomerSummaryResponse(
        Long id,
        String name,
        String phone,
        String email
) {
    public CustomerSummaryResponse(Customer customer) {
        this(customer.getId(), customer.getName(), customer.getPhone(), customer.getEmail());
    }
}
