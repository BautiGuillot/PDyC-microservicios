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
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/playlists")
public class PlaylistResource {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> getPlaylists() {
        List<Playlist> playlists = playlistService.getPlaylists();

        List<PlaylistDTO> playlistsDTO = playlists.stream()
                .map(this::mapToPlaylistInfo)
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

//    //agregar una cancion a una playlist el id de la playlist se pasa por parametro en la URL y el id de la cancion se pasa en el body
//    @POST
//    @Path("/{id}/songs/")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response addSongToPlaylistRes(@PathParam("id") Long playlistId, SongDTO songDTO) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //obtener el usuario autenticado
//        String mail = authentication.getName(); //obtener el nombre del usuario autenticado (mail)
//        playlistService.addSongToPlaylist(songDTO.getId(), playlistId, mail);
//        return Response.status(Response.Status.CREATED).build();
//    }
//
//    //eliminar una canción de una playlist
//    @DELETE
//    @Path("/{id}/songs/{song_id}")
//    public Response removeSongFromPlaylistRes(@PathParam("id") Long playlistId, @PathParam("song_id") Long songId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //obtener el usuario autenticado
//        String mail = authentication.getName(); //obtener el nombre del usuario autenticado (mail)
//        playlistService.removeSongFromPlaylist(songId, playlistId, mail);
//        return Response.status(Response.Status.OK).build();
//    }
//
//    //cambiar el nombre de una playlist por su id en la url y el nuevo nombre en el body
//    @PUT
//    @Path("/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response updatePlaylist(@PathParam("id") Long id, String newName) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //obtener el usuario autenticado
//        String mail = authentication.getName(); //obtener el nombre del usuario autenticado (mail)
//        playlistService.updatePlaylistName(id, newName, mail);
//        return Response.status(Response.Status.OK).build();
//    }
//
//    //consultar canciones de una playlist
//    @GET
//    @Path("/{id}/songs")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getSongsFromPlaylist(@PathParam("id") Long playlistId) {
//        List<Long> songIds = playlistService.getSongsFromPlaylist(playlistId); //obtener las IDs de las canciones de la playlist
//
//        List<SongDTO> songsInfo = songIds.stream() //convertir la lista de IDs a una lista de SongDTO usando el SongClient
//                .map(songClient::getSongById) //usando el metodo getSongById del SongClient consultamos al microservicio de canciones por cada ID
//                .collect(Collectors.toList());
//        return Response.ok(songsInfo).build();
//    }
//
//    //eliminar una playlist
//    @DELETE
//    @Path("/{id}")
//    public Response deletePlaylist(@PathParam("id") Long id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //obtener el usuario autenticado
//        String mail = authentication.getName(); //obtener el nombre del usuario autenticado (mail)
//        playlistService.deletePlaylist(id, mail);
//        return Response.status(Response.Status.OK).build();
//    }
//
//    //consultar playlists de un usuario autenticado
//    @GET
//    @Path("/misPlaylists")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getMyPlaylists() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //obtener el usuario autenticado
//        String mail = authentication.getName(); //obtener el nombre del usuario autenticado (mail)
//        List<Playlist> playlists = playlistService.getPlaylistsByUser(mail);
//
//        List<PlaylistDTO> playlistsInfo = playlists.stream()        //convertir la lista de playlists a una lista de PlaylistDTO
//                .map(this::mapToPlaylistInfo)                       //usando el metodo mapToPlaylistInfo
//                .collect(Collectors.toList());                      //y coleccionando los resultados en una lista
//
//        return Response.ok(playlistsInfo).build();                  //devolver la lista de PlaylistDTO
//
//    }

    public static Mono<String> getAuthenticatedUserEmail() { //metodo para obtener el mail del usuario autenticado
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName());
    }
}

