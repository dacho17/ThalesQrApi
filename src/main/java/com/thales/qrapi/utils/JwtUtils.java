package com.thales.qrapi.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.thales.qrapi.exceptions.AuthenticationApiException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${auth.jwtSecret}")
	private String jwtSecret;

	@Value("${auth.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	public String generateJwtToken(Authentication authentication) {
		UserDetailsHelper userPrincipal = (UserDetailsHelper) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			
			logger.info(String.format("JWT successfully validated [jwt=%s].", authToken));
			return true;
		} catch (SignatureException exc) {
			logger.error(exc.getStackTrace().toString());
			throw new AuthenticationApiException("Signature error");
		} catch (MalformedJwtException exc) {
			logger.error(exc.getStackTrace().toString());
			throw new AuthenticationApiException("Invalid JWT token");
		} catch (ExpiredJwtException exc) {
			logger.error(exc.getStackTrace().toString());
			throw new AuthenticationApiException("JWT token is expired");
		} catch (UnsupportedJwtException exc) {
			logger.error(exc.getStackTrace().toString());
			throw new AuthenticationApiException("JWT token is unsupported");
		} catch (IllegalArgumentException exc) {
			logger.error(exc.getStackTrace().toString());
			throw new AuthenticationApiException("JWT claims string is empty");
		}
	}
	
	public String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}
}
