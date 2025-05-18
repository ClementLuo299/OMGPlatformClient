package com.viewmodels;

import com.core.Services;
import com.google.common.eventbus.EventBus;
import com.services.LoginService;
import javafx.beans.property.*;

import java.util.function.Consumer;

/**
 * Manages GUI state and logic for the login screen
 *
 * @authors Clement Luo
 * @date May 17, 2025
 */
public class LoginViewModel {

    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty guestUsername = new SimpleStringProperty();
    private final StringProperty message = new SimpleStringProperty();

    private final LoginService loginService;
    private final EventBus eventBus;

    public LoginViewModel(LoginService loginService, EventBus eventBus) {
        this.loginService = loginService;
        this.eventBus = eventBus;
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
        }
        else {
            message.set("Login Failed!");
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
}
