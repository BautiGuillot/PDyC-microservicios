package com.microservice.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;

public class SecurityConstants {
    public static final String SECRET = "MySecretKeyToGenJWTsPDyC141jhdfhksjhkjfhjkdfhkjhfjkfsjkfhdkjhjkfhsjkfdsjkhfsdjkhfsjhdf";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";                        //Prefijo del token en el header de autorización HTTP (necesario para que el método getAuthentication() lo pueda identificar)
    public static final String HEADER_STRING = "Authorization";               //Nombre del header donde se coloca el token de autenticación en las peticiones HTTP



    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            if (!token.startsWith(TOKEN_PREFIX)) {
                throw new IllegalArgumentException("Token inválido");
            }

            token = token.replace(TOKEN_PREFIX, "");

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (Exception e) {
            System.out.println("Error al obtener la autenticación: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
