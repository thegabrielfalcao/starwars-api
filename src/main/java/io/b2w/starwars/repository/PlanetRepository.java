package io.b2w.starwars.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import io.b2w.starwars.model.Planet;

public interface PlanetRepository extends MongoRepository<Planet, String> {

	@Query("{'name': {$regex : ?0, $options: 'i'}}")
	Optional<Page<Planet>> findByName(String name, Pageable pageable);
}
