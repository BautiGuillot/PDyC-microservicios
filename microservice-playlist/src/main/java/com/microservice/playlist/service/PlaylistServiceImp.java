package com.microservice.playlist.service;


import com.microservice.playlist.client.SongClient;
import com.microservice.playlist.client.UserClient;
import com.microservice.playlist.dto.SongDTO;
import com.microservice.playlist.dto.UserDTO;
import com.microservice.playlist.model.Playlist;
import com.microservice.playlist.repository.PlaylistRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PlaylistServiceImp implements PlaylistService {

    @Autowired
    private PlaylistRepository repository;

    @Autowired
    private SongClient songClient;

    @Autowired
    private UserClient userClient;


    @Override
    public void createPlaylist(String name, String mail) {
        UserDTO owner = userClient.getUserByEmail(mail); //obtener el usuario autenticado por email, para luego contar con el id del usuario y asi poder crear la playlist con el id. Aqui consultamos el microservicio de usuarios para obtener el usuario autenticado
        System.out.println("5 Usuario dueño de la playlist: " + owner);
        Playlist playlist = new Playlist(name, owner.getId());
        repository.save(playlist);
    }

    @Override
    public void deletePlaylist(Long playlistId, String mail) {
        Optional<Playlist> optionalPlaylist = repository.findById(playlistId); //buscar la playlist por id y guardarla en un optional para verificar si existe, en el optional se guarda la playlist si existe. Si no existe se guarda un null
        if (optionalPlaylist.isPresent()) { //si la playlist existe  isPresent() retorna true
            Playlist playlist = optionalPlaylist.get(); //obtener la playlist del optional y guardarla en una variable playlist para eliminarla
            if (!isOwner(playlist, mail))
                throw new RuntimeException("You are not the owner of the playlist");
            repository.deleteById(playlistId);
        } else {
            throw new RuntimeException("Playlist not found");
        }
    }

    @Transactional
    @Override
    public void addSongToPlaylist(Long songId, Long playlistId, String mail) {
        //SongDTO songDTO = songClient.getSongById(songId);
        Optional<Playlist> optionalPlaylist = repository.findById(playlistId); //buscar la playlist por id y guardarla en un optional para verificar si existe, en el optional se guarda la playlist si existe. Si no existe se guarda un null
        if (optionalPlaylist.isPresent()) { //si la playlist existe  isPresent() retorna true
            Playlist playlist = optionalPlaylist.get(); //obtener la playlist del optional y guardarla en una variable playlist para agregarle la cancion
            if (!isOwner(playlist, mail))
                throw new RuntimeException("You are not the owner of the playlist");
            if (playlist.getSongIds().contains(songId))
                throw new RuntimeException("The song is already in the playlist");
            playlist.addSongId(songId); //agregar la cancion a la playlist
            repository.save(playlist);
        } else {
            throw new RuntimeException("Playlist not found");
        }
    }

    @Transactional
    @Override
    public void removeSongFromPlaylist(Long songId, Long playlistId, String mail) {
        Optional<Playlist> optionalPlaylist = repository.findById(playlistId);
        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            if (!isOwner(playlist, mail))
                throw new RuntimeException("You are not the owner of the playlist");
            if (!playlist.getSongIds().contains(songId))
                throw new RuntimeException("The song is not in the playlist");
            playlist.getSongIds().remove(songId);
            repository.save(playlist);
        } else {
            throw new RuntimeException("Playlist not found");
        }
    }

    @Override
    public void updatePlaylistName(Long playlistId, String newName, String mail) {
        Optional<Playlist> optionalPlaylist = repository.findById(playlistId);
        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            if (!isOwner(playlist, mail))
                throw new RuntimeException("You are not the owner of the playlist");
            playlist.setName(newName);
            repository.save(playlist);
        } else {
            throw new RuntimeException("Playlist not found");
        }
    }

    @Override
    public List<Playlist> getPlaylists() {
        return repository.findAllWithSongs();
    }

    @Override
    public List<Long> getSongsFromPlaylist(Long playlistId) { //consultar las canciones de una playlist
        // Obtener la playlist por ID
        Optional<Playlist> optionalPlaylist = repository.findById(playlistId);

        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            return playlist.getSongIds();
        } else {
            throw new RuntimeException("Playlist not found");
        }
    }

    @Override
    public Playlist getPlaylistByID(Long playlistId) {
        return repository.findById(playlistId).orElseThrow(() -> new RuntimeException("Playlist not found"));
    }


    @Override
    public List<Playlist> getPlaylistsByUser(String mail) {
        UserDTO user = userClient.getUserByEmail(mail);
        return repository.findByOwnerId(user.getId());
    }

    private boolean isOwner(Playlist playlist, String mail) {
        UserDTO owner = userClient.getUserByEmail(mail);
        return Objects.equals(playlist.getOwnerId(), owner.getId());
    }
}


