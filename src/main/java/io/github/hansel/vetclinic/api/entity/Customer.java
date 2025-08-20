package io.github.hansel.vetclinic.api.entity;

import io.github.hansel.vetclinic.api.dto.request.CustomerRequest;
import io.github.hansel.vetclinic.api.entity.common.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "customers")
@Entity(name = "Customer")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String email;
    @Embedded
    private Address address;

    public Customer(CustomerRequest request) {
        name = request.name();
        phone = request.phone();
        email = request.email();
        address = request.address();
    }
}
