package de.kottilabs.todobackend.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("swagger")
public class SpringFoxConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build()
				.securitySchemes(Collections.singletonList(new ApiKey("JWT", "Authorization", "header")))
				.securityContexts(Collections.singletonList(SecurityContext.builder()
						.securityReferences(Collections.singletonList(
								SecurityReference.builder().reference("JWT").scopes(new AuthorizationScope[0]).build()))
						.build()));
	}

}
