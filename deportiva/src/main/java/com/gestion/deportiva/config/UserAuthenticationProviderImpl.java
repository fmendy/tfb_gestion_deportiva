package com.gestion.deportiva.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.service.UsuarioService;

@Service
public class UserAuthenticationProviderImpl implements AuthenticationProvider {

	@Autowired
	private UsuarioService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString(); // Get the raw password

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		if (userDetails == null) {
			// Throw an exception if user is not found
			throw new BadCredentialsException("Invalid username or password");
		}

		// *** IMPORTANT: Compare the provided password with the stored hashed password
		// ***
		/*
		 * if (!passwordEncoder.matches(password, userDetails.getPassword())) { throw
		 * new BadCredentialsException("Invalid username or password"); }
		 */

		// If credentials are valid, return an authenticated token
		return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
