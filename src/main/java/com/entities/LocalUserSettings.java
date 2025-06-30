package com.entities;

import java.time.LocalDateTime;

/**
 * Data class for storing user settings locally on the client's computer.
 * This includes UI preferences, game settings, and other user-specific configurations.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @since 1.0
 */
public class LocalUserSettings {
    
    // ==================== UI SETTINGS ====================
    
    private boolean darkTheme;
    private boolean chatEnabled;
    private int windowWidth;
    private int windowHeight;
    private boolean windowMaximized;
    
    // ==================== GAME SETTINGS ====================
    
    private boolean soundEnabled;
    private int soundVolume;
    private boolean musicEnabled;
    private int musicVolume;
    private boolean autoSaveEnabled;
    private int autoSaveInterval; // in minutes
    
    // ==================== PRIVACY SETTINGS ====================
    
    private boolean rememberLogin;
    private boolean autoLogin;
    private boolean shareGameStats;
    private boolean showOnlineStatus;
    
    // ==================== METADATA ====================
    
    private String username;
    private LocalDateTime lastModified;
    private String version;
    
    // ==================== CONSTRUCTOR ====================
    
    public LocalUserSettings() {
        // Set default values
        this.darkTheme = false;
        this.chatEnabled = true;
        this.windowWidth = 1500;
        this.windowHeight = 800;
        this.windowMaximized = false;
        
        this.soundEnabled = true;
        this.soundVolume = 80;
        this.musicEnabled = true;
        this.musicVolume = 60;
        this.autoSaveEnabled = true;
        this.autoSaveInterval = 5;
        
        this.rememberLogin = false;
        this.autoLogin = false;
        this.shareGameStats = true;
        this.showOnlineStatus = true;
        
        this.lastModified = LocalDateTime.now();
        this.version = "1.0";
    }
    
    // ==================== GETTERS AND SETTERS ====================
    
    // UI Settings
    public boolean isDarkTheme() { return darkTheme; }
    public void setDarkTheme(boolean darkTheme) { 
        this.darkTheme = darkTheme; 
        this.lastModified = LocalDateTime.now();
    }
    
    public boolean isChatEnabled() { return chatEnabled; }
    public void setChatEnabled(boolean chatEnabled) { 
        this.chatEnabled = chatEnabled; 
        this.lastModified = LocalDateTime.now();
    }
    
    public int getWindowWidth() { return windowWidth; }
    public void setWindowWidth(int windowWidth) { 
        this.windowWidth = windowWidth; 
        this.lastModified = LocalDateTime.now();
    }
    
    public int getWindowHeight() { return windowHeight; }
    public void setWindowHeight(int windowHeight) { 
        this.windowHeight = windowHeight; 
        this.lastModified = LocalDateTime.now();
    }
    
    public boolean isWindowMaximized() { return windowMaximized; }
    public void setWindowMaximized(boolean windowMaximized) { 
        this.windowMaximized = windowMaximized; 
        this.lastModified = LocalDateTime.now();
    }
    
    // Game Settings
    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean soundEnabled) { 
        this.soundEnabled = soundEnabled; 
        this.lastModified = LocalDateTime.now();
    }
    
    public int getSoundVolume() { return soundVolume; }
    public void setSoundVolume(int soundVolume) { 
        this.soundVolume = Math.max(0, Math.min(100, soundVolume)); 
        this.lastModified = LocalDateTime.now();
    }
    
    public boolean isMusicEnabled() { return musicEnabled; }
    public void setMusicEnabled(boolean musicEnabled) { 
        this.musicEnabled = musicEnabled; 
        this.lastModified = LocalDateTime.now();
    }
    
    public int getMusicVolume() { return musicVolume; }
    public void setMusicVolume(int musicVolume) { 
        this.musicVolume = Math.max(0, Math.min(100, musicVolume)); 
        this.lastModified = LocalDateTime.now();
    }
    
    public boolean isAutoSaveEnabled() { return autoSaveEnabled; }
    public void setAutoSaveEnabled(boolean autoSaveEnabled) { 
        this.autoSaveEnabled = autoSaveEnabled; 
        this.lastModified = LocalDateTime.now();
    }
    
    public int getAutoSaveInterval() { return autoSaveInterval; }
    public void setAutoSaveInterval(int autoSaveInterval) { 
        this.autoSaveInterval = Math.max(1, Math.min(60, autoSaveInterval)); 
        this.lastModified = LocalDateTime.now();
    }
    
    // Privacy Settings
    public boolean isRememberLogin() { return rememberLogin; }
    public void setRememberLogin(boolean rememberLogin) { 
        this.rememberLogin = rememberLogin; 
        this.lastModified = LocalDateTime.now();
    }
    
    public boolean isAutoLogin() { return autoLogin; }
    public void setAutoLogin(boolean autoLogin) { 
        this.autoLogin = autoLogin; 
        this.lastModified = LocalDateTime.now();
    }
    
    public boolean isShareGameStats() { return shareGameStats; }
    public void setShareGameStats(boolean shareGameStats) { 
        this.shareGameStats = shareGameStats; 
        this.lastModified = LocalDateTime.now();
    }
    
    public boolean isShowOnlineStatus() { return showOnlineStatus; }
    public void setShowOnlineStatus(boolean showOnlineStatus) { 
        this.showOnlineStatus = showOnlineStatus; 
        this.lastModified = LocalDateTime.now();
    }
    
    // Metadata
    public String getUsername() { return username; }
    public void setUsername(String username) { 
        this.username = username; 
        this.lastModified = LocalDateTime.now();
    }
    
    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Creates a copy of these settings for a specific user.
     *
     * @param username The username to associate with the settings
     * @return A new LocalUserSettings instance with the username set
     */
    public LocalUserSettings forUser(String username) {
        LocalUserSettings copy = new LocalUserSettings();
        copy.username = username;
        copy.lastModified = LocalDateTime.now();
        return copy;
    }
    
    /**
     * Updates the last modified timestamp.
     */
    public void markAsModified() {
        this.lastModified = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "LocalUserSettings{" +
                "username='" + username + '\'' +
                ", darkTheme=" + darkTheme +
                ", chatEnabled=" + chatEnabled +
                ", soundEnabled=" + soundEnabled +
                ", soundVolume=" + soundVolume +
                ", musicEnabled=" + musicEnabled +
                ", musicVolume=" + musicVolume +
                ", rememberLogin=" + rememberLogin +
                ", autoLogin=" + autoLogin +
                ", lastModified=" + lastModified +
                ", version='" + version + '\'' +
                '}';
    }
} 