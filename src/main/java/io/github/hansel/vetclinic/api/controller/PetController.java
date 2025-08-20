package io.github.hansel.vetclinic.api.controller;

import io.github.hansel.vetclinic.api.dto.request.PetRequest;
import io.github.hansel.vetclinic.api.service.PetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
