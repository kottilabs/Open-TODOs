package de.kottilabs.todobackend;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class TodoBackendApplication {

	public static final String UTC = "UTC";

	private static final Logger log = LoggerFactory.getLogger(TodoBackendApplication.class);

	public static void main(String[] args) {
		String timeZoneId = Calendar.getInstance().getTimeZone().getID();
		if (!timeZoneId.equals(UTC)) {
			log.error("Timezone should be UTC, but is: {}", timeZoneId);
			log.error("Set with VM Options: \"-Duser.timezone=UTC\"");
			return;
		}
		SpringApplication.run(TodoBackendApplication.class, args);
	}

	@Bean
	protected ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Autowired
	public void configureObjectMapper(ObjectMapper objectMapper) {
		objectMapper.setDefaultPropertyInclusion(
				JsonInclude.Value.construct(JsonInclude.Include.NON_EMPTY, JsonInclude.Include.ALWAYS));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
