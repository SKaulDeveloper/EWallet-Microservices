package com.safalifter.authservice.config; 

import java.util.Collections; 
import org.springframework.core.convert.converter.Converter; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 
import org.springframework.security.oauth2.jwt.Jwt; 
import org.springframework.stereotype.Component;

import com.safalifter.authservice.entity.User; 

@Component
public class JWTtoUserConvertor implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
	public UsernamePasswordAuthenticationToken convert(Jwt source) { 
		User user = new User(); 
		user.setId(Long.parseLong(source.getSubject())); 
		return new UsernamePasswordAuthenticationToken(user, source, Collections.EMPTY_LIST); 
	} 
} 
