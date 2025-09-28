package com.example.practice.dto.mapping;

import com.example.practice.dto.CardStatusDTO;
import com.example.practice.model.CardStatus;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CardStatusMapper {

//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "cardStatusName", source = "cardStatusName")
//    @Mapping(target = "cards", source = "cards", qualifiedByName = "mapCards")
    CardStatusDTO toDto(CardStatus entity);

//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "cardStatusName", source = "cardStatusName")
//    @Mapping(target = "cards", ignore = true) // Игнорируем поле cards
    CardStatus toEntity(CardStatusDTO dto);

//    @Named("mapCards")
//    default List<Card> mapCards(List<Card> cards) {
//        return cards != null ? cards : Collections.emptyList();
//    }
}