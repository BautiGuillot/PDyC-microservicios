package com.microservice.song.service;


import com.microservice.song.model.Genre;
import com.microservice.song.model.Song;
import com.microservice.song.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImp implements SongService {

    @Autowired
    private SongRepository repository;


    @Override
    public void createSong(String name, String artist, Genre genre) {
        Song song = new Song(name, artist, genre);
        repository.save(song);
    }

    @Override
    public void deleteSong(Long songId) {
        repository.deleteById(songId);
    }


    @Override
    public void updateSong(Long songId, String name, String autor, Genre genre) {
        Song song = repository.findById(songId).get();
        song.setName(name);
        song.setAutor(autor);
        song.setGenre(genre);
        repository.save(song);
    }

    @Override
    public Song getSong(Long songId) {
        return repository.findById(songId).get();
    }

    @Override
    public List<Song> getSongs() {
        return repository.findAll();
    }

}
