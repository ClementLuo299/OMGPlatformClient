package com.test;

import com.core.ServiceManager;
import com.network.auth.JWTToken;
import com.services.TokenService;

/**
 * Example demonstrating how to use the TokenService through ServiceManager
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @since 1.0
 */
public class TokenServiceExample {

    public static void main(String[] args) {
        System.out.println("=== TokenService Example ===\n");

        // Get the ServiceManager instance
        ServiceManager serviceManager = ServiceManager.getInstance();
        
        // Get the TokenService
        TokenService tokenService = serviceManager.getTokenService();

        // Example 1: Check initial state
        System.out.println("1. Initial State:");
        System.out.println("   - Has valid token: " + tokenService.hasValidToken());
        System.out.println("   - Current username: " + tokenService.getCurrentUsername());
        System.out.println();

        // Example 2: Store a token
        System.out.println("2. Storing a Token:");
        JWTToken testToken = JWTToken.fromServerResponse(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.example.token",
            "john_doe",
            3600
        );
        tokenService.setToken(testToken);
        
        System.out.println("   - Token stored for: " + tokenService.getCurrentUsername());
        System.out.println("   - Has valid token: " + tokenService.hasValidToken());
        System.out.println("   - Authorization header: " + tokenService.getAuthorizationHeader());
        System.out.println();

        // Example 3: Check token expiration
        System.out.println("3. Token Expiration Check:");
        System.out.println("   - Is expired: " + tokenService.isTokenExpired());
        System.out.println("   - Expires within 10 minutes: " + tokenService.expiresWithin(10));
        System.out.println("   - Seconds until expiration: " + tokenService.getSecondsUntilExpiration());
        System.out.println();

        // Example 4: Check token expiration warning
        System.out.println("4. Token Expiration Warning:");
        tokenService.checkTokenExpiration(5); // Will warn if expires within 5 minutes
        System.out.println();

        // Example 5: Clear token (logout)
        System.out.println("5. Clearing Token (Logout):");
        tokenService.clearToken();
        System.out.println("   - Has valid token: " + tokenService.hasValidToken());
        System.out.println("   - Current username: " + tokenService.getCurrentUsername());
        System.out.println("   - Authorization header: " + tokenService.getAuthorizationHeader());
        System.out.println();

        System.out.println("=== Example completed ===");
    }
} 