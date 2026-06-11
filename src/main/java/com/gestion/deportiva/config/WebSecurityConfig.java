package com.gestion.deportiva.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/resources/**", "/css/**", "/images/**", "/js/**", "/publico/**", "/login", "/error")
				.permitAll().anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/privado/miperfil", true).permitAll())
				.logout(logout -> logout.logoutUrl("/privado/logout") // URL que invoca el logout
						.logoutSuccessUrl ("/login") // A dónde redirige después del logout
						.invalidateHttpSession(true) // invalida la sesión
						.deleteCookies("JSESSIONID") // borra cookie de sesión
						.permitAll());

		return http.build();
	}

	@Bean(name = "myPasswordEncoder")
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
