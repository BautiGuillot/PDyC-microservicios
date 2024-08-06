package com.microservice.playlist.repository;


import com.microservice.playlist.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByOwnerId(Long ownerId); //buscar las playlists de un usuario por su id

    @Query("SELECT p FROM Playlist p LEFT JOIN FETCH p.songIds")
    List<Playlist> findAllWithSongs(); //buscar todas las playlists con sus canciones
}
