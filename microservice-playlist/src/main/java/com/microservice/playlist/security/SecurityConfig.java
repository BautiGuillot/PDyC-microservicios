package com.microservice.playlist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private  AuthenticationConfiguration authenticationConfiguration;   //Se declara un objeto AuthenticationConfiguration. Este objeto se utiliza para obtener el AuthenticationManager.

    @Autowired
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration) {     //Se inyecta el UserDetailsService y el AuthenticationConfiguration en el constructor.
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,"/playlists").permitAll()
                        .requestMatchers(HttpMethod.GET,"/playlists/**").permitAll()
                        .anyRequest().fullyAuthenticated()) // Se requiere autenticación para cualquier otra petición
                .addFilter(new JWTAuthorizationFilter // Se agrega el filtro de autorización JWT a la cadena de filtros de seguridad de Spring Security para validar el token y autorizar al usuario a acceder a los recursos protegidos por la aplicación.
                        (authenticationConfiguration.getAuthenticationManager()));
        System.out.println("llego al final de securityFilterChain");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() { // Configuración de CORS, se permite el acceso desde cualquier origen, se permiten los métodos GET, POST, OPTIONS, PUT y PATCH, se permiten las cabeceras Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers y Authorization, se exponen las cabeceras Access-Control-Allow-Origin y Access-Control-Allow-Credentials y se permite el uso de credenciales.
        CorsConfiguration cc = new CorsConfiguration();
        cc.setAllowedHeaders(Arrays.asList("Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers","Access-Control-Expose-Headers","Authorization"));
        cc.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        cc.setAllowedOriginPatterns(Arrays.asList("*")); // Cambiado de setAllowedOrigins a setAllowedOriginPatterns
        cc.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT","PATCH", "DELETE"));
        cc.setMaxAge(Duration.ZERO);
        cc.setAllowCredentials(Boolean.TRUE);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cc);
        return source;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "1234";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println(encodedPassword);
    }



}
