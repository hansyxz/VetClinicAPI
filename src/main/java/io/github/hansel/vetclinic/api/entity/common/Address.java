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

    public Address(AddressRequest dto) {
        street = dto.street();
        number = dto.number();
        neighborhood = dto.neighborhood();
        postalCode = dto.postalCode();
        complement = dto.complement();
    }
}
