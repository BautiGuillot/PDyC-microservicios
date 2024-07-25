package com.microservice.song.service;


import com.microservice.song.model.Genre;
import com.microservice.song.model.Song;

import java.util.List;

public interface SongService {

    void createSong(String name, String autor, Genre genre);

    void deleteSong(Long songId);

    void updateSong(Long songId, String name, String autor, Genre genre);

    Song getSong(Long songId);

    List<Song> getSongs();
}
