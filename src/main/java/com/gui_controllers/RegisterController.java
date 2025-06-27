package com.gui_controllers;

import com.viewmodels.RegisterViewModel;
import javafx.beans.value.ChangeListener;
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
 * @edited June 26, 2025
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
    @FXML private Button backToLoginButton;
    
    // Store listeners to prevent duplicates
    private ChangeListener<Boolean> fullNameFocusListener;
    private ChangeListener<Boolean> usernameFocusListener;
    private ChangeListener<Boolean> passwordFocusListener;
    private ChangeListener<Boolean> confirmPasswordFocusListener;

    /**
     * Sets the view model and binds all UI components
     */
    public void setViewModel(RegisterViewModel viewModel) {
        if (viewModel == null) {
            return;
        }
        
        // Clear existing listeners to prevent duplicates
        clearListeners();
        
        // Bind properties and actions
        viewModel.fullNameProperty().bindBidirectional(fullNameField.textProperty());
        viewModel.usernameProperty().bindBidirectional(usernameField.textProperty());
        viewModel.passwordProperty().bindBidirectional(passwordField.textProperty());
        viewModel.confirmPasswordProperty().bindBidirectional(confirmPasswordField.textProperty());
        
        // Set up date picker binding
        setupDatePickerBinding(viewModel);
        
        // Set up action handlers
        createAccountButton.setOnAction(e -> viewModel.handleRegistration());
        backToLoginButton.setOnAction(e -> viewModel.navigateToLogin());
        
        // Ensure text fields lose focus when clicking outside
        mainContainer.setOnMouseClicked(e -> mainContainer.requestFocus());
        
        // Set up field focus logging
        fullNameFocusListener = (obs, oldVal, newVal) -> viewModel.onFieldFocus("Full name", newVal);
        usernameFocusListener = (obs, oldVal, newVal) -> viewModel.onFieldFocus("Username", newVal);
        passwordFocusListener = (obs, oldVal, newVal) -> viewModel.onFieldFocus("Password", newVal);
        confirmPasswordFocusListener = (obs, oldVal, newVal) -> viewModel.onFieldFocus("Confirm password", newVal);
        
        fullNameField.focusedProperty().addListener(fullNameFocusListener);
        usernameField.focusedProperty().addListener(usernameFocusListener);
        passwordField.focusedProperty().addListener(passwordFocusListener);
        confirmPasswordField.focusedProperty().addListener(confirmPasswordFocusListener);
    }
    
    /**
     * Sets up date picker binding to update ViewModel when date changes
     */
    private void setupDatePickerBinding(RegisterViewModel viewModel) {
        dobPicker.setOnAction(event -> {
            LocalDate selectedDate = dobPicker.getValue();
            if (selectedDate != null) {
                String dateString = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                viewModel.dateOfBirthProperty().set(dateString);
                viewModel.onDatePickerChange("Date of birth", dateString);
            }
        });
    }
    
    /**
     * Clears existing listeners to prevent duplicate logging
     */
    private void clearListeners() {
        if (fullNameFocusListener != null) {
            fullNameField.focusedProperty().removeListener(fullNameFocusListener);
        }
        if (usernameFocusListener != null) {
            usernameField.focusedProperty().removeListener(usernameFocusListener);
        }
        if (passwordFocusListener != null) {
            passwordField.focusedProperty().removeListener(passwordFocusListener);
        }
        if (confirmPasswordFocusListener != null) {
            confirmPasswordField.focusedProperty().removeListener(confirmPasswordFocusListener);
        }
    }
}