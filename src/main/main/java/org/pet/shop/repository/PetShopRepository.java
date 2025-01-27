package org.pet.shop.repository;

import org.pet.shop.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PetShopRepository extends JpaRepository<Pet, UUID> {
}