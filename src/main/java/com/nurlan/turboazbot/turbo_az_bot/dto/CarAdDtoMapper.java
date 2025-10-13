package com.nurlan.turboazbot.turbo_az_bot.dto;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CarAdDtoMapper {

    CarAdDtoMapper  MAPPER= Mappers.getMapper(CarAdDtoMapper.class);

    CarAdDto toDto(CarAd carAd);
     CarAd toEntity(CarAd carAdDto);
}
