package de.kottilabs.todobackend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.kottilabs.todobackend.dto.AuthLogoutResponse;
import de.kottilabs.todobackend.dto.AuthRequest;
import de.kottilabs.todobackend.dto.AuthResponse;
import de.kottilabs.todobackend.dto.ScopeResponse;

@SpringBootTest
@AutoConfigureMockMvc
class TodoBackendApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(TodoBackendApplicationTests.class);

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@Value("${admin.username}")
	private String adminUsername;
	@Value("${admin.password}")
	private String adminPassword;

	private String currentAdminToken;

	@Test
	public void contextLoads() {
	}

	@BeforeEach
	public void setUpTest() throws Exception {
		AuthRequest data = new AuthRequest();
		data.setUsername(adminUsername);
		data.setPassword(adminPassword);
		MvcResult mvcResult = mvc.perform(post("/api/auth/login").content(mapper.writeValueAsString(data))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		AuthResponse authResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(), AuthResponse.class);
		log.info("Auth Response: {}", authResponse);
		currentAdminToken = authResponse.getToken();
	}

	@Test
	public void getAllScopesAsAdmin() throws Exception {
		MvcResult mvcResult = mvc.perform(get("/api/scope").header("Authorization", "Bearer " + currentAdminToken))
				.andExpect(status().isOk()).andReturn();
		ArrayList<ScopeResponse> scopeResponses = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<ArrayList<ScopeResponse>>() {
				});

		log.info("Find Scope Response: {}", scopeResponses);
	}

	@AfterEach
	public void cleanUpTest() throws Exception {
		MvcResult mvcResult = mvc.perform(post("/api/auth/logout").header("Authorization", "Bearer " + currentAdminToken))
				.andExpect(status().isOk()).andReturn();
		AuthLogoutResponse authLogoutResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				AuthLogoutResponse.class);

		log.info("Logout Response: {}", authLogoutResponse);
	}

}
