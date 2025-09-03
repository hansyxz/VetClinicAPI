package io.github.hansel.vetclinic.api.entity;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import io.github.hansel.vetclinic.api.dto.request.CustomerRequest;
import io.github.hansel.vetclinic.api.entity.common.Address;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;


@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_phone", columnNames = "phone"),
                @UniqueConstraint(name = "uk_user_email", columnNames = "email")
        }
)
@Entity(name = "Customer")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 11, unique = true)
    private String phone;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "active = true")
    private List<Pet> pets = new ArrayList<>();

    @Column(nullable = false)
    private boolean active;

    public Customer(CustomerRequest dto) {
        name = dto.name();
        phone = dto.phone();
        email = dto.email();
        address = new Address(dto.address());
        active = true;
    }

    public void update(CustomerRequest dto) {
        name = dto.name() != null ? dto.name() : name;
        phone = dto.phone() != null ? dto.phone() : phone;
        email = dto.email() != null ? dto.email() : email;
        address.update(dto.address());
    }

    public void deactivate() {
        if (!active) {
            throw new BadRequestException(
                    List.of(new ErrorResponse.FieldErrorResponse("active", "Customer is already inactive"))
            );
        }
        active = false;
        pets.forEach(Pet::deactivate);
    }

    public void activate() {
        if (active) {
            throw new BadRequestException(
                    List.of(new ErrorResponse.FieldErrorResponse("active", "Customer is already active"))
            );
        }
        active = true;
    }
}
