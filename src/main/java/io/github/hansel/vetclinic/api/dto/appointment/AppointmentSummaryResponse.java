package io.github.hansel.vetclinic.api.dto.appointment;

import io.github.hansel.vetclinic.api.entity.Appointment;

import java.time.LocalDateTime;

public record AppointmentSummaryResponse(Long id, LocalDateTime dateTime, String notes) {
    public AppointmentSummaryResponse(Appointment appointment) {
        this(appointment.getId(), appointment.getDateTime(), appointment.getNotes());
    }
}
