package io.github.hansel.vetclinic.api.service;

import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import io.github.hansel.vetclinic.api.dto.offering.OfferingRequest;
import io.github.hansel.vetclinic.api.dto.offering.OfferingResponse;
import io.github.hansel.vetclinic.api.entity.Offering;
import io.github.hansel.vetclinic.api.exception.BusinessValidationException;
import io.github.hansel.vetclinic.api.exception.NotFoundException;
import io.github.hansel.vetclinic.api.repository.OfferingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferingService {

    @Autowired
    public OfferingRepository repository;

    public OfferingResponse create(OfferingRequest request) {
        validateUniqueFieldsToCreate(request.name());

        var offering = new Offering(request);
        repository.save(offering);

        return new OfferingResponse(offering);
    }

    public Page<OfferingResponse> findAllByActiveTrue(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(OfferingResponse::new);
    }

    public OfferingResponse findByIdAndActiveTrue(Long id) {
        var offering = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Offering not found with id " + id));

        return new OfferingResponse(offering);
    }

    public List<Offering> findAllById(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public OfferingResponse update(OfferingRequest request, Long id) {
        var offering = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Offering not found with id " + id));

        validateUniqueFieldsToUpdate(offering.getId(), request.name());
        offering.update(request);

        return new OfferingResponse(offering);
    }

    public void deactivate(Long id) {
        var offering = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Offering not found with id " + id));

        offering.deactivate();
    }

    public void activate(Long id) {
        var offering = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Offering not found with id " + id));

        offering.activate();
    }

    public void validateUniqueFieldsToCreate(String name) {
        if (repository.existsByName(name)) {
            List<ErrorResponse.FieldErrorResponse> errors = List.of(
                    new ErrorResponse.FieldErrorResponse("name", "Name already exists")
            );
            throw new BusinessValidationException(errors);
        }
    }

    public void validateUniqueFieldsToUpdate(Long id, String name) {
        repository.findByName(name).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                List<ErrorResponse.FieldErrorResponse> errors = List.of(
                        new ErrorResponse.FieldErrorResponse("name", "Name already exists")
                );
                throw new BusinessValidationException(errors);
            }
        });
    }
}
