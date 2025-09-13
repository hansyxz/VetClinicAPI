package io.github.hansel.vetclinic.api.controller;

import io.github.hansel.vetclinic.api.dto.employee.EmployeeDetailResponse;
import io.github.hansel.vetclinic.api.dto.employee.EmployeeRequest;
import io.github.hansel.vetclinic.api.dto.employee.EmployeeSummaryResponse;
import io.github.hansel.vetclinic.api.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    public EmployeeService service;

    @PostMapping
    public ResponseEntity<EmployeeDetailResponse> create(@RequestBody @Valid EmployeeRequest request, UriComponentsBuilder uriBuilder){
        var response = service.create(request);
        var uri = uriBuilder.path("/employees/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeSummaryResponse>> findAll(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        return ResponseEntity.ok(service.findAllByActiveTrue(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdAndActiveTrue(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDetailResponse> update(@Valid @RequestBody EmployeeRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(service.update(request, id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}
