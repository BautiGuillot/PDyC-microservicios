package com.microservice.playlist.client;

import com.microservice.playlist.dto.SongDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "songClient", url = "http://localhost:8080", configuration = FeignConfig.class)
public interface SongClient {

    @GetMapping("/songs/{id}")
    SongDTO getSongById(@PathVariable("id") Long id); //obtener una cancion por id de la base de datos de canciones (msvc-song)
}                                                                                                 //en el microservicio de canciones (msvc-song) debemos exponer un endpoint para obtener una cancion por id
                                                                                                  //y en el microservicio de playlists (msvc-playlist) debemos consumir este endpoint para obtener la cancion
                                                                                                  //y poder agregarla a la playlist creada
