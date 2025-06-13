package com.java.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtService {

	@Value("${SECRET}")
	public static String SECRET;
//	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	@Value("${token.expiration.minutes}")
	private int tokenExpirationMinutes;

	@Value("${token.signature.algorithm}")
	private String tokenSignatureAlgorithm;

	// Extract username from JWT token
	public String extractUsername(String token) {
		log.debug("Extracting username from token: {}", token);
		return extractClaim(token, Claims::getSubject);
	}

	// Extract expiration date from JWT token
	public Date extractExpiration(String token) {
		log.debug("Extracting expiration date from token: {}", token);
		return extractClaim(token, Claims::getExpiration);
	}

	// Extract claim from JWT token
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		log.debug("Extracting claim from token: {}", token);
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Parse and extract all claims from JWT token
	private Claims extractAllClaims(String token) {
		log.debug("Extracting all claims from token: {}", token);
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	// Check if JWT token is expired
	private Boolean isTokenExpired(String token) {
		log.debug("Checking if token is expired: {}", token);
		return extractExpiration(token).before(new Date());
	}

	// Validate JWT token
	public Boolean validateToken(String token, UserDetails userDetails) {
		log.debug("Validating token: {}", token);
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// Generate JWT token for given username
	public String generateToken(String userName) {
		log.debug("Generating token for user: {}", userName);
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}

	// Create JWT token with specified claims and username
	private String createToken(Map<String, Object> claims, String userName) {
		log.debug("Creating token for user: {}", userName);
		return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis()))
				// .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) //
				// Token expiration time (30
				// minutes)
				// .signWith(getSignKey(), SignatureAlgorithm.HS256)
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * tokenExpirationMinutes)) // Token
																											// expiration
																											// time
				.signWith(getSignKey(), SignatureAlgorithm.forName(tokenSignatureAlgorithm))

				.compact();
	}

	// Get the signing key used for JWT
	private Key getSignKey() {
		log.debug("Getting signing key.");
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
