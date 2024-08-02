package com.microservice.playlist.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(auth -> auth
                        .pathMatchers(HttpMethod.GET,"/playlists/**").permitAll()
                        .anyExchange().authenticated()) // se establece que cualquier otra petición requiere autenticación
                .addFilterAt(new JWTAuthorizationFilter(), SecurityWebFiltersOrder.AUTHENTICATION); // se añade el filtro de autorización JWT
        return http.build();
    }
}
