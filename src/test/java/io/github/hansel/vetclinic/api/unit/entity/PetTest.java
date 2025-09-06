package io.github.hansel.vetclinic.api.unit.entity;

import io.github.hansel.vetclinic.api.dto.pet.PetRequest;
import io.github.hansel.vetclinic.api.entity.Customer;
import io.github.hansel.vetclinic.api.entity.Pet;
import io.github.hansel.vetclinic.api.entity.enums.Gender;
import io.github.hansel.vetclinic.api.entity.enums.Species;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PetTest {

    private Customer owner;
    private PetRequest request;
    private Pet pet;

    @BeforeEach
    void setUp() {
        owner = mock(Customer.class);
        request = new PetRequest(1L, "Chico", 4, new BigDecimal("3.50"), Gender.MALE,
                Species.CAT, null, "Ragamuffin", "Lorem ipsum dolor sit amet");
        pet = new Pet(request, owner);
    }

    @Test
    void shouldCreatePetWithDTO() {
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
        PetRequest updateRequest = new PetRequest(1L, null, 5, null, Gender.FEMALE, null,
                null, "Ragdoll", null);
        pet.update(updateRequest);

        assertEquals("Chico", pet.getName()); // unchanged
        assertEquals(5, pet.getAge()); // updated
        assertEquals(Gender.FEMALE, pet.getGender()); // updated
        assertEquals("Ragdoll", pet.getBreed()); // updated
    }

    @Test
    void shouldDeactivateAndActivatePet() {
        pet.deactivate();
        assertFalse(pet.isActive());

        pet.activate();
        assertTrue(pet.isActive());
    }

    @Test
    void shouldThrowWhenDeactivatingInactivePet() {
        pet.deactivate();

        BadRequestException exception = assertThrows(BadRequestException.class, pet::deactivate);
        assertEquals("Pet is already inactive", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenActivatingActivePet() {
        BadRequestException exception = assertThrows(BadRequestException.class, pet::activate);
        assertEquals("Pet is already active", exception.getFieldErrors().get(0).message());
    }
}
