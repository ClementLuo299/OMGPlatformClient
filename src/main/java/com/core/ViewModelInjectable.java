package com.core;

/**
 * An interface for controllers that depend on a ViewModel.
 * Implement this interface in controllers to allow the injection of a ViewModel instance,
 * enabling better separation of concerns and cleaner binding of data between the view and logic layers.
 *
 * Features:
 * - Ensures that controllers can dynamically receive their associated ViewModel at runtime.
 * - Facilitates dependency injection and improves the maintainability of the application's architecture.
 *
 * Usage:
 * - Define a controller class that implements `ViewModelInjectable<T>`.
 * - Override the `setViewModel()` method to set the required ViewModel instance.
 * - Used in conjunction with navigation utilities, such as `ScreenManager`, to inject ViewModels during screen transitions.
 *
 * @param <T> The type of the ViewModel that this controller will receive.
 *
 *
 * @authors Clement Luo,
 * @date May 18, 2025
 */
/**
 * Implement this interface in controllers that accept a ViewModel.
 *
 * @param <T> the type of ViewModel
 */
public interface ViewModelInjectable<T> {
    void setViewModel(T viewModel);
}
