package com.andersen.citylist.service.impl;

import com.andersen.citylist.exception.NotFoundException;
import com.andersen.citylist.model.City;
import com.andersen.citylist.repository.CityRepository;
import com.andersen.citylist.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    @Transactional
    public City updateCity(@NonNull final Long cityId, @NonNull final City city) {
        final City cityToUpdate = cityRepository.findById(cityId)
                .orElseThrow(() -> {
                    log.error("Update city operation is failed. City not found for id {}", city);
                    return new NotFoundException("City not found");
                });
        cityToUpdate.setName(city.getName());
        cityToUpdate.setPhoto(city.getPhoto());
        return cityRepository.save(cityToUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<City> getCities(@NonNull final Pageable pageable, @Nullable final String search) {
        return isEmpty(search)
                ? cityRepository.findAll(pageable)
                : cityRepository.findByNameContainingIgnoreCase(search, pageable);
    }
}
