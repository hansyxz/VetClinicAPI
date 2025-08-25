package io.github.hansel.vetclinic.api.entity;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import io.github.hansel.vetclinic.api.dto.request.PetRequest;
import io.github.hansel.vetclinic.api.entity.enums.Gender;
import io.github.hansel.vetclinic.api.entity.enums.Species;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Table(name = "pet")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Species species;

    @Column(length = 100)
    private String otherSpecies;

    @Column(nullable = false, length = 100)
    private String breed;

    @Column(length = 300)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer owner;

    @Column(nullable = false)
    private boolean active;

    public Pet(PetRequest dto, Customer customer) {
        name = dto.name();
        age = dto.age();
        weightKg = dto.weightKg();
        gender = dto.gender();
        species = dto.species();
        otherSpecies = dto.otherSpecies();
        breed = dto.breed();
        notes = dto.notes();
        owner = customer;
        active = true;
    }

    public void update(PetRequest dto) {
        name = dto.name() != null ? dto.name() : name;
        age = dto.age() != null ? dto.age() : age;
        weightKg = dto.weightKg() != null ? dto.weightKg() : weightKg;
        gender = dto.gender() != null ? dto.gender() : gender;
        species = dto.species() != null ? dto.species() : species;
        otherSpecies = dto.otherSpecies() != null ? dto.otherSpecies() : otherSpecies;
        breed = dto.breed() != null ? dto.breed() : breed;
        notes = dto.notes() != null ? dto.notes() : notes;
    }

    public void deactivate() {
        if (!active) {
            throw new BadRequestException(
                    List.of(new ErrorResponse.FieldErrorResponse("active", "Pet is already inactive"))
            );
        }
        active = false;
    }

    public void activate() {
        if (active) {
            throw new BadRequestException(
                    List.of(new ErrorResponse.FieldErrorResponse("active", "Pet is already active"))
            );
        }
        active = true;
    }
}
