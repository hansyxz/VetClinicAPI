package io.github.hansel.vetclinic.api.dto.appointment;

import io.github.hansel.vetclinic.api.dto.customer.CustomerSummaryResponse;
import io.github.hansel.vetclinic.api.dto.pet.PetSummaryResponse;
import io.github.hansel.vetclinic.api.entity.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public record AppointmentDetailResponse(Long id, LocalDateTime dateTime, String notes, boolean pickupRequired,
                                        PetSummaryResponse pet, CustomerSummaryResponse customer, List<AppointmentItemResponse> items) {
    public AppointmentDetailResponse(Appointment appointment) {
        this(appointment.getId(), appointment.getDateTime(), appointment.getNotes(), appointment.isPickupRequired(),
                new PetSummaryResponse(appointment.getPet()), new CustomerSummaryResponse(appointment.getCustomer()),
                appointment.getItems().stream().map(AppointmentItemResponse::new).toList());
    }
}
