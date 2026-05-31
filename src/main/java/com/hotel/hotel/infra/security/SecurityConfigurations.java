package com.hotel.hotel.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        //ROTAS PÚBLICAS
                        .requestMatchers(HttpMethod.POST, "/client").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room", "/room/{id}").permitAll()
                        .requestMatchers("/file/room/{id}", "/file/{id}").permitAll()
                        .requestMatchers("/room/disponibility/{id}").permitAll()
                        // ROTAS PRIVADAS
                        .requestMatchers(HttpMethod.GET, "/client").hasAnyAuthority("ROLE_ATTENDANT", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/room").hasAuthority( "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/room/{id}").hasAuthority( "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/room/{id}").hasAuthority( "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/room/finishCleaning/{id}").hasAnyAuthority( "ROLE_ATTENDANT","ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/reservation/checkIn/{id}", "/reservation/checkOut/{id}").hasAnyAuthority("ROLE_ATTENDANT", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/reservation").hasAnyAuthority("ROLE_ATTENDANT", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/file/{id}").hasAnyAuthority("ROLE_ATTENDANT", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/register").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll()
                ).exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\": \"Usuário não autenticado.\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\": \"Permissão negada\"}");
                        })
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager getManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
