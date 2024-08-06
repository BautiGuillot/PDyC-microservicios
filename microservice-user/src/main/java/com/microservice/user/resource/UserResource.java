package com.microservice.user.resource;


import com.microservice.user.DTO.UserDTO;
import com.microservice.user.model.User;
import com.microservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        System.out.println("2 Buscando usuario por email: " + email);
        User user = (User) userService.loadUserByUsername(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("3 Usuario encontrado: " + user);

        UserDTO userDTO = mapToUserDTO(user);

        System.out.println("4 Usuario encontrado: " + userDTO);
        return ResponseEntity.ok(userDTO ); //retornar el usuario encontrado en un ResponseEntity con status 200
    }

    // Mapear un User a un UserDTO
    private UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
}



