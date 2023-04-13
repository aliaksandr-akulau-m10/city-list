package com.andersen.citylist.repository;

import com.andersen.citylist.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends PagingAndSortingRepository<City, Long>, CrudRepository<City, Long> {
    Page<City> findByNameContainingIgnoreCase(final String name, final Pageable pageable);
}
