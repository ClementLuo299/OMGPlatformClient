package com.config;

import lombok.experimental.UtilityClass;

/**
 * Configuration class for default user settings.
 * This class provides centralized configuration for default user preferences
 * including UI settings, game settings, and privacy settings. All configurations
 * are immutable and accessed through static methods.
 *
 * @authors Clement Luo
 * @date July 19, 2025
 * @edited July 19, 2025
 * @since 1.0
 */
@UtilityClass
public class UserSettingsConfig {
    
    // ==================== UI SETTINGS ====================
    
    /** Default theme setting (false = light theme, true = dark theme) */
    public static final boolean DEFAULT_DARK_THEME = false;
    
    /** Default chat enabled setting */
    public static final boolean DEFAULT_CHAT_ENABLED = true;
    
    /** Default window width in pixels */
    public static final int DEFAULT_WINDOW_WIDTH = 1500;
    
    /** Default window height in pixels */
    public static final int DEFAULT_WINDOW_HEIGHT = 800;
    
    /** Default window maximized state */
    public static final boolean DEFAULT_WINDOW_MAXIMIZED = false;
    
    // ==================== GAME SETTINGS ====================
    
    /** Default sound enabled setting */
    public static final boolean DEFAULT_SOUND_ENABLED = true;
    
    /** Default sound volume (0-100) */
    public static final int DEFAULT_SOUND_VOLUME = 80;
    
    /** Default music enabled setting */
    public static final boolean DEFAULT_MUSIC_ENABLED = true;
    
    /** Default music volume (0-100) */
    public static final int DEFAULT_MUSIC_VOLUME = 60;
    
    /** Default auto-save enabled setting */
    public static final boolean DEFAULT_AUTO_SAVE_ENABLED = true;
    
    /** Default auto-save interval in minutes */
    public static final int DEFAULT_AUTO_SAVE_INTERVAL = 5;
    
    // ==================== PRIVACY SETTINGS ====================
    
    /** Default remember login setting */
    public static final boolean DEFAULT_REMEMBER_LOGIN = false;
    
    /** Default auto-login setting */
    public static final boolean DEFAULT_AUTO_LOGIN = false;
    
    /** Default share game stats setting */
    public static final boolean DEFAULT_SHARE_GAME_STATS = true;
    
    /** Default show online status setting */
    public static final boolean DEFAULT_SHOW_ONLINE_STATUS = true;
    
    // ==================== METADATA ====================
    
    /** Default application version */
    public static final String DEFAULT_VERSION = "1.0";
} 