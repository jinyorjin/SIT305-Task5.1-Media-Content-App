package com.deakin.task51media.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Data Access Object (DAO) for the Playlist table.
 * This interface defines the database operations for video URLs.
 */
@Dao
interface PlaylistDao {

    /**
     * Inserts a new video URL into the playlist.
     * 'suspend' ensures this runs asynchronously on a background thread.
     */
    @Insert
    suspend fun insert(playlist: Playlist)

    /**
     * Retrieves all saved videos for a specific user.
     * This query implements "Data Isolation" by filtering with the 'username'.
     */
    @Query("SELECT * FROM playlist WHERE username = :username")
    suspend fun getUserPlaylist(username: String): List<Playlist>
}