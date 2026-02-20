package com.example.practice.controller;

import com.example.practice.model.Player;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")  // Упрощенный вариант
public class ApiClientController {

    private static final Logger log = LoggerFactory.getLogger(ApiClientController.class);

    private final RestTemplate restTemplate;
    private final String externalApiUrl = "http://localhost:8091/api/players";

    public ApiClientController(RestTemplateBuilder restTemplateBuilder) {
        // Настраиваем таймауты
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(java.time.Duration.ofSeconds(5))
                .setReadTimeout(java.time.Duration.ofSeconds(10))
                .build();
    }

    @GetMapping("/external-players")
    public ResponseEntity<?> getUsersFromExternalApp() {
        log.info("Вызов внешнего API: {}", externalApiUrl);

        try {
            ResponseEntity<Player[]> response = restTemplate.exchange(
                    externalApiUrl,
                    HttpMethod.GET,
                    null,
                    Player[].class
            );

            log.info("Статус ответа от внешнего API: {}", response.getStatusCode());

            // Проверяем, что тело ответа не null
            List<Player> players = response.getBody() != null
                    ? Arrays.asList(response.getBody())
                    : Collections.emptyList();

            // Проверяем статус ответа
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(players);
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body("Внешний сервис вернул статус: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Ошибка при вызове внешнего API: {}", e.getMessage());

            // Возвращаем понятную ошибку клиенту
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Не удалось получить данные из внешнего сервиса: " + e.getMessage());
        }
    }
}