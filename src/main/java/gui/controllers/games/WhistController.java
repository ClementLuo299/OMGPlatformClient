package gui.controllers.games;

import gamelogic.GameType;
import gamelogic.Player;
import gamelogic.GamePiece;
import gamelogic.pieces.Card;
import gamelogic.pieces.CardPile;
import gamelogic.pieces.SuitType;
import gamelogic.whist.WhistGame;
import gamelogic.whist.StageType;
import gui.ScreenManager;
import gui.controllers.GameLobbyController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.*;
import javafx.scene.shape.*;
import javafx.util.Duration;
import javafx.geometry.Bounds;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.scene.transform.Rotate;
import networking.accounts.UserAccount;


import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;

/**
 * Controller for the Whist card game UI
 * Handles game setup, card interactions, and player actions
 *
 * @authors Fatin Abrar Ankon, Dylan Shiels
 * @date April 7, 2025
 */
public class WhistController implements Initializable {
    // GUI ATTRIBUTES
    
    // Header controls
    @FXML private Button backButton;
    @FXML private Text gameTitle;
    @FXML private Label roundCounter;
    @FXML private Label gameStageLabel;
    @FXML private Label getCurrentPlayerLabel;
    
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
    @FXML private Button shuffleButton1; // Overhand
    @FXML private Button shuffleButton2; // Riffle
    @FXML private Button shuffleButton11; // Pile
    @FXML private Button dealButton1;
    @FXML private VBox shuffleVBox;
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

    // FXML elements from the updated UI
    @FXML private HBox playerHandArea;
    @FXML private HBox opponentHandArea;
    @FXML private HBox trickArea;
    @FXML private StackPane trumpCardDisplay;
    @FXML private StackPane dealerOverlay;
    @FXML private Label dealerTitle;
    @FXML private Label shuffleInstructionLabel;
    @FXML private Label shuffleCounterLabel;
    @FXML private Label statusLabel;
    @FXML private Label currentPlayerLabel;
    @FXML private Label timerLabel;
    @FXML private ProgressBar timerProgressBar;
    @FXML private StackPane animationContainer;
    @FXML private ListView<String> moveHistoryList;

    // Round result and game over overlays
    @FXML private StackPane roundResultOverlay;
    @FXML private Label roundResultTitle;
    @FXML private Label roundResultMessage;
    @FXML private StackPane gameOverOverlay;
    @FXML private Label gameOverTitle;
    @FXML private Label gameOverMessage;
    @FXML private Button returnToLobbyButton;

    // Add FXML field for matchIdLabel
    @FXML private Label matchIdLabel;


    // GAME ATTRIBUTES

    // Game state variables and objects
    private WhistGame whistGame;
    private String matchId;
    private boolean gameInProgress = false;
    private Timeline gameTimer;
    private int timeRemaining = 15;

    // TODO: These should be pulled from the whistGame variable instead of stored here
    //private StageType currentStage = StageType.DEAL;
    //private int currentRound = 1;
    //private String trumpSuit = "";
    //private boolean isPlayerTurn = true;
    //private String currentUsername = "Player";
    //private boolean isGuest = false;
    
    // Card management
    private List<StackPane> player1Cards = new ArrayList<>();
    private List<StackPane> player2Cards = new ArrayList<>();
    private List<StackPane> deckDisplay = new ArrayList<>();
    private List<StackPane> drawPileDisplay = new ArrayList<>();
    private List<StackPane> discardPileDisplay = new ArrayList<>();
    boolean dealToDealer = false;
    private final int REQUIRED_SHUFFLES = 5;
    private int shuffleCount = 0;
    
    // Utility objects
    private final Random random = new Random();
    private ScreenManager screenManager = ScreenManager.getInstance();

    // Add instance variables to track tricks
    private int currentTrickNumber = 0;
    private int player1TricksWon = 0;
    private int player2TricksWon = 0;


    // CONSTRUCTOR

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Create a new WhistGame instance with two players
        List<Player> players = new ArrayList<>();
        players.add(new Player(new UserAccount("Player 1", "Player 1")));
        players.add(new Player(new UserAccount("Player 2", "Player 2")));
        
        // Create the Whist game
        whistGame = new WhistGame(GameType.WHIST, players, 26);
        
        // Set initial game state
        gameInProgress = false;
        
        // Set up the initial round number
        whistGame.setRound(1);
        roundCounter.setText("1");
        
        // Hide hand areas initially
        hideHandAreas();
        
        // Hide result overlays initially
        if (roundResultOverlay != null) {
            roundResultOverlay.setVisible(false);
        }
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }
        
        // Set up the game and UI
        setupGame();
        setupButtonHandlers();
        fixButtonPositioning();
        
        // Ensure UI is in the correct state
        updateUI();
        
        // Initialize dealer overlay instead of showing the start game overlay
        setupDealerOverlay();
    }

    private void setupDealerOverlay() {
        // Initially hide the shuffle controls VBox
        shuffleVBox.setVisible(false);
        
        // Show START GAME button and position it in front
        startGameButton.setVisible(true);
        startGameButton.toFront();
        
        // Configure START GAME button action
        startGameButton.setOnAction(event -> onStartGameButtonClicked());
        
        // Set Deal button to disabled initially
        dealButton1.setDisable(true);
        
        // Configure deal button action
        dealButton1.setOnAction(e -> onDealClicked());
        
        // Initialize shuffle counter
        shuffleCounterLabel.setText("Shuffles: " + shuffleCount + "/" + REQUIRED_SHUFFLES);
        
        // Setup shuffle button actions
        setupShuffleButtons();
    }

    private void onStartGameButtonClicked() {
        // Hide START GAME button
        startGameButton.setVisible(false);
        
        // Show shuffle controls
        shuffleVBox.setVisible(true);
        
        // Reset shuffle count
        shuffleCount = 0;
        shuffleCounterLabel.setText("Shuffles: " + shuffleCount + "/" + REQUIRED_SHUFFLES);
        
        // Add a move history entry
        addMoveHistoryEntry("Game started. Shuffle the deck at least " + REQUIRED_SHUFFLES + " times.");
    }

    private void setupShuffleButtons() {
        // Cut shuffle
        shuffleButton.setOnAction(event -> {
            animateShuffle("cut");
            updateShuffleCount();
        });
        
        // Riffle shuffle
        shuffleButton2.setOnAction(event -> {
            animateShuffle("riffle");
            updateShuffleCount();
        });
        
        // Overhand shuffle
        shuffleButton1.setOnAction(event -> {
            animateShuffle("overhead");
            updateShuffleCount();
        });
        
        // Pile shuffle
        shuffleButton11.setOnAction(event -> {
            animateShuffle("scramble");
            updateShuffleCount();
        });
    }

    private void updateShuffleCount() {
        shuffleCount++;
        shuffleCounterLabel.setText("Shuffles: " + shuffleCount + "/" + REQUIRED_SHUFFLES);
        
        // Add a move history entry
        addMoveHistoryEntry("Deck shuffled. (" + shuffleCount + "/" + REQUIRED_SHUFFLES + ")");
        
        // Enable deal button if required shuffles are completed
        if (shuffleCount >= REQUIRED_SHUFFLES) {
            dealButton1.setDisable(false);
            dealButton1.getStyleClass().add("deal-button-enabled");
            shuffleInstructionLabel.setText("Deck is sufficiently shuffled. You can now deal the cards.");
            addMoveHistoryEntry("Deck sufficiently shuffled. Ready to deal.");
        }
    }


    // GAME LOOP

    /**
     * [This method is preserved for documentation purposes but its implementation has been moved]
     * See the implementation at the end of the file.
     */
    // Duplicate method removed - see implementation at the end of the file


    // RENDER METHODS

    /**
     * Renders a card pile with a stack effect
     *
     * @param pile The card pile to render
     * @return List of StackPanes representing the pile
     */
    private List<StackPane> renderPile(CardPile pile) {
        List<StackPane> pileToRender = new ArrayList<>();
        
        // Check if pile has cards
        if (pile == null || pile.getSize() == 0) {
            // Create an empty pile indicator
            StackPane emptyPile = new StackPane();
            emptyPile.setPrefWidth(80);
            emptyPile.setPrefHeight(120);
            emptyPile.getStyleClass().add("empty-pile");
            
            // Add a label to indicate empty pile
            Label emptyLabel = new Label("Empty");
            emptyLabel.getStyleClass().add("pile-label");
            emptyPile.getChildren().add(emptyLabel);
            
            pileToRender.add(emptyPile);
            return pileToRender;
        }
        
        // Calculate how many cards to show in the stack (up to 5)
        int stackSize = Math.min(pile.getSize(), 5);
        
        // Create a container for all the cards
        StackPane pileContainer = new StackPane();
        pileContainer.setPrefWidth(80);
        pileContainer.setPrefHeight(120);
        
        // Add stack effect cards (bottom to top)
        for (int i = stackSize - 1; i >= 0; i--) {
            // Create card back for stack effect
            ImageView stackCardImage = createCardImageView("/images/whist images/CardBackside1.png");
            StackPane stackCardPane = new StackPane(stackCardImage);
            stackCardPane.getStyleClass().add("card-view");
            stackCardPane.getStyleClass().add("card-back");
            
            // Position with slight offset to create stack effect
            stackCardPane.setTranslateX((stackSize - i - 1) * 2);
            stackCardPane.setTranslateY((stackSize - i - 1) * 2);
            
            // Add to container with proper z-order
            stackCardPane.setViewOrder(i);
            pileContainer.getChildren().add(stackCardPane);
        }
        
        // If the pile has at least one card, render and add the top card
        if (pile.getSize() > 0) {
            Card topCard = pile.getTopCard();
            StackPane topCardPane = renderCard(topCard);
            
            // Position the top card at the top of the stack
            topCardPane.setTranslateX(stackSize * 2);
            topCardPane.setTranslateY(stackSize * 2);
            topCardPane.setViewOrder(-1); // Ensure it's on top
            
            // Add to the container
            pileContainer.getChildren().add(topCardPane);
            
            // Store the top card for interaction
            pileContainer.getProperties().put("topCard", topCard);
        }
        
        // Add the container to the return list
        pileToRender.add(pileContainer);
        
        return pileToRender;
    }

    /**
     * Renders all cards in a pile with an interactive fan layout
     *
     * @param pile The card pile to render
     * @return List of StackPanes representing all cards in the pile
     */
    private List<StackPane> renderWholePile(CardPile pile) {
        // The List of Card StackPanes
        List<StackPane> cardsToRender = new ArrayList<>();

        // Get the total number of cards in the pile
        int totalCards = pile.getCards().size();
        if (totalCards == 0) return cardsToRender;
        
        // Calculate optimal fan parameters based on number of cards
        double centerX = 0;
        double centerY = 0;
        double radius = 300; // Increased radius for better spread
        
        // Determine fan angles based on number of cards
        double startAngle = totalCards > 26 ? -75 : -60;
        double endAngle = totalCards > 26 ? 75 : 60;
        double angleIncrement = (endAngle - startAngle) / (Math.max(totalCards - 1, 1));
        
        // Create card panes with fan layout
        int index = 0;
        for (Card cardToRender : pile.getCards()) {
            // Renders the Card using the improved method
            StackPane cardPane = renderCard(cardToRender);
            
            // Calculate position in fan layout
            double angle = startAngle + (index * angleIncrement);
            double radians = Math.toRadians(angle);
            
            // Position the card in a fan layout
            double offsetX = radius * Math.sin(radians);
            double offsetY = -radius * Math.cos(radians);
            
            // Apply position
            cardPane.setTranslateX(centerX + offsetX);
            cardPane.setTranslateY(centerY + offsetY);
            
            // Apply rotation to match fan curve
            cardPane.setRotate(angle);
            
            // Set z-order for proper stacking
            cardPane.setViewOrder(-index);
            
            // Add hover effects for better visibility
            setupCardHoverEffects(cardPane, index);
            
            // Add to result list
            cardsToRender.add(cardPane);
            index++;
        }

        return cardsToRender;
    }

    // TODO: Render a StackPane based on a card
    /**
     * Renders a Card StackPane with an image based on its Suit, Rank, and FaceDown value.
     * This Card StackPane can be used in several methods to assign it various On-Click behaviour.
     *
     * @param card The given Card to base the render off of
     * @return The rendered StackPane
     */
    private StackPane renderCard(Card card) {
        // Create the image view for the Card StackPane using the helper method
        ImageView cardImage = createCardImageView(getCardImagePath(card));
        
        // Create a Card StackPane
        StackPane cardPane = new StackPane(cardImage);
        cardPane.getStyleClass().add("card-view");
        
        // Set preferred dimensions
        cardPane.setPrefWidth(80);
        cardPane.setPrefHeight(120);
        
        // Attaches the Card object to this StackPane
        cardPane.getProperties().put("card", card);
        
        // Add card suit and rank as style classes for potential CSS styling
        if (!card.isFaceDown()) {
            cardPane.getStyleClass().add(card.getSuit().toString().toLowerCase());
            cardPane.getStyleClass().add("rank-" + card.getRank());
        } else {
            cardPane.getStyleClass().add("card-back");
        }
        
        return cardPane;
    }

    // TODO: Makes a rendered Card clickable for Playing
    private void makeIntoPlayable(StackPane cardPane) {
        // Get the associated card
        Card associatedCard = (Card) cardPane.getProperties().get("card");
        if (associatedCard == null) return;

        // Add glow effect to indicate this card is active
        makeGlow(cardPane);

        // Create click handler for card selection and animation
        cardPane.setOnMouseClicked(event -> {
            // DEBUG: Shows the card that was clicked
            System.out.println("Played Card: " + associatedCard.toString());

            // Check whose turn it is and if this card belongs to that player
            Player currentPlayer = whistGame.getTurnHolder();
            boolean isCurrentPlayerCard = currentPlayer.checkHand(associatedCard);

            if (!isCurrentPlayerCard) {
                // Not this player's turn or card
                showAlert(Alert.AlertType.WARNING, "Invalid Play", "It's not your turn or you can't play this card!");
                return;
            }

            // Check if this card is playable according to game rules
            boolean isValidCard = false;
            List<Card> playableCards;

            // If there are cards in the trick already, we need to follow suit if possible
            if (!whistGame.getTrick().isEmpty()) {
                Card leadCard = whistGame.getTrick().getFirst();
                playableCards = whistGame.getPlayableCards(currentPlayer, leadCard);
            } else {
                // Player is leading, all cards are playable
                playableCards = whistGame.getPlayableCards(currentPlayer);
            }

            // Check if the card is in the list of playable cards
            for (Card validCard : playableCards) {
                if (validCard == associatedCard) {
                    isValidCard = true;
                    break;
                }
            }

            if (!isValidCard) {
                showAlert(Alert.AlertType.WARNING, "Invalid Card", "You can't play this card - please follow suit if possible.");
                return;
            }

            // Play the card in game logic
            whistGame.playCard(associatedCard);

            // Add move history entry
            addMoveHistoryEntry(currentPlayer.getUsername() + " played " + associatedCard.toString());

            // Animate card being played to trick area
            playCardToTrick(cardPane);

            // Make card inert after being played
            makeIntoInert(cardPane);

            // Switch turn to other player
            Player nextPlayer = currentPlayer == whistGame.getPlayer1() ? 
                whistGame.getPlayer2() : whistGame.getPlayer1();
            whistGame.setTurnHolder(nextPlayer);

            // Update UI to show whose turn it is
            updateUI();
            statusLabel.setText(whistGame.getTurnHolder().getUsername() + "'s turn to play");

            // If both players have played a card, determine the trick winner
            if (whistGame.getTrick().size() == 2) {
                // Delay to show the cards before resolving the trick
                Timeline trickResolution = new Timeline(
                    new KeyFrame(Duration.seconds(1.5), e -> resolveTrick())
                );
                trickResolution.play();
            } else {
                // Make the next player's cards playable
                resetPlayableCards();
                
                // Start timer for the next player's turn
                startGameTimer(15, () -> {
                    // Auto-play a card if timer expires
                    autoPlayCard();
                });
            }
        });
    }

    // TODO: Makes a rendered Card clickable for Drawing Prize Cards
    private void makeIntoPrize(StackPane cardPane) {
        // Create click handler for card selection and animation
        cardPane.setOnMouseClicked(event -> {

            // DEBUG: Shows the card that was clicked
            System.out.println("Prized Card: " + cardPane.getProperties().get("card").toString());

            // The Card associated with this StackPane
            Card associatedCard = (Card) cardPane.getProperties().get("card");

            // TODO: Get the Player who clicked and deal them this Card
            //  (How to get player who clicked?)
            //whistGame.dealCard(whistGame.getDraw(), associatedCard, whistGame.getPlayer<1 or 2>);
            drawPileDisplay.remove(cardPane);
            //player<1 or 2>Cards.add(cardPane);

            // TODO: Play animation of Card moving from draw pile to Player's Hand

            // Makes Card inert after being clicked
            makeIntoInert(cardPane);
        });
    }

    // TODO: Will add click handling for selectable cards
    private void makeIntoSelectable(StackPane cardPane) {
        // Check for null
        if (cardPane == null) return;
        
        // Get the card from the cardPane properties
        Card card = (Card) cardPane.getProperties().get("card");
        if (card == null) return;
        
        // Add selectable styling
        cardPane.getStyleClass().add("selectable-card");
        
        // Make the card slightly enlarge when hovered
        cardPane.setOnMouseEntered(event -> {
            if (!cardPane.getStyleClass().contains("selected")) {
                cardPane.setScaleX(1.1);
                cardPane.setScaleY(1.1);
                cardPane.setEffect(new DropShadow(10, Color.GOLD));
            }
        });
        
        cardPane.setOnMouseExited(event -> {
            if (!cardPane.getStyleClass().contains("selected")) {
                cardPane.setScaleX(1.0);
                cardPane.setScaleY(1.0);
                cardPane.setEffect(null);
            }
        });
        
        // Create click handler
        cardPane.setOnMouseClicked(event -> {
            // Get the current player
            Player currentPlayer = whistGame.getCurrentTurn();
            String currentPlayerName = currentPlayer.getUsername();
            
            // Check if the current player has already selected a card
            boolean hasSelected = false;
            for (StackPane existingCardPane : deckDisplay) {
                if (existingCardPane.getStyleClass().contains("selected") && 
                    existingCardPane.getProperties().get("selectedBy") != null &&
                    existingCardPane.getProperties().get("selectedBy").equals(currentPlayerName)) {
                    hasSelected = true;
                    break;
                }
            }
            
            // If the player has already selected, don't allow another selection
            if (hasSelected) {
                showAlert(AlertType.WARNING, "Card Already Selected", 
                          "You've already selected a card. Wait for the other player to select.");
                return;
            }
            
            // Mark card as selected and store who selected it
            cardPane.getStyleClass().add("selected");
            cardPane.getProperties().put("selectedBy", currentPlayerName);
            
            // Temporarily assign card to player for dealer determination
            whistGame.dealCard(whistGame.getDeck(), card, currentPlayer);
            
            // Add to move history
            addMoveHistoryEntry(currentPlayerName + " selected " + card.toString() + " for dealer determination");
            
            // Animate card selection
            animateCardSelection(cardPane);
            
            // Disable further selection for this card
            makeIntoInert(cardPane);
            
            // Check if both players have selected a card
            boolean bothSelected = true;
            Card player1Card = null;
            Card player2Card = null;
            StackPane player1CardPane = null;
            StackPane player2CardPane = null;
            
            for (Player player : whistGame.getPlayers()) {
                boolean found = false;
                for (StackPane cp : deckDisplay) {
                    if (cp.getProperties().get("selectedBy") != null && 
                        cp.getProperties().get("selectedBy").equals(player.getUsername())) {
                        found = true;
                        Card selectedCard = (Card) cp.getProperties().get("card");
                        
                        if (player == whistGame.getPlayers().getFirst()) {
                            player1Card = selectedCard;
                            player1CardPane = cp;
                        } else {
                            player2Card = selectedCard;
                            player2CardPane = cp;
                        }
                        break;
                    }
                }
                if (!found) {
                    bothSelected = false;
                }
            }
            
            // If both players have selected, determine the dealer
            if (bothSelected && player1Card != null && player2Card != null) {
                completeDealerSelection(player1Card, player2Card, player1CardPane, player2CardPane);
            } else {
                // Switch turns to the other player
                // Replace whistGame.nextTurn() with direct turn switching
                if (whistGame.getTurnHolder() == whistGame.getPlayer1()) {
                    whistGame.setTurnHolder(whistGame.getPlayer2());
                } else {
                    whistGame.setTurnHolder(whistGame.getPlayer1());
                }
                updateUI();
                // Update status message
                statusLabel.setText("Waiting for " + whistGame.getCurrentTurn().getUsername() + " to select a card");
            }
        });
    }

    // TODO: Makes a rendered Card clickable for Revealing the Trump Suit
    private void makeIntoTrump(StackPane cardPane) {
        // Create click handler for card selection and animation
        cardPane.setOnMouseClicked(event -> {
            // DEBUG: Shows the card that was clicked
            System.out.println("Trump Card: " + cardPane.getProperties().get("card").toString());

            // Execute the trump reveal logic
            revealTrumpCard();

            // Makes Card inert after being clicked
            makeIntoInert(cardPane);
        });
    }

    /**
     * Reveals the trump card and updates the game stage
     */
    private void revealTrumpCard() {
        // Get the top card from the draw pile
        if (whistGame.getDraw() != null && !whistGame.getDraw().getCards().isEmpty()) {
            Card trumpCard = whistGame.getDraw().getTopCard();
            
            // Flip the card face up if needed
            if (trumpCard.isFaceDown()) {
                trumpCard.flip();
            }
            
            // Set the trump suit in the game logic
            whistGame.setTrump(trumpCard.getSuit());
            
            // Add move history entry
            addMoveHistoryEntry("Trump suit revealed: " + trumpCard.getSuit().getDisplayName());
            
            // Update the trump card display
            updateTrumpCardDisplay(trumpCard);
            
            // Update game status
            statusLabel.setText("Trump suit revealed: " + trumpCard.getSuit().getDisplayName());
            
            // Start a 5-second timer to transition to DRAFT stage
            startGameTimer(5, () -> {
                // Progress to DRAFT stage
                prepareDraftStage();
            });
        } else {
            // Handle error - no draw pile or empty draw pile
            showAlert(Alert.AlertType.ERROR, "Error", "No cards available to reveal as trump!");
        }
    }

    /**
     * Makes a rendered Card clickable for Dealing it to other Players
     */
    private void makeIntoDeal(StackPane cardPane) {
        // Get the associated card
        Card associatedCard = (Card) cardPane.getProperties().get("card");
        if (associatedCard == null) return;
        
        // Add glow effect to indicate this card is active
        makeGlow(cardPane);
        
        // Determine which player will receive the card
        Player recipient;
        boolean isPlayer1;
        String recipientName;
        
            if (!dealToDealer) {
            // Non-dealer receives card first (turn holder)
            recipient = whistGame.getTurnHolder();
            recipientName = recipient.getUsername();
            isPlayer1 = (recipient == whistGame.getPlayer1());
        } else {
            // Dealer receives card
            recipient = whistGame.getDealer();
            recipientName = recipient.getUsername();
            isPlayer1 = (recipient == whistGame.getPlayer1());
        }
        
        // Deal card in game logic
        whistGame.dealCard(whistGame.getDeck(), associatedCard, recipient);
        
        // Add move history entry
        addMoveHistoryEntry("Dealt " + associatedCard.toString() + " to " + recipientName);
        
        // Create animation for card moving to player's hand
        animateCardDeal(cardPane, isPlayer1);
        
        // Toggle dealToDealer flag for next card
        dealToDealer = !dealToDealer;
        
        // Make card inert after dealing
        makeIntoInert(cardPane);
        
        // Check if we need to continue dealing
        int totalDealtCards = whistGame.getPlayer1().getHand().size() + whistGame.getPlayer2().getHand().size();
        if (totalDealtCards < 26) { // 13 cards per player
            // Get the next card from the deck if available
            if (whistGame.getDeck().getSize() > 0) {
                // Update the deck display
                updateDeckDisplay();
                
                // Get the top card pane
                if (!deckDisplay.isEmpty()) {
                    StackPane nextCardPane = deckDisplay.get(0);
                    
                    // Set up a short delay before dealing the next card
                    Timeline dealDelay = new Timeline(
                        new KeyFrame(Duration.millis(300), e -> makeIntoDeal(nextCardPane))
                    );
                    dealDelay.play();
                }
            }
        } else {
            // All cards dealt
            Timeline completionDelay = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> prepareForTrumpReveal())
            );
            completionDelay.play();
        }
    }

    /**
     * Updates the deck display after a card has been dealt
     */
    private void updateDeckDisplay() {
        // Clear current deck display
        deckDisplay.clear();
        animationContainer.getChildren().clear();
        
        // Render the updated deck
        if (whistGame.getDeck().getSize() > 0) {
            deckDisplay = renderPile(whistGame.getDeck());
            
            // Position the deck in the center of the screen
            double centerX = animationContainer.getWidth() / 2;
            double centerY = animationContainer.getHeight() / 2;
            
            for (StackPane cardPane : deckDisplay) {
                // Add card to animation container
                animationContainer.getChildren().add(cardPane);
                
                // Position card in center
                cardPane.setLayoutX(centerX - cardPane.getPrefWidth() / 2);
                cardPane.setLayoutY(centerY - cardPane.getPrefHeight() / 2);
            }
        }
    }

    /**
     * Animates a card being dealt from the deck to a player's hand
     * 
     * @param cardPane The card pane to animate
     * @param isPlayer1 Whether the card is being dealt to player 1
     */
    private void animateCardDeal(StackPane cardPane, boolean isPlayer1) {
        // Store original position
        double startX = cardPane.getLayoutX();
        double startY = cardPane.getLayoutY();
        
        // Calculate target position based on which player is receiving the card
        double targetX, targetY;
        
        if (isPlayer1) {
            // Target Player 1's hand area (bottom)
            targetX = playerHandArea.localToScene(playerHandArea.getBoundsInLocal()).getMinX() + 
                     (playerHandArea.getWidth() / 2) - 40;
            targetY = playerHandArea.localToScene(playerHandArea.getBoundsInLocal()).getMinY() + 
                     (playerHandArea.getHeight() / 2) - 60;
        } else {
            // Target Player 2's hand area (top)
            targetX = opponentHandArea.localToScene(opponentHandArea.getBoundsInLocal()).getMinX() + 
                     (opponentHandArea.getWidth() / 2) - 40;
            targetY = opponentHandArea.localToScene(opponentHandArea.getBoundsInLocal()).getMinY() + 
                     (opponentHandArea.getHeight() / 2) - 60;
        }
        
        // Create animation
        SequentialTransition dealAnimation = new SequentialTransition();
        
        // Flip card if it's player 1 (face up)
        if (isPlayer1) {
            // Get the associated card and flip it face up in the logic
            Card card = (Card) cardPane.getProperties().get("card");
            if (card != null && card.isFaceDown()) {
                card.flip(); // Flip the card face up in the logic
                
                // Create a flip animation
                RotateTransition flipCard = new RotateTransition(Duration.millis(200), cardPane);
                flipCard.setAxis(Rotate.Y_AXIS);
                flipCard.setFromAngle(0);
                flipCard.setToAngle(180);
                flipCard.setCycleCount(1);
                
                // Update the card image halfway through the flip
                flipCard.setOnFinished(e -> updateCardImage(cardPane, card));
                
                dealAnimation.getChildren().add(flipCard);
            }
        }
        
        // Create move animation
        TranslateTransition moveCard = new TranslateTransition(Duration.millis(500), cardPane);
        moveCard.setFromX(0);
        moveCard.setFromY(0);
        moveCard.setToX(targetX - startX);
        moveCard.setToY(targetY - startY);
        
        dealAnimation.getChildren().add(moveCard);
        
        // Add the card to the player's hand after animation
        moveCard.setOnFinished(e -> {
            // Remove from animation container
            animationContainer.getChildren().remove(cardPane);
            
            // Add to appropriate hand area
            if (isPlayer1) {
                playerHandArea.getChildren().add(cardPane);
                    player1Cards.add(cardPane);
                } else {
                opponentHandArea.getChildren().add(cardPane);
                    player2Cards.add(cardPane);
                }

            // Reset transforms
            cardPane.setTranslateX(0);
            cardPane.setTranslateY(0);
        });
        
        // Play the animation
        dealAnimation.play();
    }

    /**
     * Deals all remaining cards to players automatically
     */
    private void dealAllCards() {
        // Make sure dealer and turn holder are set in the game
        if (whistGame.getDealer() == null) {
            // If dealer is not set, set player 1 as dealer by default
            whistGame.setDealer(whistGame.getPlayer1());
            System.out.println("Dealer was null, setting to Player 1");
        }
        
        if (whistGame.getTurnHolder() == null) {
            // Set player 2 as the turn holder (non-dealer)
            whistGame.setTurnHolder(whistGame.getPlayer2());
            System.out.println("Turn holder was null, setting to Player 2");
        }
        
        // Get remaining cards in the deck
        List<Card> remainingCards = whistGame.getDeck().getCards();
        
        // Make a copy to avoid concurrent modification
        List<Card> cardsToDeal = new ArrayList<>(remainingCards);
        
        // Determine players and track cards to add to their visual hands
        Player dealer = whistGame.getDealer();
        Player nonDealer = whistGame.getTurnHolder();
        
        // Make sure we have valid players to deal to
        if (dealer == null || nonDealer == null) {
            System.out.println("Error: Could not determine dealer or non-dealer");
            return;
        }
        
        // Alternate dealing cards to dealer and non-dealer
        boolean dealingToDealer = false;
        
        for (Card card : cardsToDeal) {
            Player recipient = dealingToDealer ? dealer : nonDealer;
            
            // Deal card in game logic
            try {
                whistGame.dealCard(whistGame.getDeck(), card, recipient);
                // Add move history entry
                addMoveHistoryEntry("Auto-dealt card to " + recipient.getUsername());
            } catch (Exception e) {
                System.out.println("Error dealing card: " + e.getMessage());
            }
            
            // Toggle flag for next card
            dealingToDealer = !dealingToDealer;
        }
        
        // Update UI to show hands
        updatePlayerHandDisplay();
    }

    /**
     * Updates the display of player hands after dealing
     */
    private void updatePlayerHandDisplay() {
        // Clear current hand displays
        playerHandArea.getChildren().clear();
        opponentHandArea.getChildren().clear();
        player1Cards.clear();
        player2Cards.clear();
        
        // Render player 1's hand
        for (GamePiece piece : whistGame.getPlayer1().getHand()) {
            if (piece instanceof Card card) {
                // Flip card face up for player 1
                if (card.isFaceDown()) {
                    card.flip();
                }
                
                // Create card pane
                StackPane cardPane = renderCard(card);
                
                // Add to player area
                playerHandArea.getChildren().add(cardPane);
                    player1Cards.add(cardPane);
            }
        }
        
        // Render player 2's hand (face down)
        for (GamePiece piece : whistGame.getPlayer2().getHand()) {
            if (piece instanceof Card card) {
                // Create card pane (face down)
                StackPane cardPane = renderCard(card);
                
                // Add to opponent area
                opponentHandArea.getChildren().add(cardPane);
                    player2Cards.add(cardPane);
            }
        }
    }

    /**
     * Prepares for the trump reveal phase after dealing is complete
     */
    private void prepareForTrumpReveal() {
        // Stop any running timer
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Add move history entry
        addMoveHistoryEntry("All cards dealt. Preparing for trump reveal.");
        
        // Update the game stage
        whistGame.setGameStage(StageType.DRAFT);
        
        // Update UI
        updateUI();
        
        // Set up draw pile for trump card
        CardPile drawPile = new CardPile();
        
        // Move any remaining cards to the draw pile
        for (Card card : whistGame.getDeck().getCards()) {
            drawPile.addCards(card);
        }
        
        // Set the draw pile in the game
        whistGame.setDraw(drawPile);
        
        // Show the dealer overlay for trump reveal
        dealerOverlay.setVisible(true);
        
        // Update dealer overlay for trump reveal
        shuffleInstructionLabel.setText("Click to reveal the trump suit.");
        dealerTitle.setText("REVEAL TRUMP");
        
        // Hide all shuffle buttons and deal button
        shuffleButton.setVisible(false);     // Cut button
        shuffleButton1.setVisible(false);    // Overhand button
        shuffleButton2.setVisible(false);    // Riffle button
        shuffleButton11.setVisible(false);   // Pile button
        dealButton1.setVisible(false);       // Deal button
        shuffleCounterLabel.setVisible(false);
        
        // Hide the button container HBoxes to ensure no buttons are visible
        for (Node node : shuffleVBox.getChildren()) {
            if (node instanceof HBox && ((HBox) node).getChildren().stream()
                    .anyMatch(child -> child instanceof Button && 
                               (((Button) child).getText().contains("Cut") || 
                                ((Button) child).getText().contains("Riffle") ||
                                ((Button) child).getText().contains("Overhand") ||
                                ((Button) child).getText().contains("Pile")))) {
                node.setVisible(false);
            }
        }
        
        // Create a trump reveal button with purple styling
        Button revealButton = new Button("Reveal Trump");
        revealButton.getStyleClass().add("reveal-trump-button");
        revealButton.setPrefWidth(200);
        revealButton.setPrefHeight(40);
        
        // Get the top card from the draw pile to display
        Card topCard = null;
        if (!drawPile.getCards().isEmpty()) {
            topCard = drawPile.getTopCard();
        } else {
            // Create a random card as fallback if draw pile is empty
            topCard = new Card(1, SuitType.SPADES);
        }
        
        // Store the trump card for use in the reveal animation
        final Card trumpCard = topCard;
        
        // Create a card view for the trump card (face down initially)
        StackPane cardContainer = new StackPane();
        cardContainer.getStyleClass().add("card-image-container");
        cardContainer.setPrefWidth(200);
        cardContainer.setPrefHeight(280);
        cardContainer.setMaxWidth(200);
        cardContainer.setMaxHeight(280);
        
        // Create card backside that fills the entire container
        ImageView cardBackImageView = new ImageView();
        try {
            Image cardBackImage = new Image(getClass().getResourceAsStream("/images/whist images/CardBackside1.png"));
            cardBackImageView.setImage(cardBackImage);
            cardBackImageView.setFitWidth(200);
            cardBackImageView.setFitHeight(280);
            cardBackImageView.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Error loading card back image: " + e.getMessage());
        }
        
        // Add the card back image to the container
        cardContainer.getChildren().add(cardBackImageView);
        
        // Connect the reveal button to the trump reveal animation
        revealButton.setOnAction(event -> {
            animateTrumpReveal(cardContainer, trumpCard);
        });
        
        // Update the dealer overlay layout
        VBox dealerContent = (VBox) dealerOverlay.lookup(".dealer-content");
        if (dealerContent != null) {
            // Find and replace the existing card image container
            for (int i = 0; i < dealerContent.getChildren().size(); i++) {
                if (dealerContent.getChildren().get(i) instanceof StackPane &&
                    dealerContent.getChildren().get(i).getStyleClass().contains("card-image-container")) {
                    dealerContent.getChildren().set(i, cardContainer);
                    break;
                }
            }
            
            // Add the reveal button after the instruction label
            for (int i = 0; i < dealerContent.getChildren().size(); i++) {
                if (dealerContent.getChildren().get(i) instanceof Label &&
                    ((Label) dealerContent.getChildren().get(i)).getText().contains("Click to reveal")) {
                    // Add button after the instruction label
                    dealerContent.getChildren().add(i + 1, revealButton);
                    break;
                }
            }
        }
        
        // Start a timer for the trump reveal phase (15 seconds)
        startGameTimer(15, () -> {
            // Auto-reveal trump if time expires
            animateTrumpReveal(cardContainer, trumpCard);
        });
    }

    /**
     * Animates the trump card reveal with a flip effect
     */
    private void animateTrumpReveal(StackPane cardContainer, Card trumpCard) {
        // Create front card view with the trumpCard image
        StackPane frontCardView = new StackPane();
        frontCardView.setPrefSize(200, 280);
        
        // Create image view for the front of the card
        ImageView frontImageView = new ImageView();
        try {
            // Get the image for the trump card
            String imagePath = getCardImagePath(trumpCard);
            Image cardFrontImage = new Image(getClass().getResourceAsStream(imagePath));
            frontImageView.setImage(cardFrontImage);
            frontImageView.setFitWidth(200);
            frontImageView.setFitHeight(280);
            frontImageView.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Error loading trump card image: " + e.getMessage());
        }
        
        // Add the image to the front view
        frontCardView.getChildren().add(frontImageView);
        frontCardView.setVisible(false); // Initially hidden
        
        // Get the current back card view (should be the first child of cardContainer)
        StackPane currentCardView = null;
        for (Node node : cardContainer.getChildren()) {
            if (node instanceof ImageView || node instanceof StackPane) {
                currentCardView = new StackPane(node);
                break;
            }
        }
        
        // If we couldn't find an existing view, create a new one
        if (currentCardView == null) {
            currentCardView = new StackPane();
            ImageView backImageView = new ImageView();
            try {
                Image cardBackImage = new Image(getClass().getResourceAsStream("/images/whist images/CardBackside1.png"));
                backImageView.setImage(cardBackImage);
                backImageView.setFitWidth(200);
                backImageView.setFitHeight(280);
                backImageView.setPreserveRatio(true);
            } catch (Exception e) {
                System.err.println("Error loading card back image: " + e.getMessage());
            }
            currentCardView.getChildren().add(backImageView);
        }
        
        // Clear container and add both views
        cardContainer.getChildren().clear();
        cardContainer.getChildren().addAll(currentCardView, frontCardView);
        
        // Call the 3-argument version with the prepared views
        animateTrumpReveal(cardContainer, currentCardView, frontCardView, trumpCard);
    }
    
    /**
     * Animates the trump card reveal with a flip effect
     * Internal implementation used by the public method
     */
    private void animateTrumpReveal(StackPane cardContainer, StackPane currentCardView, StackPane frontCardView, Card trumpCard) {
        // Create rotation transforms for the flip effect
        RotateTransition rotateOut = new RotateTransition(Duration.millis(150), currentCardView);
        rotateOut.setAxis(Rotate.Y_AXIS);
        rotateOut.setFromAngle(0);
        rotateOut.setToAngle(90);
        rotateOut.setInterpolator(Interpolator.EASE_BOTH);
        
        RotateTransition rotateIn = new RotateTransition(Duration.millis(150), frontCardView);
        rotateIn.setAxis(Rotate.Y_AXIS);
        rotateIn.setFromAngle(-90);
        rotateIn.setToAngle(0);
        rotateIn.setInterpolator(Interpolator.EASE_BOTH);
        
        // Add a little bounce effect
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), cardContainer);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), cardContainer);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        
        // Create the sequence of animations
        SequentialTransition sequence = new SequentialTransition(
            rotateOut,
            new javafx.animation.PauseTransition(Duration.millis(150)),
            rotateIn,
            new SequentialTransition(scaleUp, scaleDown)
        );
        
        // At the halfway point, switch the images
        rotateOut.setOnFinished(e -> {
            currentCardView.setVisible(false);
            frontCardView.setVisible(true);
            
            // Update the trumpCard display in the main game area
            updateTrumpCardDisplay(trumpCard);
            
            // Add move history entry for revealing trump
            addMoveHistoryEntry("Trump suit revealed: " + trumpCard.getSuit().getDisplayName());
            
            // Update the game state
            whistGame.setTrump(trumpCard.getSuit());
        });
        
        // When animation completes, prepare for the draft stage
        sequence.setOnFinished(e -> {
            // Wait 2 seconds to let player see the card, then proceed to draft stage
            PauseTransition pause = new PauseTransition(Duration.millis(2000));
            pause.setOnFinished(event -> {
                // Close the dealer overlay
                dealerOverlay.setVisible(false);
                
                // Move to the draft stage
                prepareDraftStage();
            });
            pause.play();
        });
        
        // Play the animation
        sequence.play();
        
        // Disable the reveal button to prevent multiple clicks
        for (Node node : cardContainer.getParent().getChildrenUnmodifiable()) {
            if (node instanceof Button && ((Button) node).getText().contains("Reveal")) {
                ((Button) node).setDisable(true);
            }
        }
    }

    /**
     * Updates the trump card display with the selected card
     */
    private void updateTrumpCardDisplay(Card trumpCard) {
        // Clear current trump display
        trumpCardDisplay.getChildren().clear();
        
        // Flip the trump card face up if needed
        if (trumpCard.isFaceDown()) {
            trumpCard.flip();
        }
        
        // Create a card pane for the trump card
        StackPane trumpCardPane = renderCard(trumpCard);
        
        // Make it slightly smaller to fit the display area
        trumpCardPane.setScaleX(0.9);
        trumpCardPane.setScaleY(0.9);
        
        // Add to the trump display
        trumpCardDisplay.getChildren().add(trumpCardPane);
        
        // Add a label showing the trump suit
        Label trumpLabel = new Label("Trump: " + trumpCard.getSuit().getDisplayName());
        trumpLabel.getStyleClass().add("trump-label");
        trumpLabel.setTranslateY(60);
        trumpCardDisplay.getChildren().add(trumpLabel);
    }

    // TODO: Makes a clickable rendered Card un-clickable
    private void makeIntoInert(StackPane cardPane) {
        // Remove on-click event from the card
        cardPane.setOnMouseClicked(null);

        // Remove hover effect from card if it exists
        cardPane.getStyleClass().remove("card-hover");

        // Remove any glow effects
        cardPane.setEffect(null);
    }

    // TODO: Makes a Card have a faint glow around the edge to indicate interactiveness
    private void makeGlow(StackPane cardPane) {
        // Create a glow effect
        javafx.scene.effect.Glow glow = new javafx.scene.effect.Glow(0.5);
        
        // Add a drop shadow with a yellow/gold color for highlighting
        javafx.scene.effect.DropShadow dropShadow = new javafx.scene.effect.DropShadow();
        dropShadow.setColor(javafx.scene.paint.Color.GOLD);
        dropShadow.setRadius(15);
        dropShadow.setSpread(0.2);
        dropShadow.setInput(glow);
        
        // Apply the effect to the card
        cardPane.setEffect(dropShadow);
        
        // Add hover effect to enhance interactivity
        cardPane.setOnMouseEntered(e -> {
            // Increase the glow intensity on hover
            javafx.scene.effect.Glow hoverGlow = new javafx.scene.effect.Glow(0.8);
            dropShadow.setInput(hoverGlow);
            cardPane.setEffect(dropShadow);
            
            // Scale up slightly
            cardPane.setScaleX(1.05);
            cardPane.setScaleY(1.05);
            
            // Change cursor to hand to indicate it's clickable
            cardPane.setCursor(javafx.scene.Cursor.HAND);
        });
        
        cardPane.setOnMouseExited(e -> {
            // Reset to original glow on mouse exit
            javafx.scene.effect.Glow normalGlow = new javafx.scene.effect.Glow(0.5);
            dropShadow.setInput(normalGlow);
            cardPane.setEffect(dropShadow);
            
            // Reset scale
            cardPane.setScaleX(1.0);
            cardPane.setScaleY(1.0);
        });
    }















    // SETUP METHODS

    /**
     * Set the match ID for this game session
     * @param matchId The unique match identifier
     */
    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    // TODO: Fix this to work with Whist Logic
    /**
     * Set current user information
     * @param username The current user's username
     * @param isGuest Whether the user is a guest
     */
    public void setCurrentUser(String username, boolean isGuest) {
        // Broken lines below
        //this.currentUsername = username;
        //this.isGuest = isGuest;
        
        // Update UI with player name
        if (username != null && !username.isEmpty()) {
            player1Name.setText(username);
        }
    }


    /**
     * Set up the card piles (draw and discard)
     *
     * @deprecated NO LONGER USED
     */
    /*
    private void setupCardPiles() {
        // Clear existing content
        drawPile.getChildren().clear();
        discardPile.getChildren().clear();

        // Add visual card backs to draw pile
        ImageView cardBackImage = new ImageView(new Image(getClass().getResourceAsStream("/icons/game/whist_icon.png")));
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
     */

    /**
     * Sets up the Whist Game with Players
     */
    private void setupGame() {
        // Create players (for now with dummy accounts)
        List<Player> players = new ArrayList<>();
        
        // Create dummy accounts for testing
        UserAccount player1Account = new UserAccount("Player 1", "Player 1");
        UserAccount player2Account = new UserAccount("Player 2", "Player 2");
        
        // Create players with the accounts
        Player player1 = new Player(player1Account);
        Player player2 = new Player(player2Account);
        
        // Add players to the list
        players.add(player1);
        players.add(player2);
        
        // Create the game with the players
        this.whistGame = new WhistGame(GameType.WHIST, players, 8);

        // Declares the game as started
        this.gameInProgress = true;
    }

    // TODO: This must adhere to the new multiple shuffle buttons as per whistGameLoop()
    /**
     * Setup button event handlers
     */
    private void setupButtonHandlers() {
        // Start game button handler
        if (startGameButton != null) {
            startGameButton.setOnAction(event -> onStartGameClicked());
        }
        
        // Note: Shuffle buttons are now created dynamically in showShuffleOverlay()
        
        // Deal button handler is also created in showShuffleOverlay()
        
        // Forfeit button handler
        if (forfeitButton != null) {
            forfeitButton.setOnAction(event -> onForfeitClicked());
        }
        
        // Back button handler is already set up in FXML via onBackButtonClicked
        
        // Rules button handler is already set up in FXML via onRulesClicked
    }
    
    /**
     * Handles the start game button click
     */
    private void onStartGameClicked() {
        try {
            System.out.println("onStartGameClicked method called");
            
            // Set game in progress
            gameInProgress = true;
            
            // Add move history entry
            addMoveHistoryEntry("Game started");
            
            // Make sure the dealer overlay exists and is prepared
            if (dealerOverlay == null) {
                System.err.println("Error: dealerOverlay is null");
                return;
            }
            
            // Clear animation container for transition
            if (animationContainer != null) {
                animationContainer.getChildren().clear();
            }
            
            // Switch to shuffle overlay
            showShuffleOverlay();
            
            // Reset shuffle count
            shuffleCount = 0;
            
            // Set the game stage if needed
            if (whistGame.getGameStage() == null) {
                whistGame.setGameStage(StageType.DEAL);
            }
            
            // Prepare the initial game stage
            prepareGameStage(whistGame.getGameStage());
            
            System.out.println("Game started successfully");
        } catch (Exception e) {
            System.err.println("Error in onStartGameClicked: " + e.getMessage());
            e.printStackTrace();
            
            // Show error to user
            showAlert(Alert.AlertType.ERROR, "Game Start Error", 
                     "Could not start the game: " + e.getMessage());
        }
    }
    
    /**
     * Handles clicks on the various shuffle buttons
     * @param shuffleType The type of shuffle to perform
     */
    private void onShuffleClicked(String shuffleType) {
        // Increment shuffle count
        shuffleCount++;
        
        // Update the shuffle counter label
        if (shuffleCounterLabel != null) {
            shuffleCounterLabel.setText("Shuffles: " + shuffleCount + "/" + REQUIRED_SHUFFLES);
        }
        
        // Log the shuffle
        addMoveHistoryEntry("Performed a " + shuffleType + " shuffle");
        
        // Execute the correct shuffle type on the deck
        switch (shuffleType.toLowerCase()) {
            case "riffle":
                whistGame.getDeck().riffleShuffle();
                break;
            case "overhand":
                whistGame.getDeck().overheadShuffle();
                break;
            case "cut":
                whistGame.getDeck().cut();
                break;
            case "random":
                // Choose a random shuffle method
                int randomShuffle = random.nextInt(3);
                switch (randomShuffle) {
                    case 0:
                        whistGame.getDeck().riffleShuffle();
                        break;
                    case 1:
                        whistGame.getDeck().overheadShuffle();
                        break;
                    case 2:
                        whistGame.getDeck().cut();
                        break;
                }
                break;
            default:
                // Default to overhead shuffle
                whistGame.getDeck().overheadShuffle();
                break;
        }
        
        // Play shuffle animation
        animateShuffle(shuffleType);
        
        // Enable deal button once enough shuffles have been performed
        if (shuffleCount >= REQUIRED_SHUFFLES && dealButton1 != null) {
            dealButton1.setDisable(false);
            dealButton1.getStyleClass().add("deal-button-enabled");
            
            // Update instruction to indicate player can deal now
            if (shuffleInstructionLabel != null) {
                shuffleInstructionLabel.setText("Deck has been shuffled. Ready to deal!");
            }
        }
    }
    
    /**
     * Handles the original shuffle button click (for backward compatibility)
     */
    private void onShuffleClicked() {
        // Call the new method with default shuffle type
        onShuffleClicked("overhead");
    }
    
    /**
     * Handles the deal button click
     */
    private void onDealClicked() {
        // Disable the deal button to prevent multiple clicks
        dealButton1.setDisable(true);
        
        // Add move history entry
        addMoveHistoryEntry("Cards dealt to players.");
        
        // Deal all cards in the game logic
        dealAllCards();
        
        // Hide dealer overlay after dealing is complete
        dealerOverlay.setVisible(false);
        
        // Update UI to reflect the new game state
        updateUI();
        
        // Move to the next stage (typically trump reveal)
        prepareForTrumpReveal();
    }

    // TODO: Modify this to match new shuffle logic in whistGameLoop()
    /**
     * Update all UI elements based on the current game state
     */
    private void updateUI() {
        // Update stage label
        if (gameStageLabel != null && whistGame != null) {
        gameStageLabel.setText(whistGame.getGameStage().getDisplayName());
        }
        
        // Update round counter
        if (roundCounter != null && whistGame != null) {
            roundCounter.setText(String.valueOf(whistGame.getRound()));
        }
        
        // Update shuffle counter
        if (shuffleCounterLabel != null) {
            shuffleCounterLabel.setText("Shuffles: " + shuffleCount + "/" + REQUIRED_SHUFFLES);
        }
        
        // Update current player label
        if (currentPlayerLabel != null && whistGame != null && whistGame.getTurnHolder() != null) {
            String playerName = whistGame.getTurnHolder() == whistGame.getPlayer1() ? "Player 1" : "Player 2";
            currentPlayerLabel.setText(playerName);
        }
        
        // Update game status
        if (statusLabel != null) {
            String status = "Game in progress";
            if (!gameInProgress) {
                status = "Game not started";
            } else if (whistGame != null) {
                StageType stage = whistGame.getGameStage();
                switch (stage) {
                    case DEAL:
                        status = "Dealing phase";
                        break;
                    case DRAFT:
                        status = "Draft phase";
                        break;
                    case DUEL:
                        status = "Duel phase";
                        break;
                    default:
                        status = "Game over";
                        break;
                }
            }
            statusLabel.setText(status);
        }
        
        // Update player information
        updatePlayerInfo();
    }
    
    /**
     * Update player information in the UI
     */
    private void updatePlayerInfo() {
        if (whistGame == null) return;
        
        // Update Player 1 information
        if (player1Name != null && whistGame.getPlayer1() != null) {
            player1Name.setText(whistGame.getPlayer1().getUsername());
        }
        
        if (player1Score != null && whistGame.getPlayer1() != null) {
            player1Score.setText("Score: " + whistGame.getPlayer1().getScore());
        }
        
        // Highlight current player
        if (player1Status != null && whistGame.getTurnHolder() != null) {
            if (whistGame.getTurnHolder() == whistGame.getPlayer1()) {
                player1Status.setText("Current Turn");
                player1Status.getStyleClass().add("active-status");
            } else {
                player1Status.setText("Waiting");
                player1Status.getStyleClass().remove("active-status");
            }
        }
        
        // Update Player 2 information
        if (player2Name != null && whistGame.getPlayer2() != null) {
            player2Name.setText(whistGame.getPlayer2().getUsername());
        }
        
        if (player2Score != null && whistGame.getPlayer2() != null) {
            player2Score.setText("Score: " + whistGame.getPlayer2().getScore());
        }
        
        // Highlight current player
        if (player2Status != null && whistGame.getTurnHolder() != null) {
            if (whistGame.getTurnHolder() == whistGame.getPlayer2()) {
                player2Status.setText("Current Turn");
                player2Status.getStyleClass().add("active-status");
            } else {
                player2Status.setText("Waiting");
                player2Status.getStyleClass().remove("active-status");
            }
        }
        
        // Update match ID label if available
        if (matchIdLabel != null && matchId != null) {
            matchIdLabel.setText(matchId);
        }
    }

    // TODO: Either remove this or adjust to match new deal stage logic as per whistGameLoop()
    /**
     * Fix the positioning of buttons in the dealer overlay
     */
    private void fixButtonPositioning() {
        // Set proper width for both buttons
        shuffleButton.setPrefWidth(120);
        dealButton1.setPrefWidth(120);

        // Make sure the deal button text is showing correctly
        dealButton1.setText("Deal");

        // Find the HBox containing buttons and adjust its alignment
        HBox buttonBox = (HBox) dealerOverlay.lookup(".dealer-content > HBox");
        if (buttonBox != null) {
            buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
            buttonBox.setSpacing(20);
        }
    }


    // GENERAL INTERACTION METHODS

    /**
     * Handle the Start Game button click
     * @deprecated NO LONGER USED
     */
    /*
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
     */

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

    // TODO: This feature isn't working, needs fixing
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

                // TODO: Broken line
                // Disable all game buttons
                //updateControls();
                startGameButton.setDisable(false);
            }
        });
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
     * Handle the View Rules button click
     */
    @FXML
    private void onRulesClicked() {
        // Display game rules
        String rules = "Whist Card Game Rules:\n\n" + "1. The game is played over multiple rounds, concluding once one player earns 6 points\n" +
                "2. Each round consists of 3 stages: The Deal, the Draft, and the Duel.\n" +
                "3. The Deal: The deck is shuffled, each player is dealt 13 cards, and the Trump Suit is revealed.\n" +
                "4. The Draft: Players compete in 13 Tricks to improve their hands with prize cards from the draw pile.\n" +
                "5. The Duel: Players compete in 13 Tricks to tally up points and win the round.\n" +
                "6. The Trump Suit is the suit of the first revealed prize card and is prioritized in tricks.\n" +
                "7. The winner of a trick in the Draft gets the prize card, and the loser must take the next card.\n" +
                "8. The winner of a trick in the Duel sets aside the cards played in the trick to keep score.\n" +
                "9. The first trick is lead by the non-dealer, then each trick is lead by the winner of the last.\n" +
                "10. The leader of a Trick can play any card, and the follower must follow suit if possible.\n" +
                "11. If the follower cannot follow suit, they can play any card in their hand.\n" +
                "12. The highest ranked lead-suited card wins the trick.\n" +
                "13. Trump-suited cards usurp the lead suit, and any other suit loses.\n" +
                "14. Aces are higher ranked than Kings.\n" +
                "15. The score is calculated by taking the number of tricks won in the Duel and subtracting 6.\n" +
                "16. The first player to reach 6 points across all rounds wins.\n";

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Rules");
        alert.setHeaderText("Whist Card Game Rules");
        alert.setContentText(rules);
        alert.getDialogPane().setPrefWidth(600);
        alert.showAndWait();
    }

    /**
     * Add a message to the chat panel
     * @param message The message text
     * @param isSentByPlayer Whether the current player sent the message
     */
    private void addChatMessage(String message, boolean isSentByPlayer) {
        // No chat functionality implemented yet
        // This will be implemented in future updates
    }

    /**
     * Add an entry to the move history list with timestamp
     * @param message The message to add to the move history
     */
    private void addMoveHistoryEntry(String message) {
        // Create timestamp
        String timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        // Format entry with timestamp
        String entry = "[" + timestamp + "] " + message;
        
        // Add to move history list
        if (moveHistoryList != null) {
            moveHistoryList.getItems().add(entry);
            
            // Auto-scroll to bottom
            moveHistoryList.scrollTo(moveHistoryList.getItems().size() - 1);
        }
        
        // Log to console for debugging
        System.out.println("Move History: " + entry);
    }

    // TODO: This needs to be fixed. Must cooperate with logic in whistGameLoop()
    /**
     * Navigate back to the game lobby
     */
    private void navigateToGameLobby() {
        try {
            GameLobbyController controller = (GameLobbyController)
                    screenManager.navigateTo(ScreenManager.GAME_LOBBY_SCREEN, ScreenManager.GAME_LOBBY_CSS);

            if (controller != null) {
                controller.setGame("Whist");
                // Get username from player 1's account
                String username = whistGame.getPlayer1().getUsername();
                // Use a generic "Guest" flag for now, can be updated with real authentication later
                controller.setCurrentUser(username, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error",
                    "Could not navigate to Game Lobby: " + e.getMessage());
        }
    }


    // CORE INTERACTION METHODS

    // TODO: Must be completely revamped to match the new shuffle logic in whistGameLoop()
    /**
     * Handle the Shuffle button click
     */
    /*
    @FXML
    private void onShuffleClicked() {
        if (game.getShuffleCount() < 3) {
            // Update game state
            game.incrementShuffleCount();
            
            // Play shuffle animation
            animateShuffle();
            
            // Add to move history
            addMoveHistoryEntry("Deck shuffled (" + game.getShuffleCount() + "/3)");
            
            // Update UI
            updateUI();
            
            // Enable deal button when 3 shuffles completed
            if (game.getShuffleCount() >= 3) {
                dealButton.setDisable(false);
                dealButton.getStyleClass().add("deal-button-enabled");
                shuffleInstructionLabel.setText("Deck has been shuffled. Ready to deal!");
            }
        }
    }
    */


    /**
     * Handle the Deal button click
     * @deprecated NO LONGER USED
     */
    /*
    @FXML
    private void onDealClicked() {
        if (game.getGameStage() == gamelogic.whist.StageType.DEAL && game.getShuffleCount() >= 3) {
            // Deal cards in the game logic
            game.dealAllCards();
            
            // Hide dealer overlay during animation
            dealerOverlay.setVisible(false);
            
            // Start dealing animation
            animateDealing();
            
            // Add to move history
            addMoveHistoryEntry("Cards dealt to players.");
            
            // Update game stage
            game.setGameStage(StageType.DRAFT);
            
            // Update UI
            updateUI();
        }
    }
    */

    // TODO: Cards are being given On-Click events through a different method,
    //  so this either needs removed or adjusted to fit new logic as per whistGameLoop()
    /**
     * Handle card click in player's hand
     * @param cardPane The card that was clicked
     * @deprecated Use the click handler in createPlayableCard instead
     */
    /*
    private void onCardClicked(StackPane cardPane) {
        // This method is deprecated in favor of the more advanced click handler in createPlayableCard
        // which includes animation and proper card data handling
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
     */


    /**
     * Handle the Reveal Trump button click
     * @deprecated NO LONGER USED
     */
    /*
    @FXML
    private void onRevealTrumpClicked() {
        // Reveal trump in game logic
        gamelogic.pieces.SuitType trumpSuit = game.revealTrump();
        
        // Add to move history
        addMoveHistoryEntry("Trump suit revealed: " + trumpSuit.toString());
        
        // Hide dealer overlay
        dealerOverlay.setVisible(false);
        
        // Show hand areas
        showHandAreas();
        
        // Update UI
        updateUI();
        
        // Animate trump card reveal
        animateTrumpCardReveal(trumpSuit);
    }
    */

    // TODO: Trump cards still need animating, however the logic for the trump card has changed,
    //  so this needs to be remade
    /**
     * Animate the reveal of the trump card
     * @param suit The trump suit to reveal
     */
    /*
    private void animateTrumpCardReveal(gamelogic.pieces.SuitType suit) {
        // Create a card to represent the trump suit
        ImageView cardImage = new ImageView();
        // Increase size by 1.5x for more dramatic effect
        cardImage.setFitHeight(180); // 120 * 1.5 = 180
        cardImage.setFitWidth(120); // 80 * 1.5 = 120
        cardImage.setPreserveRatio(true);
        
        // Set image based on suit
        String imagePath = "/images/whist images/";
        switch (suit) {
            case HEARTS:
                imagePath += "Hearts/ace_of_hearts.png";
                break;
            case DIAMONDS:
                imagePath += "Diamonds/ace_of_diamonds.png";
                break;
            case SPADES:
                imagePath += "Spades/ace_of_spades.png";
                break;
            case CLUBS:
                imagePath += "Clubs/ace_of_clubs.png";
                break;
            default:
                imagePath += "CardBackside1.png";
                break;
        }
        
        // Store the final imagePath to ensure we use the face-up card
        final String finalImagePath = imagePath;
        
        try {
            cardImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            // Fallback to backside if image not found
            try {
                cardImage.setImage(new Image(getClass().getResourceAsStream("/images/whist images/CardBackside1.png")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Position the card in the center of the screen
        StackPane centerCard = new StackPane(cardImage);
        centerCard.setAlignment(javafx.geometry.Pos.CENTER);
        
        // Add to animation container
        animationContainer.getChildren().add(centerCard);
        
        // Center the card in the animation container
        double screenWidth = animationContainer.getScene().getWindow().getWidth();
        double screenHeight = animationContainer.getScene().getWindow().getHeight();
        centerCard.setLayoutX((screenWidth / 2) - 60); // Adjusted for larger card width
        centerCard.setLayoutY((screenHeight / 2) - 90); // Adjusted for larger card height
        
        // Create the first animation - card rotation (face reveal)
        javafx.animation.RotateTransition rotateCard = new javafx.animation.RotateTransition(Duration.seconds(1), cardImage);
        rotateCard.setAxis(javafx.scene.transform.Rotate.Y_AXIS);
        rotateCard.setFromAngle(0);
        rotateCard.setToAngle(720); // Two full rotations for dramatic effect
        rotateCard.setCycleCount(1);
        rotateCard.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        
        // Get the exact center position of the trump display area
        final double trumpX = trumpCardDisplay.localToScene(trumpCardDisplay.getBoundsInLocal()).getMinX() + 
                           (trumpCardDisplay.getBoundsInLocal().getWidth() / 2) - 40;
        final double trumpY = trumpCardDisplay.localToScene(trumpCardDisplay.getBoundsInLocal()).getMinY() + 
                           (trumpCardDisplay.getBoundsInLocal().getHeight() / 2) - 60;
        
        // Add a slight pause after rotation
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.millis(200));
        
        // Second animation - move card to trump area with scaling down
        javafx.animation.ParallelTransition moveAndScale = new javafx.animation.ParallelTransition();
        
        // Move animation
        javafx.animation.TranslateTransition moveCard = new javafx.animation.TranslateTransition(Duration.seconds(0.5), centerCard);
        moveCard.setToX(trumpX - centerCard.getLayoutX());
        moveCard.setToY(trumpY - centerCard.getLayoutY());
        moveCard.setInterpolator(javafx.animation.Interpolator.EASE_IN);
        
        // Scale down animation (from 1.5x to normal size)
        javafx.animation.ScaleTransition scaleDown = new javafx.animation.ScaleTransition(Duration.seconds(0.5), cardImage);
        scaleDown.setFromX(1.0);
        scaleDown.setFromY(1.0);
        scaleDown.setToX(0.67); // Scale down to original size (1/1.5 = ~0.67)
        scaleDown.setToY(0.67);
        
        // Add both animations to the parallel transition
        moveAndScale.getChildren().addAll(moveCard, scaleDown);
        
        // Sequence all the animations
        javafx.animation.SequentialTransition sequence = new javafx.animation.SequentialTransition(
            rotateCard, pause, moveAndScale);
        
        sequence.setOnFinished(event -> {
            // Remove the animation card
            animationContainer.getChildren().clear();
            
            // Display the permanent trump card (ensuring it's face up)
            displayTrumpCard(suit, finalImagePath);
            
            // Add an entry to the move history
            addMoveHistoryEntry("Trump suit is: " + suit.toString());
        });
        
        // Play the animation sequence
        sequence.play();
    }
     */

    // TODO: Trump card logic changed, these may not be needed any longer
    /**
     * Display the trump card in the trump display area
     * 
     * @param suit The trump suit to display
     */
    /*
    private void displayTrumpCard(gamelogic.pieces.SuitType suit) {
        displayTrumpCard(suit, null);
    }
     */

    /**
     * Display the trump card in the trump display area
     * 
     * @param suit The trump suit to display
     * @param providedImagePath Optional image path to use (to ensure face-up card is used)
     */
    /*
    private void displayTrumpCard(SuitType suit, String providedImagePath) {
        // Clear trump display
        trumpCardDisplay.getChildren().clear();
        
        // Create a card to represent the trump suit
        ImageView cardImage = new ImageView();
        cardImage.setFitHeight(120);
        cardImage.setFitWidth(80);
        cardImage.setPreserveRatio(true);
        
        // Set image based on suit or use provided image path
        String imagePath = providedImagePath;
        
        // If no image path provided, determine it based on suit
        if (imagePath == null) {
            // Use an actual card image for each suit instead of just queen
            imagePath = "/images/whist images/";
            switch (suit) {
                case HEARTS:
                    imagePath += "Hearts/ace_of_hearts.png";
                    break;
                case DIAMONDS:
                    imagePath += "Diamonds/ace_of_diamonds.png";
                    break;
                case SPADES:
                    imagePath += "Spades/ace_of_spades.png";
                    break;
                case CLUBS:
                    imagePath += "Clubs/ace_of_clubs.png";
                    break;
                default:
                    imagePath += "CardBackside1.png";
                    break;
            }
        }
        
        try {
            cardImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            // Fallback to backside if image not found
            try {
                cardImage.setImage(new Image(getClass().getResourceAsStream("/images/whist images/CardBackside1.png")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Add the image to the trump display
        trumpCardDisplay.getChildren().add(cardImage);
        
        // Create a styled label with larger, bolder text for better visibility
        Label suitLabel = new Label("Trump: " + suit.toString());
        suitLabel.getStyleClass().addAll("area-label", "trump-label");
        // Move label to a better position so it doesn't overlap the card
        suitLabel.setTranslateY(60);
        trumpCardDisplay.getChildren().add(suitLabel);
    }
     */

    // TODO: Dealing logic changed, so this needs to be adjusted to meet implementation in whistGameLoop()
    /**
     * Animate the dealing of cards
     */
    private void animateDealing() {
        // Create animation container for dealing
        StackPane deckPosition = new StackPane();
        
        // Use the dealer overlay for positioning of the deck
        final double deckX = dealerOverlay.localToScene(dealerOverlay.getBoundsInLocal()).getMinX() + 
                        dealerOverlay.getWidth()/2 - 40;
        final double deckY = dealerOverlay.localToScene(dealerOverlay.getBoundsInLocal()).getMinY() + 
                        dealerOverlay.getHeight()/2 - 60;
        
        deckPosition.setLayoutX(deckX);
        deckPosition.setLayoutY(deckY);
        animationContainer.getChildren().add(deckPosition);
        
        // Clear any existing cards
        playerHandArea.getChildren().clear();
        opponentHandArea.getChildren().clear();
        
        // Get card back image
        final Image cardBackImage;
        try {
            cardBackImage = new Image(getClass().getResourceAsStream("/images/whist images/CardBackside1.png"));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error Loading Image", 
                     "Could not load card backside image. Please check the image path.");
            return;
        }
        
        // Create animation timeline
        javafx.animation.Timeline timeline = new javafx.animation.Timeline();
        
        // Number of cards to deal to each player
        final int CARDS_PER_PLAYER = 13;
        
        // Get positions for hand areas in scene coordinates
        final double playerAreaMinX = playerHandArea.localToScene(playerHandArea.getBoundsInLocal()).getMinX();
        final double playerAreaMinY = playerHandArea.localToScene(playerHandArea.getBoundsInLocal()).getMinY();
        final double opponentAreaMinX = opponentHandArea.localToScene(opponentHandArea.getBoundsInLocal()).getMinX();
        final double opponentAreaMinY = opponentHandArea.localToScene(opponentHandArea.getBoundsInLocal()).getMinY();
        
        // Get player hands from game logic
        gamelogic.Player player1 = whistGame.getPlayers().getFirst();
        gamelogic.Player player2 = whistGame.getPlayers().getLast();
        
        // Create and animate 13 cards for each player
        for (int i = 0; i < CARDS_PER_PLAYER && i < player1.getHand().size() && i < player2.getHand().size(); i++) {
            final int cardIndex = i;
            
            // Get actual card data from game logic
            gamelogic.pieces.Card player1Card = (gamelogic.pieces.Card) player1.getHand().get(i);
            gamelogic.pieces.Card player2Card = (gamelogic.pieces.Card) player2.getHand().get(i);
            
            // Create player card
            final ImageView playerCard = createCardImageView(cardBackImage);
            
            // Create opponent card
            final ImageView opponentCard = createCardImageView(cardBackImage);
            
            // Add cards to container for animation
            animationContainer.getChildren().addAll(playerCard, opponentCard);
            
            // Set initial position (at the deck)
            playerCard.setLayoutX(deckX);
            playerCard.setLayoutY(deckY);
            opponentCard.setLayoutX(deckX);
            opponentCard.setLayoutY(deckY);
            
            // Calculate final positions
            final double playerAreaX = playerAreaMinX + 40 + (cardIndex * 30); 
            final double playerAreaY = playerAreaMinY + playerHandArea.getHeight()/2 - 60;
            final double opponentAreaX = opponentAreaMinX + 40 + (cardIndex * 30);
            final double opponentAreaY = opponentAreaMinY + opponentHandArea.getHeight()/2 - 60;
            
            // Store the card data for later use when creating actual hand cards
            final int player1CardRank = player1Card.getRank();
            final gamelogic.pieces.SuitType player1CardSuit = player1Card.getSuit();
            final int player2CardRank = player2Card.getRank();
            final gamelogic.pieces.SuitType player2CardSuit = player2Card.getSuit();
            
            // Modified animation timing for 1.5s total duration
            // Create animations for player card
            javafx.animation.KeyFrame playerStart = new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(i * 115), // Adjusted timing for 1.5s total
                new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        // Make card visible at start of animation
                        playerCard.setVisible(true);
                    }
                }
            );
            
            javafx.animation.KeyFrame playerEnd = new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(i * 115 + 400), // Adjusted timing
                new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        // Animate to final position
                        javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(
                            javafx.util.Duration.millis(250), playerCard); // Adjusted timing
                        tt.setToX(playerAreaX - playerCard.getLayoutX());
                        tt.setToY(playerAreaY - playerCard.getLayoutY());
                        tt.play();
                        
                        // After animation completes, add card to player hand
                        tt.setOnFinished(e -> {
                            // TODO: Broken Lines
                            // Add a playable card to the player hand area with the actual card data
                            //StackPane playableCard = createPlayableCard(cardBackImage, player1CardRank, player1CardSuit, false);
                            //playableCard.setVisible(true); // Make visible
                            //playerHandArea.getChildren().add(playableCard);

                            // TODO: Broken Line
                            // Add to player cards list for reference
                            //playerCards.add(playableCard);
                            
                            // Remove animation card
                            animationContainer.getChildren().remove(playerCard);
                        });
                    }
                }
            );
            
            // Create animations for opponent card
            javafx.animation.KeyFrame opponentStart = new javafx.animation.KeyFrame(
                javafx.util.Duration.millis((i * 115) + 57), // Adjusted timing
                new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        // Make card visible at start of animation
                        opponentCard.setVisible(true);
                    }
                }
            );
            
            javafx.animation.KeyFrame opponentEnd = new javafx.animation.KeyFrame(
                javafx.util.Duration.millis((i * 115) + 57 + 400), // Adjusted timing
                new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        // Animate to final position
                        javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(
                            javafx.util.Duration.millis(250), opponentCard); // Adjusted timing
                        tt.setToX(opponentAreaX - opponentCard.getLayoutX());
                        tt.setToY(opponentAreaY - opponentCard.getLayoutY());
                        tt.play();
                        
                        // After animation completes, add card to opponent hand
                        tt.setOnFinished(e -> {
                            // TODO: Broken lines
                            // Add a basic card to the opponent hand area (no click handling needed)
                            //StackPane opponentPlayableCard = createPlayableCard(cardBackImage, player2CardRank, player2CardSuit, false);
                            //opponentPlayableCard.setVisible(true); // Make visible
                            //opponentHandArea.getChildren().add(opponentPlayableCard);

                            // TODO: Broken Line
                            // Add to opponent cards list for reference
                            //opponentCards.add(opponentPlayableCard);
                            
                            // Remove animation card
                            animationContainer.getChildren().remove(opponentCard);
                            
                            // If this is the last card, show the hand areas
                            if (cardIndex == CARDS_PER_PLAYER - 1) {
                                // After all cards are dealt, show hand areas and update overlay
                                showHandAreas();
                                // Show the updated overlay after all cards are dealt
                                updateDealerOverlayForTrumpReveal();
                                dealerOverlay.setVisible(true);
                            }
                        });
                    }
                }
            );
            
            // Add all key frames to timeline
            timeline.getKeyFrames().addAll(playerStart, playerEnd, opponentStart, opponentEnd);
        }
        
        // Add final key frame to update UI after all animations
        javafx.animation.KeyFrame finalFrame = new javafx.animation.KeyFrame(
            javafx.util.Duration.millis(1500), // Exactly 1.5 seconds
            new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(javafx.event.ActionEvent event) {
                    // Clean up animation container
                    animationContainer.getChildren().clear();
                }
            }
        );
        
        timeline.getKeyFrames().add(finalFrame);
        
        // Play the animation
        timeline.play();
    }

    // TODO: Likely not needed anymore because of new method of displaying cards
    /**
     * Creates a card ImageView with the given image
     * 
     * @param cardImage The image to use for the card
     * @return The created ImageView
     */
    private ImageView createCardImageView(Image cardImage) {
        ImageView cardView = new ImageView(cardImage);
        cardView.setFitHeight(120);
        cardView.setFitWidth(80);
        cardView.setPreserveRatio(true);
        cardView.getStyleClass().add("card-view");
        cardView.setVisible(false); // Start invisible for animation
        return cardView;
    }

    // TODO: New renderCard class replaces this one, so code using this must be made to use the new version
    /**
     * Creates a card StackPane with the given image and card data
     * Includes click functionality with animation
     * 
     * @param cardImage The image to use for the card
     * @param cardRank The rank of the card (1-13)
     * @param cardSuit The suit of the card
     * @param isFaceUp Whether the card should be displayed face up initially
     * @return The created card StackPane
     * @deprecated Use the renderCard method instead
     */
    /*
    private StackPane createPlayableCard(Image cardImage, int cardRank, gamelogic.pieces.SuitType cardSuit, boolean isFaceUp) {
        // Create the image view for the card
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setFitHeight(120);
        cardImageView.setFitWidth(80);
        cardImageView.setPreserveRatio(true);
        
        // Create a StackPane to hold the card
        StackPane cardPane = new StackPane(cardImageView);
        cardPane.getStyleClass().add("card-view");
        
        // Store card data as properties for later access
        cardPane.getProperties().put("rank", cardRank);
        cardPane.getProperties().put("suit", cardSuit);
        cardPane.getProperties().put("faceUp", isFaceUp);
        
        // Create click handler for card selection and animation
        cardPane.setOnMouseClicked(event -> {
            // Skip if not player's turn or wrong game stage
            if (!isPlayerTurn || currentStage != StageType.DUEL) return;
            
            boolean isCurrentlyFaceUp = (boolean) cardPane.getProperties().getOrDefault("faceUp", false);
            
            // If card is already selected (face up)
            if (isCurrentlyFaceUp) {
                // Play the card to the trick area
                playCardToTrick(cardPane);
                return;
            }
            
            // Check if any other card is already face up
            for (Node node : playerHandArea.getChildren()) {
                if (node instanceof StackPane && node != cardPane) {
                    StackPane otherCard = (StackPane) node;
                    if ((boolean) otherCard.getProperties().getOrDefault("faceUp", false)) {
                        // Flip the other card back face down
                        flipCardFaceDown(otherCard);
                    }
                }
            }
            
            // Show card animation
            animateCardSelection(cardPane);
        });
        
        return cardPane;
    }

     */
    // TODO: Card flipping needs to be done differently to accommodate whist logic
    /**
     * Flip a card to face up, showing the actual card image
     * 
     * @param cardPane The card to flip face up
     * @deprecated NO LONGER USED
     */
    /*
    private void flipCardFaceUp(StackPane cardPane) {
        // Get card data
        int cardRank = (int) cardPane.getProperties().get("rank");
        SuitType cardSuit = (SuitType) cardPane.getProperties().get("suit");
        
        // Build image path based on card data
        String imagePath = "/images/whist images/";
        
        // Add suit folder
        switch (cardSuit) {
            case HEARTS:
                imagePath += "Hearts/";
                break;
            case DIAMONDS:
                imagePath += "Diamonds/";
                break;
            case SPADES:
                imagePath += "Spades/";
                break;
            case CLUBS:
                imagePath += "Clubs/";
                break;
        }
        
        // Add rank specific file name
        if (cardRank == 1) {
            imagePath += "ace_of_" + cardSuit.toString().toLowerCase() + ".png";
        } else if (cardRank == 11) {
            imagePath += "jack_of_" + cardSuit.toString().toLowerCase() + ".png";
        } else if (cardRank == 12) {
            imagePath += "queen_of_" + cardSuit.toString().toLowerCase() + ".png";
        } else if (cardRank == 13) {
            imagePath += "king_of_" + cardSuit.toString().toLowerCase() + ".png";
        } else {
            imagePath += cardRank + "_of_" + cardSuit.toString().toLowerCase() + ".png";
        }
        
        // Load the card face image
        try {
            Image faceImage = new Image(getClass().getResourceAsStream(imagePath));
            // Replace the card image
            ImageView cardImageView = (ImageView) cardPane.getChildren().get(0);
            cardImageView.setImage(faceImage);
            
            // Mark card as face up
            cardPane.getProperties().put("faceUp", true);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error Loading Card Image", 
                     "Could not load card image: " + imagePath);
        }
    }
     */

    // TODO: Card flipping is done differently in whist logic, so this isn't useable
    /**
     * Flip a card to face down, showing the card back
     * 
     * @param cardPane The card to flip face down
     * @deprecated NO LONGER USED
     */
    private void flipCardFaceDown(StackPane cardPane) {
        try {
            // Load the card back image
            Image backImage = new Image(getClass().getResourceAsStream("/images/whist images/CardBackside1.png"));
            
            // Scale back to normal size
            cardPane.setScaleX(1.0);
            cardPane.setScaleY(1.0);
            
            // Move back to original position
            cardPane.setTranslateY(0);
            
            // Reset rotation
            cardPane.setRotate(0);
            
            // Replace the image
            ImageView cardImageView = (ImageView) cardPane.getChildren().get(0);
            cardImageView.setImage(backImage);
            
            // Mark card as face down
            cardPane.getProperties().put("faceUp", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Playing cards into tricks is done differently, this must be adjusted or removed
    /**
     * Play the selected card to the trick area
     * 
     * @param cardPane The card to play
     */
    private void playCardToTrick(StackPane cardPane) {
        // Get the card associated with this pane
        Card card = (Card) cardPane.getProperties().get("card");
        if (card == null) {
            System.err.println("Error: Card not found in cardPane properties");
            return;
        }
        
        // Get the current player
        Player currentPlayer = whistGame.getTurnHolder();
        
        // Play the card in game logic
        whistGame.playCard(card);
        
        // Add move history entry
        String cardName = card.isFaceDown() ? "a card" : card.getRank() + " of " + card.getSuit();
        addMoveHistoryEntry(currentPlayer.getUsername() + " played " + cardName);
        
        // Use our enhanced animation method instead of basic movement
        animatePlayCardToTrick(cardPane);
    }

    /**
     * Hides the player hand areas during certain phases (e.g. dealing)
     */
    private void hideHandAreas() {
        if (playerHandArea != null) {
            playerHandArea.setVisible(false);
        }
        if (opponentHandArea != null) {
            opponentHandArea.setVisible(false);
        }
    }
    
    /**
     * Shows the player hand areas 
     */
    private void showHandAreas() {
        if (playerHandArea != null) {
            playerHandArea.setVisible(true);
        }
        if (opponentHandArea != null) {
            opponentHandArea.setVisible(true);
        }
    }

    // TODO: Likely not usable because of new dealer selection logic in whistGameLoop()
    /**
     * Update dealer overlay to show the reveal trump button
     */
    private void updateDealerOverlayForTrumpReveal() {
        // Update instruction text
        shuffleInstructionLabel.setText("Cards have been dealt. Now reveal the trump suit.");
        
        // Hide shuffle and deal buttons
        shuffleButton.setVisible(false);
        dealButton1.setVisible(false);
        
        // Hide shuffle counter as requested
        shuffleCounterLabel.setVisible(false);
        
        // Show reveal trump button
        revealTrumpButton.setVisible(true);
        
        // Update dealer title
        dealerTitle.setText("DRAFT STAGE");
    }

    // STATUS METHODS

    // TODO: Whist game state is handled in the whistGame class, so this isn't needed
    /**
     * Update the game stage and UI elements
     * @param stage The new game stage
     * @deprecated NO LONGER USED
     */
    /*
    private void updateGameStage(StageType stage) {
        //currentStage = stage;
        gameStageLabel.setText(stage.getDisplayName());
        
        // Update status message based on stage
        switch (stage) {
            case DEAL:
                gameStatusLabel.setText("Cards need to be shuffled and dealt");
                break;
            case DRAFT:
                gameStatusLabel.setText("Trump suit needs to be revealed");
                break;
            case DUEL:
                gameStatusLabel.setText("Play your cards to win tricks");
                break;
        }
        
        // Update UI controls based on stage
        updateControls();
    }
     */

    // TODO: Potentially not needed, as these buttons are handled differently
    /**
     * Update game controls based on current game state
     */
    /*
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
                dealButton.setDisable(shuffleCount < REQUIRED_SHUFFLES);
                revealTrumpButton.setDisable(true);
                break;
            case DRAFT:
                shuffleButton.setDisable(true);
                dealButton.setDisable(true);
                revealTrumpButton.setDisable(false);
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
     */

    // TODO: Game code is being oriented toward multiplayer, so the game switches between two players,
    //  so this needs to be adjusted to fit the new logic
    /**
     * Update the player status indicators based on whose turn it is
     */
    /*
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
     */

    /**
     * Show a dialog alert
     * @param type Alert type
     * @param title Alert title
     * @param message Alert message
     */
    private void showAlert(AlertType type, String title, String message) {
        // Use Platform.runLater to avoid IllegalStateException during animations
        javafx.application.Platform.runLater(() -> {
            try {
                Alert alert = new Alert(type);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.show(); // Use show() instead of showAndWait() to avoid blocking
            } catch (Exception e) {
                System.err.println("Error showing alert: " + e.getMessage());
            }
        });
    }

    // ANIMATION METHODS

    /**
     * Animate the shuffling of cards with the specified animation
     * 
     * @param shuffleType The type of shuffle animation to display
     */
    private void animateShuffle(String shuffleType) {
        // Find the card image in the dealer overlay
        StackPane cardContainer = (StackPane) dealerOverlay.lookup(".card-image-container");
        if (cardContainer == null || cardContainer.getChildren().isEmpty()) return;
        
        // Get the card image
        ImageView cardImage = (ImageView) cardContainer.getChildren().get(0);
        
        // Create animations based on shuffle type
        switch (shuffleType) {
            case "riffle":
                // Riffle shuffle animation (splitting and combining)
                SequentialTransition riffleAnimation = new SequentialTransition();
                
                // First split the card visually (scale down width)
                ScaleTransition splitCards = new ScaleTransition(Duration.millis(300), cardImage);
                splitCards.setFromX(1.0);
                splitCards.setToX(0.5);
                
                // Then show combining motion (rapid up/down movement)
                TranslateTransition riffleMotion = new TranslateTransition(Duration.millis(500), cardImage);
                riffleMotion.setFromY(0);
                riffleMotion.setToY(10);
                riffleMotion.setCycleCount(6);
                riffleMotion.setAutoReverse(true);
                
                // Finally recombine (scale back to normal)
                ScaleTransition combineCards = new ScaleTransition(Duration.millis(300), cardImage);
                combineCards.setFromX(0.5);
                combineCards.setToX(1.0);
                
                riffleAnimation.getChildren().addAll(splitCards, riffleMotion, combineCards);
                riffleAnimation.play();
                break;
                
            case "cut":
                // Cut shuffle animation (split deck and swap positions)
                SequentialTransition cutAnimation = new SequentialTransition();
                
                // Split the card (create visual gap)
                TranslateTransition cutSplit = new TranslateTransition(Duration.millis(400), cardImage);
                cutSplit.setFromY(0);
                cutSplit.setToY(-40);
                
                // Swap positions (show cut happening)
                RotateTransition cutRotate = new RotateTransition(Duration.millis(500), cardImage);
                cutRotate.setAxis(Rotate.Z_AXIS);
                cutRotate.setFromAngle(0);
                cutRotate.setToAngle(180);
                
                // Return to normal position
                TranslateTransition cutReturn = new TranslateTransition(Duration.millis(400), cardImage);
                cutReturn.setFromY(-40);
                cutReturn.setToY(0);
                
                cutAnimation.getChildren().addAll(cutSplit, cutRotate, cutReturn);
                cutAnimation.play();
                break;
                
            case "scramble":
                // Scramble shuffle animation (random movements)
                SequentialTransition scrambleAnimation = new SequentialTransition();
                
                // First spread cards out
                ScaleTransition spreadOut = new ScaleTransition(Duration.millis(300), cardImage);
                spreadOut.setFromX(1.0);
                spreadOut.setToX(1.2);
                spreadOut.setFromY(1.0);
                spreadOut.setToY(1.2);
                
                // Chaotic motion
                Timeline chaoticMotion = new Timeline();
                for (int i = 0; i < 10; i++) {
                    double randomX = (Math.random() - 0.5) * 40;
                    double randomY = (Math.random() - 0.5) * 40;
                    double randomRotate = (Math.random() - 0.5) * 60;
                    
                    KeyFrame frame = new KeyFrame(Duration.millis(i * 100), 
                        new KeyValue(cardImage.translateXProperty(), randomX),
                        new KeyValue(cardImage.translateYProperty(), randomY),
                        new KeyValue(cardImage.rotateProperty(), randomRotate)
                    );
                    chaoticMotion.getKeyFrames().add(frame);
                }
                
                // Return to normal
                KeyFrame finalFrame = new KeyFrame(Duration.millis(1000),
                    new KeyValue(cardImage.translateXProperty(), 0),
                    new KeyValue(cardImage.translateYProperty(), 0),
                    new KeyValue(cardImage.rotateProperty(), 0)
                );
                chaoticMotion.getKeyFrames().add(finalFrame);
                
                // Back to normal size
                ScaleTransition scaleBack = new ScaleTransition(Duration.millis(300), cardImage);
                scaleBack.setFromX(1.2);
                scaleBack.setToX(1.0);
                scaleBack.setFromY(1.2);
                scaleBack.setToY(1.0);
                
                scrambleAnimation.getChildren().addAll(spreadOut, chaoticMotion, scaleBack);
                scrambleAnimation.play();
                break;
                
            case "overhead":
            default:
                // Overhead shuffle animation (simple rotation)
                RotateTransition rotate = new RotateTransition(Duration.seconds(1), cardImage);
                rotate.setAxis(Rotate.Y_AXIS);
        rotate.setFromAngle(0);
        rotate.setToAngle(360);
        rotate.setCycleCount(1);
                rotate.setInterpolator(Interpolator.EASE_BOTH);
        rotate.play();
                break;
        }
    }

    /**
     * Original animation method (for backward compatibility)
     */
    private void animateShuffle() {
        // Call the new method with default shuffle type
        animateShuffle("overhead");
    }

    // SIMULATION METHODS

    // TODO: This needs to be adjusted to fit new logic, but we will do this later
    /**
     * Simulate the opponent's turn
     */
    private void simulateOpponentTurn() {
        // Add a delay to make it feel like the opponent is thinking
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            // Different actions based on current stage
            switch (whistGame.getGameStage()) {
                case DEAL:
                    simulateOpponentShuffle();
                    break;
                case DRAFT:
                    // TODO: Broken line
                    //simulateOpponentDraft();
                    break;
                case DUEL:
                    simulateOpponentPlayCard();
                    break;
            }

            // TODO: Broken lines
            // Update UI after opponent's action
            //isPlayerTurn = true;
            //updatePlayerStatusIndicators();
        }));
        timeline.play();
    }

    // TODO: This will also need readjusted
    /**
     * Simulate opponent shuffling cards
     */
    private void simulateOpponentShuffle() {
        // Animate the shuffle
        animateShuffle();
        
        // Log the shuffle
        addMoveHistoryEntry("Opponent shuffled the deck.");
        
        // Enable dealing for player
        dealButton1.setDisable(false);
        
        // Update status
        gameStatusLabel.setText("Opponent shuffled. Your turn to deal cards.");
    }

    // TODO: This doesn't adhere to how the Draft stage works, so it should be removed or readjusted
    /**
     * Simulate opponent drafting a card
     */
    /*
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
     */

    // TODO: Needs adjusting to new playing logic which isn't quite implemented yet
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
        // Simulate opponent typing delay
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(2),
                event -> {
                    String[] responses = {
                            "Good move!", "I'll get you next time!", "Interesting play...",
                            "Nice one!", "I've got a strategy", "Let me think here..."
                    };
                    String response = responses[random.nextInt(responses.length)];
                    addChatMessage(response, false);
                }));
        timeline.play();
    }

    // The following methods will be implemented later:
    
    // private void createCardViews() {}
    // private void revealPlayerCards() {}
    // private void handleRevealTrump() {}
    // private void handleCardClick() {}
    // private void playCardToTrickAnimation() {}
    // private void handleTrickCompletion() {}
    // private void handleRoundEnd() {}
    // private void prepareNextRound() {}
    
    /**
     * Starts a game timer with a specified duration and timeout action
     * 
     * @param seconds Number of seconds to run the timer
     * @param timeoutAction Action to perform when timer expires
     */
    private void startGameTimer(int seconds, Runnable timeoutAction) {
        // Cancel any existing timer
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Set initial time
        timeRemaining = seconds;
        
        // Update display
        updateTimerDisplay();
        
        // Create new timer with 1-second intervals
        gameTimer = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                // Decrement time
                timeRemaining--;
                
                // Update timer display
                updateTimerDisplay();
                
                // Check if timer has expired
                if (timeRemaining <= 0) {
                    // Stop timer
                    gameTimer.stop();
                    
                    // Execute timeout action if provided
                    if (timeoutAction != null) {
                        timeoutAction.run();
                    }
                }
            })
        );
        
        // Set to repeat until stopped
        gameTimer.setCycleCount(seconds);
        
        // Start the timer
        gameTimer.play();
    }
    
    /**
     * Updates the timer display with current remaining time
     */
    private void updateTimerDisplay() {
        // Update label with formatted time
        if (timerLabel != null) {
            timerLabel.setText(String.format("%02d:%02d", timeRemaining / 60, timeRemaining % 60));
        }
        
        // Update progress bar
        if (timerProgressBar != null) {
            // Calculate progress (1.0 to 0.0)
            double progress = timeRemaining / 15.0; // Assuming 15 seconds is max time
            timerProgressBar.setProgress(progress);
            
            // Change color based on time remaining
            if (timeRemaining <= 5) {
                timerProgressBar.getStyleClass().remove("timer-normal");
                timerProgressBar.getStyleClass().add("timer-warning");
            } else {
                timerProgressBar.getStyleClass().remove("timer-warning");
                timerProgressBar.getStyleClass().add("timer-normal");
            }
        }
    }




    /**
     * Creates the reveal trump button and adds it to the dealer overlay
     */
    /*
    private void createRevealTrumpButton() {
        revealTrumpButton = new Button("Reveal Trump Suit");
        revealTrumpButton.getStyleClass().add("reveal-trump-button");
        revealTrumpButton.setVisible(false);
        
        // Set action for reveal trump button
        revealTrumpButton.setOnAction(event -> onRevealTrumpClicked());
        
        // Add to dealer overlay in a centered position
        VBox dealerContent = (VBox) dealerOverlay.lookup(".dealer-content");
        if (dealerContent != null) {
            // Remove existing button from old container if any
            HBox buttonBox = (HBox) dealerOverlay.lookup(".dealer-content > HBox");
            if (buttonBox != null && buttonBox.getChildren().contains(revealTrumpButton)) {
                buttonBox.getChildren().remove(revealTrumpButton);
            }
            
            // Add directly to dealer content VBox for center alignment
            dealerContent.getChildren().add(revealTrumpButton);
            // Set VBox alignment to ensure proper centering
            VBox.setMargin(revealTrumpButton, new javafx.geometry.Insets(10, 0, 0, 0));
        }
    }
    */

    // GAME STAGE MANAGEMENT

    /**
     * Prepares the game for the current stage by setting up required UI elements and interactions
     * 
     * @param stage The current game stage to prepare for
     */
    private void prepareGameStage(StageType stage) {
        if (stage == null) {
            // Game is over
            handleGameEnd();
            return;
        }
        
        // Update UI to reflect current stage
        updateUI();
        
        switch (stage) {
            case DEAL:
                prepareDealStage();
                break;
            case DRAFT:
                prepareDraftStage();
                break;
            case DUEL:
                prepareDuelStage();
                break;
        }
    }
    
    /**
     * Handles progression to the next game stage
     */
    private void progressToNextStage() {
        // Move to the next stage in the game logic
        whistGame.nextStage();
        
        // Add move history entry about stage change
        addMoveHistoryEntry("Moving into " + whistGame.getGameStage().getDisplayName() + " stage!");
        
        // Prepare UI for the new stage
        prepareGameStage(whistGame.getGameStage());
    }
    
    /**
     * Prepares the UI and game logic for the DEAL stage
     */
    private void prepareDealStage() {
        // DEBUG: Shows stage
        System.out.println("-=Dealing Stage=-\n");
        
        // Increment round counter at the start of a new round
        whistGame.setRound(whistGame.getRound() + 1);
        addMoveHistoryEntry("Round " + whistGame.getRound() + " begins!");
        
        // Update UI with new round info
        updateUI();
        
        // Setup dealer selection
        setupDealerSelectionUI();
    }
    
    /**
     * Prepares the UI and game logic for the DRAFT stage
     */
    private void prepareDraftStage() {
        // DEBUG: Shows stage
        System.out.println("-=Drafting Stage=-\n");
        
        // Hide dealer overlay
        dealerOverlay.setVisible(false);
        
        // Reset trick counters at the start of DRAFT stage
        currentTrickNumber = 0;
        player1TricksWon = 0;
        player2TricksWon = 0;
        
        // Add move history entry
        addMoveHistoryEntry("Draft stage started. Players will take turns playing cards to compete for prize cards from the draw pile.");
        
        // Show hand areas for gameplay
        showHandAreas();
        
        // Make sure the trick area is visible and clear
        trickArea.getChildren().clear();
        trickArea.setVisible(true);
        
        // Show draw pile for prize cards
        // Create discard pile if it doesn't exist
        if (whistGame.getDiscard() == null) {
            whistGame.setDiscard(new CardPile());
        }
        
        // Set up player hands - show them face up to their owners
        whistGame.showHand(whistGame.getPlayer1());
        whistGame.showHand(whistGame.getPlayer2());
        
        // Update player hand displays
        updatePlayerHandDisplay();
        
        // Update UI to reflect the current stage
        updateUI();
        
        // Show whose turn it is
        currentPlayerLabel.setText(whistGame.getTurnHolder().getUsername());
        statusLabel.setText(whistGame.getTurnHolder().getUsername() + "'s turn to play - Trick " + (currentTrickNumber + 1) + " of 13");
        
        // Make the current player's cards playable
        if (whistGame.getTurnHolder() == whistGame.getPlayer1()) {
            // Make player 1's cards playable
            for (StackPane cardPane : player1Cards) {
                makeIntoPlayable(cardPane);
            }
        } else {
            // Make player 2's cards playable
            for (StackPane cardPane : player2Cards) {
                makeIntoPlayable(cardPane);
            }
        }
        
        // Start a timer for the player's turn
        startGameTimer(15, () -> {
            // Auto-play a card if timer expires
            autoPlayCard();
        });
    }
    
    /**
     * Resolves a trick after both players have played a card
     */
    private void resolveTrick() {
        // Stop any running timer
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Get the cards in the trick
        if (whistGame.getTrick().size() != 2) {
            // Not enough cards to resolve trick
            return;
        }
        
        // Determine the trick winner
        Player trickWinner = whistGame.getTrickWinner();
        if (trickWinner == null) {
            // No winner (should not happen)
            showAlert(Alert.AlertType.ERROR, "Error", "Could not determine trick winner!");
            return;
        }
        
        // Increment trick count
        currentTrickNumber++;
        
        // Track tricks won by each player
        if (trickWinner == whistGame.getPlayer1()) {
            player1TricksWon++;
        } else {
            player2TricksWon++;
        }
        
        // Add move history entry
        addMoveHistoryEntry(trickWinner.getUsername() + " won trick " + currentTrickNumber + " of 13");
        
        // Show winner visually
        statusLabel.setText(trickWinner.getUsername() + " won trick " + currentTrickNumber + " of 13");
        
        // Update turn holder for next trick (winner leads next trick)
        whistGame.setTurnHolder(trickWinner);
        
        // Handle based on game stage
        if (whistGame.getGameStage() == StageType.DRAFT) {
            // DRAFT stage logic
            handleDraftStageTrickCompletion(trickWinner);
        } else if (whistGame.getGameStage() == StageType.DUEL) {
            // DUEL stage logic
            handleDuelStageTrickCompletion(trickWinner);
        }
    }
    
    /**
     * Handles trick completion for DRAFT stage
     * @param trickWinner The player who won the trick
     */
    private void handleDraftStageTrickCompletion(Player trickWinner) {
        // Animate trick cards moving to discard pile after a delay
        Timeline discardDelay = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                // Clear trick area visually
                animateCardsToDiscard();
                
                // Discard cards in game logic
                whistGame.completeTrick();
                
                // In DRAFT stage, winner gets to take a prize card
                if (!whistGame.getDraw().getCards().isEmpty()) {
                    // Get the top card from the draw pile
                    Card prizeCard = whistGame.getDraw().getTopCard();
                    
                    // Reveal the card face up
                    if (prizeCard.isFaceDown()) {
                        prizeCard.flip();
                    }
                    
                    // Create a visual representation of the prize card
                    StackPane prizeCardPane = renderCard(prizeCard);
                    
                    // Show animation of prize card going to winner's hand
                    animatePrizeCardToWinner(prizeCardPane, trickWinner);
                    
                    // Add to winner's hand in game logic
                    whistGame.takeCard(whistGame.getDraw(), prizeCard, trickWinner);
                    
                    // Add move history entry
                    addMoveHistoryEntry(trickWinner.getUsername() + " drew " + prizeCard.toString() + " as a prize");
                    
                    // Check if we've completed all tricks or draw pile is empty
                    if (currentTrickNumber >= 13 || whistGame.getDraw().getCards().isEmpty()) {
                        // Show summary of tricks
                        String summaryMessage = "DRAFT stage completed. Tricks won: " + 
                                              whistGame.getPlayer1().getUsername() + ": " + player1TricksWon + 
                                              ", " + whistGame.getPlayer2().getUsername() + ": " + player2TricksWon;
                        addMoveHistoryEntry(summaryMessage);
                        
                        // Progress to DUEL stage after a delay
                        Timeline stageDelay = new Timeline(
                            new KeyFrame(Duration.seconds(2), e2 -> {
                                progressToNextStage();
                            })
                        );
                        stageDelay.play();
                    } else {
                        // Update UI and continue with next trick
                        updatePlayerHandDisplay();
                        
                        // Clear trick area for next trick
                        trickArea.getChildren().clear();
                        
                        // Update status to show current trick number
                        statusLabel.setText(trickWinner.getUsername() + "'s turn to play - Trick " + (currentTrickNumber + 1) + " of 13");
                        currentPlayerLabel.setText(trickWinner.getUsername());
                        
                        // Make appropriate cards playable for the next trick
                        resetPlayableCards();
                    }
                }
            })
        );
        discardDelay.play();
    }
    
    /**
     * Animates a prize card moving to the winner's hand
     */
    private void animatePrizeCardToWinner(StackPane prizeCardPane, Player winner) {
        // Add the prize card to the animation container
        animationContainer.getChildren().add(prizeCardPane);
        
        // Position in the center initially (where the draw pile would be)
        double centerX = animationContainer.getWidth() / 2;
        double centerY = animationContainer.getHeight() / 2;
        prizeCardPane.setLayoutX(centerX - prizeCardPane.getPrefWidth() / 2);
        prizeCardPane.setLayoutY(centerY - prizeCardPane.getPrefHeight() / 2);
        
        // Determine target position based on which player won
        double targetY = winner == whistGame.getPlayer1() ? 
                       playerHandArea.localToScene(playerHandArea.getBoundsInLocal()).getMinY() : 
                       opponentHandArea.localToScene(opponentHandArea.getBoundsInLocal()).getMinY();
        
        // Create animation to move card to winner's hand
        TranslateTransition moveToHand = new TranslateTransition(Duration.millis(800), prizeCardPane);
        moveToHand.setToY(targetY - centerY);
        
        // Add rotation for visual flair
        RotateTransition rotate = new RotateTransition(Duration.millis(800), prizeCardPane);
        rotate.setByAngle(360);
        rotate.setInterpolator(Interpolator.EASE_BOTH);
        
        // Combine animations
        ParallelTransition prizeAnimation = new ParallelTransition(prizeCardPane, moveToHand, rotate);
        
        // Remove card from animation container when done
        prizeAnimation.setOnFinished(e -> {
            animationContainer.getChildren().remove(prizeCardPane);
        });
        
        // Play the animation
        prizeAnimation.play();
    }
    
    /**
     * Animates cards in the trick area moving to the discard pile
     */
    private void animateCardsToDiscard() {
        // Get all cards in the trick area
        List<Node> trickCards = new ArrayList<>(trickArea.getChildren());
        
        // Calculate discard pile position (assuming it's offscreen)
        double discardX = 500;
        double discardY = 300;
        
        // Create a sequential animation to stagger card movements
        SequentialTransition staggeredAnimation = new SequentialTransition();
        
        for (int i = 0; i < trickCards.size(); i++) {
            Node cardNode = trickCards.get(i);
            if (cardNode instanceof StackPane cardPane) {
                // Create movement animation
                Path path = new Path();
                
                // Get start position
                double startX = cardPane.getLayoutX();
                double startY = cardPane.getLayoutY();
                
                // Create curved path with a control point
                MoveTo moveTo = new MoveTo(0, 0);
                
                // Random curve height for natural movement
                double curveHeight = -100 - (Math.random() * 50);
                
                // Create a quadratic curve for more natural movement
                QuadCurveTo curve = new QuadCurveTo(
                    (discardX - startX) / 2, // Control point X
                    curveHeight,             // Control point Y (above the path)
                    discardX - startX,       // End X
                    discardY - startY        // End Y
                );
                
                path.getElements().add(moveTo);
                path.getElements().add(curve);
                
                // Create path transition
                PathTransition pathTransition = new PathTransition(Duration.millis(600), path, cardPane);
                pathTransition.setInterpolator(Interpolator.EASE_OUT);
                
                // Add rotation for visual interest
                RotateTransition rotateTransition = new RotateTransition(
                    Duration.millis(600),
                    cardPane
                );
                rotateTransition.setByAngle(
                    (Math.random() > 0.5 ? 1 : -1) * (180 + Math.random() * 180)
                );
                rotateTransition.setInterpolator(Interpolator.EASE_OUT);
                
                // Add scaling for visual interest
                ScaleTransition scaleTransition = new ScaleTransition(
                    Duration.millis(600),
                    cardPane
                );
                scaleTransition.setToX(0.7);
                scaleTransition.setToY(0.7);
                
                // Fade out
                FadeTransition fadeTransition = new FadeTransition(
                    Duration.millis(400),
                    cardPane
                );
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.setDelay(Duration.millis(200 + (i * 100)));
                
                // Create combined animation for this card
                ParallelTransition cardAnimation = new ParallelTransition(
                    cardPane,
                    pathTransition,
                    rotateTransition,
                    scaleTransition,
                    fadeTransition
                );
                
                // Set up completion action
                final int index = i;
                cardAnimation.setOnFinished(e -> {
                    // Remove card from trick area
                    trickArea.getChildren().remove(cardPane);
                    
                    // If this was the last card, update game state
                    if (index == trickCards.size() - 1) {
                        // Trigger any post-discard updates
                        resetPlayableCards();
                    }
                });
                
                // Add slight delay between cards
                PauseTransition delay = new PauseTransition(Duration.millis(i * 100));
                
                // Add to sequential animation
                SequentialTransition cardSequence = new SequentialTransition(delay, cardAnimation);
                staggeredAnimation.getChildren().add(cardSequence);
            }
        }
        
        // Play the animation
        staggeredAnimation.play();
    }
    
    /**
     * Resets playable cards for the next trick
     */
    private void resetPlayableCards() {
        // Clear any existing click handlers
        for (StackPane cardPane : player1Cards) {
            makeIntoInert(cardPane);
        }
        for (StackPane cardPane : player2Cards) {
            makeIntoInert(cardPane);
        }
        
        // Make current player's cards playable
        if (whistGame.getTurnHolder() == whistGame.getPlayer1()) {
            for (StackPane cardPane : player1Cards) {
                makeIntoPlayable(cardPane);
            }
        } else {
            for (StackPane cardPane : player2Cards) {
                makeIntoPlayable(cardPane);
            }
        }
        
        // Start timer for the next player's turn
        startGameTimer(15, () -> {
            autoPlayCard();
        });
    }
    
    /**
     * Prepares the UI and game logic for the DUEL stage
     */
    private void prepareDuelStage() {
        // DEBUG: Shows stage
        System.out.println("-=Dueling Stage=-\n");
        
        // Reset trick counters for the DUEL stage
        currentTrickNumber = 0;
        player1TricksWon = 0;
        player2TricksWon = 0;
        
        // Add move history entry
        addMoveHistoryEntry("Duel stage started. Players will take turns playing cards to win tricks and score points.");
        
        // Make sure hand areas are visible
        showHandAreas();
        
        // Make sure trick area is visible and clear
        trickArea.getChildren().clear();
        trickArea.setVisible(true);
        
        // Set up player hands if needed
        whistGame.showHand(whistGame.getPlayer1());
        whistGame.showHand(whistGame.getPlayer2());
        
        // Update hand displays
        updatePlayerHandDisplay();
        
        // Update UI to reflect the current stage
        updateUI();
        
        // Show whose turn it is - first player to play in DUEL is non-dealer
        Player firstPlayer = whistGame.getDealer() == whistGame.getPlayer1() ? 
                            whistGame.getPlayer2() : whistGame.getPlayer1();
        whistGame.setTurnHolder(firstPlayer);
        
        // Update labels
        currentPlayerLabel.setText(firstPlayer.getUsername());
        statusLabel.setText(firstPlayer.getUsername() + "'s turn to play - Trick " + (currentTrickNumber + 1) + " of 13");
        
        // Make first player's cards playable
        resetPlayableCards();
        
        // Start timer for the first player's turn
        startGameTimer(15, () -> {
            // Auto-play a card if timer expires
            autoPlayCard();
        });
    }
    
    /**
     * Ends the current round and checks for game completion
     */
    private void endRound() {
        // Stop any running timer
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Get the current round number
        int currentRound = whistGame.getRound();
        
        // Add move history entry
        addMoveHistoryEntry("Round " + currentRound + " completed.");
        
        // Check if any player has reached 6 points
        if (checkForGameEnd()) {
            // Game is over
            handleGameEnd();
        } else {
            // Show round results before starting a new round
            showRoundResults(currentRound);
        }
    }
    
    /**
     * Shows the round results overlay
     * @param roundNumber The round that was just completed
     */
    private void showRoundResults(int roundNumber) {
        // Set round result title and message
        roundResultTitle.setText("Round " + roundNumber + " Complete");
        
        // Create detailed round results message
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append(whistGame.getPlayer1().getUsername())
                  .append(" won ").append(player1TricksWon).append(" tricks (")
                  .append(player1TricksWon - 6).append(" points)\n\n")
                  .append(whistGame.getPlayer2().getUsername())
                  .append(" won ").append(player2TricksWon).append(" tricks (")
                  .append(player2TricksWon - 6).append(" points)\n\n")
                  .append("Current scores: ")
                  .append(whistGame.getPlayer1().getUsername()).append(" ")
                  .append(whistGame.getPlayer1().getScore()).append(" - ")
                  .append(whistGame.getPlayer2().getUsername()).append(" ")
                  .append(whistGame.getPlayer2().getScore());
        
        roundResultMessage.setText(resultMessage.toString());
        
        // Create a continue button
        Button continueButton = new Button("Continue to Next Round");
        continueButton.getStyleClass().add("result-button");
        
        // Add button to the overlay
        VBox content = (VBox) roundResultOverlay.lookup(".result-content");
        if (content != null && !content.getChildren().contains(continueButton)) {
            content.getChildren().add(continueButton);
        }
        
        // Set up button action
        continueButton.setOnAction(e -> {
            // Hide overlay
            roundResultOverlay.setVisible(false);
            
            // Start new round
            prepareNextRound();
        });
        
        // Show overlay with animation
        roundResultOverlay.setOpacity(0);
        roundResultOverlay.setVisible(true);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), roundResultOverlay);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
    
    /**
     * Prepares and starts the next round
     */
    private void prepareNextRound() {
        // Increase round number
        whistGame.setRound(whistGame.getRound() + 1);
        
        // Set stage back to DEAL
        whistGame.setGameStage(StageType.DEAL);
        
        // Create a new deck
        whistGame.resetDeck();
        
        // Reset draw and discard piles
        whistGame.setDraw(new CardPile());
        whistGame.setDiscard(new CardPile());
        
        // Clear players' hands and reset trick counter
        whistGame.getPlayer1().setHand(new ArrayList<>());
        whistGame.getPlayer2().setHand(new ArrayList<>());
        currentTrickNumber = 0;
        player1TricksWon = 0;
        player2TricksWon = 0;
        
        // Reset trump suit
        whistGame.setTrump(null);
        
        // Reset shuffle count
        shuffleCount = 0;
        
        // Reset the game board visually
        resetGameBoard();
        
        // Add move history entry for the new round with a separator
        addMoveHistoryEntry("----- Round " + whistGame.getRound() + " -----");
        
        // Reset dealer for alternating rounds
        // If Player 1 was dealer last round, Player 2 becomes dealer this round and vice versa
        if (whistGame.getDealer() == whistGame.getPlayer1()) {
            whistGame.setDealer(whistGame.getPlayer2());
        } else {
            whistGame.setDealer(whistGame.getPlayer1());
        }
        
        // Update dealer display
        if (dealerTitle != null) {
            dealerTitle.setText("DEALER: " + whistGame.getDealer().getUsername());
        }
        
        // Reset turn holder to non-dealer to start the round
        Player nonDealer = (whistGame.getDealer() == whistGame.getPlayer1()) ? 
                         whistGame.getPlayer2() : whistGame.getPlayer1();
        whistGame.setTurnHolder(nonDealer);
        
        // Update player status indicators to show the new dealer
        updatePlayerInfo();
        
        // Prepare for the next round by starting with dealer selection
        prepareGameStage(StageType.DEAL);
    }
    
    /**
     * Sets up the dealer selection phase of the DEAL stage
     */
    private void setupDealerSelection() {
        // For rounds after the first, we already have a dealer assigned
        int currentRound = whistGame.getRound();
        if (currentRound > 1 && whistGame.getDealer() != null) {
            // Skip dealer selection and move directly to shuffling phase
            addMoveHistoryEntry("Round " + currentRound + ": " + whistGame.getDealer().getUsername() + " is the dealer");
            showShuffleOverlay();
            return;
        }
        
        // First round - need to select a dealer
        // Add move history entry
        addMoveHistoryEntry("Dealer selection phase started");
        
        // Hide the dealer overlay to show the deck for selection
        dealerOverlay.setVisible(false);
        
        // Show hand areas to prepare for card selection
        showHandAreas();
        
        // Track which player has selected a card
        final boolean[] player1Selected = {false};
        final boolean[] player2Selected = {false};
        final Card[] selectedCards = {null, null};
        final StackPane[] selectedCardDisplays = {null, null};
        
        // Shuffles the Deck a bit so Players' selections are random
        whistGame.getDeck().overheadShuffle();

        // Renders the whole face-down deck so that Players can select a card from it
        deckDisplay = renderWholePile(whistGame.getDeck());
        
        // Makes every Card StackPane able to be picked by the Players
        for (StackPane currentPane : deckDisplay) {
            // The Card associated with this StackPane
            Card associatedCard = (Card) currentPane.getProperties().get("card");
            
            // Set up click handler
            currentPane.setOnMouseClicked(event -> {
                // Determine which player clicked based on selection state
                if (!player1Selected[0]) {
                    // Player 1 selection
                    player1Selected[0] = true;
                    selectedCards[0] = associatedCard;
                    selectedCardDisplays[0] = currentPane;
                    
                    // Deal the card to player 1 temporarily
                    whistGame.dealCard(whistGame.getDeck(), associatedCard, whistGame.getPlayer1());
                    
                    // Add move history entry
                    addMoveHistoryEntry("Player 1 selected a card for dealer determination");
                    
                    // Make the card inert after selection
                    makeIntoInert(currentPane);
                    
                } else if (!player2Selected[0]) {
                    // Player 2 selection
                    player2Selected[0] = true;
                    selectedCards[1] = associatedCard;
                    selectedCardDisplays[1] = currentPane;
                    
                    // Deal the card to player 2 temporarily
                    whistGame.dealCard(whistGame.getDeck(), associatedCard, whistGame.getPlayer2());
                    
                    // Add move history entry
                    addMoveHistoryEntry("Player 2 selected a card for dealer determination");
                    
                    // Make the card inert after selection
                    makeIntoInert(currentPane);
                    
                    // Both players have selected, proceed to comparison
                    completeDealerSelection(selectedCards[0], selectedCards[1], 
                                          selectedCardDisplays[0], selectedCardDisplays[1]);
                }
            });
            
            // Indicates that the Card is Clickable to Players with a visible glow
            makeGlow(currentPane);
        }
        
        // Start a timer for card selection
        startSelectionTimer(15, () -> {
            // If time runs out without selections, auto-select cards
            if (!player1Selected[0] || !player2Selected[0]) {
                addMoveHistoryEntry("Time ran out! Auto-selecting cards for dealer determination");
                
                // Reset selection state to force progression
                player1Selected[0] = true;
                player2Selected[0] = true;
                
                // Get two random cards
                if (selectedCards[0] == null) {
                    selectedCards[0] = whistGame.getDeck().getCards().get(0);
                    selectedCardDisplays[0] = deckDisplay.get(0);
                    whistGame.dealCard(whistGame.getDeck(), selectedCards[0], whistGame.getPlayer1());
                }
                
                if (selectedCards[1] == null) {
                    selectedCards[1] = whistGame.getDeck().getCards().get(0);
                    selectedCardDisplays[1] = deckDisplay.get(0);
                    whistGame.dealCard(whistGame.getDeck(), selectedCards[1], whistGame.getPlayer2());
                }
                
                // Proceed to comparison
                completeDealerSelection(selectedCards[0], selectedCards[1], 
                                      selectedCardDisplays[0], selectedCardDisplays[1]);
            }
        });
    }
    
    /**
     * Starts a timer for the given duration and executes the provided action when time runs out
     * @param seconds The duration in seconds
     * @param timeoutAction The action to execute when time runs out
     */
    private void startSelectionTimer(int seconds, Runnable timeoutAction) {
        // Set time remaining
        timeRemaining = seconds;
        
        // Update timer display
        updateTimerDisplay();
        
        // Create and start a new timer
        gameTimer = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                // Decrement time
                timeRemaining--;
                
                // Update display
                updateTimerDisplay();
                
                // Check if time is up
                if (timeRemaining <= 0) {
                    // Stop timer
                    gameTimer.stop();
                    
                    // Execute timeout action
                    timeoutAction.run();
                }
            })
        );
        
        // Set cycle count to indefinite and start
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }

    /**
     * Completes the dealer selection process after both players have selected cards
     * @param player1Card Card selected by first player
     * @param player2Card Card selected by second player
     * @param player1CardPane StackPane of first player's card
     * @param player2CardPane StackPane of second player's card
     */
    private void completeDealerSelection(Card player1Card, Card player2Card, 
                                        StackPane player1CardPane, StackPane player2CardPane) {
        // Stop any existing timer
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Reveal cards by flipping them face up if they're face down
        if (player1Card.isFaceDown()) {
            player1Card.flip();
            // Update the card image in UI
            updateCardImage(player1CardPane, player1Card);
        }
        
        if (player2Card.isFaceDown()) {
            player2Card.flip();
            // Update the card image in UI
            updateCardImage(player2CardPane, player2Card);
        }
        
        // Get players
        Player player1 = whistGame.getPlayers().getFirst();
        Player player2 = whistGame.getPlayers().getLast();
        
        // Add move history entries
        addMoveHistoryEntry(player1.getUsername() + " drew " + player1Card.toString());
        addMoveHistoryEntry(player2.getUsername() + " drew " + player2Card.toString());
        
        // Compare cards using whistGame.compareCards()
        Card winningCard = whistGame.compareCards(player1Card, player2Card);
        Player dealer;
        String resultText;
        
        // Handle win or tie
        if (winningCard == null) {
            // It's a tie - cards have the same rank
            addMoveHistoryEntry("Tie! Cards have the same rank. Comparing suits...");
            
            // Break tie by comparing suits (Spades > Hearts > Diamonds > Clubs)
            if (player1Card.getSuit().ordinal() > player2Card.getSuit().ordinal()) {
                dealer = player1;
                resultText = player1.getUsername() + " becomes the dealer with " + player1Card.toString() + " (tie broken by suit)";
            } else if (player2Card.getSuit().ordinal() > player1Card.getSuit().ordinal()) {
                dealer = player2;
                resultText = player2.getUsername() + " becomes the dealer with " + player2Card.toString() + " (tie broken by suit)";
            } else {
                // Complete tie (same rank and suit) - very rare but handle it
                addMoveHistoryEntry("Complete tie! Same rank AND suit. Re-selecting...");
                
                // Return cards to deck and restart dealer selection after a delay
                Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(2), e -> {
                        returnCardsToDeck(player1Card, player2Card);
                        setupDealerSelectionUI();
                    })
                );
                timeline.play();
                
                // Show message about re-selection
                showAlert(AlertType.INFORMATION, "Complete Tie", 
                          "Both players selected the same card! Restarting dealer selection.");
                return;
            }
        } else if (winningCard == player1Card) {
            dealer = player1;
            resultText = player1.getUsername() + " becomes the dealer with " + player1Card.toString();
        } else {
            dealer = player2;
            resultText = player2.getUsername() + " becomes the dealer with " + player2Card.toString();
        }
        
        // Set the dealer in the game
        whistGame.setDealer(dealer);
        // Set turn holder (non-dealer goes first)
        whistGame.setTurnHolder(dealer == player1 ? player2 : player1);
        
        // Add result to move history
        addMoveHistoryEntry(resultText);
        
        // Apply visual indication to winning card
        StackPane winningCardPane = (dealer == player1) ? player1CardPane : player2CardPane;
        StackPane losingCardPane = (dealer == player1) ? player2CardPane : player1CardPane;
        
        // Show gold glow on winning card, dim the losing card
        makeGlow(winningCardPane);
        losingCardPane.setOpacity(0.7);
        
        // Show result in alert
        showAlert(AlertType.INFORMATION, "Dealer Selected", resultText);
        
        // Animate results
        Timeline timeline = new Timeline();
        
        // Highlight winning card
        KeyFrame highlightFrame = new KeyFrame(Duration.millis(500), 
            new KeyValue(winningCardPane.effectProperty(), new DropShadow(20, Color.GOLD)));
        
        // After highlighting, transition to the next phase
        KeyFrame transitionFrame = new KeyFrame(Duration.millis(2000), e -> {
            // Return cards to the deck
            returnCardsToDeck(player1Card, player2Card);
            
            // Clean up display
            for (StackPane cardPane : deckDisplay) {
                animationContainer.getChildren().remove(cardPane);
            }
            deckDisplay.clear();
            
            // Transition to the next part of the game (DEAL stage)
            whistGame.setGameStage(StageType.DEAL);
            updateUI();
            
            // Add move history entry
            addMoveHistoryEntry("Dealer selection complete. Moving to dealing phase.");
            
            // Show shuffle overlay for next phase
            showShuffleOverlay();
        });
        
        timeline.getKeyFrames().addAll(highlightFrame, transitionFrame);
        timeline.play();
    }
    
    /**
     * Updates a card's image in the UI after it's been flipped
     * @param cardPane The StackPane containing the card
     * @param card The card with updated state
     */
    private void updateCardImage(StackPane cardPane, Card card) {
        // Get the image view from the card pane
        ImageView cardImageView = (ImageView) cardPane.getChildren().get(0);
        
        // Update image using the centralized method
        String imagePath = getCardImagePath(card);
        
        try {
            cardImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
        } catch (Exception e) {
            try {
                cardImageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/unknown.png"))));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Update style classes
        cardPane.getStyleClass().removeAll("hearts", "diamonds", "spades", "clubs", "card-back");
        cardPane.getStyleClass().removeAll("rank-1", "rank-2", "rank-3", "rank-4", "rank-5",
                               "rank-6", "rank-7", "rank-8", "rank-9", "rank-10",
                               "rank-11", "rank-12", "rank-13");
        
        if (card.isFaceDown()) {
            cardPane.getStyleClass().add("card-back");
        } else {
            cardPane.getStyleClass().add(card.getSuit().toString().toLowerCase());
            cardPane.getStyleClass().add("rank-" + card.getRank());
        }
    }
    
    /**
     * Sets up the dealer selection phase with a deck display and timer
     */
    private void setupDealerSelectionUI() {
        // Add move history entry
        addMoveHistoryEntry("Dealer selection phase started");
        
        // Hide the dealer overlay to show the deck for selection
        dealerOverlay.setVisible(false);
        
        // Show hand areas to prepare for card selection
        showHandAreas();
        
        // Clear animation container first to prevent stacking
        animationContainer.getChildren().clear();
        
        // Shuffles the Deck a bit so Players' selections are random
        whistGame.getDeck().overheadShuffle();

        // Renders the whole face-down deck so that Players can select a card from it
        deckDisplay = renderWholePile(whistGame.getDeck());
        
        // Add cards to the animation container
        for (StackPane cardPane : deckDisplay) {
            animationContainer.getChildren().add(cardPane);
            
            // Make each card selectable
            makeIntoSelectable(cardPane);
        }
        
        // Update player status
        currentPlayerLabel.setText(whistGame.getCurrentTurn().getUsername());
        statusLabel.setText("Select a card to determine the dealer");
        
        // Start a 10-second timer for selection
        startSelectionTimer(10, () -> {
            // If time runs out, select random cards for both players
            statusLabel.setText("Time's up! Selecting random cards...");
            
            // Simulate card selection for both players
            List<Card> allCards = whistGame.getDeck().getCards();
            if (allCards.size() >= 2) {
                Card randomCard1 = allCards.get(random.nextInt(allCards.size()));
                allCards.remove(randomCard1);
                Card randomCard2 = allCards.get(random.nextInt(allCards.size()));
                
                // Get the corresponding StackPanes
                StackPane cardPane1 = null;
                StackPane cardPane2 = null;
                
                for (StackPane cardPane : deckDisplay) {
                    Card card = (Card) cardPane.getProperties().get("card");
                    if (card == randomCard1) {
                        cardPane1 = cardPane;
                    } else if (card == randomCard2) {
                        cardPane2 = cardPane;
                    }
                }
                
                if (cardPane1 != null && cardPane2 != null) {
                    // Complete dealer selection with the random cards
                    completeDealerSelection(randomCard1, randomCard2, cardPane1, cardPane2);
                }
            }
        });
    }
    
    /**
     * Returns cards from players back to the deck
     * 
     * @param player1Card Card from player 1 to return
     * @param player2Card Card from player 2 to return
     */
    private void returnCardsToDeck(Card player1Card, Card player2Card) {
        // Remove cards from player hands
        whistGame.getPlayer1().getHand().remove(player1Card);
        whistGame.getPlayer2().getHand().remove(player2Card);
        
        // Add the cards back to the deck
        whistGame.getDeck().addCard(player1Card);
        whistGame.getDeck().addCard(player2Card);
    }
    
    /**
     * Animates a card when it's selected
     * @param cardPane The StackPane containing the card to animate
     */
    private void animateCardSelection(StackPane cardPane) {
        // Store original position
        double origX = cardPane.getTranslateX();
        double origY = cardPane.getTranslateY();
        
        // Get current player to determine animation direction
        Player currentPlayer = whistGame.getCurrentTurn();
        double targetY = (currentPlayer == whistGame.getPlayers().getFirst()) ? -100 : 100;
        
        // Create animation
        Timeline timeline = new Timeline();
        
        // Scale up
        KeyFrame scaleUp = new KeyFrame(Duration.millis(150), 
            new KeyValue(cardPane.scaleXProperty(), 1.2),
            new KeyValue(cardPane.scaleYProperty(), 1.2));
        
        // Move toward player
        KeyFrame moveToPlayer = new KeyFrame(Duration.millis(300),
            new KeyValue(cardPane.translateYProperty(), targetY));
        
        // Add glow effect
        DropShadow glow = new DropShadow(15, Color.GOLD);
        KeyFrame addGlow = new KeyFrame(Duration.millis(400), 
            new KeyValue(cardPane.effectProperty(), glow));
        
        timeline.getKeyFrames().addAll(scaleUp, moveToPlayer, addGlow);
        timeline.play();
    }

    /**
     * Checks if any player has reached the winning score
     * @return true if a player has won, false otherwise
     */
    private boolean checkForGameEnd() {
        for (Player currentPlayer : whistGame.getPlayers()) {
            if (currentPlayer.getScore() >= 6) {
                // Set the winner in the game
                whistGame.setWinner(currentPlayer);
                return true;
            }
        }
        return false;
    }
    

    /**
     * Shows the start game overlay with a start game button
     */
    private void showStartGameOverlay() {
        // Check if animationContainer exists
        if (animationContainer == null) {
            System.out.println("Warning: animationContainer is null, cannot show start game overlay");
            return; // Exit early if animationContainer is null
        }
        
        // Hide other overlays
        dealerOverlay.setVisible(false);
        if (roundResultOverlay != null) roundResultOverlay.setVisible(false);
        if (gameOverOverlay != null) gameOverOverlay.setVisible(false);
        
        // Create overlay container
        StackPane startGameOverlay = new StackPane();
        startGameOverlay.getStyleClass().add("start-game-overlay");
        startGameOverlay.setPrefWidth(400);
        startGameOverlay.setPrefHeight(300);
        
        // Create content
        VBox content = new VBox(20);
        content.setAlignment(javafx.geometry.Pos.CENTER);
        content.setPadding(new Insets(30));
        
        // Title
        Label title = new Label("WHIST");
        title.getStyleClass().add("dealer-title");
        title.setStyle("-fx-font-size: 36px;");
        
        // Description
        Label description = new Label("A classic trick-taking card game");
        description.getStyleClass().add("shuffle-instruction");
        
        // Use the existing FXML startGameButton instead of creating a new button
        if (startGameButton == null) {
            // Create a new button only if the FXML button doesn't exist
            startGameButton = new Button("Start Game");
        }
        
        // Update button styling
        startGameButton.getStyleClass().add("start-game-button");
        startGameButton.setPrefWidth(200);
        startGameButton.setPrefHeight(50);
        
        // Make sure the click listener works - this is important and wasn't set up correctly
        startGameButton.setOnAction(e -> {
            System.out.println("Start button clicked");
            onStartGameClicked();
        });
        
        // Add elements to content
        content.getChildren().addAll(title, description, startGameButton);
        startGameOverlay.getChildren().add(content);
        
        // Add to animation container
        animationContainer.getChildren().clear();
        animationContainer.getChildren().add(startGameOverlay);
        
        // Center properly in the animation container 
        // Use JavaFX Platform.runLater to ensure positioning happens after layout is complete
        javafx.application.Platform.runLater(() -> {
            double containerWidth = animationContainer.getWidth();
            double containerHeight = animationContainer.getHeight();
            
            if (containerWidth > 0 && containerHeight > 0) {
                startGameOverlay.setLayoutX((containerWidth - startGameOverlay.getPrefWidth()) / 2);
                startGameOverlay.setLayoutY((containerHeight - startGameOverlay.getPrefHeight()) / 2);
            } else {
                // Fallback position if container dimensions not available
                startGameOverlay.setLayoutX(400);
                startGameOverlay.setLayoutY(250);
            }
            
            // Ensure visibility
            startGameOverlay.setVisible(true);
            startGameOverlay.toFront();
            
            System.out.println("Start game overlay positioned at: " + 
                              startGameOverlay.getLayoutX() + ", " + 
                              startGameOverlay.getLayoutY());
        });
    }
    
    /**
     * Shows the shuffle overlay with shuffle options
     */
    private void showShuffleOverlay() {
        // Make the dealer overlay visible
        dealerOverlay.setVisible(true);
        
        // Set the title
        dealerTitle.setText("SHUFFLE DECK");
        
        // Set the instruction text
        shuffleInstructionLabel.setText("Please shuffle the deck at least " + REQUIRED_SHUFFLES + " times");
        
        // Reset the shuffle counter
        shuffleCount = 0;
        shuffleCounterLabel.setText("Shuffles: " + shuffleCount + "/" + REQUIRED_SHUFFLES);
        shuffleCounterLabel.setVisible(true);
        
        // Get the HBox containing the buttons
        HBox buttonBox = (HBox) dealerOverlay.lookup(".dealer-content > HBox");
        if (buttonBox != null) {
            // Clear existing buttons
            buttonBox.getChildren().clear();
            
            // Create shuffle buttons for different shuffle types
            Button riffleButton = new Button("Riffle Shuffle");
            riffleButton.getStyleClass().add("shuffle-button");
            riffleButton.setOnAction(e -> onShuffleClicked("riffle"));
            
            Button overheadButton = new Button("Overhead Shuffle");
            overheadButton.getStyleClass().add("shuffle-button");
            overheadButton.setOnAction(e -> onShuffleClicked("overhead"));
            
            Button cutButton = new Button("Cut Deck");
            cutButton.getStyleClass().add("shuffle-button");
            cutButton.setOnAction(e -> onShuffleClicked("cut"));
            
            // Create deal button (initially disabled)
            dealButton1 = new Button("Deal");
            dealButton1.getStyleClass().add("deal-button");
            dealButton1.setDisable(true);
            dealButton1.setOnAction(e -> onDealClicked());
            
            // Add buttons in a 2x2 grid
            GridPane buttonGrid = new GridPane();
            buttonGrid.setHgap(10);
            buttonGrid.setVgap(10);
            buttonGrid.add(riffleButton, 0, 0);
            buttonGrid.add(overheadButton, 1, 0);
            buttonGrid.add(cutButton, 0, 1);
            buttonGrid.add(dealButton1, 1, 1);
            
            // Add the grid to the button box
            buttonBox.getChildren().add(buttonGrid);
        }
    }

    /**
     * Sets up the dealing phase with automatic dealing animation
     */
    private void setupDealingPhase() {
        // Hide dealer overlay during dealing
        dealerOverlay.setVisible(false);
        
        // Clear animation container to prevent stacking
        animationContainer.getChildren().clear();
        
        // Show hand areas for dealing
        showHandAreas();
        
        // Set initial deal state - first card goes to non-dealer (whoever has the turn)
        dealToDealer = false;
        
        // Add move history entry
        addMoveHistoryEntry("Dealing cards to players");
        
        // Update status for dealing phase
        statusLabel.setText("Dealing cards to players...");
        currentPlayerLabel.setText("Dealer: " + (whistGame.getDealer() == whistGame.getPlayer1() ? 
                                    whistGame.getPlayer1().getUsername() : 
                                    whistGame.getPlayer2().getUsername()));
        
        // Render the deck for dealing
        deckDisplay = renderPile(whistGame.getDeck());
        
        // Position the deck in the center of the screen
        double centerX = animationContainer.getWidth() / 2;
        double centerY = animationContainer.getHeight() / 2;
        
        for (StackPane cardPane : deckDisplay) {
            // Add card to animation container
            animationContainer.getChildren().add(cardPane);
            
            // Position card in center
            cardPane.setLayoutX(centerX - cardPane.getPrefWidth() / 2);
            cardPane.setLayoutY(centerY - cardPane.getPrefHeight() / 2);
        }
        
        // Start dealing animation for the first card
        if (!deckDisplay.isEmpty()) {
            StackPane topCard = deckDisplay.get(0);
            // Call makeIntoDeal for top card (will trigger dealing animation)
            makeIntoDeal(topCard);
        }
        
        // Start timer for the dealing phase (10 seconds)
        startDealingTimer(10, () -> {
            // If timer runs out, auto-complete dealing
            completeDealing();
        });
    }

    /**
     * Starts a timer for the dealing phase
     * @param seconds Number of seconds for the timer
     * @param timeoutAction Action to perform when timer expires
     */
    private void startDealingTimer(int seconds, Runnable timeoutAction) {
        // Reset timer if it exists
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Create a new timer
        timeRemaining = seconds;
        
        // Update timer display
        updateTimerDisplay();
        
        // Create a new timeline for the timer
        gameTimer = new Timeline();
        gameTimer.setCycleCount(seconds);
        
        // Create the tick event - each second
        KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            
            // Update timer display
            updateTimerDisplay();
            
            // Check if timer has expired
            if (timeRemaining <= 0) {
                gameTimer.stop();
                if (timeoutAction != null) {
                    timeoutAction.run();
                }
            }
        });
        
        gameTimer.getKeyFrames().add(frame);
        gameTimer.play();
    }

    /**
     * Completes the dealing process automatically
     */
    private void completeDealing() {
        // Stop any running timer
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Add move history entry
        addMoveHistoryEntry("Dealing completed automatically");
        
        // Deal all remaining cards to players
        dealAllCards();
        
        // Proceed to next stage
        prepareForTrumpReveal();
    }

    /**
     * Automatically plays a card for the current player when their turn timer expires
     */
    private void autoPlayCard() {
        // Get the current player
        Player currentPlayer = whistGame.getTurnHolder();
        
        // Get list of valid cards
        List<Card> playableCards;
        if (!whistGame.getTrick().isEmpty()) {
            Card leadCard = whistGame.getTrick().getFirst();
            playableCards = whistGame.getPlayableCards(currentPlayer, leadCard);
        } else {
            playableCards = whistGame.getPlayableCards(currentPlayer);
        }
        
        // If no playable cards, show an error and return
        if (playableCards.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No playable cards found!");
            return;
        }
        
        // Select a random card from the playable ones
        Card cardToPlay = playableCards.get(random.nextInt(playableCards.size()));
        
        // Find the UI representation of the card
        StackPane cardPane = null;
        if (currentPlayer == whistGame.getPlayer1()) {
            for (StackPane pane : player1Cards) {
                Card card = (Card) pane.getProperties().get("card");
                if (card == cardToPlay) {
                    cardPane = pane;
                    break;
                }
            }
        } else {
            for (StackPane pane : player2Cards) {
                Card card = (Card) pane.getProperties().get("card");
                if (card == cardToPlay) {
                    cardPane = pane;
                    break;
                }
            }
        }
        
        if (cardPane == null) {
            // Could not find the UI representation, play the card without animation
            whistGame.playCard(cardToPlay);
            
            // Add move history entry
            addMoveHistoryEntry(currentPlayer.getUsername() + " auto-played " + cardToPlay.toString());
            
            // Switch turn to other player
            Player nextPlayer = currentPlayer == whistGame.getPlayer1() ? 
                whistGame.getPlayer2() : whistGame.getPlayer1();
            whistGame.setTurnHolder(nextPlayer);
            
            // Update UI
            updateUI();
            statusLabel.setText(nextPlayer.getUsername() + "'s turn to play");
            
            // If both players have played a card, determine the trick winner
            if (whistGame.getTrick().size() == 2) {
                // Delay to show the cards before resolving the trick
                Timeline trickResolution = new Timeline(
                    new KeyFrame(Duration.seconds(1.5), e -> resolveTrick())
                );
                trickResolution.play();
            }
        } else {
            // Found the UI representation, trigger its click handler to play it
            cardPane.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                    MouseButton.PRIMARY, 1, false, false, false, false,
                    false, false, false, false, false, false, null));
        }
    }

    /**
     * Animates cards in the trick area moving to the winner's spoils
     * 
     * @param winner The player who won the trick
     */
    private void animateCardsToSpoils(Player winner) {
        // Get all cards in the trick area
        List<Node> trickCards = new ArrayList<>(trickArea.getChildren());
        
        for (Node cardNode : trickCards) {
            if (cardNode instanceof StackPane cardPane) {
                // Create animation
                TranslateTransition spoilsTransition = new TranslateTransition(Duration.millis(600), cardPane);
                
                // Calculate target position based on which player won
                double targetX;
                double targetY;
                
                if (winner == whistGame.getPlayer1()) {
                    // Animate to bottom right for player 1
                    targetX = 400;
                    targetY = 200;
                } else {
                    // Animate to top right for player 2
                    targetX = 400;
                    targetY = -200;
                }
                
                spoilsTransition.setToX(targetX);
                spoilsTransition.setToY(targetY);
                
                // Add rotation and scaling for visual flair
                RotateTransition rotate = new RotateTransition(Duration.millis(600), cardPane);
                rotate.setByAngle(winner == whistGame.getPlayer1() ? 360 : -360);
                
                ScaleTransition scale = new ScaleTransition(Duration.millis(600), cardPane);
                scale.setToX(0.7);
                scale.setToY(0.7);
                
                // Create parallel animation for smoother effect
                ParallelTransition spoilsAnimation = new ParallelTransition(cardPane, 
                                                                           spoilsTransition, 
                                                                           rotate,
                                                                           scale);
                
                // Add a fade effect at the end
                FadeTransition fadeOut = new FadeTransition(Duration.millis(200), cardPane);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setDelay(Duration.millis(500));
                
                // Combine sequential animations
                SequentialTransition fullAnimation = new SequentialTransition(spoilsAnimation, fadeOut);
                
                // Remove from trick area when complete
                fullAnimation.setOnFinished(e -> trickArea.getChildren().remove(cardPane));
                
                // Play the animation
                fullAnimation.play();
            }
        }
    }
    
    /**
     * Handles trick completion for DUEL stage
     * @param trickWinner The player who won the trick
     */
    private void handleDuelStageTrickCompletion(Player trickWinner) {
        // Animate trick cards moving to winner's spoils after a delay
        Timeline spoilsDelay = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                // Get the cards from the trick
                List<Card> trickCards = new ArrayList<>(whistGame.getTrick());
                
                // Add the cards to the winner's spoils in game logic
                for (Card card : trickCards) {
                    trickWinner.addToSpoils(card);
                }
                
                // Visually animate cards moving to winner's side
                animateCardsToSpoils(trickWinner);
                
                // Discard cards from trick and players' hands
                whistGame.completeTrick();
                
                // Add move history entry about trick winner
                String trickMessage = trickWinner.getUsername() + " added the trick cards to their spoils";
                addMoveHistoryEntry(trickMessage);
                
                // Check if we've completed all 13 tricks
                if (currentTrickNumber >= 13 || whistGame.getPlayer1().getHand().isEmpty()) {
                    // Calculate scores for the round
                    int player1RoundScore = player1TricksWon - 6; // Tricks won minus 6
                    int player2RoundScore = player2TricksWon - 6; // Tricks won minus 6
                    
                    // Update player scores
                    whistGame.getPlayer1().addPoints(player1RoundScore);
                    whistGame.getPlayer2().addPoints(player2RoundScore);
                    
                    // Update UI to reflect scores
                    player1Score.setText("Score: " + whistGame.getPlayer1().getScore());
                    player2Score.setText("Score: " + whistGame.getPlayer2().getScore());
                    
                    // Show summary of round
                    String summaryMessage = "DUEL stage completed. " +
                                          whistGame.getPlayer1().getUsername() + " won " + player1TricksWon + 
                                          " tricks (" + player1RoundScore + " points), " +
                                          whistGame.getPlayer2().getUsername() + " won " + player2TricksWon + 
                                          " tricks (" + player2RoundScore + " points)";
                    addMoveHistoryEntry(summaryMessage);
                    
                    // End the round after a delay
                    Timeline roundEndDelay = new Timeline(
                        new KeyFrame(Duration.seconds(2), e2 -> {
                            endRound();
                        })
                    );
                    roundEndDelay.play();
                } else {
                    // Continue with the next trick
                    
                    // Clear trick area for next trick
                    trickArea.getChildren().clear();
                    
                    // Update status to show current trick number
                    statusLabel.setText(trickWinner.getUsername() + "'s turn to play - Trick " + (currentTrickNumber + 1) + " of 13");
                    currentPlayerLabel.setText(trickWinner.getUsername());
                    
                    // Make appropriate cards playable for the next trick
                    resetPlayableCards();
                    
                    // Start timer for the next turn
                    startGameTimer(15, () -> {
                        // Auto-play a card if timer expires
                        autoPlayCard();
                    });
                }
            })
        );
        spoilsDelay.play();
    }

    /**
     * Handles the end of the game, showing the game over overlay
     */
    private void handleGameEnd() {
        // Get the winning player
        Player winner = whistGame.getWinner();
        if (winner == null) {
            // No winner set, shouldn't happen but handle it gracefully
            showAlert(Alert.AlertType.ERROR, "Error", "Could not determine game winner!");
            return;
        }
        
        // Add move history entry about game end
        String gameEndMessage = "Game Over! " + winner.getUsername() + " wins with " + winner.getScore() + " points!";
        addMoveHistoryEntry(gameEndMessage);
        
        // Set game over message and title
        gameOverTitle.setText("Game Over");
        gameOverMessage.setText(winner.getUsername() + " won the game with " + winner.getScore() + " points!");
        
        // Add winning player highlight
        if (winner == whistGame.getPlayer1()) {
            gameOverTitle.setStyle("-fx-text-fill: -fx-player-x-color;");
        } else {
            gameOverTitle.setStyle("-fx-text-fill: -fx-player-o-color;");
        }
        
        // Show game over overlay with animation
        gameOverOverlay.setOpacity(0);
        gameOverOverlay.setVisible(true);
        
        // Set up return to lobby button action
        returnToLobbyButton.setOnAction(e -> navigateToGameLobby());
        
        // Animate overlay appearance
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), gameOverOverlay);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        
        // Play celebratory animation for winner
        playCelebrationAnimation(winner);
        
        // Stop the game
        gameInProgress = false;
    }
    
    /**
     * Plays a celebration animation for the winning player
     * @param winner The player who won the game
     */
    private void playCelebrationAnimation(Player winner) {
        // Clear animation container first
        animationContainer.getChildren().clear();
        
        // Determine color palette based on winner
        Color baseColor;
        List<Color> colorPalette = new ArrayList<>();
        
        if (winner == whistGame.getPlayer1()) {
            // Player 1 winning colors (blues and golds)
            baseColor = Color.web("#3B82F6");
            colorPalette.add(Color.web("#3B82F6")); // Blue
            colorPalette.add(Color.web("#2563EB")); // Darker blue
            colorPalette.add(Color.web("#FBBF24")); // Gold
            colorPalette.add(Color.web("#F59E0B")); // Amber
            colorPalette.add(Color.web("#FFFFFF")); // White
        } else {
            // Player 2 winning colors (reds and golds)
            baseColor = Color.web("#EF4444");
            colorPalette.add(Color.web("#EF4444")); // Red
            colorPalette.add(Color.web("#DC2626")); // Darker red
            colorPalette.add(Color.web("#FBBF24")); // Gold
            colorPalette.add(Color.web("#F59E0B")); // Amber
            colorPalette.add(Color.web("#FFFFFF")); // White
        }
        
        // Create several animation layers
        
        // 1. Confetti particles
        createConfettiAnimation(colorPalette, 150);
        
        // 2. Firework explosions
        createFireworkAnimation(colorPalette, 8);
        
        // 3. Trophy icon burst
        createTrophyIconAnimation(baseColor);
        
        // 4. Create winner text with particle effects
        createWinnerTextAnimation(winner.getUsername(), baseColor);
        
        // Play a winning sound
        // MediaPlayer winSound = new MediaPlayer(new Media(getClass().getResource("/sounds/win_celebration.mp3").toString()));
        // winSound.play();
    }
    
    /**
     * Creates confetti particle animation
     * @param colorPalette Colors to use for particles
     * @param particleCount Number of particles to create
     */
    private void createConfettiAnimation(List<Color> colorPalette, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
            // Create a random particle shape
            Node particle;
            double size = random.nextInt(15) + 5;
            
            if (random.nextDouble() < 0.7) {
                // Rectangle particle (70% chance)
                Rectangle confetti = new Rectangle(size, size);
                
                // Sometimes use elongated rectangles
                if (random.nextDouble() < 0.3) {
                    confetti.setWidth(size * 2);
                    confetti.setHeight(size / 2);
                }
                
                particle = confetti;
            } else {
                // Circle particle (30% chance)
                Circle circle = new Circle(size / 2);
                particle = circle;
            }
            
            // Set random color from palette
            Color color = colorPalette.get(random.nextInt(colorPalette.size()));
            if (particle instanceof Shape shape) {
                shape.setFill(color);
            }
            
            // Add subtle glow effect
            DropShadow glow = new DropShadow(8, color.brighter());
            particle.setEffect(glow);
            
            // Set initial position spread across top of screen
            particle.setTranslateX(random.nextInt((int) animationContainer.getWidth()));
            particle.setTranslateY(-50 - random.nextInt(200)); // Start above the visible area
            
            // Add to animation container
            animationContainer.getChildren().add(particle);
            
            // Create falling animation with physics
            TranslateTransition fall = new TranslateTransition(
                    Duration.millis(random.nextInt(3000) + 2000), particle);
            
            // End position varying across the screen
            fall.setToY(animationContainer.getHeight() + 100);
            
            // Horizontal drift
            double drift = (random.nextDouble() - 0.5) * 400;
            fall.setToX(particle.getTranslateX() + drift);
            
            // Rotation animation
            RotateTransition rotate = new RotateTransition(
                    Duration.millis(random.nextInt(3000) + 1000), particle);
            rotate.setByAngle(random.nextInt(720) - 360);
            rotate.setCycleCount(Animation.INDEFINITE);
            
            // Add subtle size pulsing
            ScaleTransition pulse = new ScaleTransition(
                    Duration.millis(random.nextInt(500) + 500), particle);
            pulse.setByX(0.2);
            pulse.setByY(0.2);
            pulse.setCycleCount(Animation.INDEFINITE);
            pulse.setAutoReverse(true);
            
            // Play combined animations
            ParallelTransition animation = new ParallelTransition(
                    particle, fall, rotate, pulse);
            
            // Remove particle when animation completes
            animation.setOnFinished(e -> 
                    animationContainer.getChildren().remove(particle));
            
            // Add slight delay for staggered effect
            PauseTransition delay = new PauseTransition(
                    Duration.millis(random.nextInt(1500)));
            delay.setOnFinished(e -> animation.play());
            delay.play();
        }
    }
    
    /**
     * Creates firework explosion animation
     * @param colorPalette Colors to use for fireworks
     * @param burstCount Number of firework bursts to create
     */
    private void createFireworkAnimation(List<Color> colorPalette, int burstCount) {
        for (int b = 0; b < burstCount; b++) {
            // Random position for this firework
            double burstX = random.nextDouble() * animationContainer.getWidth();
            double burstY = 100 + random.nextDouble() * (animationContainer.getHeight() / 2);
            Color burstColor = colorPalette.get(random.nextInt(colorPalette.size()));
            
            // Create a launch streamer effect
            Circle launchPoint = new Circle(3);
            launchPoint.setFill(burstColor);
            launchPoint.setTranslateX(burstX);
            launchPoint.setTranslateY(animationContainer.getHeight());
            
            // Add launch point to container
            animationContainer.getChildren().add(launchPoint);
            
            // Create upward launch animation
            TranslateTransition launch = new TranslateTransition(
                    Duration.millis(700 + random.nextInt(500)), launchPoint);
            launch.setToY(burstY);
            
            // Create trail effect during launch
            Timeline trailEffect = new Timeline();
            int trailFrames = 10;
            for (int i = 0; i < trailFrames; i++) {
                final int frameIndex = i;
                KeyFrame trailFrame = new KeyFrame(Duration.millis(i * 80), e -> {
                    Circle trailParticle = new Circle(2);
                    trailParticle.setFill(burstColor.deriveColor(0, 1, 1, 0.7));
                    trailParticle.setTranslateX(launchPoint.getTranslateX() + (random.nextDouble() - 0.5) * 5);
                    trailParticle.setTranslateY(launchPoint.getTranslateY() + (random.nextDouble() * 10));
                    
                    animationContainer.getChildren().add(trailParticle);
                    
                    FadeTransition fadeTrial = new FadeTransition(
                            Duration.millis(300), trailParticle);
                    fadeTrial.setToValue(0);
                    fadeTrial.setOnFinished(ev -> 
                            animationContainer.getChildren().remove(trailParticle));
                    fadeTrial.play();
                });
                trailEffect.getKeyFrames().add(trailFrame);
            }
            
            // Create the burst effect after launch completes
            launch.setOnFinished(e -> {
                // Remove the launch point
                animationContainer.getChildren().remove(launchPoint);
                
                // Create the burst particles
                int particleCount = 30 + random.nextInt(30);
                for (int i = 0; i < particleCount; i++) {
                    Circle particle = new Circle(2 + random.nextDouble() * 2);
                    
                    // Vary colors slightly within palette
                    Color particleColor = burstColor.interpolate(
                            colorPalette.get(random.nextInt(colorPalette.size())), 
                            random.nextDouble() * 0.3);
                    
                    particle.setFill(particleColor);
                    particle.setTranslateX(burstX);
                    particle.setTranslateY(burstY);
                    
                    // Add glow effect
                    DropShadow glow = new DropShadow(8, particleColor);
                    particle.setEffect(glow);
                    
                    animationContainer.getChildren().add(particle);
                    
                    // Calculate random direction
                    double angle = random.nextDouble() * 360;
                    double distance = 50 + random.nextDouble() * 150;
                    double endX = burstX + Math.cos(Math.toRadians(angle)) * distance;
                    double endY = burstY + Math.sin(Math.toRadians(angle)) * distance;
                    
                    // Create particle animation
                    TranslateTransition move = new TranslateTransition(
                            Duration.millis(500 + random.nextInt(500)), particle);
                    move.setToX(endX);
                    move.setToY(endY);
                    
                    // Add gravity effect
                    Timeline gravity = new Timeline(new KeyFrame(
                            Duration.millis(700),
                            new KeyValue(
                                    particle.translateYProperty(),
                                    endY + 50 + random.nextDouble() * 100,
                                    Interpolator.EASE_IN
                            )
                    ));
                    
                    // Fade out
                    FadeTransition fade = new FadeTransition(
                            Duration.millis(600), particle);
                    fade.setDelay(Duration.millis(200));
                    fade.setToValue(0);
            
            // Play animations
                    SequentialTransition sequence = new SequentialTransition(
                            new ParallelTransition(move, fade),
                            gravity
                    );
                    
                    sequence.setOnFinished(ev -> 
                            animationContainer.getChildren().remove(particle));
                    sequence.play();
                }
                
                // Add a flash effect at burst point
                Circle flash = new Circle(100);
                flash.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.7));
                flash.setTranslateX(burstX);
                flash.setTranslateY(burstY);
                animationContainer.getChildren().add(flash);
                
                FadeTransition flashFade = new FadeTransition(
                        Duration.millis(300), flash);
                flashFade.setFromValue(0.7);
                flashFade.setToValue(0);
                flashFade.setOnFinished(ev -> 
                        animationContainer.getChildren().remove(flash));
                flashFade.play();
            });
            
            // Add delay between fireworks
            PauseTransition burstDelay = new PauseTransition(
                    Duration.millis(random.nextInt(1500) + b * 300));
            burstDelay.setOnFinished(e -> {
                launch.play();
                trailEffect.play();
            });
            burstDelay.play();
        }
    }
    
    /**
     * Creates a trophy icon animation in the center of the screen
     * @param baseColor Base color for the trophy
     */
    private void createTrophyIconAnimation(Color baseColor) {
        // Create central trophy icon
        ImageView trophyIcon = new ImageView();
        try {
            trophyIcon.setImage(new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/images/trophy.png"))));
        } catch (Exception e) {
            // Fallback to a simple trophy shape if image not found
            Rectangle trophyBase = new Rectangle(40, 50);
            trophyBase.setFill(baseColor);
            trophyBase.setArcWidth(10);
            trophyBase.setArcHeight(10);
            trophyIcon = new ImageView();
        }
        
        trophyIcon.setFitWidth(100);
        trophyIcon.setFitHeight(100);
        trophyIcon.setPreserveRatio(true);
        
        // Position in center
        trophyIcon.setTranslateX((animationContainer.getWidth() / 2) - 50);
        trophyIcon.setTranslateY((animationContainer.getHeight() / 2) - 150);
        
        // Add a gold glow
        DropShadow trophyGlow = new DropShadow(20, Color.GOLD);
        trophyIcon.setEffect(trophyGlow);
        
        // Make initially invisible
        trophyIcon.setOpacity(0);
        trophyIcon.setScaleX(0.1);
        trophyIcon.setScaleY(0.1);
        
        animationContainer.getChildren().add(trophyIcon);
        
        // Create entrance animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), trophyIcon);
        fadeIn.setToValue(1.0);
        
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(800), trophyIcon);
        scaleUp.setToX(1.2);
        scaleUp.setToY(1.2);
        scaleUp.setInterpolator(Interpolator.EASE_OUT);
        
        ScaleTransition scaleBounce = new ScaleTransition(Duration.millis(300), trophyIcon);
        scaleBounce.setToX(1.0);
        scaleBounce.setToY(1.0);
        scaleBounce.setDelay(Duration.millis(800));
        
        // Add rotation for dramatic effect
        RotateTransition spin = new RotateTransition(Duration.millis(1000), trophyIcon);
        spin.setByAngle(360 * 2);
        spin.setInterpolator(Interpolator.EASE_OUT);
        
        // Play the animation with delay
        PauseTransition trophyDelay = new PauseTransition(Duration.millis(500));
        trophyDelay.setOnFinished(e -> {
            ParallelTransition parallel = new ParallelTransition(
                    fadeIn, scaleUp, spin);
            SequentialTransition sequence = new SequentialTransition(
                    parallel, scaleBounce);
            sequence.play();
        });
        trophyDelay.play();
    }
    
    /**
     * Creates animated winner text
     * @param winnerName Name of the winning player
     * @param baseColor Color to use for text
     */
    private void createWinnerTextAnimation(String winnerName, Color baseColor) {
        // Create winner text
        Text winnerText = new Text(winnerName + " WINS!");
        winnerText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        winnerText.setFill(baseColor);
        winnerText.setStroke(Color.WHITE);
        winnerText.setStrokeWidth(2);
        
        // Center text
        double textWidth = winnerText.getBoundsInLocal().getWidth();
        winnerText.setTranslateX((animationContainer.getWidth() / 2) - (textWidth / 2));
        winnerText.setTranslateY((animationContainer.getHeight() / 2) - 50);
        
        // Add glow effect
        DropShadow textGlow = new DropShadow(15, baseColor);
        winnerText.setEffect(textGlow);
        
        // Make initially invisible
        winnerText.setOpacity(0);
        winnerText.setScaleX(0.5);
        winnerText.setScaleY(0.5);
        
        animationContainer.getChildren().add(winnerText);
        
        // Create entrance animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), winnerText);
        fadeIn.setToValue(1.0);
        fadeIn.setDelay(Duration.millis(1000)); // Appear after trophy
        
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(800), winnerText);
        scaleUp.setToX(1.2);
        scaleUp.setToY(1.2);
        scaleUp.setDelay(Duration.millis(1000));
        
        ScaleTransition scaleBounce = new ScaleTransition(Duration.millis(300), winnerText);
        scaleBounce.setToX(1.0);
        scaleBounce.setToY(1.0);
        scaleBounce.setDelay(Duration.millis(1800));
        
        // Add pulsing effect
        Timeline pulsate = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(textGlow.radiusProperty(), 15)),
            new KeyFrame(Duration.millis(500),
                new KeyValue(textGlow.radiusProperty(), 25)),
            new KeyFrame(Duration.millis(1000),
                new KeyValue(textGlow.radiusProperty(), 15))
        );
        pulsate.setCycleCount(Animation.INDEFINITE);
        
        // Play animations
        ParallelTransition parallel = new ParallelTransition(
                fadeIn, scaleUp);
        SequentialTransition sequence = new SequentialTransition(
                parallel, scaleBounce);
        sequence.setOnFinished(e -> pulsate.play());
        sequence.play();
    }

    /**
     * Resets the game board visually for a new round
     */
    private void resetGameBoard() {
        // Clear all visual elements
        if (playerHandArea != null) {
            playerHandArea.getChildren().clear();
        }
        if (opponentHandArea != null) {
            opponentHandArea.getChildren().clear();
        }
        if (trickArea != null) {
            trickArea.getChildren().clear();
        }
        if (trumpCardDisplay != null) {
            trumpCardDisplay.getChildren().clear();
        }
        if (animationContainer != null) {
            animationContainer.getChildren().clear();
        }
        
        // Reset collection data structures
        player1Cards.clear();
        player2Cards.clear();
        deckDisplay.clear();
        drawPileDisplay.clear();
        discardPileDisplay.clear();
        
        // Hide any overlays that might be visible
        if (roundResultOverlay != null) {
            roundResultOverlay.setVisible(false);
        }
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }
        
        // Show appropriate areas
        showHandAreas();
        
        // Update UI labels
        statusLabel.setText("Starting round " + whistGame.getRound());
        gameStageLabel.setText(StageType.DEAL.getDisplayName());
        roundCounter.setText(String.valueOf(whistGame.getRound()));
        
        // Update player info displays
        updatePlayerInfo();
    }

    /**
     * Generates the appropriate image path for a card based on its properties
     * 
     * @param card The card to generate an image path for
     * @return The path to the card image
     */
    private String getCardImagePath(Card card) {
        String imagePath = "/images/whist images/";
        
        if (card.isFaceDown()) {
            imagePath += "CardBackside1.png";
        } else {
            // Add suit folder
            switch (card.getSuit()) {
                case HEARTS:
                    imagePath += "Hearts/";
                    break;
                case DIAMONDS:
                    imagePath += "Diamonds/";
                    break;
                case SPADES:
                    imagePath += "Spades/";
                    break;
                case CLUBS:
                    imagePath += "Clubs/";
                    break;
            }
            
            // Add rank to filename
            int rank = card.getRank();
            switch (rank) {
                case 1 -> imagePath += "ace_of_" + card.getSuit().toString().toLowerCase() + ".png";
                case 13 -> imagePath += "king_of_" + card.getSuit().toString().toLowerCase() + ".png";
                case 12 -> imagePath += "queen_of_" + card.getSuit().toString().toLowerCase() + ".png";
                case 11 -> imagePath += "jack_of_" + card.getSuit().toString().toLowerCase() + ".png";
                default -> imagePath += rank + "_of_" + card.getSuit().toString().toLowerCase() + ".png";
            }
        }
        
        return imagePath;
    }

    /**
     * Creates an ImageView for a card with proper error handling
     *
     * @param imagePath Path to the card image
     * @return ImageView with the card image
     */
    private ImageView createCardImageView(String imagePath) {
        ImageView cardImage = new ImageView();
        cardImage.setFitHeight(120);
        cardImage.setFitWidth(80);
        cardImage.setPreserveRatio(true);
        
        try {
            cardImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
        } catch (Exception e) {
            try {
                // Fallback to placeholder if image not found
                cardImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/unknown.png"))));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return cardImage;
    }

    /**
     * Sets up hover effects for cards in fan layout
     * 
     * @param cardPane The card to add effects to
     * @param index The index of the card in the layout
     */
    private void setupCardHoverEffects(StackPane cardPane, int index) {
        cardPane.setOnMouseEntered(e -> {
            // Lift card slightly
            cardPane.setTranslateY(cardPane.getTranslateY() - 20);
            
            // Add highlight effect
            DropShadow glow = new DropShadow(15, Color.GOLD);
            cardPane.setEffect(glow);
            
            // Bring to front
            cardPane.toFront();
            
            // Scale up slightly
            cardPane.setScaleX(1.1);
            cardPane.setScaleY(1.1);
        });
        
        cardPane.setOnMouseExited(e -> {
            // Return to original position
            cardPane.setTranslateY(cardPane.getTranslateY() + 20);
            
            // Remove effect
            cardPane.setEffect(null);
            
            // Reset z-order
            cardPane.setViewOrder(-index);
            
            // Reset scale
            cardPane.setScaleX(1.0);
            cardPane.setScaleY(1.0);
        });
    }

    /**
     * Animates playing a card to the trick area with enhanced visual effects
     * @param cardPane The card pane to animate
     */
    private void animatePlayCardToTrick(StackPane cardPane) {
        // Check if animation container exists
        if (animationContainer == null) {
            System.out.println("Warning: animationContainer is null, cannot animate card to trick");
            
            // Just add the card directly to the trick area without animation
            if (playerHandArea.getChildren().contains(cardPane)) {
                playerHandArea.getChildren().remove(cardPane);
            } else if (opponentHandArea.getChildren().contains(cardPane)) {
                opponentHandArea.getChildren().remove(cardPane);
            }
            
            // Add to trick area directly
            trickArea.getChildren().add(cardPane);
            
            // Reset transforms
            cardPane.setTranslateX(0);
            cardPane.setTranslateY(0);
            cardPane.setRotate(0);
            cardPane.setScaleX(1.0);
            cardPane.setScaleY(1.0);
            
            return;
        }
        
        // Store original position
        Bounds originalBounds = cardPane.localToScene(cardPane.getBoundsInLocal());
        double startX = originalBounds.getMinX();
        double startY = originalBounds.getMinY();
        
        // Calculate target position in trick area
        double targetX, targetY;
        
        // Get trick area bounds
        Bounds trickAreaBounds = trickArea.localToScene(trickArea.getBoundsInLocal());
        
        // Determine position based on current trick status
        boolean isFirstCard = trickArea.getChildren().isEmpty();
        
        if (isFirstCard) {
            // Position first card on left side
            targetX = trickAreaBounds.getMinX() + 20;
            targetY = trickAreaBounds.getMinY() + (trickAreaBounds.getHeight() / 2) - 60;
        } else {
            // Position second card on right side
            targetX = trickAreaBounds.getMinX() + 140;
            targetY = trickAreaBounds.getMinY() + (trickAreaBounds.getHeight() / 2) - 60;
        }
        
        // Remove from player hand
        if (playerHandArea.getChildren().contains(cardPane)) {
            playerHandArea.getChildren().remove(cardPane);
        } else if (opponentHandArea.getChildren().contains(cardPane)) {
            opponentHandArea.getChildren().remove(cardPane);
        }
        
        // Add to animation container for movement
        animationContainer.getChildren().add(cardPane);
        
        // Position correctly in animation container
        cardPane.setTranslateX(startX - animationContainer.localToScene(animationContainer.getBoundsInLocal()).getMinX());
        cardPane.setTranslateY(startY - animationContainer.localToScene(animationContainer.getBoundsInLocal()).getMinY());
        
        // Create animations
        
        // Create arc path for more natural movement
        Path path = new Path();
        MoveTo moveTo = new MoveTo(cardPane.getTranslateX(), cardPane.getTranslateY());
        
        // Create quadratic curve for more natural movement
        QuadCurveTo curve = new QuadCurveTo(
            (targetX + cardPane.getTranslateX()) / 2,          // Control point X (midway)
            cardPane.getTranslateY() - 80,                     // Control point Y (above path)
            targetX - animationContainer.localToScene(animationContainer.getBoundsInLocal()).getMinX(),  // End X
            targetY - animationContainer.localToScene(animationContainer.getBoundsInLocal()).getMinY()   // End Y
        );
        
        path.getElements().addAll(moveTo, curve);
        
        // Create path transition
        PathTransition pathTransition = new PathTransition(Duration.millis(600), path, cardPane);
        pathTransition.setInterpolator(Interpolator.EASE_OUT);
        
        // Add rotation for visual interest
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(600), cardPane);
        rotateTransition.setByAngle(isFirstCard ? 5 : -5); // Slight rotation
        
        // Add scale for emphasis
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), cardPane);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        
        ScaleTransition scaleBack = new ScaleTransition(Duration.millis(300), cardPane);
        scaleBack.setToX(1.0);
        scaleBack.setToY(1.0);
        scaleBack.setDelay(Duration.millis(300));
        
        // Create combined animation
        ParallelTransition mainAnimation = new ParallelTransition(
            cardPane, 
            pathTransition,
            rotateTransition,
            new SequentialTransition(scaleTransition, scaleBack)
        );
        
        // Add highlight effect when card lands
        FadeTransition highlightFade = new FadeTransition(Duration.millis(300), cardPane);
        highlightFade.setFromValue(1.0);
        highlightFade.setToValue(0.7);
        highlightFade.setCycleCount(2);
        highlightFade.setAutoReverse(true);
        
        // When animation completes, move the card to the trick area
        mainAnimation.setOnFinished(e -> {
            // Remove from animation container
            animationContainer.getChildren().remove(cardPane);
            
            // Reset transforms
            cardPane.setTranslateX(0);
            cardPane.setTranslateY(0);
            cardPane.setRotate(0);
            
            // Add to trick area
            trickArea.getChildren().add(cardPane);
            
            // Play highlight effect
            highlightFade.play();
            
            // Add sound effect (optional)
            // Media cardSound = new Media(getClass().getResource("/sounds/card_play.mp3").toString());
            // MediaPlayer mediaPlayer = new MediaPlayer(cardSound);
            // mediaPlayer.play();
            
            // Update game status
            if (trickArea.getChildren().size() == 2) {
                // Both players have played, resolve the trick
                resolveTrick();
            } else {
                // Switch turns
                Player nextPlayer = (whistGame.getTurnHolder() == whistGame.getPlayer1()) ? 
                    whistGame.getPlayer2() : whistGame.getPlayer1();
                whistGame.setTurnHolder(nextPlayer);
                
                // Update UI
                currentPlayerLabel.setText(nextPlayer.getUsername());
                
                // Make the next player's cards playable
                resetPlayableCards();
            }
        });
        
        // Play the animation
        mainAnimation.play();
    }
}