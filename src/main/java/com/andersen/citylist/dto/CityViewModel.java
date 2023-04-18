package com.andersen.citylist.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityViewModel {
    private static final String PHOTO_URL_PATTERN = "(?i)(https?:\\/\\/.*\\.(?:png|jpg))";
    private Long id;
    @NotEmpty(message = "city name should be presented")
    private String name;
    @NotEmpty(message = "city photo should be presented")
    @Pattern(regexp = PHOTO_URL_PATTERN, message = "city photo must meet url pattern")
    private String photo;
}
