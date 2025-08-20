package io.github.hansel.vetclinic.api.entity;

import io.github.hansel.vetclinic.api.dto.request.CustomerRequest;
import io.github.hansel.vetclinic.api.entity.common.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Table(name = "customer")
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

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    public Customer(CustomerRequest dto) {
        name = dto.name();
        phone = dto.phone();
        email = dto.email();
        address = dto.address();
    }
}
