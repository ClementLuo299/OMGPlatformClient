package com.models.accounts;

/**
 * Invalid password exception, thrown when password does not meet complexity requirements.
 *
 * @authors Clement Luo,
 * @date March 22, 2025
 */
public class PasswordException extends Exception {
    public PasswordException(String message){
        super(message);
    }
}