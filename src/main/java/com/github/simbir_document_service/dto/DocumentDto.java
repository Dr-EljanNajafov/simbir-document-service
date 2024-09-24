package com.github.simbir_document_service.dto;

import java.time.LocalDateTime;

public record DocumentDto (

     LocalDateTime date,
     Long patientId,
     Long hospitalId,
     Long doctorId,
     String room,
     String data
) {
}