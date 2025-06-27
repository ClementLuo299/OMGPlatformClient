package com.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Configuration constants for validation input specifications.
 * This class centralizes all validation-related input rules and limits
 * to make them easily configurable and maintainable.
 *
 * @authors Clement Luo
 * @date June 26, 2025
 * @edited June 26, 2025
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationConfig {
    
    // ==================== PASSWORD VALIDATION ====================
    
    /** Minimum password length required */
    public static final int MIN_PASSWORD_LENGTH = 6;
    
    /** Maximum password length allowed */
    public static final int MAX_PASSWORD_LENGTH = 128;
    
    // ==================== USERNAME VALIDATION ====================
    
    /** Minimum username length required */
    public static final int MIN_USERNAME_LENGTH = 3;
    
    /** Maximum username length allowed */
    public static final int MAX_USERNAME_LENGTH = 20;
    
    /** Regex pattern for valid username characters (alphanumeric and underscores only) */
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]+$";
    
    // ==================== EMAIL VALIDATION ====================
    
    /** Regex pattern for valid email format */
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
} 