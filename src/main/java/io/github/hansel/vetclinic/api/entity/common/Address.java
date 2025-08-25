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

    public void update(AddressRequest dto) {
        street = dto.street() != null ? dto.street() : street;
        number = dto.number() != null ? dto.number() : number;
        neighborhood = dto.neighborhood() != null ? dto.neighborhood() : neighborhood;
        postalCode = dto.postalCode() != null ? dto.postalCode() : postalCode;
        complement = dto.complement() != null ? dto.complement() : complement;
    }
}
