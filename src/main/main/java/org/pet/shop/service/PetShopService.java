package org.pet.shop.service;

import org.pet.shop.domain.Pet;
import org.pet.shop.dto.PetDto;
import org.pet.shop.exception.PetNotFoundException;
import org.pet.shop.mapper.PetShopMapper;
import org.pet.shop.repository.PetShopRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PetShopService {

    private static final String PET_ID_NOT_FOUND = "PetID not found";
    private PetShopRepository petShopRepository;
    private PetShopMapper petShopMapper;

    public PetShopService(PetShopRepository petShopRepository, PetShopMapper petShopMapper) {
        this.petShopRepository = petShopRepository;
        this.petShopMapper = petShopMapper;
    }

    public PetDto createPet(PetDto petDto) {
        Pet pet = petShopMapper.toEntity(petDto);
        petShopRepository.save(pet);
        return petShopMapper.toDto(pet);
    }

    public PetDto findById(UUID petId) {
        Optional<Pet> pet = petShopRepository.findById(petId);

        return pet.map(petEntity -> {
            return petShopMapper.toDto(petEntity);
        }).orElseThrow(() -> new PetNotFoundException(PET_ID_NOT_FOUND));
    }

    public PetDto updatePet(UUID petId, PetDto petDto) {
        return petShopRepository.findById(petId)
                .map(existingPet -> {
                    existingPet.setName(petDto.name());
                    existingPet.setType(petDto.type());
                    existingPet.setColor(petDto.color());
                    existingPet.setMother(petShopRepository.findById(petDto.parents().mother()).orElse(null));
                    existingPet.setFather(petShopRepository.findById(petDto.parents().father()).orElse(null));
                    petShopRepository.save(existingPet);

                    return petShopMapper.toDto(existingPet);
                }).orElseThrow(() -> new PetNotFoundException(PET_ID_NOT_FOUND));

    }
}
