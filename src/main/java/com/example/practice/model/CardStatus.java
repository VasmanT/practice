package com.example.practice.model;

//import jakarta.persistence.*;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//@Data
//@RequiredArgsConstructor
@Entity
@Table(name = "card_status")
@Getter @Setter
@NoArgsConstructor
public class CardStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор статуса карты", example = "1")
    private Long id;

    @Column(name = "card_status_name")
    @Schema(description = "Наименование статуса карты", example = "Активна")
    private String cardStatusName;

}