package com.microservice.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(auth -> auth
                        .pathMatchers(HttpMethod.POST, "/login").permitAll() // se permite el acceso a la autenticación
                        .pathMatchers(HttpMethod.GET,"/songs/**").permitAll() // se permite el acceso a la consulta de canciones
                        .pathMatchers(HttpMethod.POST,"/songs/**").permitAll() // se permite el acceso a la creación de canciones, esto es para poder crear las canciones directamente en la bd, ya que desde front no se da la opcion
                        .pathMatchers(HttpMethod.GET,"/playlists/**").permitAll() // se permite el acceso a la consulta de playlists
                        .pathMatchers(HttpMethod.GET,"/users/**").permitAll() // se permite el acceso al endpoint de usuarios para que otro microservicio pueda consultar la información de un usuario
                        .anyExchange().authenticated()) // se establece que cualquier otra petición requiere autenticación
                .addFilterAt(new JWTAuthorizationFilter(), SecurityWebFiltersOrder.AUTHENTICATION); // se añade el filtro de autorización JWT
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Access-Control-Expose-Headers", "Authorization"));
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // Permitir cualquier origen
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "PATCH", "DELETE"));
        configuration.setMaxAge(Duration.ZERO);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
