package com.microservice.playlist.resource;

import com.microservice.playlist.client.SongClient;
import com.microservice.playlist.dto.PlaylistDTO;
import com.microservice.playlist.dto.SongDTO;
import com.microservice.playlist.model.Playlist;
import com.microservice.playlist.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/playlists")
public class PlaylistResource {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> getPlaylists() {
        List<Playlist> playlists = playlistService.getPlaylists(); //obtener todas las playlists

        List<PlaylistDTO> playlistsDTO = playlists.stream() //convertir la lista de playlists a una lista de PlaylistDTO
                .map(this::mapToPlaylistInfo) //usando el metodo mapToPlaylistInfo que convierte una playlist a un PlaylistDTO
                .collect(Collectors.toList());

        return new ResponseEntity<>(playlistsDTO, HttpStatus.OK);
    }

    private PlaylistDTO mapToPlaylistInfo(Playlist playlist) {
        PlaylistDTO playlistInfo = new PlaylistDTO();
        playlistInfo.setId(playlist.getId());
        playlistInfo.setName(playlist.getName());
        playlistInfo.setSongCount(playlist.getSongIds().size());
        return playlistInfo;
    }

    // Crear una playlist
    @PostMapping
    public Mono<ResponseEntity<Void>> createPlaylist(@RequestBody PlaylistDTO playlistDTO) {
        return getAuthenticatedUserEmail()
                .flatMap(mail -> { //flatmap se utiliza para transformar el valor de un Mono en otro Mono y poder encadenar operaciones reactivas con el resultado del primer Mono (en este caso el mail del usuario autenticado) y el resultado del segundo Mono (en este caso la creación de la playlist)
                    System.out.println("1 Usuario autenticado: " + mail);
                    playlistService.createPlaylist(playlistDTO.getName(), mail);
                    return Mono.just(new ResponseEntity<>(HttpStatus.CREATED));
                });
    }

    //consultar canciones de una playlist
    @GetMapping("/{id}/songs")
    public ResponseEntity<List<SongDTO>> getSongsFromPlaylist(@PathVariable("id") Long playlistId) {
        List<SongDTO> songsInfo = playlistService.getSongsFromPlaylist(playlistId); // Obtener la lista de SongDTOs
        return new ResponseEntity<>(songsInfo, HttpStatus.OK); // Devolver la lista de SongDTO
    }

    //agregar una cancion a una playlist, el id de la playlist se pasa por parametro en la URL y el id de la cancion se pasa en el body
    @PostMapping("/{id}/songs")
    public Mono<ResponseEntity<Void>> addSongToPlaylist(@PathVariable("id") Long playlistId, @RequestBody SongDTO songDTO) {
        return getAuthenticatedUserEmail()
                .flatMap(mail -> {
                    System.out.println("Usuario autenticado: " + mail);
                    playlistService.addSongToPlaylist(songDTO.getId(), playlistId, mail);
                    return Mono.just(new ResponseEntity<Void>(HttpStatus.CREATED));
                });
    }

    //eliminar una canción de una playlist
    @DeleteMapping("/{id}/songs/{song_id}")
    public Mono<ResponseEntity<Void>> removeSongFromPlaylist(@PathVariable("id") Long playlistId, @PathVariable("song_id") Long songId) {
        return getAuthenticatedUserEmail()
                .flatMap(mail -> {
                    System.out.println("Usuario autenticado: " + mail);
                    playlistService.removeSongFromPlaylist(songId, playlistId, mail);
                    return Mono.just(new ResponseEntity<>(HttpStatus.OK));
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Void>> updatePlaylist(@PathVariable("id") Long id, @RequestBody PlaylistDTO playlistDTO ) { //actualizar el nombre de una playlist por id y el nuevo nombre se pasa en el body
        return getAuthenticatedUserEmail()  // Obtener el correo electrónico del usuario autenticado
                .flatMap(mail -> {  // Usar flatMap para encadenar la operación asíncrona
                    System.out.println("Usuario autenticado: " + mail);
                    playlistService.updatePlaylistName(id, playlistDTO.getName(), mail);  // Actualizar el nombre de la playlist
                    return Mono.just(new ResponseEntity<Void>(HttpStatus.OK));  // Retornar una respuesta HTTP 200 OK
                });
    }


    //eliminar una playlist
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePlaylist(@PathVariable("id") Long playlistId) {
        return getAuthenticatedUserEmail()
                .flatMap(mail -> {
                    System.out.println("Usuario autenticado: " + mail);
                    playlistService.deletePlaylist(playlistId, mail);
                    return Mono.just(new ResponseEntity<>(HttpStatus.OK));
                });
    }

    //consultar playlists de un usuario autenticado
    @GetMapping("/misPlaylists")
    public Mono<ResponseEntity<List<PlaylistDTO>>> getPlaylistsByUser() {
        return getAuthenticatedUserEmail()
                .flatMap(mail -> {
                    List<Playlist> playlists = playlistService.getPlaylistsByUser(mail); //obtener las playlists del usuario autenticado
                    List<PlaylistDTO> playlistsDTO = playlists.stream() //convertir la lista de playlists a una lista de PlaylistDTO
                            .map(this::mapToPlaylistInfo) //usando el metodo mapToPlaylistInfo que convierte una playlist a un PlaylistDTO
                            .collect(Collectors.toList());
                    return Mono.just(new ResponseEntity<>(playlistsDTO, HttpStatus.OK));
                });
    }

    public static Mono<String> getAuthenticatedUserEmail() { //metodo para obtener el mail del usuario autenticado
        return ReactiveSecurityContextHolder.getContext() //obtener el contexto de seguridad reactiva (ReactiveSecurityContextHolder) y mapearlo a un Mono de tipo String con el nombre del usuario autenticado (mail)
                .map(context -> context.getAuthentication().getName()); //obtener la autenticación del contexto y el nombre del usuario autenticado (mail) y mapearlo a un Mono de tipo String
    }
}

