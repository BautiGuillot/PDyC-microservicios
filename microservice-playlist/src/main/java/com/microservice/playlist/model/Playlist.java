package com.microservice.playlist.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name="owner_id", nullable = false)
    private Long ownerId;

    @ElementCollection
    @CollectionTable(name = "playlist_song_ids", joinColumns = @JoinColumn(name = "playlist_id"))
    @Column(name = "song_id")
    private List<Long> songIds = new ArrayList<>(); // List of song ids in the playlist

    public Playlist(String name, Long ownerId) {
        this.name = name;
        this.ownerId = ownerId;
    }

    public Playlist() {
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<Long> songIds) {
        this.songIds = songIds;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addSongId(Long songId) {
        this.songIds.add(songId);
    }
}
