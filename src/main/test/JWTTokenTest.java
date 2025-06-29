package com.network.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Tests for JWT token functionality
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @since 1.0
 */
public class JWTTokenTest {

    private JWTToken validToken;
    private JWTToken expiredToken;

    @BeforeEach
    void setUp() {
        // Create a valid token (expires in 1 hour)
        validToken = JWTToken.fromServerResponse(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token",
            "testuser",
            3600
        );

        // Create an expired token (expired 1 hour ago)
        LocalDateTime expiredTime = LocalDateTime.now().minusHours(1);
        expiredToken = JWTToken.fromServerResponse(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.expired.token",
            "testuser",
            expiredTime
        );
    }

    @Test
    void testValidTokenCreation() {
        assertNotNull(validToken);
        assertEquals("testuser", validToken.getUsername());
        assertEquals("Bearer", validToken.getTokenType());
        assertFalse(validToken.isExpired());
        assertTrue(validToken.getSecondsUntilExpiration() > 0);
    }

    @Test
    void testExpiredToken() {
        assertNotNull(expiredToken);
        assertTrue(expiredToken.isExpired());
        assertTrue(expiredToken.getSecondsUntilExpiration() < 0);
    }

    @Test
    void testAuthorizationHeader() {
        String authHeader = validToken.getAuthorizationHeader();
        assertTrue(authHeader.startsWith("Bearer "));
        assertTrue(authHeader.contains(validToken.getToken()));
    }

    @Test
    void testExpiresWithin() {
        // Create a token that expires in 5 minutes
        JWTToken shortLivedToken = JWTToken.fromServerResponse(
            "test.token",
            "testuser",
            300 // 5 minutes
        );

        // Should expire within 10 minutes
        assertTrue(shortLivedToken.expiresWithin(10));
        
        // Should not expire within 1 minute (unless we're very unlucky with timing)
        assertFalse(shortLivedToken.expiresWithin(1));
    }

    @Test
    void testTokenToString() {
        String tokenString = validToken.toString();
        assertTrue(tokenString.contains("testuser"));
        assertTrue(tokenString.contains("expired=false"));
    }
} 