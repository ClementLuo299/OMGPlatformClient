package com.core.lifecycle;

import java.time.Instant;

/**
 * Represents a lifecycle event in the application.
 * Used for tracking and logging application state changes.
 * 
 * @authors Clement Luo
 * @date June 24, 2025
 * @edited June 24, 2025
 * @since 1.0
 */
public class LifecycleEvent {

    //The state of the lifecycle
    private final LifecycleState state;

    //The message of the event
    private final String message;

    //The timestamp of the event
    private final Instant timestamp;

    //The error of the event
    private final Throwable error;

    /**
     * Creates a new lifecycle event.
     * 
     * @param state the lifecycle state
     * @param message the event message
     */
    public LifecycleEvent(LifecycleState state, String message) { this(state, message, null); }

    /**
     * Creates a new lifecycle event with an error.
     * 
     * @param state the lifecycle state
     * @param message the event message
     * @param error the error that occurred (can be null)
     */
    public LifecycleEvent(LifecycleState state, String message, Throwable error) {
        this.state = state;
        this.message = message;
        this.timestamp = Instant.now();
        this.error = error;
    }

    /**
     * Gets the lifecycle state of this event.
     * 
     * @return the lifecycle state
     */
    public LifecycleState getState() { return state; }

    /**
     * Gets the message describing this event.
     * 
     * @return the event message
     */
    public String getMessage() { return message;}

    /**
     * Gets the timestamp when this event occurred.
     * 
     * @return the event timestamp
     */
    public Instant getTimestamp() { return timestamp; }

    /**
     * Gets the error associated with this event, if any.
     * 
     * @return the error, or null if no error occurred
     */
    public Throwable getError() { return error;}

    /**
     * Checks if this event has an associated error.
     * 
     * @return true if this event has an error
     */
    public boolean hasError() { return error != null; }

    @Override
    public String toString() {
        return String.format("LifecycleEvent{state=%s, message='%s', timestamp=%s, hasError=%s}",
                           state, message, timestamp, hasError());
    }
} 