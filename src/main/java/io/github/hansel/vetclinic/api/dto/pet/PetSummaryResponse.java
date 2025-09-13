package io.github.hansel.vetclinic.api.dto.pet;

import io.github.hansel.vetclinic.api.entity.Pet;
import io.github.hansel.vetclinic.api.entity.enums.Gender;
import io.github.hansel.vetclinic.api.entity.enums.Species;

public record PetSummaryResponse(Long id, String name, int age, Species species, Gender gender) {
    public PetSummaryResponse(Pet pet) {
        this(pet.getId(), pet.getName(), pet.getAge(), pet.getSpecies(), pet.getGender());
    }
}
