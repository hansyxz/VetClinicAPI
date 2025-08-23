package io.github.hansel.vetclinic.api.controller;

import io.github.hansel.vetclinic.api.dto.request.PetRequest;
import io.github.hansel.vetclinic.api.dto.response.CustomerDetailResponse;
import io.github.hansel.vetclinic.api.dto.response.CustomerSummaryResponse;
import io.github.hansel.vetclinic.api.dto.response.PetDetailResponse;
import io.github.hansel.vetclinic.api.dto.response.PetSummaryResponse;
import io.github.hansel.vetclinic.api.service.PetService;
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
@RequestMapping("/pet")
public class PetController {

    @Autowired
    public PetService service;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid PetRequest request, UriComponentsBuilder uriBuilder) {
        var response = service.create(request);
        var uri = uriBuilder.path("/pet/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Page<PetSummaryResponse>> findAll(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<PetDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
