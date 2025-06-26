package com.gui_controllers;

import com.viewmodels.RegisterViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * GUI controller for registration screen - delegates all logic to ViewModel
 *
 * @authors Fatin Abrar Ankon, Clement Luo, Dylan Shiels
 * @date March 17, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class RegisterController {

    @FXML private AnchorPane mainContainer;
    
    // Registration form fields
    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private DatePicker dobPicker;
    
    // Action buttons
    @FXML private Button createAccountButton;
    @FXML private Button loginButton;

    private RegisterViewModel viewModel;

    @FXML
    public void initialize() {
        // Set up date picker to update the ViewModel when date changes
        dobPicker.setOnAction(event -> {
            LocalDate selectedDate = dobPicker.getValue();
            if (selectedDate != null && viewModel != null) {
                String dateString = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                viewModel.dateOfBirthProperty().set(dateString);
            }
        });
    }

    /**
     * Sets the view model and binds UI components
     */
    public void setViewModel(RegisterViewModel viewModel) {
        this.viewModel = viewModel;
        if (viewModel != null) {
            bindViewModel();
        }
    }

    /**
     * Binds UI components to the view model
     */
    private void bindViewModel() {
        viewModel.fullNameProperty().bindBidirectional(fullNameField.textProperty());
        viewModel.usernameProperty().bindBidirectional(usernameField.textProperty());
        viewModel.passwordProperty().bindBidirectional(passwordField.textProperty());
        viewModel.confirmPasswordProperty().bindBidirectional(confirmPasswordField.textProperty());
        
        // Date picker binding is handled in initialize() method
    }

    // Event handlers - delegate to ViewModel
    @FXML private void handleRegistration() { viewModel.handleRegistration(); }
    @FXML private void backToLogin() { viewModel.handleBackToLogin(); }
}