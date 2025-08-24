package io.github.hansel.vetclinic.api.controller;

import io.github.hansel.vetclinic.api.dto.request.CustomerRequest;
import io.github.hansel.vetclinic.api.dto.response.CustomerDetailResponse;
import io.github.hansel.vetclinic.api.dto.response.CustomerSummaryResponse;
import io.github.hansel.vetclinic.api.service.CustomerService;
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
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    public CustomerService service;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid CustomerRequest request, UriComponentsBuilder uriBuilder){
        var response = service.create(request);
        var uri = uriBuilder.path("/customer/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Page<CustomerSummaryResponse>> findAll(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        return ResponseEntity.ok(service.findAllByActiveTrue(pageable));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<CustomerDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdAndActiveTrue(id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(@Valid @RequestBody CustomerRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(service.update(request, id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
