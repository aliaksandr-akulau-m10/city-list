package com.andersen.citylist.service;

import com.andersen.citylist.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {
    City updateCity(final Long cityId, final City city);

    Page<City> getCities(final Pageable pageable, final String search);
}
