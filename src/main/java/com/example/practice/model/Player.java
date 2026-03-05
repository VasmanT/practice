package com.example.practice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.*;

//todo: преобразовать в Record, почистить

@Entity
@Table(name = "players")
@Schema(description = "Сущность игрока")
public record Player(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Schema(description = "Уникальный идентификатор игрока", example = "1")
        Long id,

        @Column(name = "first_name")
        @Schema(description = "Имя игрока", example = "Максим")
        String firstName,

        @Column(name = "last_name")
        @Schema(description = "Фамилия игрока", example = "Максимов")
        String lastName,

        @Column(name = "patronymic")
        @Schema(description = "Отчество игрока", example = "Максимович")
        String patronymic,

        @Column(name = "gender")
        @Schema(description = "Пол игрока", example = "man")
        String gender,

        @Column(name = "game_number")
        @Schema(description = "Игровой номер игрока", example = "11")
        byte gameNumber,

        @Column(name = "birth_day")
        @Schema(description = "Дата рождения игрока", example = "2022-01-27")
        LocalDate birthDay,

        @Column(name = "city_player")
        @Schema(description = "Город проживания игрока", example = "Кострома")
        String cityPlayer
) {}

//
//@Entity
//@Table(name = "players")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Player {
//    @Id
//    @GeneratedValue(strategy = IDENTITY)
//    @Schema(description = "Уникальный идентификатор игрока", example = "1")
//    private Long id;
//
//    @Column(name = "first_name")
//    @Schema(description = "Имя игрока", example = "Максим")
//    private String firstName;
//
//    @Column(name = "last_name")
//    @Schema(description = "Фамилия игрока", example = "Максимов")
//    private String lastName;
//
//    @Column(name = "patronymic")
//    @Schema(description = "Отчество игрока", example = "Максимович")
//    private String patronymic;
//
//    @Column(name = "gender")
//    @Schema(description = "Пол игрока", example = "man")
//    private String gender;
//
//    @Column(name = "game_number")
//    @Schema(description = "Игровой номер игрока", example = "11")
//    private byte gameNumber;
//
//    @Column(name = "birth_day")
//    @Schema(description = "Дата рождения игрока", example = "2022, 1, 27")
//    private LocalDate birthDay;
//
//    @Column(name = "city_player")
//    @Schema(description = "Город проживания игрока", example = "Кострома")
//    private String cityPlayer;
//}
