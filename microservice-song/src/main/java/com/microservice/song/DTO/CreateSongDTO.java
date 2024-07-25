package com.microservice.song.DTO;


import com.microservice.song.model.Genre;

public class CreateSongDTO {
    private String name;
    private String autor;
    private Genre genre;

    public CreateSongDTO() {
    }

    public CreateSongDTO(String name, String autor, Genre genre) {
        this.name = name;
        this.autor = autor;
        this.genre = genre;
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
