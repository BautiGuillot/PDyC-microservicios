package com.microservice.playlist.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import static com.microservice.playlist.security.SecurityConstants.HEADER_STRING;
import static com.microservice.playlist.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter implements WebFilter { // clase que implementa WebFilter para filtrar las peticiones que llegan al servidor y verificar si el token JWT es válido y si el usuario tiene permisos para acceder a los recursos solicitados

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {  // método que se encarga de filtrar las peticiones que llegan al servidor y verificar si el token JWT es válido y si el usuario tiene permisos para acceder a los recursos solicitados
        return ServerWebExchangeMatchers.pathMatchers("/login").matches(exchange) // se verifica si la petición es para el endpoint /login y si es así, se permite el acceso sin necesidad de un token JWT
                .flatMap(matchResult -> {
                    if (matchResult.isMatch()) { // si la petición es para el endpoint /login, se permite el acceso sin necesidad de un token JWT
                        System.out.println("Login request");
                        return chain.filter(exchange);
                    }

                    String header = exchange.getRequest().getHeaders().getFirst(HEADER_STRING); // se obtiene el token JWT del header de la petición
                    if (header == null || !header.startsWith(TOKEN_PREFIX)) { // si el token JWT es nulo o no empieza con el prefijo Bearer, se rechaza la petición
                        return chain.filter(exchange);
                    }

                    UsernamePasswordAuthenticationToken authentication = SecurityConstants.getAuthentication(header); // se obtiene la autenticación del token JWT
                    System.out.println("Token valid:" + authentication.getName());
                    if (authentication != null) { // si la autenticación es diferente de nulo, se permite el acceso a la petición y se establece la autenticación en el contexto de seguridad del servidor
                        return chain.filter(exchange) // se permite el acceso a la petición
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)); // se establece la autenticación en el contexto de seguridad del servidor para permitir el acceso a los recursos solicitados por el usuario
                    }

                    return chain.filter(exchange);
                });
    }
}
