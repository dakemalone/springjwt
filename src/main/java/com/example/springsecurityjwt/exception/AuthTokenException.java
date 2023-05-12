package com.example.springsecurityjwt.exception;

/**
 * @author dake malone
 * @date 2023年04月27日 下午 4:00
 */

public class AuthTokenException extends Exception{
    public AuthTokenException() {
    }

    public AuthTokenException(String message) {
        super(message);
    }
}
