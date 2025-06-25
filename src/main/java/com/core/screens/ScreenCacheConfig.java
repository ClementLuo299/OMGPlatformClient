package com.core.screens;

import java.util.HashSet;
import java.util.Set;

/**
 * Configuration data structure for screen preloading.
 * Uses builder pattern to create immutable configuration instances.
 *
 * @authors Clement Luo
 * @date May 18, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ScreenCacheConfig {

    /**
     * Set of screens that should be preloaded when the application starts.
     * These screens are loaded into memory during initialization to reduce
     * loading times when they are first accessed.
     */
    private final Set<ScreenLoadable> preloadScreens;

    /** GETTERS */
    public Set<ScreenLoadable> getPreloadScreens() { return preloadScreens; }

    /**
     * Private constructor used by the Builder.
     * Creates an immutable ScreenCacheConfig instance.
     *
     * @param builder The builder containing configuration settings
     */
    private ScreenCacheConfig(Builder builder) {
        this.preloadScreens = Set.copyOf(builder.preloadScreenViews);
    }

    /**
     * Builder class for creating ScreenCacheConfig instances.
     * Provides a fluent interface for setting configuration options.
     */
    public static class Builder {
        /**
         * Set of screens to be preloaded, initialized empty.
         * Screens added to this set will be loaded during application startup.
         */
        private Set<ScreenLoadable> preloadScreenViews = new HashSet<>();

        /**
         * Adds a screen to be preloaded when the application starts.
         *
         * @param screen The screen to preload
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if the screen is null
         */
        public Builder addPreloadScreen(ScreenLoadable screen) {
            preloadScreenViews.add(screen);
            return this;
        }

        /**
         * Creates a new immutable ScreenCacheConfig instance with the current settings.
         *
         * @return A new ScreenCacheConfig instance
         */
        public ScreenCacheConfig build() { return new ScreenCacheConfig(this); }
    }
} 