package io.github.hansel.vetclinic.api.service;

import io.github.hansel.vetclinic.api.dto.request.PetRequest;
import io.github.hansel.vetclinic.api.dto.response.PetResponse;
import io.github.hansel.vetclinic.api.entity.Pet;
import io.github.hansel.vetclinic.api.entity.enums.Species;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import io.github.hansel.vetclinic.api.exception.NotFoundException;
import io.github.hansel.vetclinic.api.repository.CustomerRepository;
import io.github.hansel.vetclinic.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PetService {

    @Autowired
    public PetRepository petRepository;

    @Autowired
    public CustomerRepository customerRepository;

    public PetResponse create(PetRequest request) {

        var customer = customerRepository.findById(request.ownerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + request.ownerId()));

        validateSpecies(request);
        validateAge(request.age());
        validateWeight(request.weightKg());

        var pet = new Pet(request, customer);
        petRepository.save(pet);

        return new PetResponse(pet);
    }

    private void validateSpecies(PetRequest request) {
        if (request.species() == Species.OTHER && (request.otherSpecies() == null || request.otherSpecies().isBlank())) {
            throw new BadRequestException("You must provide 'otherSpecies' when species is set to OTHER");
        }

        if (request.species() != Species.OTHER && request.otherSpecies() != null) {
            throw new BadRequestException("The field 'otherSpecies' can only be used when species is set to OTHER");
        }
    }

    private void validateAge(Integer age) {
        if (age < 0) {
            throw new BadRequestException("Age cannot be negative");
        }
        if (age > 120) {
            throw new BadRequestException("Age cannot exceed 120 years");
        }
    }

    private void validateWeight(BigDecimal weightKg) {
        if (weightKg != null) {
            if (weightKg.doubleValue() <= 0) {
                throw new BadRequestException("Weight must be greater than zero");
            }
            if (weightKg.doubleValue() > 100) {
                throw new BadRequestException("Weight cannot exceed 100 kg");
            }
        }
    }
}
