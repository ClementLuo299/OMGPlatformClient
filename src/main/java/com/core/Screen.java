package com.core;

/**
 * Maps fxml, css, controllers, and viemodels. Css and viewmodels are optional.
 *
 * @author Clement Luo
 * @date May 19, 2025
 */
public class Screen {

    private final String fxmlPath;
    private final String cssPath;
    private final Class<?> controllerType;
    private final Class<?> viewModelType;

    public Screen(String fxmlPath, String cssPath, Class<?> controllerType, Class<?> viewModelType) {
        this.fxmlPath = fxmlPath;
        this.cssPath = cssPath;
        this.controllerType = controllerType;
        this.viewModelType = viewModelType;
    }

    public Screen(String fxmlPath, String cssPath, Class<?> controllerType){
        this(fxmlPath, cssPath, controllerType, null);
    }

    public Screen(String fxmlPath, Class<?> controllerType){
        this(fxmlPath, null, controllerType, null);
    }

    public String getFxmlPath() { return fxmlPath; }

    public String getCssPath() { return cssPath; }

    public Class<?> getControllerType() { return controllerType; }

    public Class<?> getViewModelType() { return viewModelType; }

    public boolean hasViewModel() { return viewModelType != null; }

    public boolean hasCss() { return cssPath != null; }
}
