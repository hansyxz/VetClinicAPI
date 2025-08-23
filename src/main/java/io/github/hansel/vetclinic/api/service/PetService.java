package io.github.hansel.vetclinic.api.service;

import io.github.hansel.vetclinic.api.dto.request.PetRequest;
import io.github.hansel.vetclinic.api.dto.response.PetDetailResponse;
import io.github.hansel.vetclinic.api.dto.response.PetSummaryResponse;
import io.github.hansel.vetclinic.api.entity.Pet;
import io.github.hansel.vetclinic.api.entity.enums.Species;
import io.github.hansel.vetclinic.api.exception.BadRequestException;
import io.github.hansel.vetclinic.api.exception.NotFoundException;
import io.github.hansel.vetclinic.api.repository.CustomerRepository;
import io.github.hansel.vetclinic.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PetService {

    @Autowired
    public PetRepository repository;

    @Autowired
    public CustomerRepository customerRepository;

    public PetDetailResponse create(PetRequest request) {

        var customer = customerRepository.findById(request.ownerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + request.ownerId()));

        validateSpecies(request);

        var pet = new Pet(request, customer);
        repository.save(pet);

        return new PetDetailResponse(pet);
    }

    public Page<PetSummaryResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(PetSummaryResponse::new);
    }

    public PetDetailResponse findById(Long id) {
        var pet = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet not found with id " + id));

        return new PetDetailResponse(pet);
    }

    private void validateSpecies(PetRequest request) {
        if (request.species() == Species.OTHER && (request.otherSpecies() == null || request.otherSpecies().isBlank())) {
            throw new BadRequestException("You must provide 'otherSpecies' when species is set to OTHER");
        }

        if (request.species() != Species.OTHER && request.otherSpecies() != null) {
            throw new BadRequestException("The field 'otherSpecies' can only be used when species is set to OTHER");
        }
    }
}
