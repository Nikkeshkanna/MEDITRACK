package com.meditrack.config;

import com.meditrack.security.JwtAuthenticationFilter;
import com.meditrack.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {}) // Enable CORS support
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/**", "/auth/**",
                    "/api/emergency/**", "/emergency/**",
                    "/api/uploads/**", "/uploads/**"
                ).permitAll()
                .requestMatchers("/api/admin/**", "/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/patient/**", "/patient/**").hasAnyAuthority("ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_NURSE", "ROLE_ADMIN")
                .requestMatchers("/api/doctor/**", "/doctor/**").hasAnyAuthority("ROLE_DOCTOR", "ROLE_ADMIN")
                .requestMatchers("/api/nurse/**", "/nurse/**").hasAnyAuthority("ROLE_NURSE", "ROLE_ADMIN")
                .requestMatchers("/api/lab/**", "/api/reports/**", "/lab/**", "/reports/**").hasAnyAuthority("ROLE_LAB_TECHNICIAN", "ROLE_DOCTOR", "ROLE_ADMIN")
                .requestMatchers("/api/reception/**", "/reception/**").hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN")
                .requestMatchers("/api/appointments/**", "/appointments/**").hasAnyAuthority("ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_ADMIN")
                .requestMatchers("/api/support/**", "/support/**").authenticated()
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}