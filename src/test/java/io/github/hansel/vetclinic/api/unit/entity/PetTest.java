package io.github.hansel.vetclinic.api.unit.entity;

import io.github.hansel.vetclinic.api.dto.request.PetRequest;
import io.github.hansel.vetclinic.api.entity.Customer;
import io.github.hansel.vetclinic.api.entity.Pet;
import io.github.hansel.vetclinic.api.entity.enums.Gender;
import io.github.hansel.vetclinic.api.entity.enums.Species;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import io.github.hansel.vetclinic.api.exception.BusinessValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PetTest {

    @Test
    void shouldCreatePetWithDTO() {
        Customer owner = new Customer();
        PetRequest dto = new PetRequest(
                1L,
                "Chico",
                4,
                new BigDecimal("3.50"),
                Gender.MALE,
                Species.CAT,
                null,
                "Ragamuffin",
                "Lorem ipsum dolor sit amet"
        );
        Pet pet = new Pet(dto, owner);

        assertEquals("Chico", pet.getName());
        assertEquals(4, pet.getAge());
        assertEquals(new BigDecimal("3.50"), pet.getWeightKg());
        assertEquals(Gender.MALE, pet.getGender());
        assertEquals(Species.CAT, pet.getSpecies());
        assertEquals("Ragamuffin", pet.getBreed());
        assertEquals("Lorem ipsum dolor sit amet", pet.getNotes());
        assertEquals(owner, pet.getOwner());
        assertTrue(pet.isActive());
    }

    @Test
    void shouldUpdatePetPartially() {
        Customer owner = new Customer();
        PetRequest dto = new PetRequest(1L, "Chico", 4, new BigDecimal("3.50"), Gender.MALE,
                Species.CAT, null, "Ragamuffin", "Lorem ipsum dolor sit amet");
        Pet pet = new Pet(dto, owner);

        PetRequest updateDto = new PetRequest(1L, null, 5, null, null, null, null, "Ragdoll", null);
        pet.update(updateDto);

        assertEquals("Chico", pet.getName()); // unchanged
        assertEquals(5, pet.getAge()); // updated
        assertEquals("Ragdoll", pet.getBreed()); // updated
    }

    @Test
    void shouldDeactivateAndActivatePet() {
        Customer owner = new Customer();
        PetRequest dto = new PetRequest(1L, "Chico", 4, new BigDecimal("3.50"), Gender.MALE,
                Species.CAT, null, "Ragamuffin", "Lorem ipsum dolor sit amet");
        Pet pet = new Pet(dto, owner);

        pet.deactivate();
        assertFalse(pet.isActive());

        pet.activate();
        assertTrue(pet.isActive());
    }

    @Test
    void shouldThrowWhenDeactivatingInactivePet() {
        Customer owner = new Customer();
        PetRequest dto = new PetRequest(1L, "Chico", 4, new BigDecimal("3.50"), Gender.MALE,
                Species.CAT, null, "Ragamuffin", "Lorem ipsum dolor sit amet");
        Pet pet = new Pet(dto, owner);
        pet.deactivate();

        BadRequestException exception = assertThrows(BadRequestException.class, pet::deactivate);
        assertEquals("Pet is already inactive", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenActivatingActivePet() {
        Customer owner = new Customer();
        PetRequest dto = new PetRequest(1L, "Chico", 4, new BigDecimal("3.50"), Gender.MALE,
                Species.CAT, null, "Ragamuffin", "Lorem ipsum dolor sit amet");
        Pet pet = new Pet(dto, owner);

        BadRequestException exception = assertThrows(BadRequestException.class, pet::activate);
        assertEquals("Pet is already active", exception.getFieldErrors().get(0).message());
    }
}
