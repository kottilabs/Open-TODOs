package de.kottilabs.todobackend.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("swagger")
public class SpringFoxConfig {

	public static final String AUTHENTICATION = "Authentication";
	public static final String SCOPE = "Scopes";
	public static final String TODO = "Todos";
	public static final String USER = "Users";

	public static final String UPDATE_REQUEST = "The update request";

	public static final String SCOPE_ID = "The scopes id";
	public static final String TODO_ID = "The todos id";
	public static final String USER_ID = "The users id";

	@Bean
	public Docket api() {

		final List<ResponseMessage> globalResponses = Arrays.asList(
				new ResponseMessageBuilder().code(200).message("OK").build(),
				new ResponseMessageBuilder().code(400).message("Bad Request").build(),
				new ResponseMessageBuilder().code(401).message("Your token is invalid or expired").build(),
				new ResponseMessageBuilder().code(403).message("You dont have permission to do this").build(),
				new ResponseMessageBuilder().code(404).message("The resource you were trying to reach is not found")
						.build());

		ApiInfo apiInfo = new ApiInfoBuilder().title("OpenTODO API").version("1.0.0").build();

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("de.kottilabs.todobackend")).paths(PathSelectors.any())
				.build() //
				.apiInfo(apiInfo)//
				.useDefaultResponseMessages(false) //
				.globalResponseMessage(RequestMethod.GET, globalResponses)
				.globalResponseMessage(RequestMethod.POST, globalResponses)
				.globalResponseMessage(RequestMethod.PUT, globalResponses)
				.globalResponseMessage(RequestMethod.DELETE, globalResponses)
				.tags(new Tag(AUTHENTICATION, "Authentication Endpoints"), //
						new Tag(SCOPE, "Scope Endpoints"), //
						new Tag(TODO, "Todo Endpoints"), //
						new Tag(USER, "User Endpoints"))
				.securitySchemes(Collections.singletonList(new ApiKey("JWT", "Authorization", "header")))
				.securityContexts(Collections.singletonList(SecurityContext.builder()
						.securityReferences(Collections.singletonList(
								SecurityReference.builder().reference("JWT").scopes(new AuthorizationScope[0]).build()))
						.build()));
	}
}
