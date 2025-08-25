package io.github.hansel.vetclinic.api.repository;

import io.github.hansel.vetclinic.api.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Page<Pet> findAllByActiveTrue(Pageable pageable);
    Optional<Pet> findByIdAndActiveTrue(Long id);
}
