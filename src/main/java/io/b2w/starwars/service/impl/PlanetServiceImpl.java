package io.b2w.starwars.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.b2w.starwars.model.Planet;
import io.b2w.starwars.repository.PlanetRepository;
import io.b2w.starwars.service.PlanetService;

@Service
public class PlanetServiceImpl implements PlanetService {

	@Autowired
	private PlanetRepository planetRepository;
	
	@Override
	public void insert(Planet planet) {
		planetRepository.insert(planet);
	}
	
	@Override
	public Page<Planet> find(String name, Pageable pageable) {
		return (StringUtils.isEmpty(name)) ? planetRepository.findAll(pageable) : findByName(name, pageable);
	}

	@Override
	public Planet findById(String id) {
		return planetRepository.findById(id).orElseThrow(() -> new NoSuchElementException("PlanetServiceImpl.findById : Not found on database"));
	}
	
	@Override
	public void deleteById(String id) {
		this.findById(id);
		planetRepository.deleteById(id);
	}
	
	private Page<Planet> findByName(String name, Pageable pageable) {
		
		Optional<Page<Planet>> findByName = planetRepository.findByName(name, pageable);
		
		if (findByName.get().isEmpty()) {
			throw new NoSuchElementException("PlanetServiceImpl.findByName : Not found on database");
		}
		
		return findByName.get();
	}

}
