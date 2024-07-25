package com.microservice.song.repository;


import com.microservice.song.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
