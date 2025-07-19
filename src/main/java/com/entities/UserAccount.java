package com.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a user account.
 *
 * @authors Clement Luo, Irith Irith, Dylan Shiels, Fatin Abrar Ankon
 * @date March 4, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {
    private String username;
    private String password;
    private String email;
    private String created_date; //Date that account was created
    private String bio; //Biography
    private String fullName;
    private String dob; // Date of Birth as String
    private float sessionIntensityLevel = 1.0f; // Default to neutral
    private boolean online; // The online status of this player
    private int level;
    private int expInLevel;
    private int nextLevelThreshold;

    /**
     * Constructor that takes only required fields.
     */
    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.created_date = LocalDate.now().toString();
        this.expInLevel = 0;
        this.nextLevelThreshold = 10;
        this.level = 0;
    }

    /**
     * Checks if the player is online
     *
     * @return true if the player is online, false otherwise
     */
    public boolean isOnline() {
        // Since this is a stub implementation, always return true
        // In a real application, this would check the player's connection status
        return true;
    }

    /**
     * Sets the Online status of this com.network.IO.User to true
     */
    public void logIn() {
        this.online = true;
    }

    /**
     * Sets the Online status of this com.network.IO.User to false
     */
    public void logOff() {
        this.online = false;
    }
}
