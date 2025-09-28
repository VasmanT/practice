package com.example.practice.dto.mapping;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE

//        componentModel = "spring",
//        unmappedTargetPolicy = ReportingPolicy.ERROR,
//        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CentralMapperConfig {
}
//
//
//Особенности реализации:
//Использование общего конфига - все мапперы наследуют настройки из CentralMapperConfig
//
//Игнорирование неизвестных полей - unmappedTargetPolicy = ReportingPolicy.IGNORE
//
//Поддержка Spring - componentModel = MappingConstants.ComponentModel.SPRING
//
//Кастомные методы - для обработки ссылок на связанные сущности
//
//Игнорирование циклических зависимостей - через @Mapping(target = "...", ignore = true)
//
//Поддержка коллекций - через отдельный CollectionMapper
//
//Эти мапперы обеспечат полный набор преобразований между вашими сущностями и DTO с минимальным написанием boilerplate-кода.
//
