package com.deakin.task51media.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Data Access Object (DAO) for User Authentication.
 * Handles registration and login operations.
 */
@Dao
interface UserDao {

    /**
     * Registers a new user into the database.
     */
    @Insert
    suspend fun insertUser(user: User)

    /**
     * Checks if a username already exists during the sign-up process.
     * Returns a User object if found, or null if available.
     */
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    /**
     * Validates credentials during the login process.
     * Searches for a matching username and password.
     */
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?
}