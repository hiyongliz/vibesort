package com.vibesort;

/**
 * Custom exception for VibeSort operations
 */
public class VibesortException extends Exception {
    public VibesortException(String message) {
        super(message);
    }
    
    public VibesortException(String message, Throwable cause) {
        super(message, cause);
    }
}