package io.github.hansel.vetclinic.api.repository;

import io.github.hansel.vetclinic.api.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findAllByActiveTrue(Pageable pageable);
    Optional<Customer> findByIdAndActiveTrue(Long id);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findByEmail(String email);
}
