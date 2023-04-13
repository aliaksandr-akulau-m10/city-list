package com.andersen.citylist.mapper;

import com.andersen.citylist.dto.CityViewModel;
import com.andersen.citylist.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityMapper {
    CityViewModel toViewModel(final City city);

    City toModel(final CityViewModel cityViewModel);
}
