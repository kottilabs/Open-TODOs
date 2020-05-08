package de.kottilabs.todobackend.config;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import de.kottilabs.todobackend.dao.AuthToken;
import de.kottilabs.todobackend.dao.AuthTokenRepository;
import io.jsonwebtoken.*;

@Component
public class JwtTokenProvider {
	private static final String AUTH_TOKEN = "auth-token";
	private static final String ROLES = "roles";
	private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

	private String secretKey;

	@Autowired
	private TodoInitializingBean initializingBean;
	@Autowired
	private AuthTokenRepository authTokenRepository;

	@Value("${security.jwt.token.expire-length:18000000}")
	private long validityInMilliseconds = 18000000; // 30 min

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(initializingBean.getJwtSecret().getBytes());
	}

	public String createToken(String username, String password, Set<String> roles) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("roles", roles);
		Date now = new Date();
		String token = Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.signWith(SignatureAlgorithm.HS256, secretKey)//
				.compact();
		long validity = System.currentTimeMillis() + validityInMilliseconds;
		AuthToken authToken = new AuthToken();
		authToken.setUsername(username);
		authToken.setPassword(password);
		authToken.setIssuedAt(now.getTime() / 1000);
		authToken.setValidity(validity);
		authTokenRepository.save(authToken);
		return token;
	}

	public Authentication getAuthentication(ServletRequest req) {
		AuthToken authToken = (AuthToken) req.getAttribute(JwtTokenProvider.AUTH_TOKEN);
		List<String> roles = (List<String>) req.getAttribute(JwtTokenProvider.ROLES);
		UserDetails userDetails = new User(authToken.getUsername(), authToken.getPassword(),
				roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token, ServletRequest req) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			long now = System.currentTimeMillis();
			Claims body = claims.getBody();
			Date issuedAt = body.getIssuedAt();
			AuthToken authToken = authTokenRepository
					.findByUsernameAndIssuedAtAndValidityGreaterThan(body.getSubject(), issuedAt.getTime() / 1000, now)
					.orElse(null);
			if (authToken == null) {
				return false;
			}
			req.setAttribute(AUTH_TOKEN, authToken);
			req.setAttribute(ROLES, body.get("roles"));

			if (authToken.getValidity() < now + validityInMilliseconds / 2) {
				log.info("Update validitiy of token from: {} for: {}", issuedAt, authToken.getUsername());
				long validity = now + validityInMilliseconds;
				authToken.setValidity(validity);
				authTokenRepository.save(authToken);
			}
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	@Scheduled(fixedRate = 10000)
	@Transactional
	public void dropExpiredTokens() {
		long count = authTokenRepository.deleteByValidityLessThan(new Date().getTime());
		if (count > 0) {
			log.info("Dropped {} expired token(s)", count);
		}
	}
}
