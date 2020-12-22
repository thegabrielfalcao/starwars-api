package io.b2w.starwars.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)  
		          .select()                                  
		          .apis(RequestHandlerSelectors.basePackage("io.b2w.starwars.controller"))              
		          .paths(PathSelectors.any())                          
		          .build()
		          .useDefaultResponseMessages(false)
		          .apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
	    return new ApiInfoBuilder()
	    		.title("Planets API")
	    		.description("API dos planetas do universo Star Wars")
	    		.version("1.0")
	    		.contact(new Contact("Gabriel Falcão", "https://www.linkedin.com/in/gabriel-falcao", "thegabrielfalcao@gmail.com"))
	    		.build();
	}
}