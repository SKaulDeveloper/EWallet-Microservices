package com.safalifter.authservice.controller;

import com.safalifter.authservice.config.TokenGenerator;
import com.safalifter.authservice.dto.Login;
import com.safalifter.authservice.dto.SignUp;
import com.safalifter.authservice.entity.User;
import com.safalifter.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
	UserDetailsManager userDetailsManager; 
	
	@Autowired
	TokenGenerator tokenGenerator; 
	
	@Autowired
	DaoAuthenticationProvider daoAuthenticationProvider; 
	
	@Autowired
	@Qualifier("jwtRefreshTokenAuthProvider") 
	JwtAuthenticationProvider refreshTokenAuthProvider; 
	
	@Autowired
	UserRepository userRepository;

	@PostMapping("/register")
	public ResponseEntity register(@RequestBody SignUp signupDTO) {
		User user = new User(signupDTO.getUserName(), signupDTO.getPassword());
		userDetailsManager.createUser(user);
		//Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, signupDTO.getPassword(), Collections.EMPTY_LIST); 
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			    user,signupDTO.getPassword(),Collections.emptyList());
		return ResponseEntity.ok(tokenGenerator.createToken(authentication));
	}

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody Login loginDTO) { 
		//Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUserName(), loginDTO.getPassword())); 
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			    loginDTO.getUserName(),loginDTO.getPassword(),Collections.EMPTY_LIST);
		userRepository.findByUserName(authentication.getName());
		return ResponseEntity.ok(tokenGenerator.createToken(authentication));
	} 
}
