package com.safalifter.authservice.repository; 

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safalifter.authservice.entity.User; 


@Repository
public interface UserRepository extends JpaRepository<User, Long>{ 
	boolean existsByUserName(String username); 
	Optional<User> findByUserName(String username); 
	Optional<User> findById(Long id);

} 
