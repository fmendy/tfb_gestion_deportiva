package com.gestion.deportiva.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.service.UsuarioService;

@Service
public class UserAuthenticationProviderImpl implements AuthenticationProvider {

	private final UsuarioService userDetailsService;

	private final PasswordEncoder passwordEncoder;

	UserAuthenticationProviderImpl(UsuarioService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		Object credentials = authentication.getCredentials();

		if (credentials == null) {
			throw new BadCredentialsException("Invalid username or password");
		}

		String password = credentials.toString();

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		if (userDetails == null) {
			throw new BadCredentialsException("Invalid username or password");
		}

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid username or password");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}