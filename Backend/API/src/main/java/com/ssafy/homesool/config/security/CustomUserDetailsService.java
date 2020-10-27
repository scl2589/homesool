package com.ssafy.homesool.config.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.ssafy.homesool.exception.UserNotFoundException;
import com.ssafy.homesool.repository.UserRepository;

@AllArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public CustomUserDetails loadUserByUsername(String userId) {
		return userRepository.findById(Long.parseLong(userId))
			.map(u -> new CustomUserDetails(u,
				Collections.singleton(new SimpleGrantedAuthority("USER"))))
			.orElseThrow(() -> new UserNotFoundException(Long.parseLong(userId)));
	}
}
