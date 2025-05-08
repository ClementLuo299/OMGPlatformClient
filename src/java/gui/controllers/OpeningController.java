package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import java.net.URL;
import gui.ScreenManager;

/**
 * Controller for the Opening screen that displays a START button
 * which redirects to the login screen when clicked
 */
public class OpeningController {
    
    @FXML
    private WebView buttonWebView;
    
    private ScreenManager screenManager;
    private WebEngine webEngine;
    
    /**
     * Initialize the controller
     */
    @FXML
    private void initialize() {
        screenManager = ScreenManager.getInstance();
        
        // Set up WebView with the glowing button HTML
        webEngine = buttonWebView.getEngine();
        
        // Configure WebView to take full size of the screen
        buttonWebView.setPrefWidth(1400);
        buttonWebView.setPrefHeight(800);
        
        // Make the WebView background transparent
        buttonWebView.setStyle("-fx-background-color: transparent;");
        
        // Load the HTML file with the button
        URL buttonUrl = getClass().getClassLoader().getResource("html/glowButton.html");
        webEngine.load(buttonUrl.toExternalForm());
        
        // Add a JavaScript alert handler to detect button clicks
        webEngine.setOnAlert(event -> {
            if ("START_CLICKED".equals(event.getData())) {
                navigateToLogin();
            }
        });
    }
    
    /**
     * Navigates to the login screen
     */
    private void navigateToLogin() {
        screenManager.navigateTo(ScreenManager.LOGIN_SCREEN, ScreenManager.LOGIN_CSS);
    }
} 