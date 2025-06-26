package com.viewmodels;

import com.core.screens.ScreenManager;
//import com.gui_controllers.DashboardController;
import com.services.LoginService;
import com.utils.error_handling.Dialog;
import javafx.beans.property.*;

import java.util.function.Consumer;

/**
 * Manages GUI state and logic for the login screen
 *
 * @authors Clement Luo
 * @date May 17, 2025
 * @edited June 25, 2025
 * @since 1.0
 */
public class LoginViewModel {

    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty guestUsername = new SimpleStringProperty();
    private final StringProperty message = new SimpleStringProperty();

    private final LoginService loginService;
    private final ScreenManager screenManager;

    public LoginViewModel(LoginService loginService, ScreenManager screenManager) {
        this.loginService = loginService;
        this.screenManager = screenManager;
    }

    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty guestUsernameProperty() { return guestUsername; }
    public StringProperty messageProperty() { return message; }

    /**
     *
     */
    public void login(Consumer<String> onSuccess, Consumer<String> onError) {
        if(loginService.login(username.get(), password.get())) {
            message.set("Login Successful!");
            onSuccess.accept(username.get());
        }
        else {
            message.set("Login Failed!");
            onError.accept("Guest login failed");
        }
    }

    /**
     *
     */
    public void guestLogin(Consumer<String> onSuccess, Consumer<String> onError) {
        if(loginService.guestLogin(guestUsername.get())){
            System.out.println("Guest login successful!");
            onSuccess.accept(guestUsername.get());
        }
        else {
            onError.accept("Guest login failed");
        }
    }

    /**
     *
     */
    public void register(Consumer<String> onSuccess, Consumer<String> onError) {
        //
    }

    /**
     *
     */
    public void forgotPassword(Consumer<String> onSuccess, Consumer<String> onError) {
        //
    }

    public void handleGuestLogin() {
        System.out.println("Guest login button pressed");
        guestLogin(
                username -> {
                    System.out.println("Guest login successful!");
                    //navigateToDashboard(username, true);
                },
                errorMessage -> showError("Guest Login Error", errorMessage)
        );
    }

    public void handleLogin() {
        login(
                username -> {
                    System.out.println("Login successful for: " + username);
                    //navigateToDashboard(username, false);
                },
                errorMessage -> showError("Login Error", errorMessage)
        );
    }

    public void handleCreateAccount() {
        try {
            //screenManager.navigateTo(ScreenManager.REGISTER_SCREEN, ScreenManager.REGISTER_CSS);
        } catch (Exception e) {
            showError("Error", "Could not open register screen: " + e.getMessage());
        }
    }

    public void handleForgotPassword() {
        showInfo("Info", "Forgot password functionality not implemented yet");
    }

    /*
    private void navigateToDashboard(String username, boolean isGuest) {
        try {
            DashboardController controller = (DashboardController)
                    screenManager.reloadAndNavigateTo(ScreenManager.DASHBOARD_SCREEN, ScreenManager.DASHBOARD_CSS);

            if (controller != null) {
                controller.setCurrentUser(username, isGuest);
            }
        } catch (Exception e) {
            showError("Error", "Could not open dashboard: " + e.getMessage());
        }
    }

     */

    private void showError(String title, String message) {
        Dialog.showError(title, message, null);
    }

    private void showInfo(String title, String message) {
        Dialog.showInfo(title, message);
    }

}
