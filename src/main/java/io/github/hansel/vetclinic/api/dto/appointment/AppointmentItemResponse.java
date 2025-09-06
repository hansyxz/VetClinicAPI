package io.github.hansel.vetclinic.api.dto.appointment;

import io.github.hansel.vetclinic.api.dto.employee.EmployeeSummaryResponse;
import io.github.hansel.vetclinic.api.dto.offering.OfferingResponse;
import io.github.hansel.vetclinic.api.entity.AppointmentItem;

import java.math.BigDecimal;

public record AppointmentItemResponse(Long id, AppointmentSummaryResponse appointment, OfferingResponse offering,
                                      EmployeeSummaryResponse employee, int amount, BigDecimal unitPrice) {
    public AppointmentItemResponse(AppointmentItem appointmentItem) {
        this(appointmentItem.getId(), new AppointmentSummaryResponse(appointmentItem.getAppointment()), new OfferingResponse(appointmentItem.getOffering()),
                new EmployeeSummaryResponse(appointmentItem.getEmployee()), appointmentItem.getAmount(), appointmentItem.getUnitPrice());
    }
}
