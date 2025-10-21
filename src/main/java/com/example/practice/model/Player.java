package com.example.practice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "player")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор игрока", example = "1")
    private Long id;

    @Column(name = "firstName")
    @Schema(description = "Имя игрока", example = "Максим")
    private String firstName;

    @Column(name = "lastName")
    @Schema(description = "Фамилия игрока", example = "Максимов")
    private String lastName;

    @Column(name = "patronymic")
    @Schema(description = "Отчество игрока", example = "Максимович")
    private String patronymic;

    @Column(name = "gender")
    @Schema(description = "Пол игрока", example = "man")
    private String gender;

    @Column(name = "gameNumber")
    @Schema(description = "Игровой номер игрока", example = "11")
    private byte gameNumber;

    @Column(name = "birthDay")
    @Schema(description = "Дата рождения игрока", example = "2022, 1, 27")
    private LocalDate birthDay;

    @Column(name = "cityPlayer")
    @Schema(description = "Город проживания игрока", example = "Кострома")
    private String cityPlayer;
}
