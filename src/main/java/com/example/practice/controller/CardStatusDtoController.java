package com.example.practice.controller;

import com.example.practice.dto.CardStatusDTO;
import com.example.practice.dto.mapping.CardStatusMapper;
import com.example.practice.service.CardStatusServiceImpl;
import com.example.practice.service.ServiceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({"/api/cards", "/api/cards/"})
@Tag(name = "Card Status API", description = "Управление статусами карт")

public class CardStatusDtoController {

    private final CardStatusServiceImpl cardStatusService;
    private final CardStatusMapper cardStatusMapper;

    @Autowired
    public CardStatusDtoController(CardStatusServiceImpl cardStatusService, CardStatusMapper cardStatusMapper) {
        this.cardStatusService = cardStatusService;
        this.cardStatusMapper = cardStatusMapper;
    }

        @Operation(
            summary = "Получить статус по id",
            description = "Возвращает статус карты по выбранному id"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение статуса")
    @ApiResponse(responseCode = "404", description = "Статус не найден")
    @GetMapping
    public ResponseEntity<String> getById(){

        String dto = cardStatusService.getData();
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}