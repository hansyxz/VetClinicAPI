package io.github.hansel.vetclinic.api.repository;

import io.github.hansel.vetclinic.api.entity.Offering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfferingRepository extends JpaRepository<Offering, Long> {
    Page<Offering> findAllByActiveTrue(Pageable pageable);
    Optional<Offering> findByIdAndActiveTrue(Long id);
    boolean existsByName(String name);
    Optional<Offering> findByName(String name);
}
