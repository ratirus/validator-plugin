package com.validator.common.jwt;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class JWTValidatorAI {
	
	public static String HEADER_NAME = "VALIDATOR-AUTH-TOKEN";
	
	public static String ISSUER = "Validator";
	
	@Value("${validator.jwt.secret-key}")
	public static String SECRET_KEY = "Validator-AI_Token_key$24";
	
	@Value("${validator.jwt.access-token-expires-limit}")
	public static int EXPIRES_LIMIT = 365;


	
	public static Algorithm getAlgorithm() {
		try {
			return Algorithm.HMAC256(JWTValidatorAI.SECRET_KEY);
		} catch (IllegalArgumentException e) {
			return Algorithm.none();
		}
	}
	
	public String createToken(String user) {
		return createToken(user, nowAfterDaysToDate(JWTValidatorAI.EXPIRES_LIMIT));
	}
	
	private String createToken(String user, Date date) {
		try {
			return JWT.create()
					.withIssuer(JWTValidatorAI.ISSUER)
					.withClaim("user", user)
					.withExpiresAt(date)
					.sign(JWTValidatorAI.getAlgorithm());
		} catch (JWTCreationException createEx) {
			return null;
		}
	}

	public String createToken(String user, String role) {
		return createToken(user, role, nowAfterDaysToDate(JWTValidatorAI.EXPIRES_LIMIT));
	}

	private String createToken(String user, String role, Date date) {
		try {
			return JWT.create()
					// .withIssuer(JWTValidatorAI.ISSUER)
					.withSubject(HEADER_NAME)
					.withClaim("user", user)
					.withClaim("role", role)
					.withExpiresAt(date)
					.sign(JWTValidatorAI.getAlgorithm());
		} catch (JWTCreationException createEx) {
			return null;
		}
	}

	public void setTokenHeader(HttpServletResponse response, String token) {
		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader(HEADER_NAME, token);
		// response.setHeader(HEADER_NAME, token);
	}

	public Optional<String> getTokenId(String token) {
		try {
			return Optional.ofNullable(JWT.require(getAlgorithm()).build().verify(token).getClaim("user").asString());
		} catch (Exception e) {
			log.error("Token id verification failed: {}", e.getMessage());
			return Optional.empty();
		} 
	}

	public Optional<String> getTokenRole(String token) {
		try {
			return Optional.ofNullable(JWT.require(getAlgorithm()).build().verify(token).getClaim("role").asString());
		} catch (Exception e) {
			log.error("Token role verification failed: {}", e.getMessage());
			return Optional.empty();
		} 
	}
	
	public Boolean isTokenValid(String token) {
		try {
			JWTVerifier verifier = JWT.require(JWTValidatorAI.getAlgorithm()).build();
			verifier.verify(token);
			
			return Boolean.TRUE;
		} catch (JWTVerificationException verifyEx) {
			return Boolean.FALSE;
		}
	}

	public String refreshToken(String member) {
		return createToken(member, nowAfterDaysToDate(JWTValidatorAI.EXPIRES_LIMIT));
	}
	
	public DecodedJWT tokenToJwt(String token) {
		try {
			return JWT.decode(token);
		} catch (JWTDecodeException decodeEx) {
			return null;
		}
	}
	
	public Date nowAfterDaysToDate(int days) {
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, days);
		dt = c.getTime();
		return dt;
	}
}
