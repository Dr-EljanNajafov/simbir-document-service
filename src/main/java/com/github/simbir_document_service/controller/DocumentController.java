package com.github.simbir_document_service.controller;

import com.github.simbir_document_service.client.AccountServiceClient;
import com.github.simbir_document_service.client.HospitalServiceClient;
import com.github.simbir_document_service.config.context.UserContext;
import com.github.simbir_document_service.document.Document;
import com.github.simbir_document_service.dto.DocumentDto;
import com.github.simbir_document_service.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/History")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final AccountServiceClient accountServiceClient;
    private final HospitalServiceClient hospitalServiceClient;
    private final UserContext userContext;

    // Endpoint to get history by patient ID
    @GetMapping("/Account/{id}")
    public ResponseEntity<List<DocumentDto>> getHistoryByAccountId(@PathVariable Long id) {
        List<DocumentDto> documents = documentService.getDocumentByPatientId(id);
        return ResponseEntity.ok(documents);
    }

//    @GetMapping
//    public ResponseEntity<List<DocumentDto>> getAllDocuments() {
//        List<DocumentDto> documents = documentService.getAllDocuments();
//        return ResponseEntity.ok(documents);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Long id) {
//        DocumentDto document = documentService.getDocumentById(id);
//        return ResponseEntity.ok(document);
//    }
//
//    @PostMapping
//    public ResponseEntity<DocumentDto> createDocument(@RequestBody DocumentDto documentDto) {
//        DocumentDto createdDocument = documentService.createDocument(documentDto);
//        return ResponseEntity.status(201).body(createdDocument);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<DocumentDto> updateDocument(@PathVariable Long id, @RequestBody DocumentDto documentDto) {
//        DocumentDto updatedDocument = documentService.updateDocument(id, documentDto);
//        return ResponseEntity.ok(updatedDocument);
//    }
}