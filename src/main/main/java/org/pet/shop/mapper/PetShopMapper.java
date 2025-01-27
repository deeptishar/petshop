package org.pet.shop.mapper;

import org.pet.shop.domain.Pet;
import org.pet.shop.dto.PetDto;
import org.pet.shop.repository.PetShopRepository;
import org.springframework.stereotype.Component;

@Component
public class PetShopMapper {

    final PetShopRepository petShopRepository;

    public PetShopMapper(PetShopRepository petShopRepository) {
        this.petShopRepository = petShopRepository;
    }

    public Pet toEntity(PetDto petDto) {
        if (petDto == null) {
            return null;
        }
        Pet pet = new Pet();
        pet.setName(petDto.name());
        pet.setType(petDto.type());
        pet.setColor(petDto.color());

        if (petDto.parents() != null) {
            PetDto.ParentDto parents = petDto.parents();
            if (parents.mother() != null) {
                pet.setMother(petShopRepository.findById(parents.mother()).orElse(null));
            }
            if (parents.father() != null) {
                pet.setFather(petShopRepository.findById(parents.father()).orElse(null));
            }
        }
        return pet;
    }

    public PetDto toDto(Pet petEntity) {
        PetDto.ParentDto parentDto = null;

        if (petEntity.getMother() != null && petEntity.getFather() != null) {
            parentDto = new PetDto.ParentDto(petEntity.getMother().getId(), petEntity.getFather().getId());
        } else if (petEntity.getMother() != null) {
            parentDto = new PetDto.ParentDto(petEntity.getMother().getId(), null);
        } else if (petEntity.getFather() != null) {
            parentDto = new PetDto.ParentDto(null, petEntity.getFather().getId());
        }

        return new PetDto(petEntity.getId(),
                petEntity.getName(),
                petEntity.getType(),
                petEntity.getColor(),
                parentDto);

    }

}
