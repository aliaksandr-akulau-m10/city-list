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
    @NotNull
    private String name;
    @NotEmpty
    private String photo;
}
