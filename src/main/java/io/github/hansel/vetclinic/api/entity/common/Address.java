package io.github.hansel.vetclinic.api.entity.common;

import io.github.hansel.vetclinic.api.dto.request.AddressRequest;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String street;
    private String number;
    private String neighborhood;
    private String postalCode;
    private String complement;

    public Address(AddressRequest request) {
        street = request.street();
        number = request.number();
        neighborhood = request.neighborhood();
        postalCode = request.postalCode();
        complement = request.complement();
    }
}
