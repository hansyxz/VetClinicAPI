package io.github.hansel.vetclinic.api.entity;

import io.github.hansel.vetclinic.api.dto.request.OfferingRequest;
import io.github.hansel.vetclinic.api.entity.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
    private boolean active;

    public Offering(OfferingRequest dto) {
        this.name = dto.name();
        this.description = dto.description();
        this.category = dto.category();
        this.price = dto.price();
        this.active = true;
    }
}
