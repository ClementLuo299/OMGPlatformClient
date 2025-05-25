package com.core.screens;

import java.net.URL;

/**
 * Maps fxml, css, controllers, and viemodels. Css and viewmodels are optional.
 *
 * @author Clement Luo
 * @date May 19, 2025
 */
public class ScreenTemplate {

    private final String fxmlPath;
    private final String cssPath;
    private final boolean cacheable;
    private final Class<?> controllerType;
    private final Class<?> viewModelType;

    private ScreenTemplate(Builder builder) {
        this.fxmlPath = builder.fxmlPath;
        this.cssPath = builder.cssPath;
        this.controllerType = builder.controllerType;
        this.viewModelType = builder.viewModelType;
        this.cacheable = builder.cacheable;
    }

    public String getFxmlPath() { return fxmlPath; }

    public String getCssPath() { return cssPath; }

    public Class<?> getControllerType() { return controllerType; }

    public Class<?> getViewModelType() { return viewModelType; }

    public boolean hasViewModel() { return viewModelType != null; }

    public boolean hasCss() { return cssPath != null; }

    public boolean isCacheable() { return cacheable; }

    // Builder for the Screen class
    public static class Builder {
        private final String fxmlPath;
        private final Class<?> controllerType;

        private String cssPath = null; // Optional
        private Class<?> viewModelType = null; // Optional
        private boolean cacheable = true; // Default to true

        public Builder(String fxmlPath, Class<?> controllerType) {
            this.fxmlPath = fxmlPath;
            this.controllerType = controllerType;
        }

        public Builder withCssPath(String cssPath) {
            this.cssPath = cssPath;
            return this;
        }

        public Builder withViewModelType(Class<?> viewModelType) {
            this.viewModelType = viewModelType;
            return this;
        }

        public Builder cacheable(boolean cacheable) {
            this.cacheable = cacheable;
            return this;
        }

        public ScreenTemplate build() {
            // Add debug logging
            System.out.println("Building ScreenTemplate:");
            System.out.println("FXML Path: " + fxmlPath);

            // Check if resource exists
            URL fxmlResource = getClass().getResource(fxmlPath);
            System.out.println("FXML Resource URL: " + fxmlResource);

            if (fxmlResource == null) {
                // Try alternative class loaders
                fxmlResource = Thread.currentThread().getContextClassLoader().getResource(fxmlPath);
                System.out.println("FXML Resource URL (context loader): " + fxmlResource);

                if (fxmlResource == null) {
                    fxmlResource = ClassLoader.getSystemResource(fxmlPath);
                    System.out.println("FXML Resource URL (system loader): " + fxmlResource);
                }
            }

            if (fxmlResource == null) {
                throw new IllegalStateException("Cannot find FXML resource: " + fxmlPath);
            }

            return new ScreenTemplate(this);
        }

    }
}