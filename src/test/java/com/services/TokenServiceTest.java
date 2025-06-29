package com.services;

import com.network.auth.JWTToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Tests for TokenService functionality
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @since 1.0
 */
public class TokenServiceTest {

    private TokenService tokenService;
    private JWTToken testToken;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        
        // Create a test token
        testToken = JWTToken.fromServerResponse(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token",
            "testuser",
            3600
        );
    }

    @Test
    void testTokenStorageAndRetrieval() {
        // Initially no token should be stored
        assertNull(tokenService.getToken());
        assertFalse(tokenService.hasValidToken());
        assertTrue(tokenService.isTokenExpired());
        
        // Store a token
        tokenService.setToken(testToken);
        
        // Token should now be available
        assertNotNull(tokenService.getToken());
        assertTrue(tokenService.hasValidToken());
        assertFalse(tokenService.isTokenExpired());
        assertEquals("testuser", tokenService.getCurrentUsername());
    }

    @Test
    void testTokenClearing() {
        // Store a token
        tokenService.setToken(testToken);
        assertTrue(tokenService.hasValidToken());
        
        // Clear the token
        tokenService.clearToken();
        
        // Token should be cleared
        assertNull(tokenService.getToken());
        assertFalse(tokenService.hasValidToken());
        assertTrue(tokenService.isTokenExpired());
        assertNull(tokenService.getCurrentUsername());
    }

    @Test
    void testAuthorizationHeader() {
        // No token should return null
        assertNull(tokenService.getAuthorizationHeader());
        
        // Store a token
        tokenService.setToken(testToken);
        
        // Should return proper authorization header
        String authHeader = tokenService.getAuthorizationHeader();
        assertNotNull(authHeader);
        assertTrue(authHeader.startsWith("Bearer "));
        assertTrue(authHeader.contains(testToken.getToken()));
    }

    @Test
    void testExpiredTokenHandling() {
        // Create an expired token
        JWTToken expiredToken = JWTToken.fromServerResponse(
            "expired.token",
            "testuser",
            -3600 // Expired 1 hour ago
        );
        
        // Store expired token
        tokenService.setToken(expiredToken);
        
        // Should not be considered valid
        assertFalse(tokenService.hasValidToken());
        assertTrue(tokenService.isTokenExpired());
    }

    @Test
    void testTokenExpirationChecking() {
        // Create a token that expires in 5 minutes
        JWTToken shortLivedToken = JWTToken.fromServerResponse(
            "short.token",
            "testuser",
            300 // 5 minutes
        );
        
        tokenService.setToken(shortLivedToken);
        
        // Should expire within 10 minutes
        assertTrue(tokenService.expiresWithin(10));
        
        // Should not expire within 1 minute (unless we're very unlucky with timing)
        assertFalse(tokenService.expiresWithin(1));
    }

    @Test
    void testSecondsUntilExpiration() {
        tokenService.setToken(testToken);
        
        Long secondsLeft = tokenService.getSecondsUntilExpiration();
        assertNotNull(secondsLeft);
        assertTrue(secondsLeft > 0);
        assertTrue(secondsLeft <= 3600); // Should be less than or equal to 1 hour
    }
} 