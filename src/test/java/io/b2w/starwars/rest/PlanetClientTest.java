package io.b2w.starwars.rest;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.b2w.starwars.model.Planet;

/**
 * Test class of the REST requests
 * @author Gabriel Falcão
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlanetClientTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Test
	public void findAll() throws Exception {
		mockMvc.perform(get("/api/planets/").contentType(MediaType.APPLICATION_JSON))
								   .andExpect(status().isOk())
								   .andExpect(jsonPath("$.first").value(true));
	}
	
	@Test
	public void createAndRemove() throws Exception {
		Planet planet = new Planet();
		planet.setId("Id");
		planet.setName("Name");
		planet.setTerrain("Terrain");
		planet.setClimate("Climate");
		planet.setTimesOnMovies(50);

		MvcResult result = mockMvc.perform(post("/api/planets/").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(planet)))
							   .andExpect(status().isCreated())
							   .andReturn();
		
		mockMvc.perform(delete(result.getResponse().getHeader("Location")).contentType(MediaType.APPLICATION_JSON))
		  					.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteWithIdUnknown() throws Exception {
		mockMvc.perform(delete("/api/planets/UNREACHEABLEID"))
						.andExpect(status().isNotFound());
	}
	
	@Test
	public void createWithRequiredAttributeMissing() throws Exception {
		Planet planet = new Planet();
		planet.setClimate("Climate");
		
		mockMvc.perform(post("/api/planets/").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(planet)))
						.andExpect(status().isBadRequest())
						.andExpect(jsonPath("$[*].userMessage", hasItem("Planet: The attribute name is required")))
						.andExpect(jsonPath("$[*].userMessage", hasItem("Planet: The attribute terrain is required")));
	}
	
	@Test
	public void findByNameThatNotExists() throws Exception {
		mockMvc.perform(get("/api/planets/").queryParam("name", "NAOEXISTEEEE"))
						.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdThatNotExists() throws Exception {
		mockMvc.perform(get("/api/planets/NAOEXISTEEEE"))
						.andExpect(status().isNotFound());
	}
}
