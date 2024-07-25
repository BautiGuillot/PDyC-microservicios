package com.microservice.user.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

import static com.microservice.user.security.SecurityConstants.*;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,         // Método que se ejecuta en cada petición para validar el token y autorizar al usuario a acceder a los recursos
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(HEADER_STRING);

        if(header == null || !header.startsWith(TOKEN_PREFIX)){         // Si el token es nulo o no comienza con el prefijo, se continúa con la cadena de filtros
            chain.doFilter(req,res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(header);   // Obtiene la autenticación del token y la guarda en la variable authentication

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));      // Establece los detalles de la autenticación en la petición HTTP actual (req)

        SecurityContextHolder.getContext().setAuthentication(authentication);   // Establece la autenticación
        chain.doFilter(req,res);


    }


}
