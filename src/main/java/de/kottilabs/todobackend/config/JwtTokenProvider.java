package de.kottilabs.todobackend.config;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import de.kottilabs.todobackend.dao.AuthToken;
import de.kottilabs.todobackend.dao.AuthTokenRepository;
import io.jsonwebtoken.*;

@Component
public class JwtTokenProvider {
	private static Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${security.jwt.token.secret-key:todoSecret}")
	private String secretKey = "todoSecret";
	@Value("${security.jwt.token.expire-length:180000}")
	private long validityInMilliseconds = 180000; // 30 min
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthTokenRepository authTokenRepository;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(String username, List<String> roles) {
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
		authToken.setValidity(validity);
		authTokenRepository.save(authToken);
		return token;
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			long now = System.currentTimeMillis();
			AuthToken authToken = authTokenRepository
					.findByUsernameAndValidityGreaterThan(claims.getBody().getSubject(), now).orElse(null);
			if (authToken == null) {
				return false;
			}

			if (authToken.getValidity() < now + validityInMilliseconds / 2) {
				log.info("Update validitiy of token for: {}", authToken.getUsername());
				long validity = now + validityInMilliseconds;
				authToken.setValidity(validity);
				authTokenRepository.save(authToken);
			}
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}
