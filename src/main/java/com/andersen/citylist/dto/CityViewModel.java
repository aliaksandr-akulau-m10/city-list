package com.andersen.citylist.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityViewModel {
    private Long id;
    @NotNull(message = "city name should be presented")
    private String name;
    @NotEmpty(message = "city photo should be presented")
    private String photo;
}
