package com.microservice.user.service;


import com.microservice.user.model.User;
import com.microservice.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Buscando usuario por email: " + email);
        User user = repository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("Usuario no encontrado");
        System.out.println("Usuario encontrado: " + user);
        return user;
    }
}
