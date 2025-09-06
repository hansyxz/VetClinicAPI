package io.github.hansel.vetclinic.api.entity;

import io.github.hansel.vetclinic.api.dto.request.AppointmentItemRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table(name = "appointment_items")
@Entity(name = "AppointmentItem")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class AppointmentItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id", nullable = false)
    private Offering offering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    public AppointmentItem(AppointmentItemRequest dto, Appointment appointment, Offering offering, Employee employee) {
        this.appointment = appointment;
        this.offering = offering;
        this.employee = employee;
        this.amount = dto.amount();
        this.unitPrice = dto.unitPrice();
    }
}
