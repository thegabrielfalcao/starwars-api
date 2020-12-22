package io.b2w.starwars.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.b2w.starwars.model.Planet;
import io.b2w.starwars.service.PlanetService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/planets")
@CrossOrigin("*")
public class PlanetController {

	@Autowired
	private PlanetService service;
	
	@Value("${io.b2w.starwars.path}")
	private String path;
	
	@Value("${io.b2w.starwars.endpoint.planet}")
	private String endpoint;
	
	@PostMapping(produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Criação do planeta feita com sucesso"),
			@ApiResponse(code = 400, message = "Falha ao criar o planeta, pois há atributos faltando")
	})
	@ApiOperation(value = "Criar planeta")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<Planet> create(@Valid @RequestBody @NotNull Planet planet) throws URISyntaxException {
		service.insert(planet);
		return ResponseEntity.created(new URI(path.concat(endpoint.concat(planet.getId().toString())))).build();
	}

	@GetMapping(produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna os planetas de forma paginada"),
			@ApiResponse(code = 404, message = "Planeta não encontrado com os filtros selecionados")
	})
	@ApiOperation(value = "Listar planeta de forma paginada, ou busca pelo nome de forma paginada")
	public ResponseEntity<Page<Planet>> find(
			@ApiParam(value = "Nome do planeta", required = false) @RequestParam(name = "name", required = false) String name,
			@ApiParam(value = "Nº da página (Começa a partir do 0)") @RequestParam(name = "page", defaultValue = "0") Integer page, 
			@ApiParam(value = "Nº de planetas retornados na página") @RequestParam(name = "size", defaultValue = "5") Integer size
			) {
		return ResponseEntity.ok(service.find(name, PageRequest.of(page, size)));
	}
	
	@GetMapping(path = "/{id}", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna o planeta de acordo com o ID"),
			@ApiResponse(code = 404, message = "Planeta não encontrado com o ID selecionado")
	})
	@ApiOperation(value = "Procurar planeta por id")
	public ResponseEntity<Planet> findById(@ApiParam(value = "Id do planeta") @PathVariable("id") String id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@DeleteMapping("/{id}")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Exclusão efetuada com sucesso"),
			@ApiResponse(code = 404, message = "Planeta não encontrado com o ID selecionado")
	})
	@ApiOperation(value = "Remover planeta")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public ResponseEntity<Planet> remove(@ApiParam(value = "Id do planeta") @PathVariable("id") String id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
