package com.microservice.user.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.user.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {  //Esta clase se utiliza para manejar el proceso de autenticación de la aplicación.

    private AuthenticationManager authenticationManager;    //Se declara un objeto AuthenticationManager. Este objeto se utiliza para autenticar al usuario. Se inyecta en el constructor.

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){    //Se recibe el AuthenticationManager en el constructor.
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException { //Este método se ejecuta cuando se intenta autenticar al usuario.
        try {                                                                                                                                                                                                                                              //Se obtiene el usuario y la contraseña del request.
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);                                                                                  //Se crea un objeto User con los datos del request.
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getEmail(),
                                                                                                    user.getPassword(),
                                                                                                    new ArrayList<>()); //Se crea un objeto UsernamePasswordAuthenticationToken con el email y la contraseña del usuario.
            return authenticationManager.authenticate(authToken);                                                                                         //Se autentica al usuario con el AuthenticationManager y se retorna el resultado.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,                                //Este método se ejecuta cuando la autenticación es exitosa.
                                            HttpServletResponse response,                                                              //Si es exitosa se crea un token y se agrega al header de la respuesta. Por lo que el usuario queda autenticado con su token.
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        User user = (User) auth.getPrincipal();                                                                            //Se obtiene el usuario autenticado del Authentication. Se castea a User.
        String token = SecurityConstants.createToken(user.getUsername());                      //Se crea un token con el email del usuario. Se utiliza el método createToken de SecurityConstants.
        response.setHeader("Access-Control-Expose-Headers", "Authorization");  //Se expone el header de la respuesta.
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);  //Se agrega el token al header de la respuesta.
        response.getWriter().flush();                                                                                   //Se envía la respuesta.

    }




}
