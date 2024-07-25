package com.microservice.song.resource;

import com.microservice.song.DTO.CreateSongDTO;
import com.microservice.song.DTO.SongDTO;
import com.microservice.song.model.Song;
import com.microservice.song.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/songs")
public class SongResource {

    @Autowired
    private SongService songService;

    // Consultar las canciones disponibles
    @GetMapping
    public ResponseEntity<List<SongDTO>> getSongs() {
        List<Song> songs = songService.getSongs();
        List<SongDTO> songsInfo = songs.stream()
                .map(this::mapToSongInfo)
                .collect(Collectors.toList());
        return new ResponseEntity<>(songsInfo, HttpStatus.OK);
    }

    // Mapear una cancion a un CreateSongDTO con el nombre, autor y genero de la cancion
    private SongDTO mapToSongInfo(Song song) {
        SongDTO songInfo = new SongDTO();
        songInfo.setId(song.getId());
        songInfo.setName(song.getName());
        songInfo.setAutor(song.getAutor());
        songInfo.setGenre(song.getGenre());
        return songInfo;
    }

    // Crear una cancion
    @PostMapping
    public ResponseEntity<String> createSong(@RequestBody CreateSongDTO songDTO) {
        try {
            songService.createSong(songDTO.getName(), songDTO.getAutor(), songDTO.getGenre());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("El género proporcionado no es válido. Los géneros válidos son: rock, techno, pop, jazz, folk, classical.", HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener canción por ID
    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> getSongById(@PathVariable Long id) {
        Song song = songService.getSong(id);
        SongDTO songDTO = mapToSongInfo(song);
        return new ResponseEntity<>(songDTO, HttpStatus.OK);
    }
}
