package com.config;

import com.core.screens.ScreenView;

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
public class ScreenManagementConfig {

    /**
     * Set of screens that should be preloaded when the application starts.
     * These screens are loaded into memory during initialization to reduce
     * loading times when they are first accessed.
     */
    private final Set<ScreenView> preloadScreenViews;

    /**
     * Maximum number of screens that can be cached in memory at once.
     * When this limit is reached, least recently used screens will be
     * removed from the cache to make room for new ones.
     */
    private final int cacheSize;

    /**
     * Flag indicating whether screen caching is enabled.
     * When true, screens are cached in memory after being loaded.
     * When false, screens are reloaded from the disk each time they are accessed.
     */
    private final boolean enableCaching;

    /** GETTERS */
    public Set<ScreenView> getPreloadScreens() { return preloadScreenViews; }
    public int getCacheSize() { return cacheSize; }
    public boolean isEnableCaching() { return enableCaching; }


    /**
     * Private constructor used by the Builder.
     * Creates an immutable ScreenConfig instance.
     *
     * @param builder The builder containing configuration settings
     */
    private ScreenManagementConfig(Builder builder) {
        this.preloadScreenViews = Collections.unmodifiableSet(new HashSet<>(builder.preloadScreenViews));
        this.cacheSize = builder.cacheSize;
        this.enableCaching = builder.enableCaching;
    }

    /**
     * Builder class for creating ScreenConfig instances.
     * Provides a fluent interface for setting configuration options.
     */
    public static class Builder {
        /**
         * Set of screens to be preloaded, initialized empty.
         * Screens added to this set will be loaded during application startup.
         */
        private Set<ScreenView> preloadScreenViews = new HashSet<>();

        /**
         * Default cache size set to 10 screens.
         * This value can be modified using {@link #setCacheSize(int)}.
         */
        private int cacheSize = 10;

        /**
         * Screen caching enabled by default.
         * Can be modified using {@link #setEnableCaching(boolean)}.
         */
        private boolean enableCaching = true;

        /**
         * Adds a screen to be preloaded when the application starts.
         *
         * @param screenView The screen to preload
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if the screen is null
         */
        public Builder addPreloadScreen(ScreenView screenView) {
            preloadScreenViews.add(screenView);
            return this;
        }

        /**
         * Sets the maximum number of screens to keep in the cache.
         *
         * @param size The maximum cache size (must be positive)
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if the size is less than 1
         */
        public Builder setCacheSize(int size) {
            this.cacheSize = size;
            return this;
        }

        /**
         * Enables or disables screen caching globally.
         * When disabled, screens will be reloaded every time they are accessed.
         *
         * @param enable true to enable caching, false to disable
         * @return this builder instance for method chaining
         */
        public Builder setEnableCaching(boolean enable) {
            this.enableCaching = enable;
            return this;
        }

        /**
         * Creates a new immutable ScreenConfig instance with the current settings.
         *
         * @return A new ScreenConfig instance
         */
        public ScreenManagementConfig build() {
            return new ScreenManagementConfig(this);
        }
    }
}