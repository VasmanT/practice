package com.example.practice;

import com.example.practice.controller.ApiClientController;
import com.example.practice.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для проверки связи между practice и dbmicro
 * ВНИМАНИЕ: Для запуска этого теста dbmicro ДОЛЖЕН быть запущен!
 */
public class ApiClientIntegrationTest {

    private ApiClientController controller;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        // Создаем реальный RestTemplate как в контроллере
        RestTemplateBuilder builder = new RestTemplateBuilder();
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();

        // Создаем контроллер с реальным RestTemplate
        this.controller = new ApiClientController(
                new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5))
        );
    }

    @Test
    void testConnectionToDbmicroIsSuccessful() {
        // Этот тест проверяет, что dbmicro доступен
        try {
            ResponseEntity<Player[]> response = restTemplate.getForEntity(
                    "http://localhost:8091/api/players",
                    Player[].class
            );

            assertTrue(response.getStatusCode().is2xxSuccessful(),
                    "dbmicro должен отвечать с успешным статусом");
            assertNotNull(response.getBody(),
                    "Ответ от dbmicro не должен быть null");

            System.out.println("✅ dbmicro доступен! Статус: " + response.getStatusCode());
            System.out.println("   Количество игроков: " + response.getBody().length);

        } catch (Exception e) {
            fail("Не удалось подключиться к dbmicro. Убедитесь, что dbmicro запущен на порту 8091\n" +
                    "Ошибка: " + e.getMessage());
        }
    }

    @Test
    void testGetUsersFromExternalAppReturnsData() {
        // Этот тест проверяет работу метода контроллера
        ResponseEntity<?> response = controller.getUsersFromExternalApp();

        assertTrue(response.getStatusCode().is2xxSuccessful(),
                "Метод контроллера должен вернуть успешный статус");

        assertNotNull(response.getBody(),
                "Тело ответа не должно быть null");

        if (response.getBody() instanceof List) {
            List<?> players = (List<?>) response.getBody();
            System.out.println("✅ Контроллер успешно получил " + players.size() + " игроков");

            if (!players.isEmpty()) {
                System.out.println("   Первый игрок: " + players.get(0));
            }
        } else {
            fail("Ожидался List<Player>, но получен: " + response.getBody().getClass());
        }
    }

    @Test
    void testExternalApiUrlIsCorrect() {
        // Проверяем, что URL указан правильно
        String expectedUrl = "http://localhost:8091/api/players";

        // Используем рефлексию для получения private поля
        try {
            java.lang.reflect.Field field = ApiClientController.class.getDeclaredField("externalApiUrl");
            field.setAccessible(true);
            String actualUrl = (String) field.get(controller);

            assertEquals(expectedUrl, actualUrl,
                    "URL внешнего API должен быть правильным");
            System.out.println("✅ URL внешнего API: " + actualUrl);

        } catch (Exception e) {
            fail("Не удалось проверить URL: " + e.getMessage());
        }
    }
}