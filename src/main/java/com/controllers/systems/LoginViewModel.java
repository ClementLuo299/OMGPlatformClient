package com.controllers.systems;

import com.google.common.eventbus.EventBus;
import javafx.beans.property.*;

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
    public void login() {
        if(loginService.authenticate(username.get(), password.get())) {
            message.set("Login Successful!");
        }
        else {
            message.set("Login Failed!");
        }
    }

    /**
     *
     */
    public void guestLogin() {
        //
    }
}
