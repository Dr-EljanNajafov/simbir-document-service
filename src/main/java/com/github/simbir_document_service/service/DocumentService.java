package com.github.simbir_document_service.service;

import com.github.simbir_document_service.client.AccountServiceClient;
import com.github.simbir_document_service.client.HospitalServiceClient;
import com.github.simbir_document_service.document.Document;
import com.github.simbir_document_service.dto.DocumentDto;
import com.github.simbir_document_service.dto.mapper.DocumentDtoMapper;
import com.github.simbir_document_service.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentDtoMapper documentDtoMapper;
    private final DocumentRepository documentRepository;

    public List<DocumentDto> getDocumentByPatientId(Long patientId) {
        List<Document> documents = documentRepository.findByPatientId(patientId); // Assuming you have this method in your repository
        return documents.stream()
                .map(documentDtoMapper)
                .collect(Collectors.toList());
    }

    public DocumentDto getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return documentDtoMapper.apply(document);
    }

    public DocumentDto createDocument(DocumentDto documentDto) {
        Document document = new Document();
        document.setDate(documentDto.date());
        document.setPatientId(documentDto.patientId());
        document.setHospitalId(documentDto.hospitalId());
        document.setDoctorId(documentDto.doctorId());
        document.setRoom(documentDto.room());
        document.setData(documentDto.data());
        Document savedDocument = documentRepository.save(document);
        return documentDtoMapper.apply(savedDocument);
    }

    public DocumentDto updateDocument(Long id, DocumentDto documentDto) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        document.setDate(documentDto.date());
        document.setPatientId(documentDto.patientId());
        document.setHospitalId(documentDto.hospitalId());
        document.setDoctorId(documentDto.doctorId());
        document.setRoom(documentDto.room());
        document.setData(documentDto.data());
        Document updatedDocument = documentRepository.save(document);
        return documentDtoMapper.apply(updatedDocument);
    }
}