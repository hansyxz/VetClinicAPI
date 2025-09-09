package io.github.hansel.vetclinic.api.controller;

import io.github.hansel.vetclinic.api.dto.offering.OfferingRequest;
import io.github.hansel.vetclinic.api.dto.offering.OfferingResponse;
import io.github.hansel.vetclinic.api.service.OfferingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/offering")
public class OfferingController {

    @Autowired
    public OfferingService service;

    @PostMapping
    @Transactional
    public ResponseEntity<OfferingResponse> create(@RequestBody @Valid OfferingRequest request, UriComponentsBuilder uriBuilder){
        var response = service.create(request);
        var uri = uriBuilder.path("/offering/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Page<OfferingResponse>> findAll(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        return ResponseEntity.ok(service.findAllByActiveTrue(pageable));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<OfferingResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdAndActiveTrue(id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<OfferingResponse> update(@Valid @RequestBody OfferingRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(service.update(request, id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/restore")
    @Transactional
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}
