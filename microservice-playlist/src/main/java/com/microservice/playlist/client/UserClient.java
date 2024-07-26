package com.microservice.playlist.client;

import com.microservice.playlist.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-user", url = "http://localhost:8080")
public interface UserClient {

    @GetMapping("/users/{email}")
    UserDTO getUserByEmail(@PathVariable("email") String email); //obtener un usuario por email (mail) de la base de datos de usuarios (msvc-user)
}                                                                                                                     //en el microservicio de usuarios (msvc-user) debemos exponer un endpoint para obtener un usuario por email
                                                                                                                      //y en el microservicio de playlists (msvc-playlist) debemos consumir este endpoint para obtener el usuario autenticado
                                                                                                                      //y poder asignarle la playlist creada
