package com.example.practice.controller;

import com.example.practice.model.Player;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/external-players")
public class ApiClientController {

    private static final Logger log = LoggerFactory.getLogger(ApiClientController.class);

    private final RestTemplate restTemplate;
    private final String externalApiUrl = "http://host.docker.internal:8091/api/players";

    public ApiClientController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }

    // GET all - получение всех игроков
    @GetMapping
    public ResponseEntity<?> getUsersFromExternalApp() {
        log.info("Вызов внешнего API для получения всех игроков: {}", externalApiUrl);

        try {
            ResponseEntity<Player[]> response = restTemplate.getForEntity(externalApiUrl, Player[].class);
            log.info("Статус ответа от внешнего API: {}", response.getStatusCode());

            List<Player> players = response.getBody() != null
                    ? Arrays.asList(response.getBody())
                    : Collections.emptyList();

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(players);
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body("Внешний сервис вернул статус: " + response.getStatusCode());
            }

        } catch (ResourceAccessException e) {
            log.error("Ошибка подключения к внешнему API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Не удалось подключиться к внешнему сервису");
        } catch (Exception e) {
            log.error("Ошибка при вызове внешнего API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Внутренняя ошибка сервера");
        }
    }

    // GET by ID - получение одного игрока
    @GetMapping("/{id}")
    public ResponseEntity<?> getOneUserFromExternalApp(@PathVariable Long id) {
        String url = externalApiUrl + "/" + id;
        log.info("Вызов внешнего API для получения игрока с id {}: {}", id, url);

        try {
            ResponseEntity<Player> response = restTemplate.getForEntity(url, Player.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Игрок с id {} успешно получен", id);
                return ResponseEntity.ok(response.getBody());
            }

            return ResponseEntity.status(response.getStatusCode())
                    .body("Внешний сервис вернул статус: " + response.getStatusCode());

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Игрок с id {} не найден", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Игрок с id " + id + " не найден");
        } catch (ResourceAccessException e) {
            log.error("Ошибка подключения: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Не удалось подключиться к внешнему сервису");
        } catch (Exception e) {
            log.error("Ошибка при получении игрока с id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Внутренняя ошибка сервера");
        }
    }

    // POST - создание нового игрока
    @PostMapping
    public ResponseEntity<?> createUserInExternalApp(@RequestBody Player player) {
        log.info("Вызов внешнего API для создания нового игрока: {}", externalApiUrl);
        log.debug("Данные нового игрока: {}", player);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Player> request = new HttpEntity<>(player, headers);

            ResponseEntity<Player> response = restTemplate.postForEntity(
                    externalApiUrl,
                    request,
                    Player.class
            );

            log.info("Статус ответа от внешнего API при создании: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode().is2xxSuccessful()) {
                log.info("Игрок успешно создан с id: {}",
                        response.getBody() != null ? response.getBody().getId() : "unknown");
                return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body("Внешний сервис вернул статус: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException.BadRequest e) {
            log.error("Неверные данные при создании игрока: {}", e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Неверные данные: " + e.getResponseBodyAsString());
        } catch (HttpClientErrorException.Conflict e) {
            log.error("Конфликт при создании игрока: {}", e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Игрок уже существует: " + e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            log.error("Ошибка подключения при создании: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Не удалось подключиться к внешнему сервису");
        } catch (Exception e) {
            log.error("Ошибка при создании игрока: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Внутренняя ошибка сервера");
        }
    }

    // PUT - полное обновление игрока
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserInExternalApp(@PathVariable Long id, @RequestBody Player player) {
        String url = externalApiUrl + "/" + id;
        log.info("Вызов внешнего API для обновления игрока с id {}: {}", id, url);
        log.debug("Данные для обновления: {}", player);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Player> request = new HttpEntity<>(player, headers);

            ResponseEntity<Player> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    request,
                    Player.class
            );

            log.info("Статус ответа от внешнего API при обновлении: {}", response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Игрок с id {} успешно обновлен", id);
                return ResponseEntity.ok(response.getBody() != null ? response.getBody() : "Игрок успешно обновлен");
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body("Внешний сервис вернул статус: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Игрок с id {} не найден для обновления", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Игрок с id " + id + " не найден");
        } catch (HttpClientErrorException.BadRequest e) {
            log.error("Неверные данные при обновлении игрока {}: {}", id, e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Неверные данные: " + e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            log.error("Ошибка подключения при обновлении: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Не удалось подключиться к внешнему сервису");
        } catch (Exception e) {
            log.error("Ошибка при обновлении игрока с id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Внутренняя ошибка сервера");
        }
    }

    // PATCH - частичное обновление игрока
    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateUserInExternalApp(@PathVariable Long id, @RequestBody Player player) {
        String url = externalApiUrl + "/" + id;
        log.info("Вызов внешнего API для частичного обновления игрока с id {}: {}", id, url);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Player> request = new HttpEntity<>(player, headers);

            ResponseEntity<Player> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    request,
                    Player.class
            );

            log.info("Статус ответа от внешнего API при частичном обновлении: {}", response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Игрок с id {} успешно частично обновлен", id);
                return ResponseEntity.ok(response.getBody() != null ? response.getBody() : "Игрок успешно обновлен");
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body("Внешний сервис вернул статус: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Игрок с id {} не найден для частичного обновления", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Игрок с id " + id + " не найден");
        } catch (HttpClientErrorException.BadRequest e) {
            log.error("Неверные данные при частичном обновлении: {}", e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Неверные данные: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Ошибка при частичном обновлении игрока с id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Внутренняя ошибка сервера");
        }
    }

    // DELETE - удаление игрока
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserFromExternalApp(@PathVariable Long id) {
        String url = externalApiUrl + "/" + id;
        log.info("Вызов внешнего API для удаления игрока с id {}: {}", id, url);

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<?> request = new HttpEntity<>(headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    request,
                    Void.class
            );

            log.info("Статус ответа от внешнего API при удалении: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode().is2xxSuccessful()) {
                log.info("Игрок с id {} успешно удален", id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body("Внешний сервис вернул статус: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Игрок с id {} не найден для удаления", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Игрок с id " + id + " не найден");
        } catch (ResourceAccessException e) {
            log.error("Ошибка подключения при удалении: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Не удалось подключиться к внешнему сервису");
        } catch (Exception e) {
            log.error("Ошибка при удалении игрока с id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Внутренняя ошибка сервера");
        }
    }

    // DELETE all - удаление всех игроков (если API поддерживает)
    @DeleteMapping
    public ResponseEntity<?> deleteAllUsersFromExternalApp() {
        log.info("Вызов внешнего API для удаления всех игроков: {}", externalApiUrl);

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<?> request = new HttpEntity<>(headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    externalApiUrl,
                    HttpMethod.DELETE,
                    request,
                    Void.class
            );

            log.info("Статус ответа от внешнего API при удалении всех: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode().is2xxSuccessful()) {
                log.info("Все игроки успешно удалены");
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body("Внешний сервис вернул статус: " + response.getStatusCode());
            }

        } catch (ResourceAccessException e) {
            log.error("Ошибка подключения при удалении всех: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Не удалось подключиться к внешнему сервису");
        } catch (Exception e) {
            log.error("Ошибка при удалении всех игроков: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Внутренняя ошибка сервера");
        }
    }
}