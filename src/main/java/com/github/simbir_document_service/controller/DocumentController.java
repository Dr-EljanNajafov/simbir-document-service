package com.github.simbir_document_service.controller;

import com.github.simbir_document_service.client.AccountServiceClient;
import com.github.simbir_document_service.client.HospitalServiceClient;
import com.github.simbir_document_service.config.context.UserContext;
import com.github.simbir_document_service.document.Document;
import com.github.simbir_document_service.dto.AccountDto;
import com.github.simbir_document_service.dto.DocumentDto;
import com.github.simbir_document_service.repository.DocumentRepository;
import com.github.simbir_document_service.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/History")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final AccountServiceClient accountServiceClient;
    private final HospitalServiceClient hospitalServiceClient;
    private final UserContext userContext;

    private boolean isUserAuthorized(String role) {
        List<String> roles = userContext.getRoles();
        return roles.contains(role);
    }

    // Метод для проверки, является ли текущий пользователь владельцем истории
//    private boolean isAccountOwner(String ownerUsername) {
//        String currentUserSub = userContext.getUsername(); //  userContext может вернуть 'sub' (имя пользователя)
//        log.info("Current user: {}, Owner expected: {}", currentUserSub, ownerUsername);
//        return currentUserSub.equals(ownerUsername);
//    }

    // Endpoint to get history by patient ID
    @GetMapping("/Account/{id}")
    public ResponseEntity<List<DocumentDto>> getHistoryByAccountId(@PathVariable Long id, HttpServletRequest request) {
        if (!isUserAuthorized("doctor") /**&& !isAccountOwner(Objects.requireNonNull(accountServiceClient.getCurrentAccount(request.getHeader("Authorization")).getBody()).username())*/) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }
        List<DocumentDto> documents = documentService.getDocumentByPatientId(id);
        return ResponseEntity.ok(documents);

    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Long id, HttpServletRequest request) {
        if (!isUserAuthorized("doctor") /**&& !isAccountOwner(Objects.requireNonNull(accountServiceClient.getCurrentAccount(request.getHeader("Authorization")).getBody()).username())*/) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }
        DocumentDto document = documentService.getDocumentById(id);
        return ResponseEntity.ok(document);
    }

    @PostMapping
    public ResponseEntity<DocumentDto> createDocument(@RequestBody DocumentDto documentDto) {
        if (!isUserAuthorized("admin") && !isUserAuthorized("manager") && !isUserAuthorized("doctor")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }
        // Validate pacientId to ensure the user has role 'User'
//        AccountDto accountDto = accountServiceClient.getUserById(documentDto.getPacientId()).getBody();
//        if (accountDto == null || !accountDto.getRoles().contains("User")) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: пациент не имеет роли 'User'.");
//        }
        DocumentDto createdDocument = documentService.createDocument(documentDto);
        return ResponseEntity.status(201).body(createdDocument);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDto> updateDocument(@PathVariable Long id, @RequestBody DocumentDto documentDto) {
        if (!isUserAuthorized("admin") && !isUserAuthorized("manager") && !isUserAuthorized("doctor")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }
        // Validate pacientId to ensure the user has role 'User'
//        AccountDto accountDto = accountServiceClient.getUserById(documentDto.getPacientId()).getBody();
//        if (accountDto == null || !accountDto.getRoles().contains("User")) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: пациент не имеет роли 'User'.");
//        }
        DocumentDto updatedDocument = documentService.updateDocument(id, documentDto);
        return ResponseEntity.ok(updatedDocument);
    }
}