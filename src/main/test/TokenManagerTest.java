package com.network.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TokenManager functionality
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @since 1.0
 */
public class TokenManagerTest {

    private TokenManager tokenManager;
    private JWTToken testToken;

    @BeforeEach
    void setUp() {
        // Get singleton instance
        tokenManager = TokenManager.getInstance();
        
        // Clear any existing token
        tokenManager.clearToken();
        
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
        assertNull(tokenManager.getToken());
        assertFalse(tokenManager.hasValidToken());
        assertTrue(tokenManager.isTokenExpired());
        
        // Store a token
        tokenManager.setToken(testToken);
        
        // Token should now be available
        assertNotNull(tokenManager.getToken());
        assertTrue(tokenManager.hasValidToken());
        assertFalse(tokenManager.isTokenExpired());
        assertEquals("testuser", tokenManager.getCurrentUsername());
    }

    @Test
    void testTokenClearing() {
        // Store a token
        tokenManager.setToken(testToken);
        assertTrue(tokenManager.hasValidToken());
        
        // Clear the token
        tokenManager.clearToken();
        
        // Token should be cleared
        assertNull(tokenManager.getToken());
        assertFalse(tokenManager.hasValidToken());
        assertTrue(tokenManager.isTokenExpired());
        assertNull(tokenManager.getCurrentUsername());
    }

    @Test
    void testAuthorizationHeader() {
        // No token should return null
        assertNull(tokenManager.getAuthorizationHeader());
        
        // Store a token
        tokenManager.setToken(testToken);
        
        // Should return proper authorization header
        String authHeader = tokenManager.getAuthorizationHeader();
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
        tokenManager.setToken(expiredToken);
        
        // Should not be considered valid
        assertFalse(tokenManager.hasValidToken());
        assertTrue(tokenManager.isTokenExpired());
    }

    @Test
    void testSingletonPattern() {
        TokenManager instance1 = TokenManager.getInstance();
        TokenManager instance2 = TokenManager.getInstance();
        
        // Should be the same instance
        assertSame(instance1, instance2);
    }
} 