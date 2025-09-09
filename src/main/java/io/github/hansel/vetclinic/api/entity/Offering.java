package io.github.hansel.vetclinic.api.entity;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import io.github.hansel.vetclinic.api.dto.offering.OfferingRequest;
import io.github.hansel.vetclinic.api.entity.enums.Category;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Table(
        name = "offerings",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_offering_name", columnNames = "name")
        }
)
@Entity(name = "Offering")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Offering {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    private int durationMinutes;

    @ManyToMany(mappedBy = "offerings")
    private List<Employee> employees = new ArrayList<>();

    @Column(nullable = false)
    private boolean active;

    public Offering(OfferingRequest dto) {
        this.name = dto.name();
        this.description = dto.description();
        this.category = dto.category();
        this.price = dto.price().setScale(2, RoundingMode.HALF_UP);
        this.durationMinutes = dto.durationMinutes();
        this.active = true;
    }

    public void update(OfferingRequest dto) {
        this.name = dto.name() != null ? dto.name() : name;
        this.description = dto.description() != null ? dto.description() : description;
        this.category = dto.category() != null ? dto.category() : category;
        this.price = dto.price() != null ? dto.price().setScale(2, RoundingMode.HALF_UP) : price;
        this.durationMinutes = dto.durationMinutes();
    }

    public void deactivate() {
        if (!active) {
            throw new BadRequestException(
                    List.of(new ErrorResponse.FieldErrorResponse("active", "Offering is already inactive"))
            );
        }
        active = false;
    }

    public void activate() {
        if (active) {
            throw new BadRequestException(
                    List.of(new ErrorResponse.FieldErrorResponse("active", "OfferingOffering is already active"))
            );
        }
        active = true;
    }
}
