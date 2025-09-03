package io.github.hansel.vetclinic.api.controller;

import io.github.hansel.vetclinic.api.dto.request.EmployeeRequest;
import io.github.hansel.vetclinic.api.dto.response.EmployeeDetailResponse;
import io.github.hansel.vetclinic.api.dto.response.EmployeeSummaryResponse;
import io.github.hansel.vetclinic.api.service.EmployeeService;
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
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    public EmployeeService service;

    @PostMapping
    @Transactional
    public ResponseEntity<EmployeeDetailResponse> create(@RequestBody @Valid EmployeeRequest request, UriComponentsBuilder uriBuilder){
        var response = service.create(request);
        var uri = uriBuilder.path("/employee/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Page<EmployeeSummaryResponse>> findAll(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        return ResponseEntity.ok(service.findAllByActiveTrue(pageable));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<EmployeeDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdAndActiveTrue(id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<EmployeeDetailResponse> update(@Valid @RequestBody EmployeeRequest request, @PathVariable Long id) {
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
