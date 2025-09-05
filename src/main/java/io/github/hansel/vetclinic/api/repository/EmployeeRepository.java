package io.github.hansel.vetclinic.api.repository;

import io.github.hansel.vetclinic.api.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findAllByActiveTrue(Pageable pageable);
    Optional<Employee> findByIdAndActiveTrue(Long id);
    boolean existsByCrmv(String crmv);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    Optional<Employee> findByCrmv(String crmv);
    Optional<Employee> findByPhone(String phone);
    Optional<Employee> findByEmail(String email);
}
