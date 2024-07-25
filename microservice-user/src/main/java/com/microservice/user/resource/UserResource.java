package com.microservice.user.resource;


import com.microservice.user.DTO.UserDTO;
import com.microservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping("/{email}")
    public UserDTO getUserByEmail(@PathVariable("email") String email) {
        return (UserDTO) userService.loadUserByUsername(email); //endpoint para obtener un usuario por email (mail) de la base de datos de usuarios (msvc-user)
    }                                                                                                              //es consumido por el microservicio de playlists (msvc-playlist) para obtener el usuario autenticado
}
