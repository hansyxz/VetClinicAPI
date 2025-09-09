package io.github.hansel.vetclinic.api.service;

import io.github.hansel.vetclinic.api.dto.appointment.AppointmentDetailResponse;
import io.github.hansel.vetclinic.api.dto.appointment.AppointmentRequest;
import io.github.hansel.vetclinic.api.dto.appointment.AppointmentSummaryResponse;
import io.github.hansel.vetclinic.api.dto.error.ErrorResponse;
import io.github.hansel.vetclinic.api.entity.Appointment;
import io.github.hansel.vetclinic.api.exception.BusinessValidationException;
import io.github.hansel.vetclinic.api.exception.NotFoundException;
import io.github.hansel.vetclinic.api.repository.AppointmentRepository;
import io.github.hansel.vetclinic.api.repository.CustomerRepository;
import io.github.hansel.vetclinic.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    public AppointmentRepository appointmentRepository;

    @Autowired
    public CustomerRepository customerRepository;

    @Autowired
    public PetRepository petRepository;

    public AppointmentDetailResponse create(AppointmentRequest request) {
        var customer = customerRepository.findByIdAndActiveTrue(request.customerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id " + request.customerId()));

        var pet = petRepository.findByIdAndActiveTrue(request.petId())
                .orElseThrow(() -> new NotFoundException("Pet not found with id " + request.petId()));

        var appointment = new Appointment(request, pet, customer);

        validateDateTime(request);
        appointmentRepository.save(appointment);

        return new AppointmentDetailResponse(appointment);
    }

    public Page<AppointmentSummaryResponse> findAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(AppointmentSummaryResponse::new);
    }

    public AppointmentDetailResponse findById(Long id) {
        var appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id " + id));

        return new AppointmentDetailResponse(appointment);
    }

    public AppointmentDetailResponse update(AppointmentRequest request, Long id) {
        var appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id " + id));

        validateDateTime(request);
        appointment.update(request);

        return new AppointmentDetailResponse(appointment);
    }

    public void validateDateTime(AppointmentRequest request) {
        if (request.dateTime().isBefore(LocalDateTime.now())) {
            List<ErrorResponse.FieldErrorResponse> errors = List.of(
                    new ErrorResponse.FieldErrorResponse("dateTime", "Appointment date/time must be in the future")
            );
            throw new BusinessValidationException(errors);
        }
    }

    public void validateFieldsToUpdate(Appointment appointment, AppointmentRequest request) {

    }
}
