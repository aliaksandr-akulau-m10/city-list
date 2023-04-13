package com.andersen.citylist.service;

import com.andersen.citylist.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CityService {
    void importCities(final MultipartFile multipartFile) throws IOException;

    City updateCity(final Long cityId, final City city);

    Page<City> getCities(final Pageable pageable, final String search);
}
