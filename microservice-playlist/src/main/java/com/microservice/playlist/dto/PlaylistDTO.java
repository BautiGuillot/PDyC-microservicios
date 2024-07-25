package com.microservice.playlist.dto;

public class PlaylistDTO {
    private Long id;
    private String name;
    private int songCount;

    public PlaylistDTO(Long id, String name, int songCount) {
        this.id = id;
        this.name = name;
        this.songCount = songCount;
    }

    public PlaylistDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }
}