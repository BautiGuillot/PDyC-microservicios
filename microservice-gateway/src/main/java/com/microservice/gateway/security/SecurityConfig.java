package com.microservice.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(auth -> auth
                        .pathMatchers(HttpMethod.POST, "/login").permitAll()
                        .pathMatchers(HttpMethod.GET,"/songs/**").permitAll()
                        .pathMatchers(HttpMethod.POST,"/songs/**").permitAll()
                        .pathMatchers(HttpMethod.GET,"/playlists/**").permitAll()
                        .pathMatchers(HttpMethod.POST,"/playlists/**").permitAll()
                        .pathMatchers(HttpMethod.GET,"/users/**").permitAll()
                        .anyExchange().authenticated()) // se establece que cualquier otra petición requiere autenticación
                .addFilterAt(new JWTAuthorizationFilter(), SecurityWebFiltersOrder.AUTHENTICATION); // se añade el filtro de autorización JWT
        return http.build();
    }
}
