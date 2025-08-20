package io.github.hansel.vetclinic.api.entity;

import io.github.hansel.vetclinic.api.dto.request.PetRequest;
import io.github.hansel.vetclinic.api.entity.enums.Gender;
import io.github.hansel.vetclinic.api.entity.enums.Species;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table(name = "pet")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private BigDecimal weightKg;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Species species;
    private String otherSpecies;
    private String breed;
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer owner;

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
    }
}
