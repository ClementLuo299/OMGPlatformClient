package com.entities;

import java.time.LocalDate;

/**
 * Represents a user account.
 *
 * @authors Clement Luo, Irith Irith, Dylan Shiels, Fatin Abrar Ankon
 * @date March 4, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
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
     * Default constructor.
     */
    public UserAccount() {
    }

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
     * Constructor with all fields.
     */
    public UserAccount(String username, String password, String email, String created_date, 
                      String bio, String fullName, String dob, float sessionIntensityLevel, 
                      boolean online, int level, int expInLevel, int nextLevelThreshold) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.created_date = created_date;
        this.bio = bio;
        this.fullName = fullName;
        this.dob = dob;
        this.sessionIntensityLevel = sessionIntensityLevel;
        this.online = online;
        this.level = level;
        this.expInLevel = expInLevel;
        this.nextLevelThreshold = nextLevelThreshold;
    }

    /**
     * Gets the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the created date.
     */
    public String getCreated_date() {
        return created_date;
    }

    /**
     * Sets the created date.
     */
    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    /**
     * Gets the bio.
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the bio.
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Gets the full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the date of birth.
     */
    public String getDob() {
        return dob;
    }

    /**
     * Sets the date of birth.
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * Gets the session intensity level.
     */
    public float getSessionIntensityLevel() {
        return sessionIntensityLevel;
    }

    /**
     * Sets the session intensity level.
     */
    public void setSessionIntensityLevel(float sessionIntensityLevel) {
        this.sessionIntensityLevel = sessionIntensityLevel;
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
     * Sets the online status.
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
     * Gets the level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the level.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Gets the experience in level.
     */
    public int getExpInLevel() {
        return expInLevel;
    }

    /**
     * Sets the experience in level.
     */
    public void setExpInLevel(int expInLevel) {
        this.expInLevel = expInLevel;
    }

    /**
     * Gets the next level threshold.
     */
    public int getNextLevelThreshold() {
        return nextLevelThreshold;
    }

    /**
     * Sets the next level threshold.
     */
    public void setNextLevelThreshold(int nextLevelThreshold) {
        this.nextLevelThreshold = nextLevelThreshold;
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
