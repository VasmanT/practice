package com.example.practice.controller;

import com.example.practice.model.Player;
import com.example.practice.service.ServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/players", "/api/players/"})
@Qualifier("playerService")
public class PlayerController {
    public final ServiceInterface<Long, Player> playerServiceInterface;

    public PlayerController(ServiceInterface<Long, Player> playerServiceInterface) {
        this.playerServiceInterface = playerServiceInterface;
    }

    @Operation(summary = "Получить игрока по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Player> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(playerServiceInterface.getById(id));
    }


    @Operation(summary = "Получить всех игроков")
    @GetMapping
    public ResponseEntity<List<Player>> getAll() {
        return ResponseEntity.ok(playerServiceInterface.getAll());
    }

    @Operation(summary = "Создать нового игрока")
    @PostMapping
    public ResponseEntity<Player> create(@RequestBody Player dto) {
        return new ResponseEntity<>(playerServiceInterface.addNew(dto), HttpStatus.CREATED);
    }


    @Operation(summary = "Обновить данные игрока")
    @PutMapping("/{id}")
    public ResponseEntity<Player> update(
            @PathVariable Long id,
            @RequestBody Player entity) {
        if (!entity.getId().equals(id)) {
//        if (!entity.id().equals(id)) {
            throw new IllegalArgumentException("ID в пути и теле запроса не совпадают");
        }
        return ResponseEntity.ok(playerServiceInterface.updateById(entity));
    }


    @Operation(summary = "Удалить игрока по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playerServiceInterface.deleteByID(id);
        return ResponseEntity.noContent().build();
    }
}
