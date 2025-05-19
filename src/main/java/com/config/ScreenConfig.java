package com.config;

import com.core.Screen;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration class for managing screen loading and caching behavior in the application.
 * This class uses the Builder pattern to create immutable configuration objects
 * that control how screens are preloaded and cached during runtime.
 *
 * @authors Clement Luo
 * @date May 18, 2025
 */
public class ScreenConfig {
    private final Set<Screen> preloadScreens;
    private final int cacheSize;
    private final boolean enableCaching;

    private ScreenConfig(Builder builder) {
        this.preloadScreens = Collections.unmodifiableSet(new HashSet<>(builder.preloadScreens));
        this.cacheSize = builder.cacheSize;
        this.enableCaching = builder.enableCaching;
    }

    /**
     * Builder class for creating ScreenConfig instances.
     * Provides a fluent interface for setting configuration options.
     */
    public static class Builder {
        private Set<Screen> preloadScreens = new HashSet<>();
        private int cacheSize = 10;
        private boolean enableCaching = true;

        /**
         * Adds a screen to be preloaded when the application starts.
         *
         * @param screen The screen to preload
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if screen is null
         */
        public Builder addPreloadScreen(Screen screen) {
            preloadScreens.add(screen);
            return this;
        }

        /**
         * Sets the maximum number of screens to keep in the cache.
         *
         * @param size The maximum cache size (must be positive)
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if size is less than 1
         */
        public Builder setCacheSize(int size) {
            this.cacheSize = size;
            return this;
        }

        public Builder setEnableCaching(boolean enable) {
            this.enableCaching = enable;
            return this;
        }

        public ScreenConfig build() {
            return new ScreenConfig(this);
        }
    }
}