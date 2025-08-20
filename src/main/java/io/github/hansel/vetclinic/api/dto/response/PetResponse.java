package io.github.hansel.vetclinic.api.dto.response;

import io.github.hansel.vetclinic.api.entity.Pet;
import io.github.hansel.vetclinic.api.entity.enums.Gender;
import io.github.hansel.vetclinic.api.entity.enums.Species;

import java.math.BigDecimal;

public record PetResponse(Long ownerId, Long id, String name, Integer age, BigDecimal weightKg, Gender gender,
                          Species species, String otherSpecies, String breed, String notes) {
        public PetResponse(Pet pet){
                this(pet.getOwner().getId(), pet.getId(), pet.getName(), pet.getAge(), pet.getWeightKg(), pet.getGender(),
                        pet.getSpecies(), pet.getOtherSpecies(), pet.getBreed(), pet.getNotes());
        }
}
