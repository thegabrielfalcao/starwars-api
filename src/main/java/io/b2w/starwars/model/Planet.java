package io.b2w.starwars.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@Document(collection = "planets")
public class Planet {

	@Id
	@ApiModelProperty(value = "Código do planeta")
	@JsonProperty(access = Access.READ_ONLY)
	private String id;
	
	@NotBlank(message = "The attribute name is required")
	@ApiModelProperty(value = "Nome do planeta")
	private String name;
	
	@ApiModelProperty(value = "Clima do planeta")
	@NotBlank(message = "The attribute climate is required")
	private String climate;
	
	@ApiModelProperty(value = "Terreno do planeta")
	@NotBlank(message = "The attribute terrain is required")
	private String terrain;
	
	@ApiModelProperty(value = "Nº de aparições em filmes")
	private int timesOnMovies;
}
