package com.github.simbir_document_service.dto;

public record DoctorDto(
        Long id,
        String lastName,
        String firstName,
        String specialty
) {
}
