package com.java.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.java.filter.JwtAuthFilter;



import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
	private JwtAuthFilter authFilter;
	  @Value("${permit.all.endpoints}")
	    private String permitAllEndpoint;
	@Bean
	// authentication
	public UserDetailsService userDetailsService() {
//        UserDetails admin = User.withUsername("shalu")
//                .password(encoder.encode("abc123"))
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.withUsername("vansh")
//                .password(encoder.encode("abc12"))
//                .roles("USER","ADMIN","HR")
//                .build();
//        return new InMemoryUserDetailsManager(admin, user);
		return new UserInfoUserDetailsService();
	}

	// Configures security filter chain
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 List<String> permitAllEndpointsList = Arrays.asList(permitAllEndpoint.split(","));

		return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(requests -> requests
				// Allow certain endpoints to be accessed without authentication
				//.requestMatchers("/products/new", "/products/welcome", "/products/authenticate").permitAll())
				.requestMatchers(permitAllEndpointsList.toArray(new String[0])).permitAll())
				.authorizeHttpRequests(requests -> requests.requestMatchers("/products/**")
						// Require authentication for other endpoints
						.authenticated())
				.sessionManagement(management -> management
						// Configure session creation policy to be stateless (no sessions)
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				// Add JWT authentication filter before the UsernamePasswordAuthenticationFilter
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	// Configures password encoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		// Using BCrypt password encoder
		return new BCryptPasswordEncoder();
	}

	// Configures authentication provider
	@Bean
	public AuthenticationProvider authenticationProvider() {
		// Configuring DAO authentication provider with custom user details service and
		// password encoder
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	// Configures authentication manager
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		// Retrieves the authentication manager from the provided configuration
		return config.getAuthenticationManager();
	}
}