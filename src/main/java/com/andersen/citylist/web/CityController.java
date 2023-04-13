package com.andersen.citylist.web;

import com.andersen.citylist.dto.CityViewModel;
import com.andersen.citylist.mapper.CityMapper;
import com.andersen.citylist.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/cities")
@RequiredArgsConstructor
@Validated
public class CityController {
    private final CityService service;
    private final CityMapper cityMapper;

    @GetMapping
    public Page<CityViewModel> getCities(@RequestParam(value = "search", required = false) final String search,
                                         @SortDefault(sort = {"id"}, direction = Sort.Direction.ASC) final Pageable pageable) {
        return service.getCities(pageable, search)
                .map(cityMapper::toViewModel);
    }

    @PutMapping("/{cityId}")
    @PreAuthorize(value = "hasAuthority('ALLOW_EDIT')")
    public CityViewModel updateCity(@PathVariable final Long cityId,
                                    @RequestBody @Valid final CityViewModel cityViewModel) {
        return cityMapper.toViewModel(
                service.updateCity(cityId, cityMapper.toModel(cityViewModel))
        );
    }

    @PostMapping("/import")
    @PreAuthorize(value = "hasAuthority('ALLOW_EDIT')")
    public void importCities(@RequestParam("cities") final MultipartFile multipartFile) throws IOException {
        service.importCities(multipartFile);
    }
}
