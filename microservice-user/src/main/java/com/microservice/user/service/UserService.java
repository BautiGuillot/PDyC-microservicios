package com.microservice.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService  extends UserDetailsService {

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

}
