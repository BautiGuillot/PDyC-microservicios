package com.microservice.song.model;

import jakarta.persistence.*;

@Entity
@Table(name="songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false)
    private String autor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    public Song() {
    }

    public Song(String name, String autor, Genre genre) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

}
