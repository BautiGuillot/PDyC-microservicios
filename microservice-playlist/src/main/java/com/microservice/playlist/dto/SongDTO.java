package com.microservice.playlist.dto;


import com.microservice.playlist.model.Genre;

public class SongDTO {
    private Long id;
    private String name;
    private String autor;
    private Genre genre;

    public SongDTO() {
    }

    public SongDTO(Long id, String name, String autor, Genre genre) {
        this.id = id;
        this.name = name;
        this.autor = autor;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAutor() {
        return autor;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
