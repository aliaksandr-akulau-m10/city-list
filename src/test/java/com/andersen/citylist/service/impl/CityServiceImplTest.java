package com.andersen.citylist.service.impl;

import com.andersen.citylist.model.City;
import com.andersen.citylist.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    @Test
    void importCities() throws IOException {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("cities.csv")) {
            // given
            MockMultipartFile mockMultipartFile = new MockMultipartFile("file.csv", inputStream);
            doNothing().when(cityRepository).deleteAll();

            // when
            cityService.importCities(mockMultipartFile);

            // then
            verify(cityRepository, times(1)).deleteAll();
            verify(cityRepository, times(1)).saveAll(List.of(
                    City.builder().name("Tokyo").photo("TokyoPhoto").build(),
                    City.builder().name("Jakarta").photo("JakartaPhoto").build(),
                    City.builder().name("Delhi").photo("DelhiPhoto").build(),
                    City.builder().name("Mumbai").photo("MumbaiPhoto").build(),
                    City.builder().name("Manila").photo("ManilaPhoto").build()
            ));
        }
    }

    @Test
    void updateCity() {
        // given
        final Long cityId = 1L;
        final City requestCity = City.builder().name("Warsaw").photo("warsaw.jpg").build();
        final City cityToUpdate = City.builder().id(cityId).name("Gdansk").photo("gdansk.jpg").build();

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(cityToUpdate));
        when(cityRepository.save(cityToUpdate)).thenReturn(cityToUpdate);

        // when
        final City updatedCity = cityService.updateCity(cityId, requestCity);

        // then
        assertNotNull(updatedCity);
        assertEquals(requestCity.getName(), updatedCity.getName());
        assertEquals(requestCity.getPhoto(), updatedCity.getPhoto());

        verify(cityRepository, times(1)).findById(cityId);
        verify(cityRepository, times(1)).save(cityToUpdate);
    }

    @Test
    void getCities() {
        // given
        Pageable pageable = Pageable.unpaged();

        when(cityRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(new City(1L, "Sydney", "sydney.jpg"))));

        // when
        Page<City> cities = cityService.getCities(pageable, null);

        // then
        assertEquals(1, cities.getContent().size());
        assertEquals("Sydney", cities.getContent().get(0).getName());
        assertEquals("sydney.jpg", cities.getContent().get(0).getPhoto());

        verify(cityRepository, times(1)).findAll(pageable);
        verify(cityRepository, times(0)).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void testGetCities_withSearch() {
        // given
        Pageable pageable = Pageable.unpaged();

        when(cityRepository.findByNameContainingIgnoreCase("Sydney", pageable))
                .thenReturn(new PageImpl<>(List.of(new City(1L, "Sydney", "sydney.jpg"))));

        // when
        Page<City> cities = cityService.getCities(pageable, "Sydney");

        // then
        assertEquals(1, cities.getContent().size());
        assertEquals("Sydney", cities.getContent().get(0).getName());
        assertEquals("sydney.jpg", cities.getContent().get(0).getPhoto());

        verify(cityRepository, times(0)).findAll(pageable);
        verify(cityRepository, times(1)).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }
}
