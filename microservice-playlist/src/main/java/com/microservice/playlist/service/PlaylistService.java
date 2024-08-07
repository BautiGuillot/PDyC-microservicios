package com.microservice.playlist.service;

import com.microservice.playlist.dto.SongDTO;
import com.microservice.playlist.model.Playlist;

import java.util.List;

public interface PlaylistService {

    void createPlaylist(String name, String mail);

    void deletePlaylist(Long playlistId, String mail);

    void addSongToPlaylist(Long songId, Long playlistId, String mail);

    void removeSongFromPlaylist(Long songId, Long playlistId, String mail);

    void updatePlaylistName(Long playlistId, String newName, String mail);

    List<Playlist> getPlaylists();

    List<SongDTO> getSongsFromPlaylist(Long playlistId);

    Playlist getPlaylistByID(Long playlistId);

    List<Playlist> getPlaylistsByUser(String mail);

}

