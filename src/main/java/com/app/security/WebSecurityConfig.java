package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.app.security.services.UserDetailsImpl;
import com.app.security.jwt.AuthEntryPointJwt;
import com.app.security.jwt.AuthTokenFilter;
import com.app.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfiguration {

	@Autowired
	UserDetailsImpl userDetailsService;

	@Autowired
	private com.app.security.jwt.AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthenticationEventPublisher authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	@Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
           AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
            
    
            authenticationManagerBuilder.authenticationEventPublisher(authenticationJwtTokenFilter());
            return authenticationManagerBuilder.build();
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests().requestMatchers("/api/auth/**").permitAll()
			.requestMatchers("/api/test/**").permitAll()
			.anyRequest().authenticated();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}*/
