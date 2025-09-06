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
        this.street = dto.street();
        this.number = dto.number();
        this.neighborhood = dto.neighborhood();
        this.postalCode = dto.postalCode();
        this.complement = dto.complement();
    }

    public void update(AddressRequest dto) {
        this.street = dto.street() != null ? dto.street() : street;
        this.number = dto.number() != null ? dto.number() : number;
        this.neighborhood = dto.neighborhood() != null ? dto.neighborhood() : neighborhood;
        this.postalCode = dto.postalCode() != null ? dto.postalCode() : postalCode;
        this.complement = dto.complement() != null ? dto.complement() : complement;
    }
}
