package io.github.hansel.vetclinic.api.unit.service;

import io.github.hansel.vetclinic.api.dto.request.PetRequest;
import io.github.hansel.vetclinic.api.dto.response.PetDetailResponse;
import io.github.hansel.vetclinic.api.entity.Customer;
import io.github.hansel.vetclinic.api.entity.Pet;
import io.github.hansel.vetclinic.api.entity.enums.Gender;
import io.github.hansel.vetclinic.api.entity.enums.Species;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import io.github.hansel.vetclinic.api.exception.ForbiddenOperationException;
import io.github.hansel.vetclinic.api.exception.NotFoundException;
import io.github.hansel.vetclinic.api.repository.CustomerRepository;
import io.github.hansel.vetclinic.api.repository.PetRepository;
import io.github.hansel.vetclinic.api.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private PetService service;

    private Customer owner;
    private PetRequest request;
    private Pet pet;

    @BeforeEach
    void setUp() {
        owner = mock(Customer.class);
        request = new PetRequest(1L, "Chico", 4, new BigDecimal("3.50"), Gender.MALE,
                Species.CAT, null, "Ragamuffin", "Lorem ipsum dolor sit amet");
        pet = spy(new Pet(request, owner));
    }

    @Test
    void shouldCreatePet() {
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(customerRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(owner));

        PetDetailResponse response = service.create(request);

        assertEquals("Chico", response.name());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void shouldThrowWhenCustomerDoesNotExistOnCreate() {
        when(customerRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.create(request));

        assertEquals("Customer not found with id 1", exception.getMessage());
    }

    @Test
    void shouldThrowWhenOtherSpeciesProvidedButSpeciesIsNotOther() {
        PetRequest requestOtherSpeciesForNonOther = new PetRequest(1L, "Chico", 4, new BigDecimal("3.50"), Gender.MALE,
                Species.CAT, "Lorem ipsum", "Ragamuffin", "Lorem ipsum dolor sit amet");

        when(customerRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(owner));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.create(requestOtherSpeciesForNonOther));

        assertEquals("The field 'otherSpecies' can only be used when species is set to OTHER", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldThrowWhenOtherSpeciesIsMissing() {
        PetRequest requestOtherSpeciesMissing = new PetRequest(1L, "Chico", 4, new BigDecimal("3.50"), Gender.MALE,
                Species.OTHER, null, "Ragamuffin", "Lorem ipsum dolor sit amet");

        when(customerRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(owner));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.create(requestOtherSpeciesMissing));

        assertEquals("You must provide 'otherSpecies' when species is set to OTHER", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldFindPetById() {
        when(petRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(pet));

        PetDetailResponse response = service.findByIdAndActiveTrue(1L);

        assertEquals("Chico", response.name());
    }

    @Test
    void shouldThrowNotFoundWhenPetDoesNotExist() {
        when(petRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findByIdAndActiveTrue(1L));

        assertEquals("Pet not found with id 1", exception.getMessage());
    }

    @Test
    void shouldUpdatePet() {
        when(petRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(pet));
        when(customerRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(owner));

        PetDetailResponse response = service.update(request, 1L);

        assertNotNull(response);
        verify(petRepository).findByIdAndActiveTrue(1L);
        verify(pet).update(any(PetRequest.class));
    }

    @Test
    void shouldThrowWhenPetDoesNotBelongToCustomer() {
        Customer anotherCustomer = mock(Customer.class);

        when(owner.getId()).thenReturn(1L);
        when(anotherCustomer.getId()).thenReturn(2L);

        ForbiddenOperationException exception = assertThrows(ForbiddenOperationException.class, () -> service.validateOwnership(pet, anotherCustomer));

        assertEquals("Pet does not belong to this customer", exception.getFieldErrors().get(0).message());
    }

    @Test
    void shouldDeactivatePet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        service.deactivate(1L);

        verify(pet).deactivate();
    }

    @Test
    void shouldActivatePet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        service.deactivate(1L);
        service.activate(1L);

        verify(pet).activate();
    }
}
