package io.github.hansel.vetclinic.api.repository;

import io.github.hansel.vetclinic.api.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
