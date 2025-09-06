package io.github.hansel.vetclinic.api.dto.pet;

import io.github.hansel.vetclinic.api.entity.Pet;
import io.github.hansel.vetclinic.api.entity.enums.Gender;
import io.github.hansel.vetclinic.api.entity.enums.Species;

import java.math.BigDecimal;

public record PetDetailResponse(Long ownerId, Long id, String name, String age, BigDecimal weightKg, Gender gender,
                                Species species, String otherSpecies, String breed, String notes) {
        public PetDetailResponse(Pet pet){
                this(pet.getOwner().getId(), pet.getId(), pet.getName(), formatAge(pet.getAge()), pet.getWeightKg(),
                        pet.getGender(), pet.getSpecies(), pet.getOtherSpecies(), pet.getBreed(), pet.getNotes());
        }

        private static String formatAge(Integer age) {
                if (age == null) return null;
                int months = age * 12;
                return age + " years (" + months + " months)";
        }
}
