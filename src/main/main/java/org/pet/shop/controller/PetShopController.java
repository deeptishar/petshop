package org.pet.shop.controller;

import org.pet.shop.dto.PetDto;
import org.pet.shop.service.PetShopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/pet")
public class PetShopController {

    private final PetShopService petshopService;

    public PetShopController(PetShopService petshopService) {
        this.petshopService = petshopService;
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto) {
        PetDto pet = petshopService.createPet(petDto);
        return new ResponseEntity<>(pet, HttpStatus.CREATED);
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetDto> getPetById(@PathVariable UUID petId) {
        PetDto pet = petshopService.findById(petId);
        return ResponseEntity.ok(pet);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetDto> updatePetById(@PathVariable UUID petId, @RequestBody PetDto petDto) {
        PetDto updatedPet = petshopService.updatePet(petId, petDto);
        return ResponseEntity.ok(updatedPet);
    }

}
