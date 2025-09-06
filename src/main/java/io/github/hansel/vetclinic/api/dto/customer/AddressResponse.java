package io.github.hansel.vetclinic.api.dto.customer;

import io.github.hansel.vetclinic.api.entity.common.Address;

public record AddressResponse(String street, String number, String neighborhood,
                              String postalCode, String complement) {
    public AddressResponse(Address address) {
        this(address.getStreet(), address.getNumber(), address.getNeighborhood(),
                address.getPostalCode(), address.getComplement());
    }
}