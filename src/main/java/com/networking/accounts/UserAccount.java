package com.networking.accounts;

import java.time.LocalDate;

/**
 * Represents a user account.
 *
 * @authors Clement Luo, Irith Irith, Dylan Shiels
 * @date March 4, 2025
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
     * Getters.
     */
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getFullName() {
        return fullName;
    }
    public String getDob() {
        return dob;
    }
    public String getEmail() {
        return email;
    }
    public int getExperienceLevel() {return level;}
    public float getSessionIntensityLevel() {return sessionIntensityLevel;}

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

    public int getLevel()  {
        return this.level;
    }

    public int getExpInLevel ()  {
        return this.expInLevel;
    }

    public int getNextLevelThreshold()  {
        return this.nextLevelThreshold;
    }


    /**
     * Setters.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setDob(String dob) {
        this.dob = dob;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setSessionIntensityLevel(float sessionIntensityLevel) {this.sessionIntensityLevel = sessionIntensityLevel;}
    /**
     * Sets the Online status of this User to true
     */
    public void logIn() {
        this.online = true;
    }

    /**
     * Sets the Online status of this User to false
     */
    public void logOff() {
        this.online = false;
    }

    public void setLevel(int num)  {
        this.level = num;
    }

    public void setExpInLevel(int num)  {
        this.expInLevel = num;
    }

    public void setNextLevelThreshold(int num)  {
        this.nextLevelThreshold = num;
    }
}
