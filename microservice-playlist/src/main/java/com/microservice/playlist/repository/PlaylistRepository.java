package com.microservice.playlist.repository;


import com.microservice.playlist.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByOwnerId(Long ownerId); //buscar las playlists de un usuario por su id
}
