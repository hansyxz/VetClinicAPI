package io.github.hansel.vetclinic.api.repository;

import io.github.hansel.vetclinic.api.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByIdAndActiveTrue(Long id);
    Page<Customer> findAllByActiveTrue(Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
