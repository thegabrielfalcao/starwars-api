package io.b2w.starwars.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.b2w.starwars.model.Planet;

/**
 * Unit tests on the planet controller.
 * 
 * @author Gabriel Falcão
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetControllerTests {

	@Autowired
	private PlanetController controller;

	@TestConfiguration
	static class PlanetTestConfig {

		@Bean
		public Planet getPlanet() {
			Planet planet = new Planet();
			planet.setId("Id");
			planet.setName("Name");
			planet.setTerrain("Terrain");
			planet.setClimate("Climate");
			planet.setTimesOnMovies(50);

			return planet;
		}
	}

	@Autowired
	private Planet planet;

	final int FIRST_PAGE = 0;
	final int SIZE_BY_PAGE = 5;

	@Test
	public void createAndRemovePlanet() throws Exception {
		ResponseEntity<Planet> response = controller.create(planet);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		response = controller.remove(planet.getId());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	public void removePlanetWithError() {
		assertThrows(NoSuchElementException.class, () -> controller.remove(planet.getId()));
	}
	
	@Test
	public void findPlanetById() {
		ResponseEntity<Planet> findById = controller.findById("5f31ddac7cc70566188aa13c");
		assertTrue(findById.getBody() != null);
		assertThat(findById.getBody().getName()).isEqualTo("Alderaan");
	}
	
	@Test
	public void findPlanetByIdWithError() {
		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> controller.findById("INEXISTENTE"));
		assertTrue(exception.getMessage().contains("Not found on database"));
	}

	@Test
	public void findFirstPageWithDefaultFilter() {
		ResponseEntity<Page<Planet>> response = controller.find(null, FIRST_PAGE, SIZE_BY_PAGE);
		assertThat(response.getBody().getPageable().getPageNumber()).isEqualTo(FIRST_PAGE);
		assertThat(response.getBody().getPageable().getPageSize()).isEqualTo(SIZE_BY_PAGE);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}
