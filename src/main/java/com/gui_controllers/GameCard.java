package com.gui_controllers;

import com.games.GameModule;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Custom component for displaying game cards in the dashboard.
 * This represents a single game with its stats and play button.
 *
 * @authors Clement Luo
 * @date July 18, 2025
 * @edited July 18, 2025
 * @since 1.0
 */
public class GameCard extends VBox {
    
    @FXML private ImageView gameIcon;
    @FXML private Label gameName;
    @FXML private Label gameDetails;
    @FXML private Button playButton;
    @FXML private HBox cardContent;
    
    private GameModule gameModule;
    private Runnable onPlayAction;
    
    /**
     * Creates a new game card with the specified game module.
     * 
     * @param gameModule The game module to display
     * @param onPlayAction Action to execute when play button is clicked
     */
    public GameCard(GameModule gameModule, Runnable onPlayAction) {
        this.gameModule = gameModule;
        this.onPlayAction = onPlayAction;
        
        // Load the FXML for this component
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/GameCard.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            // If FXML loading fails, create a basic layout
            createBasicLayout();
        }
        
        initializeCard();
    }
    
    /**
     * Creates a basic layout if FXML loading fails.
     */
    private void createBasicLayout() {
        this.getStyleClass().add("game-stat-card");
        
        cardContent = new HBox();
        cardContent.setSpacing(10);
        
        gameIcon = new ImageView();
        gameIcon.setFitHeight(48);
        gameIcon.setFitWidth(48);
        gameIcon.getStyleClass().add("game-icon");
        
        VBox gameInfo = new VBox();
        gameInfo.setSpacing(2);
        
        gameName = new Label();
        gameName.getStyleClass().add("game-name");
        
        gameDetails = new Label();
        gameDetails.getStyleClass().add("game-details");
        
        gameInfo.getChildren().addAll(gameName, gameDetails);
        
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        playButton = new Button("Play");
        playButton.getStyleClass().add("play-button");
        
        cardContent.getChildren().addAll(gameIcon, gameInfo, spacer, playButton);
        this.getChildren().add(cardContent);
    }
    
    /**
     * Initializes the card with game data.
     */
    private void initializeCard() {
        if (gameModule == null) {
            return;
        }
        
        // Set game name
        gameName.setText(gameModule.getGameName());
        
        // Set game details
        String details = String.format("Level %d • %d-%d Players • %d min",
            getRandomLevel(), // TODO: Get actual user level for this game
            gameModule.getMinPlayers(),
            gameModule.getMaxPlayers(),
            gameModule.getEstimatedDuration()
        );
        gameDetails.setText(details);
        
        // Set game icon (try to load from module resources)
        try {
            String iconPath = gameModule.getGameIconPath();
            if (iconPath != null && !iconPath.isEmpty()) {
                Image icon = new Image(getClass().getResourceAsStream(iconPath));
                if (!icon.isError()) {
                    gameIcon.setImage(icon);
                } else {
                    // Fallback to default icon
                    loadDefaultIcon();
                }
            } else {
                // No icon path specified, use default
                loadDefaultIcon();
            }
        } catch (Exception e) {
            // Use default icon if loading fails
            loadDefaultIcon();
        }
        
        // Set up play button action
        playButton.setOnAction(e -> {
            if (onPlayAction != null) {
                onPlayAction.run();
            }
        });
    }
    
    /**
     * Loads the default game icon.
     */
    private void loadDefaultIcon() {
        try {
            Image defaultIcon = new Image(getClass().getResourceAsStream("/icons/games/default_game_icon.png"));
            gameIcon.setImage(defaultIcon);
        } catch (Exception ex) {
            // If even default icon fails, leave it empty
        }
    }
    
    /**
     * Gets a random level for demonstration purposes.
     * TODO: Replace with actual user level for this game.
     */
    private int getRandomLevel() {
        return (int) (Math.random() * 10) + 1; // 1-10
    }
    
    /**
     * Gets the game module associated with this card.
     * 
     * @return The game module
     */
    public GameModule getGameModule() {
        return gameModule;
    }
} 