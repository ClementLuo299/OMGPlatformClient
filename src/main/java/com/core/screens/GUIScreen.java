package com.core.screens;

import javafx.scene.Parent;

/**
 * A class representing a modular GUI screen, combining its root view, controller,
 * and associated ViewModel into a single structure.
 *
 * Features:
 * - Encapsulates and provides access to the loaded FXML view (`rootView`).
 * - Manages associated controller and ViewModel instances for the screen.
 * - Simplifies retrieval of screen components with generic type support.
 *
 * Use this class to manage individual screens in conjunction with a navigation system
 * (such as ScreenManager) for a clean and organized GUI architecture.
 *
 * @authors Clement Luo,
 * @date May 18, 2025
 */
public class GUIScreen {
    private final Parent rootView; // The loaded FXML view
    private final Object controller;
    private final Object viewModel;

    public GUIScreen(Parent rootView, Object controller, Object viewModel) {
        this.rootView = rootView;
        this.controller = controller;
        this.viewModel = viewModel;
    }

    public Parent getRootView() {
        return rootView;
    }

    @SuppressWarnings("unchecked")
    public <T> T getController() {
        return (T) controller;
    }

    @SuppressWarnings("unchecked")
    public <T> T getViewModel() {
        return (T) viewModel;
    }

}
