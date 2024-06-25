package com.safalifter.authservice.config; 

import java.text.MessageFormat; 
import java.time.Duration; 
import java.time.Instant; 
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Qualifier; 
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet; 
import org.springframework.security.oauth2.jwt.JwtEncoder; 
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;


import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.BadCredentialsException; 
import org.springframework.security.oauth2.jwt.Jwt; 
import org.springframework.stereotype.Component;

import com.safalifter.authservice.entity.User;
import com.safalifter.authservice.repository.UserRepository;
import com.safalifter.authservice.userModel.Token; 

@Component
@Slf4j
public class TokenGenerator { 
	
	@Autowired
	JwtEncoder accessTokenEncoder; 

	@Autowired
	@Qualifier("jwtRefreshTokenEncoder") 
	JwtEncoder refreshTokenEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	private String createAccessToken(Authentication authentication) { 
		User user = (User) authentication.getPrincipal(); 
		Instant now = Instant.now(); 

		JwtClaimsSet claimsSet = JwtClaimsSet.builder()
				.issuer(user.getUsername())
				.issuedAt(now)
				.expiresAt(now.plus(20, ChronoUnit.MINUTES))
				.subject(user.getId().toString())
				.build();

		return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue(); 
	} 
	
	public Token createToken(Authentication authentication) { 
		Object principal = authentication.getPrincipal();
		if (principal instanceof String) {
		    Optional<User> userOptional = userRepository.findByUserName((String) principal);
		    if (userOptional.isPresent()) {
		        principal = userOptional.get();
		    } else {
		        throw new UsernameNotFoundException("User with username " + principal + " not found");
		    }
		}
		if (!(principal instanceof User)) { 
			 throw new BadCredentialsException(MessageFormat.format(
					 "Principal {0} is not of User type", principal.getClass()));
		}
	    User user = (User) principal;
	    Token tokenDTO = new Token();
	    tokenDTO.setUserId(user.getId());
  
        Object credentials = authentication.getCredentials();
        if (!(credentials instanceof Jwt)) {
        	 tokenDTO.setAccessToken(createAccessToken(authentication));
        } else {        	
        	Jwt jwt = (Jwt) credentials;
            Instant now = Instant.now(); 
            Instant expiresAt = jwt.getExpiresAt(); 
            Duration duration = Duration.between(now, expiresAt); 
            long daysUntilExpired = duration.toMinutes(); 
            if (daysUntilExpired > 20) {
                tokenDTO.setAccessToken(createAccessToken(authentication));
            } 
        } 
        return tokenDTO; 
    }
} 
