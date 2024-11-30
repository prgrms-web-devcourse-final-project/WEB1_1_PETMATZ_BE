package com.petmatz.api.pet.dto;

public record PetIdResponse(Long id) {
    public static PetIdResponse of(Long id) {
        return new PetIdResponse(id);
    }
}
