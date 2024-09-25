package com.github.simbir_document_service.controller;

import com.github.simbir_document_service.client.AccountServiceClient;
import com.github.simbir_document_service.client.HospitalServiceClient;
import com.github.simbir_document_service.config.context.UserContext;
import com.github.simbir_document_service.document.Document;
import com.github.simbir_document_service.dto.AccountDto;
import com.github.simbir_document_service.dto.DocumentDto;
import com.github.simbir_document_service.dto.Role;
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
    private boolean isAccountOwner(String ownerUsername) {
        String currentUserSub = userContext.getUsername(); //  userContext может вернуть 'sub' (имя пользователя)
        return ownerUsername.equals(currentUserSub);
    }

    // Endpoint to get history by patient ID
    @GetMapping("/Account/{id}")
    public ResponseEntity<List<DocumentDto>> getHistoryByAccountId(@PathVariable Long id, HttpServletRequest request) {
        // Извлекаем токен из заголовка запроса
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Токен не найден или недействителен.");
        }

        // Вызываем Feign-клиент для получения данных аккаунта по его ID
        AccountDto account = accountServiceClient.getAccountById(id, bearerToken).getBody();

        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Аккаунт не найден.");
        }

        // Проверяем, является ли пользователь врачом или владельцем аккаунта
        if (!isUserAuthorized("doctor") && !isAccountOwner(account.username())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }

        // Получаем историю документов по ID пациента
        List<DocumentDto> documents = documentService.getDocumentByPatientId(id);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}") // todo проблемы с логикой
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Long id, HttpServletRequest request) {
        // Извлекаем токен из заголовка запроса
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Токен не найден или недействителен.");
        }

        // Вызываем Feign-клиент для получения данных аккаунта по его ID
        AccountDto account = accountServiceClient.getAccountById(id, bearerToken).getBody();

        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Аккаунт не найден.");
        }

        // Проверяем, является ли пользователь врачом или владельцем аккаунта
        if (!isUserAuthorized("doctor") && !isAccountOwner(account.username())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }

        DocumentDto document = documentService.getDocumentById(id);
        return ResponseEntity.ok(document);
    }

    @PostMapping
    public ResponseEntity<DocumentDto> createDocument(@RequestBody DocumentDto documentDto,  @RequestHeader("Authorization") String bearerToken) {

        // Проверка прав доступа для администраторов, менеджеров и врачей
        if (!isUserAuthorized("admin") && !isUserAuthorized("manager") && !isUserAuthorized("doctor")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }

        // Валидация pacientId, чтобы убедиться, что пользователь имеет роль 'user'
        AccountDto accountDto = accountServiceClient.getAccountById(documentDto.patientId(), bearerToken).getBody();

        // Проверяем, что аккаунт существует и имеет роль 'user'
        if (accountDto == null || accountDto.role() != Role.user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: пациент не имеет роли 'user'.");
        }

        DocumentDto createdDocument = documentService.createDocument(documentDto);
        return ResponseEntity.status(201).body(createdDocument);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDto> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentDto documentDto,
            @RequestHeader("Authorization") String bearerToken
    ) {
        if (!isUserAuthorized("admin") && !isUserAuthorized("manager") && !isUserAuthorized("doctor")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }

        // Проверка прав доступа для администраторов, менеджеров и врачей
        if (!isUserAuthorized("admin") && !isUserAuthorized("manager") && !isUserAuthorized("doctor")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: недостаточно прав.");
        }

        // Валидация pacientId, чтобы убедиться, что пользователь имеет роль 'user'
        AccountDto accountDto = accountServiceClient.getAccountById(documentDto.patientId(), bearerToken).getBody();

        // Проверяем, что аккаунт существует и имеет роль 'user'
        if (accountDto == null || accountDto.role() != Role.user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен: пациент не имеет роли 'user'.");
        }

        DocumentDto updatedDocument = documentService.updateDocument(id, documentDto);
        return ResponseEntity.ok(updatedDocument);
    }
}