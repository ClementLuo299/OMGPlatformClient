package gui.controllers.games;

import gui.ScreenManager;
import gui.controllers.GameLobbyController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import gamelogic.whist.StageType;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Whist card game UI
 * Handles game setup, card interactions, and player actions
 */
public class WhistController implements Initializable {
    
    // Header controls
    @FXML private Button backButton;
    @FXML private Text gameTitle;
    @FXML private Label roundCounter;
    @FXML private ImageView trumpSuitImage;
    @FXML private Label gameStageLabel;
    
    // Player info
    @FXML private ImageView player1Avatar;
    @FXML private Label player1Name;
    @FXML private Label player1Score;
    @FXML private Label player1Status;
    @FXML private ImageView player2Avatar;
    @FXML private Label player2Name;
    @FXML private Label player2Score;
    @FXML private Label player2Status;
    @FXML private Label pointsToWinLabel;
    
    // Game controls
    @FXML private Button startGameButton;
    @FXML private Button shuffleButton;
    @FXML private Button dealButton;
    @FXML private Button revealTrumpButton;
    @FXML private Button forfeitButton;
    @FXML private Button rulesButton;
    
    // Game board elements
    @FXML private StackPane drawPile;
    @FXML private StackPane discardPile;
    @FXML private HBox currentTrickArea;
    @FXML private Label gameStatusLabel;
    @FXML private HBox opponentCardContainer;
    @FXML private HBox playerCardContainer;
    
    // Move history and chat
    @FXML private VBox moveHistoryContainer;
    @FXML private VBox chatMessagesContainer;
    @FXML private TextField chatInputField;
    @FXML private Button sendChatButton;
    
    // Game state variables
    private StageType currentStage = StageType.DEAL;
    private int currentRound = 1;
    private String trumpSuit = "";
    private boolean isPlayerTurn = true;
    private boolean gameInProgress = false;
    private String matchId;
    private String currentUsername = "Player";
    private boolean isGuest = false;
    
    // Card management
    private final List<Node> playerCards = new ArrayList<>();
    private final List<Node> opponentCards = new ArrayList<>();
    private final List<Node> drawPileCards = new ArrayList<>();
    private final List<Node> discardPileCards = new ArrayList<>();
    
    // Utility objects
    private final Random random = new Random();
    private ScreenManager screenManager = ScreenManager.getInstance();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initial setup
        updateGameStage(StageType.DEAL);
        updateControls();
        setupCardPiles();
    }
    
    /**
     * Set the match ID for this game session
     * @param matchId The unique match identifier
     */
    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
    
    /**
     * Set current user information
     * @param username The current user's username
     * @param isGuest Whether the user is a guest
     */
    public void setCurrentUser(String username, boolean isGuest) {
        this.currentUsername = username;
        this.isGuest = isGuest;
        
        // Update UI with player name
        if (username != null && !username.isEmpty()) {
            player1Name.setText(username);
        }
    }
    
    /**
     * Update the game stage and UI elements
     * @param stage The new game stage
     */
    private void updateGameStage(StageType stage) {
        currentStage = stage;
        gameStageLabel.setText(stage.getDisplayName());
        
        // Update status message based on stage
        switch (stage) {
            case DEAL:
                gameStatusLabel.setText("Cards need to be shuffled and dealt");
                break;
            case DRAFT:
                gameStatusLabel.setText("Select cards from the draw pile");
                break;
            case DUEL:
                gameStatusLabel.setText("Play your cards to win tricks");
                break;
        }
        
        // Update UI controls based on stage
        updateControls();
    }
    
    /**
     * Update game controls based on current game state
     */
    private void updateControls() {
        if (!gameInProgress) {
            startGameButton.setDisable(false);
            shuffleButton.setDisable(true);
            dealButton.setDisable(true);
            revealTrumpButton.setDisable(true);
            forfeitButton.setDisable(true);
            return;
        }
        
        // Disable start button once game is in progress
        startGameButton.setDisable(true);
        forfeitButton.setDisable(false);
        
        // Enable/disable buttons based on game stage
        switch (currentStage) {
            case DEAL:
                shuffleButton.setDisable(false);
                dealButton.setDisable(true);
                revealTrumpButton.setDisable(true);
                break;
            case DRAFT:
                shuffleButton.setDisable(true);
                dealButton.setDisable(true);
                revealTrumpButton.setDisable(true);
                break;
            case DUEL:
                shuffleButton.setDisable(true);
                dealButton.setDisable(true);
                revealTrumpButton.setDisable(true);
                break;
        }
        
        // Additional state-based controls
        updatePlayerStatusIndicators();
    }
    
    /**
     * Update the player status indicators based on whose turn it is
     */
    private void updatePlayerStatusIndicators() {
        if (isPlayerTurn) {
            player1Status.setText("Your Turn");
            player2Status.setText("Waiting");
            player1Status.getParent().getParent().getStyleClass().add("active-player");
            player2Status.getParent().getParent().getStyleClass().remove("active-player");
        } else {
            player1Status.setText("Waiting");
            player2Status.setText("Their Turn");
            player1Status.getParent().getParent().getStyleClass().remove("active-player");
            player2Status.getParent().getParent().getStyleClass().add("active-player");
        }
    }
    
    /**
     * Set up the card piles (draw and discard)
     */
    private void setupCardPiles() {
        // Clear existing content
        drawPile.getChildren().clear();
        discardPile.getChildren().clear();
        
        // Add visual card backs to draw pile
        ImageView cardBackImage = new ImageView(new Image(getClass().getResourceAsStream("/icons/whist_icon.png")));
        cardBackImage.setFitHeight(80);
        cardBackImage.setFitWidth(60);
        cardBackImage.setPreserveRatio(true);
        
        // Add the card back image to the draw pile
        drawPile.getChildren().add(cardBackImage);
        
        // Add a label
        Label drawLabel = new Label("Draw");
        drawLabel.getStyleClass().add("pile-label");
        drawPile.getChildren().add(drawLabel);
    }
    
    /**
     * Add a message to the move history panel
     * @param message The message to add
     */
    private void addMoveHistoryEntry(String message) {
        Label entryLabel = new Label(message);
        entryLabel.getStyleClass().add("move-entry");
        entryLabel.setWrapText(true);
        moveHistoryContainer.getChildren().add(0, entryLabel); // Add at the top
    }
    
    /**
     * Add a message to the chat panel
     * @param message The message text
     * @param isSentByPlayer Whether the current player sent the message
     */
    private void addChatMessage(String message, boolean isSentByPlayer) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add("chat-message");
        
        if (isSentByPlayer) {
            messageLabel.getStyleClass().add("sent");
            messageLabel.setText("You: " + message);
        } else {
            messageLabel.getStyleClass().add("received");
            messageLabel.setText("Opponent: " + message);
        }
        
        chatMessagesContainer.getChildren().add(messageLabel);
    }
    
    /**
     * Handle the Back button click
     */
    @FXML
    private void onBackButtonClicked() {
        try {
            // Show confirmation dialog if game is in progress
            if (gameInProgress) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Exit Game");
                alert.setHeaderText("Are you sure you want to exit?");
                alert.setContentText("Your game progress will be lost.");
                
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        navigateToGameLobby();
                    }
                });
            } else {
                navigateToGameLobby();
            }
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Navigation Error", 
                    "Could not navigate back to Game Lobby: " + e.getMessage());
        }
    }
    
    /**
     * Navigate back to the game lobby
     */
    private void navigateToGameLobby() {
        try {
            GameLobbyController controller = (GameLobbyController)
                    screenManager.navigateTo(ScreenManager.GAME_LOBBY_SCREEN, ScreenManager.GAME_LOBBY_CSS);
            
            if (controller != null) {
                controller.setGame("Whist");
                controller.setCurrentUser(currentUsername, isGuest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", 
                    "Could not navigate to Game Lobby: " + e.getMessage());
        }
    }
    
    /**
     * Handle the Start Game button click
     */
    @FXML
    private void onStartGameClicked() {
        gameInProgress = true;
        currentRound = 1;
        roundCounter.setText(String.valueOf(currentRound));
        
        // Choose random player to go first
        isPlayerTurn = random.nextBoolean();
        
        // Update UI
        updateGameStage(StageType.DEAL);
        updateControls();
        
        // Log the game start in move history
        addMoveHistoryEntry("Game started. Round 1 begins.");
        
        // Enable shuffle button since we're in DEAL stage
        shuffleButton.setDisable(false);
        
        // Update game status
        gameStatusLabel.setText(isPlayerTurn ? 
                "You go first! Shuffle the deck." : 
                "Opponent goes first. Wait for them to shuffle.");
        
        // If opponent goes first, simulate their turn after a delay
        if (!isPlayerTurn) {
            simulateOpponentTurn();
        }
    }
    
    /**
     * Handle the Shuffle button click
     */
    @FXML
    private void onShuffleClicked() {
        if (!isPlayerTurn) return;
        
        // Animate the shuffle
        animateShuffle();
        
        // Log the shuffle
        addMoveHistoryEntry("You shuffled the deck.");
        
        // Enable dealing after shuffling
        dealButton.setDisable(false);
        shuffleButton.setDisable(true);
        
        // Update status
        gameStatusLabel.setText("Deck shuffled. Ready to deal cards.");
    }
    
    /**
     * Animate the shuffling of cards
     */
    private void animateShuffle() {
        // Simple visual feedback for shuffling
        drawPile.setRotate(5);
        Timeline shakeTimeline = new Timeline(
            new KeyFrame(Duration.millis(100), e -> drawPile.setRotate(-5)),
            new KeyFrame(Duration.millis(200), e -> drawPile.setRotate(5)),
            new KeyFrame(Duration.millis(300), e -> drawPile.setRotate(-5)),
            new KeyFrame(Duration.millis(400), e -> drawPile.setRotate(5)),
            new KeyFrame(Duration.millis(500), e -> drawPile.setRotate(0))
        );
        shakeTimeline.play();
    }
    
    /**
     * Handle the Deal Cards button click
     */
    @FXML
    private void onDealClicked() {
        if (!isPlayerTurn) return;
        
        // Animate dealing
        animateDealing();
        
        // Log the deal
        addMoveHistoryEntry("You dealt the cards.");
        
        // Enable revealing trump after dealing
        dealButton.setDisable(true);
        revealTrumpButton.setDisable(false);
        
        // Update status
        gameStatusLabel.setText("Cards dealt. Ready to reveal trump suit.");
    }
    
    /**
     * Animate the dealing of cards
     */
    private void animateDealing() {
        // Clear any existing cards
        playerCardContainer.getChildren().clear();
        opponentCardContainer.getChildren().clear();
        
        // Deal 13 cards to each player with animation
        Timeline dealTimeline = new Timeline();
        for (int i = 0; i < 13; i++) {
            final int index = i;
            
            // Deal to player
            KeyFrame playerFrame = new KeyFrame(Duration.millis(100 * (i * 2)), e -> {
                addCardToHand(playerCardContainer, true, index);
            });
            
            // Deal to opponent
            KeyFrame opponentFrame = new KeyFrame(Duration.millis(100 * (i * 2 + 1)), e -> {
                addCardToHand(opponentCardContainer, false, index);
            });
            
            dealTimeline.getKeyFrames().addAll(playerFrame, opponentFrame);
        }
        
        dealTimeline.play();
    }
    
    /**
     * Add a card to a player's hand
     * @param container The container to add the card to
     * @param isPlayer Whether it's the player's hand or opponent's
     * @param cardIndex Index for positioning
     */
    private void addCardToHand(HBox container, boolean isPlayer, int cardIndex) {
        try {
            StackPane cardPane = new StackPane();
            cardPane.getStyleClass().add("card");
            
            if (!isPlayer) {
                cardPane.getStyleClass().add("card-back");
            } else {
                // For player cards, show the face
                Label cardLabel = new Label(getRandomCardLabel());
                cardLabel.getStyleClass().add("card-label");
                
                // Assign a random suit class for styling
                String[] suits = {"hearts", "diamonds", "spades", "clubs"};
                cardPane.getStyleClass().add(suits[random.nextInt(suits.length)]);
                
                cardPane.getChildren().add(cardLabel);
                
                // Make player cards clickable
                cardPane.setOnMouseClicked(e -> onCardClicked(cardPane));
            }
            
            // Add to container and store reference
            container.getChildren().add(cardPane);
            
            if (isPlayer) {
                playerCards.add(cardPane);
            } else {
                opponentCards.add(cardPane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Generate a random card label (e.g. "A♥", "10♠")
     * @return A string representation of a card
     */
    private String getRandomCardLabel() {
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] suits = {"♥", "♦", "♠", "♣"};
        
        return ranks[random.nextInt(ranks.length)] + suits[random.nextInt(suits.length)];
    }
    
    /**
     * Handle card click in player's hand
     * @param cardPane The card that was clicked
     */
    private void onCardClicked(StackPane cardPane) {
        if (!isPlayerTurn || currentStage != StageType.DUEL) return;
        
        // Toggle selection state
        if (cardPane.getStyleClass().contains("selected")) {
            cardPane.getStyleClass().remove("selected");
        } else {
            // Deselect any previously selected card
            for (Node card : playerCards) {
                card.getStyleClass().remove("selected");
            }
            
            // Select this card
            cardPane.getStyleClass().add("selected");
        }
    }
    
    /**
     * Handle the Reveal Trump button click
     */
    @FXML
    private void onRevealTrumpClicked() {
        if (!isPlayerTurn) return;
        
        // Choose random trump suit
        String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"};
        String[] suitSymbols = {"♥", "♦", "♠", "♣"};
        int suitIndex = random.nextInt(suits.length);
        trumpSuit = suits[suitIndex];
        
        // Update the trump suit display
        try {
            // Create a colored label for the trump suit
            Label trumpLabel = new Label(suitSymbols[suitIndex]);
            trumpLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: " + 
                               (suitIndex < 2 ? "red" : "black") + ";");
            
            // Update the image container
            trumpSuitImage.setImage(null);
            StackPane trumpContainer = (StackPane) trumpSuitImage.getParent();
            trumpContainer.getChildren().clear();
            trumpContainer.getChildren().add(trumpLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Log the trump reveal
        addMoveHistoryEntry("Trump suit revealed: " + trumpSuit);
        
        // Update UI
        revealTrumpButton.setDisable(true);
        updateGameStage(StageType.DRAFT);
        
        // Update status
        gameStatusLabel.setText("Trump suit is " + trumpSuit + ". Drafting phase begins.");
        
        // Pass turn to opponent
        isPlayerTurn = false;
        updatePlayerStatusIndicators();
        
        // Simulate opponent's turn
        simulateOpponentTurn();
    }
    
    /**
     * Handle the Forfeit button click
     */
    @FXML
    private void onForfeitClicked() {
        // Show confirmation dialog
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Forfeit Game");
        alert.setHeaderText("Are you sure you want to forfeit?");
        alert.setContentText("You will lose this game.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // End the game and update UI
                gameInProgress = false;
                addMoveHistoryEntry("You forfeited the game.");
                gameStatusLabel.setText("Game over. You forfeited.");
                
                // Disable all game buttons
                updateControls();
                startGameButton.setDisable(false);
            }
        });
    }
    
    /**
     * Handle the View Rules button click
     */
    @FXML
    private void onRulesClicked() {
        // Display game rules
        StringBuilder rules = new StringBuilder("Whist Card Game Rules:\n\n");
        rules.append("1. The game is played over multiple rounds, concluding once one player earns 6 points\n");
        rules.append("2. Each round consists of 3 stages: The Deal, the Draft, and the Duel.\n");
        rules.append("3. The Deal: The deck is shuffled, each player is dealt 13 cards, and the Trump Suit is revealed.\n");
        rules.append("4. The Draft: Players compete in 13 Tricks to improve their hands with prize cards from the draw pile.\n");
        rules.append("5. The Duel: Players compete in 13 Tricks to tally up points and win the round.\n");
        rules.append("6. The Trump Suit is the suit of the first revealed prize card and is prioritized in tricks.\n");
        rules.append("7. The winner of a trick in the Draft gets the prize card, and the loser must take the next card.\n");
        rules.append("8. The winner of a trick in the Duel sets aside the cards played in the trick to keep score.\n");
        rules.append("9. The first trick is lead by the non-dealer, then each trick is lead by the winner of the last.\n");
        rules.append("10. The leader of a Trick can play any card, and the follower must follow suit if possible.\n");
        rules.append("11. If the follower cannot follow suit, they can play any card in their hand.\n");
        rules.append("12. The highest ranked lead-suited card wins the trick.\n");
        rules.append("13. Trump-suited cards usurp the lead suit, and any other suit loses.\n");
        rules.append("14. Aces are higher ranked than Kings.\n");
        rules.append("15. The score is calculated by taking the number of tricks won in the Duel and subtracting 6.\n");
        rules.append("16. The first player to reach 6 points across all rounds wins.\n");
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Rules");
        alert.setHeaderText("Whist Card Game Rules");
        alert.setContentText(rules.toString());
        alert.getDialogPane().setPrefWidth(500);
        alert.showAndWait();
    }
    
    /**
     * Handle the Send Chat button click
     */
    @FXML
    private void onSendChatClicked() {
        String message = chatInputField.getText().trim();
        if (!message.isEmpty()) {
            addChatMessage(message, true);
            chatInputField.clear();
            
            // In a real game, this would send to the opponent
            // For now, simulate a response
            simulateOpponentChatResponse();
        }
    }
    
    /**
     * Simulate the opponent's turn
     */
    private void simulateOpponentTurn() {
        // Add a delay to make it feel like the opponent is thinking
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            // Different actions based on current stage
            switch (currentStage) {
                case DEAL:
                    simulateOpponentShuffle();
                    break;
                case DRAFT:
                    simulateOpponentDraft();
                    break;
                case DUEL:
                    simulateOpponentPlayCard();
                    break;
            }
            
            // Update UI after opponent's action
            isPlayerTurn = true;
            updatePlayerStatusIndicators();
        }));
        timeline.play();
    }
    
    /**
     * Simulate opponent shuffling cards
     */
    private void simulateOpponentShuffle() {
        // Animate the shuffle
        animateShuffle();
        
        // Log the shuffle
        addMoveHistoryEntry("Opponent shuffled the deck.");
        
        // Enable dealing for player
        dealButton.setDisable(false);
        
        // Update status
        gameStatusLabel.setText("Opponent shuffled. Your turn to deal cards.");
    }
    
    /**
     * Simulate opponent drafting a card
     */
    private void simulateOpponentDraft() {
        // Log the draft
        addMoveHistoryEntry("Opponent drew a card from the deck.");
        
        // Update status
        gameStatusLabel.setText("Opponent drew a card. Your turn.");
        
        // Randomly decide if we should transition to DUEL stage
        if (random.nextDouble() < 0.2) {
            updateGameStage(StageType.DUEL);
        }
    }
    
    /**
     * Simulate opponent playing a card
     */
    private void simulateOpponentPlayCard() {
        // Only proceed if there are cards in opponent's hand
        if (opponentCardContainer.getChildren().isEmpty()) return;
        
        // Pick a random card from opponent's hand
        int cardIndex = random.nextInt(opponentCardContainer.getChildren().size());
        Node card = opponentCardContainer.getChildren().get(cardIndex);
        
        // Move the card to the current trick area
        opponentCardContainer.getChildren().remove(card);
        currentTrickArea.getChildren().add(card);
        
        // Log the move
        addMoveHistoryEntry("Opponent played a card.");
        
        // Update status
        gameStatusLabel.setText("Opponent played. Your turn to play a card.");
    }
    
    /**
     * Simulate an opponent chat response
     */
    private void simulateOpponentChatResponse() {
        String[] responses = {
            "Good move!",
            "Let me think...",
            "Interesting strategy.",
            "I need to be careful here.",
            "That's not what I expected.",
            "Nice trick!"
        };
        
        // Add a delay to make it feel natural
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.seconds(1 + random.nextDouble() * 2),
            e -> addChatMessage(responses[random.nextInt(responses.length)], false)
        ));
        timeline.play();
    }
    
    /**
     * Show a dialog alert
     * @param type Alert type
     * @param title Alert title
     * @param message Alert message
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 