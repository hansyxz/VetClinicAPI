package io.github.hansel.vetclinic.api.entity;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import io.github.hansel.vetclinic.api.dto.pet.PetRequest;
import io.github.hansel.vetclinic.api.entity.enums.Gender;
import io.github.hansel.vetclinic.api.entity.enums.Species;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Table(name = "pets")
@Entity(name = "Pet")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 120, message = "Age cannot exceed 120 years")
    private int age;

    @Column(precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Species species;

    @Column(length = 30)
    private String otherSpecies;

    @Column( length = 30)
    private String breed;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer owner;

    @Column(nullable = false)
    private boolean active;

    public Pet(PetRequest dto, Customer customer) {
        this.name = dto.name();
        this.age = dto.age();
        this.weightKg = dto.weightKg();
        this.gender = dto.gender();
        this.species = dto.species();
        this.otherSpecies = dto.otherSpecies();
        this.breed = dto.breed();
        this.notes = dto.notes();
        this.owner = customer;
        this.active = true;
    }

    public void update(PetRequest dto) {
        this.name = dto.name() != null ? dto.name() : name;
        this.age = dto.age();
        this.weightKg = dto.weightKg() != null ? dto.weightKg() : weightKg;
        this.gender = dto.gender() != null ? dto.gender() : gender;
        this.species = dto.species() != null ? dto.species() : species;
        this.otherSpecies = dto.otherSpecies() != null ? dto.otherSpecies() : otherSpecies;
        this.breed = dto.breed() != null ? dto.breed() : breed;
        this.notes = dto.notes() != null ? dto.notes() : notes;
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
