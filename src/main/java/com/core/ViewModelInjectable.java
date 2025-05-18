package com.core;

/**
 * Implement this interface in controllers that accept a ViewModel.
 *
 * @param <T> the type of ViewModel
 */
public interface ViewModelInjectable<T> {
    void setViewModel(T viewModel);
}
