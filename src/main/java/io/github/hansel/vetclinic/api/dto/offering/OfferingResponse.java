package io.github.hansel.vetclinic.api.dto.offering;

import io.github.hansel.vetclinic.api.entity.Offering;
import io.github.hansel.vetclinic.api.entity.enums.Category;

import java.math.BigDecimal;

public record OfferingResponse(Long id, String name, String description, Category category, BigDecimal price) {
    public OfferingResponse(Offering offering) {
        this(offering.getId(), offering.getName(), offering.getDescription(), offering.getCategory(), offering.getPrice());
    }
}
