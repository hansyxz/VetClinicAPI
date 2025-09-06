package io.github.hansel.vetclinic.api.service;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import io.github.hansel.vetclinic.api.dto.pet.PetRequest;
import io.github.hansel.vetclinic.api.dto.pet.PetDetailResponse;
import io.github.hansel.vetclinic.api.dto.pet.PetSummaryResponse;
import io.github.hansel.vetclinic.api.entity.Customer;
import io.github.hansel.vetclinic.api.entity.Pet;
import io.github.hansel.vetclinic.api.entity.enums.Species;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import io.github.hansel.vetclinic.api.exception.ForbiddenOperationException;
import io.github.hansel.vetclinic.api.exception.NotFoundException;
import io.github.hansel.vetclinic.api.repository.CustomerRepository;
import io.github.hansel.vetclinic.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {

    @Autowired
    public PetRepository petRepository;

    @Autowired
    public CustomerRepository customerRepository;

    public PetDetailResponse create(PetRequest request) {
        var customer = customerRepository.findByIdAndActiveTrue(request.ownerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + request.ownerId()));

        validateSpecies(request);

        var pet = new Pet(request, customer);
        petRepository.save(pet);

        return new PetDetailResponse(pet);
    }

    public Page<PetSummaryResponse> findAllByActiveTrue(Pageable pageable) {
        return petRepository.findAllByActiveTrue(pageable).map(PetSummaryResponse::new);
    }

    public PetDetailResponse findByIdAndActiveTrue(Long id) {
        var pet = petRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Pet not found with id " + id));

        return new PetDetailResponse(pet);
    }

    public PetDetailResponse update(PetRequest request, Long id) {
        var pet = petRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Pet not found with id " + id));

        var customer = customerRepository.findByIdAndActiveTrue(request.ownerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + request.ownerId()));

        validateOwnership(pet, customer);
        validateSpecies(request);
        pet.update(request);

        return new PetDetailResponse(pet);
    }

    public void deactivate(Long id) {
        var pet = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet not found with id " + id));

        pet.deactivate();
    }

    public void activate(Long id) {
        var pet = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet not found with id " + id));

        pet.activate();
    }

    private void validateSpecies(PetRequest request) {
        if (request.species() == Species.OTHER && (request.otherSpecies() == null || request.otherSpecies().isBlank())) {
            throw new BadRequestException(List.of(
                    new ErrorResponse.FieldErrorResponse("otherSpecies", "You must provide 'otherSpecies' when species is set to OTHER")
            ));
        }
        if (request.species() != Species.OTHER && request.otherSpecies() != null) {
            throw new BadRequestException(List.of(
                    new ErrorResponse.FieldErrorResponse("otherSpecies", "The field 'otherSpecies' can only be used when species is set to OTHER")
            ));
        }
    }

    public void validateOwnership(Pet pet, Customer customer) {
        List<ErrorResponse.FieldErrorResponse> errors = new ArrayList<>();

        if (!pet.getOwner().getId().equals(customer.getId())) {
            errors.add(new ErrorResponse.FieldErrorResponse("owner", "Pet does not belong to this customer"));
        }

        if (!errors.isEmpty()) {
            throw new ForbiddenOperationException(errors);
        }
    }
}
