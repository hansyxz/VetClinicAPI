package io.github.hansel.vetclinic.api.entity.common;

import io.github.hansel.vetclinic.api.dto.request.AddressRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 10)
    private String number;

    @Column(nullable = false, length = 100)
    private String neighborhood;

    @Column(nullable = false, length = 8)
    private String postalCode;

    @Column(length = 100)
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
