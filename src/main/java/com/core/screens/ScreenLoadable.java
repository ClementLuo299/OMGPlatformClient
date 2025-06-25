package com.core.screens;

import java.net.URL;
import java.util.function.Supplier;

/**
 * Maps fxml, css, controllers, and viewmodels. Css and viewmodels are optional.
 *
 * @author Clement Luo
 * @date May 19, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class ScreenLoadable {

    private final String fxmlPath;
    private final String cssPath;
    private final Class<?> controllerType;
    private final Class<?> viewModelType;
    private final Supplier<?> viewModelSupplier; // Services

    private ScreenLoadable(Builder builder) {
        this.fxmlPath = builder.fxmlPath;
        this.cssPath = builder.cssPath;
        this.controllerType = builder.controllerType;
        this.viewModelType = builder.viewModelType;
        this.viewModelSupplier = builder.viewModelSupplier;
    }

    public String getFxmlPath() { return fxmlPath; }
    public String getCssPath() { return cssPath; }
    public Class<?> getControllerType() { return controllerType; }
    public Class<?> getViewModelType() { return viewModelType; }
    public Supplier<?> getViewModelSupplier() { return viewModelSupplier; }
    public boolean hasViewModel() { return viewModelType != null; }
    public boolean hasCss() { return cssPath != null; }

    // Builder for the Screen class
    public static class Builder {
        private final String fxmlPath;
        private final Class<?> controllerType;

        private String cssPath = null; // Optional
        private Class<?> viewModelType = null; // Optional
        private Supplier<?> viewModelSupplier = null;

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

        public Builder withViewModelSupplier(Supplier<?> viewModelSupplier) {
            this.viewModelSupplier = viewModelSupplier;
            return this;
        }

        public ScreenLoadable build() {
            // Check if resource exists
            URL fxmlResource = getClass().getResource(fxmlPath);

            if (fxmlResource == null) {
                // Try alternative class loaders
                fxmlResource = Thread.currentThread().getContextClassLoader().getResource(fxmlPath);

                if (fxmlResource == null) {
                    fxmlResource = ClassLoader.getSystemResource(fxmlPath);
                }
            }

            if (fxmlResource == null) {
                throw new IllegalStateException("Cannot find FXML resource: " + fxmlPath);
            }

            if (viewModelType != null && viewModelSupplier != null) {
                throw new IllegalStateException("Specify either viewModelType OR viewModelSupplier, not both");
            }

            return new ScreenLoadable(this);
        }
    }
}