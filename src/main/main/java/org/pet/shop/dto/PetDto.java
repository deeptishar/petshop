package org.pet.shop.dto;

import java.util.UUID;

public record PetDto(
        UUID id,
        String name,
        String type,
        String color,
        ParentDto parents
) {

    public record ParentDto(
             UUID mother,
             UUID father
    ) {
    }
}