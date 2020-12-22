package io.b2w.starwars.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.b2w.starwars.model.Planet;

public interface PlanetService {

	void insert(Planet planet);
	Planet findById(String id);
	Page<Planet> find(String name, Pageable pageable);
	void deleteById(String id);
}
