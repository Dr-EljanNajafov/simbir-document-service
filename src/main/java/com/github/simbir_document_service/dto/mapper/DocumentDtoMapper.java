package com.github.simbir_document_service.dto.mapper;

import com.github.simbir_document_service.document.Document;
import com.github.simbir_document_service.dto.DocumentDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DocumentDtoMapper implements Function<Document, DocumentDto> {

    @Override
    public DocumentDto apply(Document document) {
        return new DocumentDto(
                document.getDate(),
                document.getPatientId(),
                document.getHospitalId(),
                document.getDoctorId(),
                document.getRoom(),
                document.getData()
        );
    }
}
