package com.gui_controllers.game_library;

import com.game.GameModule;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Custom component for displaying game cards in the game library.
 * This represents a single game with its details and play button.
 *
 * @authors Clement Luo
 * @date July 18, 2025
 * @edited July 18, 2025
 * @since 1.0
 */
public class GameLibraryCard extends VBox {
    
    @FXML private ImageView gameImage;
    @FXML private Label gameTitle;
    @FXML private Label gamePlayers;
    @FXML private Label gameTime;
    @FXML private Label gameDescription;
    @FXML private Button playButton;
    @FXML private HBox gameInfo;
    @FXML private VBox gameCardSpacer;
    
    private GameModule gameModule;
    private Runnable onPlayAction;
    
    /**
     * Creates a new game library card with the specified game module.
     * 
     * @param gameModule The game module to display
     * @param onPlayAction Action to execute when play button is clicked
     */
    public GameLibraryCard(GameModule gameModule, Runnable onPlayAction) {
        this.gameModule = gameModule;
        this.onPlayAction = onPlayAction;
        
        // Load the FXML for this component
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/GameLibraryCard.fxml"));
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
        this.getStyleClass().add("game-card");
        
        // Game image
        gameImage = new ImageView();
        gameImage.setFitHeight(100);
        gameImage.setFitWidth(100);
        gameImage.setPreserveRatio(true);
        gameImage.getStyleClass().add("game-image");
        
        // Game title
        gameTitle = new Label();
        gameTitle.getStyleClass().add("game-title");
        
        // Game info container
        gameInfo = new HBox();
        gameInfo.getStyleClass().add("game-info");
        
        // Players label
        gamePlayers = new Label();
        gamePlayers.getStyleClass().add("game-players");
        
        // Time label
        gameTime = new Label();
        gameTime.getStyleClass().add("game-time");
        
        gameInfo.getChildren().addAll(gamePlayers, gameTime);
        
        // Game description
        gameDescription = new Label();
        gameDescription.getStyleClass().add("game-description");
        gameDescription.setWrapText(true);
        
        // Spacer
        gameCardSpacer = new VBox();
        gameCardSpacer.getStyleClass().add("game-card-spacer");
        VBox.setVgrow(gameCardSpacer, javafx.scene.layout.Priority.ALWAYS);
        
        // Play button
        playButton = new Button("Play Now");
        playButton.getStyleClass().add("game-card-button");
        
        // Add all components
        this.getChildren().addAll(
            gameImage,
            gameTitle,
            gameInfo,
            gameDescription,
            gameCardSpacer,
            playButton
        );
    }
    
    /**
     * Initializes the card with game data.
     */
    private void initializeCard() {
        if (gameModule == null) {
            return;
        }
        
        // Set game title
        gameTitle.setText(gameModule.getGameName());
        
        // Set game info
        String playersText = gameModule.getMinPlayers() + "-" + gameModule.getMaxPlayers() + " Players";
        gamePlayers.setText(playersText);
        
        String timeText = gameModule.getEstimatedDuration() + " min";
        gameTime.setText(timeText);
        
        // Set game description
        gameDescription.setText(gameModule.getGameDescription());
        
        // Set game image (try to load from module resources)
        try {
            String iconPath = gameModule.getGameIconPath();
            if (iconPath != null && !iconPath.isEmpty()) {
                Image icon = new Image(getClass().getResourceAsStream(iconPath));
                if (!icon.isError()) {
                    gameImage.setImage(icon);
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
            gameImage.setImage(defaultIcon);
        } catch (Exception ex) {
            // If even default icon fails, leave it empty
        }
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