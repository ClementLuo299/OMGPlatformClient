package gui.controllers.games;

import gamelogic.GameType;
import gamelogic.Player;
import gamelogic.pieces.Card;
import gamelogic.pieces.CardPile;
import gamelogic.pieces.SuitType;
import gamelogic.whist.WhistGame;
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
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import networking.accounts.UserAccount;

import java.net.URL;
import java.util.*;

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


    // CONSTRUCTOR

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initial setup
        setupGame();

            // DEBUG: Shows the deck in the console to prove shuffling occurred
            System.out.println("[Game Started]");

        // TODO: It is likely better to put this into a onStartGameClicked() method of some sort to give players time
        //  to load into the game. That would make it so they aren't under time pressure from the timers before they
        //  are prepared to play.
        startWhistGameLoop();


        updateUI();

        // Hide hand areas during dealing stage
        hideHandAreas();
        
        // Set initial button states
        shuffleButton.setDisable(false);
        dealButton.setDisable(true);
        
        // Setup button handlers
        //setupButtonHandlers();
        
        // Create reveal trump button (initially not visible)
        //createRevealTrumpButton();
        
        // Fix button positioning
        fixButtonPositioning();
    }


    // GAME LOOP

    /**
     * Loops through each stage of a Game of Whist, calling
     */
    public void startWhistGameLoop() {
        // Loops until a Player reaches 6 Points
        while (gameInProgress) {
            // Increments the Round counter every new round
            whistGame.setRound(whistGame.getRound() + 1);
            // TODO: Move history entry: Round 1 begins!

            // Performs different tasks depending on the Game Stage
            switch (whistGame.getGameStage()) {
                // Handles the selection of the dealer, deck shuffling, and card dealing
                case DEAL:
                    // DEBUG: Shows stage
                    System.out.println("-=Dealing Stage=-\n");

                    /*
                     The Dealer is the Player who draws the lowest rank Card
                     */

                    // Shuffles the Deck a bit so Players' selections are random
                    whistGame.getDeck().overheadShuffle();

                    // Renders the whole face-down deck so that Players can select a card from it
                    deckDisplay = renderWholePile(whistGame.getDeck());
                    // TODO: Play animation of all cards starting in the centre of the screen then splaying out
                    //  (They will overlap each other when fully splayed)

                    // Makes every Card StackPane able to be picked by the Players
                    for (StackPane currentPane : deckDisplay) {
                        // Makes the Card able to be Selected by a Player
                        makeIntoSelectable(currentPane);
                        // Indicates that the Card is Clickable to Players with a visible glow
                        makeGlow(currentPane);
                    }


                    /*
                     Players are prompted to pick cards to compare them, looping until a successful comparison
                     */

                    // Declares if the comparison was successful
                    boolean goodCompare = false;

                    // Loops until a good comparison is made
                    while (!goodCompare) {
                        // Cards that were clicked by the Players
                        Card card1 = null;
                        Card card2 = null;
                        Card winningCard;
                        StackPane card1Display = null;
                        StackPane card2Display = null;

                        // TODO: Prompt each Player to select a Card
                        //  (When they select a card, it will be dealt to them with whistGame.dealCard(<clicked card>))
                        //  (However, this won't be reflected visually!)

                        // TODO: Play animation of selected Cards moving from the deck to the bottom middle of screen

                        // TODO: Make 10 second timer for Selecting Cards

                        // Flips selected Cards face up to reveal them to the Players
                        // (They are generated face down during initialization)
                        // Flip real cards
                        card1.flip();
                        card2.flip();
                        // Re-render flipped cards to update visuals
                        card1Display = renderCard(card1);
                        card2Display = renderCard(card2);

                        // TODO: Move history entry: The cards that each player picked after they are revealed

                        // Compares selected Cards to select Dealer. Lowest Rank gets Dealer
                        winningCard = whistGame.compareCards(card1, card2);

                        // Checks if the comparison is valid, cancelling the loop if so
                        if (winningCard != null) {
                            // Ends the loop because the comparison was successful
                            goodCompare = true;

                            // Gives a gold glow to the lower valued Card
                            if (card1 == winningCard) {
                                makeGlow(card2Display);

                                // TODO: Prompt all Players that Player 2 is the Dealer
                                // TODO: Move history entry: Dealer selected
                            } else {
                                makeGlow(card1Display);

                                // TODO: Prompt all Players that Player 1 is the Dealer
                                // TODO: Move history entry: Dealer selected

                            }

                            // Checks each Players' Hands for the Card
                            // First Player
                            if (whistGame.getPlayer1().checkHand(winningCard)) {
                                // Sets the turn holder to the first Player, as they get to lead the first trick
                                whistGame.setTurnHolder(whistGame.getPlayer1());
                            }
                            // Second Player
                            if (whistGame.getPlayer2().checkHand(winningCard)) {
                                // Sets the turn holder to the second Player, as they get to lead the first trick
                                whistGame.setTurnHolder(whistGame.getPlayer2());
                            }

                            // Flips selected Cards face down to match the deck orientation
                            // (They are face up from before)
                            // Flip real cards
                            card1.flip();
                            card2.flip();
                            // Re-renders flipped cards to update visuals
                            card1Display = renderCard(card1);
                            card2Display = renderCard(card2);

                            // Removes the Cards from the Players' Hands and puts them back into the Deck
                            whistGame.getPlayer1().moveFromHand(card1, whistGame.getDeck());
                            whistGame.getPlayer2().moveFromHand(card2, whistGame.getDeck());

                            // Shuffles the Deck
                            whistGame.getDeck().overheadShuffle();

                            // TODO: Play animation of all Cards consolidating into one pile

                            // Re-renders the deck as a simplified visual stack
                            deckDisplay = renderPile(whistGame.getDeck());

                        } else {
                            // TODO: Prompt the Players that they have to pick again
                            // TODO: Move history entry: Tied! Must go again

                            // Flips selected Cards face down to match the deck orientation
                            // (They are face up from before)
                            // Flip real cards
                            card1.flip();
                            card2.flip();
                            // Re-render flipped cards to update visuals
                            card1Display = renderCard(card1);
                            card2Display = renderCard(card2);

                            // Removes the Cards from the Players' Hands and puts them back into the Deck
                            // Real Cards
                            whistGame.getPlayer1().moveFromHand(card1, whistGame.getDeck());
                            whistGame.getPlayer2().moveFromHand(card2, whistGame.getDeck());
                            // Visual Cards

                            // Shuffles the Deck
                            whistGame.getDeck().overheadShuffle();
                        }
                    }


                    /*
                     The Dealer is prompted to finish setting up the game by shuffling, dealing, and then showing Trump
                     */

                    // Shuffling
                    // TODO: Prompt the Dealer to shuffle 5 times

                    // TODO: Create buttons for all types of shuffling and displays them at the bottom of the screen
                    //  Shuffle Types: Cut, Scramble, Riffle, Overhand
                    //  (Use the card.cut, card.scrambleShuffle, card.riffleShuffle, and card.overheadShuffle methods)
                    //  (Buttons should be disabled while a short shuffle animation plays)
                    //  (We should also make icons for each type of shuffle to have on the buttons)
                    //  (Move history entry: Dealer used <shuffle name> on the deck)

                    // TODO: Make 10 second timer for Shuffling Cards

                    // TODO: Remove buttons after 5 shuffles are done



                    // Dealing
                    // Indicates who should get the card each loop
                    boolean toDealer = false;

                    // TODO: Prompt the dealer to deal 13 cards to each Player

                    // TODO: Make 10 second timer for Dealing Cards

                    // Loops until both Players have 13 Cards in their Hands
                    while (whistGame.getDeck().getSize() > 26) {
                        // Re-renders the Deck to update visuals
                        deckDisplay = renderPile(whistGame.getDeck());
                        // Makes the Card able to be Dealt by the Dealer into Player's Hands
                        makeIntoDeal(deckDisplay.getFirst());
                        // Indicates that the Card is Clickable to Players with a visible glow
                        makeGlow(deckDisplay.getFirst());
                    }

                    // TODO: Move history entry: 13 cards have been dealt to each player


                    // Creating Draw Pile
                    // Creates the Draw Pile from the remaining deck once cards are dealt out
                    whistGame.setDraw(whistGame.getDeck());

                    // Clears the Deck
                    whistGame.getDeck().clear();
                    // TODO: Un-render the Display Deck
                    //deckDisplay.clear(); // I am not sure how to do this

                    // Renders the Draw Pile
                    drawPileDisplay = renderPile(whistGame.getDraw());

                    // Sets the top card to a clickable Trump Card to be revealed
                    makeIntoTrump(drawPileDisplay.getFirst());
                    // Makes it glow to indicate interactiveness
                    makeGlow(drawPileDisplay.getFirst());

                    // TODO: Make 5 second timer for Revealing Trump


                    /*
                     Players' Hands are sorted and revealed to them
                     */

                    // TODO: For now, it is okay if both players' hands are shown on the screen for ease of testing

                    // Sorts each Players' Hand and then reveals their Cards
                    whistGame.getPlayer1().sortHand();
                    whistGame.getPlayer2().sortHand();
                    whistGame.showHand(whistGame.getPlayer1());
                    whistGame.showHand(whistGame.getPlayer2());

                    // Re-renders and makes all Cards playable in each Players' Hand to match new sorting
                    // Player 1
                    for (int i = 0; i < player1Cards.size(); i++) {
                        // Current Card in Display
                        StackPane displayCard = player1Cards.get(i);
                        // Current Card in Logic
                        Card whistCard = (Card) whistGame.getPlayer1().getHand().get(i);

                        // Re-renders the Card
                        displayCard = renderCard(whistCard);
                        // Makes the Card Playable
                        makeIntoPlayable(displayCard);

                        // Puts the modified Card into the Player's visual Hand
                        player1Cards.set(i, displayCard);
                    }
                    // Player 2
                    for (int i = 0; i < player2Cards.size(); i++) {
                        // Current Card in Display
                        StackPane displayCard = player2Cards.get(i);
                        // Current Card in Logic
                        Card whistCard = (Card) whistGame.getPlayer2().getHand().get(i);

                        // Re-renders the Card
                        displayCard = renderCard(whistCard);
                        // Makes the Card Playable
                        makeIntoPlayable(displayCard);

                        // Puts the modified Card into the Player's visual Hand
                        player2Cards.set(i, displayCard);
                    }



                    // Moves the game to the next stage
                    whistGame.nextStage();
                    // TODO: Move history entry: Moving into <whistGame.getStage()> stage!
                    break;

                // Handles the trick taking logic while Players are drafting their hand
                case DRAFT:
                    // DEBUG: Shows stage
                    System.out.println("-=Drafting Stage=-\n");


                    // Moves the game to the next stage
                    whistGame.nextStage();
                    // TODO: Move history entry: Moving into <whistGame.getStage()> stage!
                    break;

                // Handles the trick taking logic while Players are tallying points
                case DUEL:
                    // DEBUG: Shows stage
                    System.out.println("-=Dueling Stage=-\n");

                    // Moves the game to the next stage
                    whistGame.nextStage();
                    break;
            }

            // Checks if any Player has yet reached 6 points
            for (Player currentPlayer : whistGame.getPlayers()) {
                if (currentPlayer.getScore() >= 6) {
                    // TODO: Display end screen
                    // TODO: Move history entry: Gave over, winner = blah

                    // Ends the Game
                    gameInProgress = false;
                    break;
                }
            }
        }
    }


    // RENDER METHODS

    //TODO: Will render a pile with the top card pane visible, and the other cards as a single image pane of a card pile
    private List<StackPane> renderPile(CardPile pile) {
        // The List of Card StackPanes
        List<StackPane> pileToRender = new ArrayList<>();

        // Gets the first card of the given pile to render it
        Card cardToRender = pile.getTopCard();

        // TODO: Render a StackPane that is a stack of cards in a single image
        StackPane bottomPile = new StackPane();

        // Renders the top Card
        StackPane topCard = renderCard(cardToRender);

        // Puts each element into the list
        pileToRender.add(topCard);
        pileToRender.add(bottomPile);

        // Returns the list of cards
        return pileToRender;
    }

    // TODO: Will create card panes for all cards in a pile
    private List<StackPane> renderWholePile(CardPile pile) {
        // The List of Card StackPanes
        List<StackPane> cardsToRender = new ArrayList<>();

        // Iterates through the card pile to create a StackPane for each of its cards and put it into the list
        for (Card cardToRender : pile.getCards()) {
            // Renders the Card
            StackPane topCard = renderCard(cardToRender);

            // Adds the Card to the List
            cardsToRender.add(topCard);
        }

        // Returns the list of cards
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
        // Gets the information of the Card
        SuitType suit = card.getSuit();
        int rank = card.getRank();
        boolean faceDown = card.isFaceDown();

        // Create the image view for the Card StackPane
        ImageView cardImage = new ImageView();
        cardImage.setFitHeight(120);
        cardImage.setFitWidth(80);
        cardImage.setPreserveRatio(true);

        // Create a Card StackPane
        StackPane cardPane = new StackPane(cardImage);
        cardPane.getStyleClass().add("card-view");

        // Attaches the Card object to this StackPane
        cardPane.getProperties().put("card", card);

        // Renders the relevant image for the Card based on which way its facing
        // Initializes a String Filepath to the relevant image for this Card
        String imagePath = "/images/whist images/";

        // Gets the back image for the Card StackPane when it is face down
        if (faceDown) {
            imagePath += "CardBackside1.png";
        }
        // Gets the face image for the Card StackPane when it is face up
        else {
            // Adds the Suit folders to the Filepath
            switch (suit) {
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

            // Adds the Rank of the card to the Filepath image name
            switch (rank) {
                case 1 -> imagePath += "ace_of_" + suit.toString().toLowerCase() + ".png";
                case 13 -> imagePath += "king_of_" + suit.toString().toLowerCase() + ".png";
                case 12 -> imagePath += "queen_of_" + suit.toString().toLowerCase() + ".png";
                case 11 -> imagePath += "jack_of_" + suit.toString().toLowerCase() + ".png";
                default -> imagePath += rank + "_of_" + suit.toString().toLowerCase() + ".png";
            }
        }

        // Applies the image to the Card StackPane. Displays placeholder image if the path is unreachable
        try {
            cardImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
        } catch (Exception e) {
            // Falls back to placeholder image if desired image is not found
            try {
                cardImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/unknown.png"))));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Adds the Image to the Card StackPane
        cardPane.getChildren().add(cardImage);

        // Returns the Card StackPane
        return cardPane;
    }

    // TODO: Makes a rendered Card clickable for Playing
    private void makeIntoPlayable(StackPane cardPane) {
        // Create click handler for card selection and animation
        cardPane.setOnMouseClicked(event -> {

            // DEBUG: Shows the card that was clicked
            System.out.println("Played Card: " + cardPane.getProperties().get("card").toString());

            // The Card associated with this StackPane
            Card associatedCard = (Card) cardPane.getProperties().get("card");


            // TODO:

            // Makes Card inert after being clicked
            makeIntoInert(cardPane);
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

    // TODO: Makes a rendered Card clickable for Selecting Dealer
    private void makeIntoSelectable(StackPane cardPane) {
        // Create click handler for card selection and animation
        cardPane.setOnMouseClicked(event -> {

            // DEBUG: Shows the card that was clicked
            System.out.println("Selected Card: " + cardPane.getProperties().get("card").toString());

            // The Card associated with this StackPane
            Card associatedCard = (Card) cardPane.getProperties().get("card");

            // TODO: Get the Player who clicked and put it into their Hand temporarily
            //  (How to get player who clicked?)
            //whistGame.dealCard(whistGame.getDraw(), associatedCard, whistGame.getPlayer<1 or 2>);

            // Makes Card inert after being clicked
            makeIntoInert(cardPane);
        });
    }

    // TODO: Makes a rendered Card clickable for Revealing the Trump Suit
    private void makeIntoTrump(StackPane cardPane) {
        // Create click handler for card selection and animation
        cardPane.setOnMouseClicked(event -> {

            // DEBUG: Shows the card that was clicked
            System.out.println("Trump Card: " + cardPane.getProperties().get("card").toString());

            // The Card associated with this StackPane
            Card associatedCard = (Card) cardPane.getProperties().get("card");

            // Flips the real Card
            associatedCard.flip();
            // TODO: Re-render Card to show that it is flipped

            // Sets the Trump Suit of the Game
            whistGame.setTrump(associatedCard.getSuit());

            // Makes Card inert after being clicked
            makeIntoInert(cardPane);
        });
    }

    // TODO: Makes a rendered Card clickable for Dealing it to other Players
    private void makeIntoDeal(StackPane cardPane) {
        // Create click handler for card selection and animation
        cardPane.setOnMouseClicked(event -> {

            // DEBUG: Shows the card that was clicked
            System.out.println("Dealt Card: " + cardPane.getProperties().get("card").toString());

            // The Card associated with this StackPane
            Card associatedCard = (Card) cardPane.getProperties().get("card");

            // Checks which Player the Card is to be dealt to, starting with the Non-dealer
            if (!dealToDealer) {
                // Puts the real Card into the Non-dealer's Hand
                whistGame.dealCard(whistGame.getDeck(), associatedCard, whistGame.getTurnHolder());

                // Puts the display Card into the Non-dealer's Hand
                // Checks for the Player who is the turn holder to give them the Card
                if (whistGame.getPlayer1() == whistGame.getTurnHolder()) {
                    // Gives display Card
                    deckDisplay.remove(cardPane);
                    player1Cards.add(cardPane);
                } else {
                    // Gives display Card
                    deckDisplay.remove(cardPane);
                    player2Cards.add(cardPane);
                }

                // TODO: Play animation of top card moving to Player's Hand

                // Sets the recipient to the other Player
                dealToDealer = true;
            } else  {
                // Puts the real Card into the Dealer's Hand
                // Checks for the Player who is not the turn holder to give them the Card
                if (whistGame.getPlayer1() != whistGame.getTurnHolder()) {
                    // Gives real Card
                    whistGame.dealCard(whistGame.getDeck(), associatedCard, whistGame.getPlayer1());
                    // Gives display Card
                    deckDisplay.remove(cardPane);
                    player1Cards.add(cardPane);


                } else {
                    // Gives real Card
                    whistGame.dealCard(whistGame.getDeck(), associatedCard, whistGame.getPlayer2());
                    // Gives display Card
                    deckDisplay.remove(cardPane);
                    player2Cards.add(cardPane);

                }

                // TODO: Play animation of top card moving to Player's Hand

                // Sets the recipient to the other Player
                dealToDealer = false;
            }


            // Makes Card inert after being clicked
            makeIntoInert(cardPane);
        });
    }

    // TODO: Makes a clickable rendered Card un-clickable
    private void makeIntoInert(StackPane cardPane) {
        // TODO: Remove on-click event from the given cardPane

        // TODO: Remove glow if there is any

    }

    // TODO: Makes a Card have a faint glow around the edge to indicate interactiveness
    private void makeGlow(StackPane cardPane) {
        // TODO: Add the glow effect
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
        UserAccount player1Account = new UserAccount("Player 1", "password");
        UserAccount player2Account = new UserAccount("Player 2", "password");
        
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
        // Shuffle button handler
        //shuffleButton.setOnAction(event -> onShuffleClicked());
        
        // Deal button handler
        //dealButton.setOnAction(event -> onDealClicked());
    }

    // TODO: Modify this to match new shuffle logic in whistGameLoop()
    /**
     * Update all UI elements based on the current game state
     */
    private void updateUI() {
        // Update stage label
        gameStageLabel.setText(whistGame.getGameStage().getDisplayName());
        
        // Update round counter
        roundCounter.setText(String.valueOf(whistGame.getRound() + 1));
        
        // Update shuffle counter
        //shuffleCounterLabel.setText("Shuffles: " + game.getShuffleCount() + "/3");
        
        // Update player information
        updatePlayerInfo();
    }
    
    /**
     * Update player information in the UI
     */
    private void updatePlayerInfo() {
        // Get players
        Player player1 = whistGame.getPlayers().getFirst();
        Player player2 = whistGame.getPlayers().getLast();
        
        // Update player names and scores
        player1Name.setText(player1.getUsername());
        player2Name.setText(player2.getUsername());
        player1Score.setText("Score: " + player1.getScore());
        player2Score.setText("Score: " + player2.getScore());
    }

    // TODO: Either remove this or adjust to match new deal stage logic as per whistGameLoop()
    /**
     * Fix the positioning of buttons in the dealer overlay
     */
    private void fixButtonPositioning() {
        // Set proper width for both buttons
        shuffleButton.setPrefWidth(120);
        dealButton.setPrefWidth(120);

        // Make sure the deal button text is showing correctly
        dealButton.setText("Deal");

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
     * Add a message to the move history list
     * 
     * @param message The message to add to the history
     */
    private void addMoveHistoryEntry(String message) {
        // Add the move to the history list
        if (moveHistoryList != null) {
            moveHistoryList.getItems().add(message);
            // Scroll to bottom
            moveHistoryList.scrollTo(moveHistoryList.getItems().size() - 1);
        }
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
                // TODO: Broken line
                //controller.setCurrentUser(currentUsername, isGuest);
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





    // TODO: May not be useful anymore
    /**
     * Animate a card being selected (moves up, scales up, and flips face up)
     * 
     * @param cardPane The card to animate
     */
    private void animateCardSelection(StackPane cardPane) {
        // Store initial position for later
        double initialY = cardPane.getTranslateY();
        
        // Create a sequential animation
        javafx.animation.SequentialTransition sequence = new javafx.animation.SequentialTransition();
        
        // Move up animation
        javafx.animation.TranslateTransition moveUp = new javafx.animation.TranslateTransition(Duration.millis(150), cardPane);
        moveUp.setByY(-30); // Move up by 30 pixels
        
        // Rotation animation
        javafx.animation.RotateTransition rotate = new javafx.animation.RotateTransition(Duration.millis(300), cardPane);
        rotate.setAxis(javafx.scene.transform.Rotate.Y_AXIS);
        rotate.setFromAngle(0);
        rotate.setToAngle(360);
        rotate.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
        
        // Scale animation
        javafx.animation.ScaleTransition scale = new javafx.animation.ScaleTransition(Duration.millis(200), cardPane);
        scale.setToX(1.5);
        scale.setToY(1.5);
        
        // Add all animations to sequence
        sequence.getChildren().addAll(moveUp, new javafx.animation.ParallelTransition(rotate, scale));
        
        // When animation finishes, flip the card face up
        sequence.setOnFinished(event -> {
            // TODO: Broken line
            // Flip card face up
            //flipCardFaceUp(cardPane);
        });
        
        // Play the animation
        sequence.play();
    }

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
        // Animation to move card to the trick area
        javafx.animation.TranslateTransition moveToTrick = new javafx.animation.TranslateTransition(Duration.millis(300), cardPane);
        
        // Calculate move to center
        double cardX = cardPane.localToScene(cardPane.getBoundsInLocal()).getMinX();
        double cardY = cardPane.localToScene(cardPane.getBoundsInLocal()).getMinY();
        
        double trickAreaX = trickArea.localToScene(trickArea.getBoundsInLocal()).getMinX() + 
                          trickArea.getWidth()/2 - 40;
        double trickAreaY = trickArea.localToScene(trickArea.getBoundsInLocal()).getMinY() + 
                          trickArea.getHeight()/2 - 60;
        
        moveToTrick.setByX(trickAreaX - cardX);
        moveToTrick.setByY(trickAreaY - cardY);
        
        // Scale back to normal size during move
        javafx.animation.ScaleTransition scaleBack = new javafx.animation.ScaleTransition(Duration.millis(300), cardPane);
        scaleBack.setToX(1.0);
        scaleBack.setToY(1.0);
        
        // Play parallel animations
        javafx.animation.ParallelTransition playCardAnimation = new javafx.animation.ParallelTransition(moveToTrick, scaleBack);
        
        // When animation completes
        playCardAnimation.setOnFinished(event -> {
            // Remove from hand
            playerHandArea.getChildren().remove(cardPane);
            
            // Add to trick area
            trickArea.getChildren().add(cardPane);
            
            // Reset position in new parent
            cardPane.setTranslateX(0);
            cardPane.setTranslateY(0);
            
            // Record move in history
            int cardRank = (int) cardPane.getProperties().get("rank");
            gamelogic.pieces.SuitType cardSuit = (gamelogic.pieces.SuitType) cardPane.getProperties().get("suit");
            
            String rankName;
            switch (cardRank) {
                case 1: rankName = "Ace"; break;
                case 11: rankName = "Jack"; break;
                case 12: rankName = "Queen"; break;
                case 13: rankName = "King"; break;
                default: rankName = String.valueOf(cardRank); break;
            }
            
            addMoveHistoryEntry("You played: " + rankName + " of " + cardSuit.toString());

            // TODO: Broken lines
            // End player's turn
            //isPlayerTurn = false;
            //updatePlayerStatusIndicators();
            
            // Simulate opponent's turn after a delay
            simulateOpponentTurn();
        });
        
        // Play the animation
        playCardAnimation.play();
    }

    /**
     * Hide hand areas during dealing stage
     */
    private void hideHandAreas() {
        if (playerHandArea != null && opponentHandArea != null) {
            playerHandArea.getStyleClass().add("hand-area-hidden");
            opponentHandArea.getStyleClass().add("hand-area-hidden");
        }
    }
    
    /**
     * Show hand areas after dealing is complete
     */
    private void showHandAreas() {
        if (playerHandArea != null && opponentHandArea != null) {
            playerHandArea.getStyleClass().remove("hand-area-hidden");
            opponentHandArea.getStyleClass().remove("hand-area-hidden");
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
        dealButton.setVisible(false);
        
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
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ANIMATION METHODS

    // TODO: This may need to be adjusted to fit new shuffle logic
    /**
     * Animate the shuffling of cards
     */
    private void animateShuffle() {
        // Find the card image in the dealer overlay
        StackPane cardContainer = (StackPane) dealerOverlay.lookup(".card-image-container");
        if (cardContainer == null || cardContainer.getChildren().isEmpty()) return;
        
        // Get the card image
        ImageView cardImage = (ImageView) cardContainer.getChildren().get(0);
        
        // Create a rotation animation
        javafx.animation.RotateTransition rotate = new javafx.animation.RotateTransition(Duration.seconds(1), cardImage);
        rotate.setAxis(javafx.scene.transform.Rotate.Y_AXIS);
        rotate.setFromAngle(0);
        rotate.setToAngle(360);
        rotate.setCycleCount(1);
        rotate.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
        
        // Play the animation
        rotate.play();
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
        dealButton.setDisable(false);
        
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
     * Start the game timer (will be used in DUEL stage)
     */
    private void startGameTimer() {
        // Only start timer in DUEL stage
        if (whistGame.getGameStage() != gamelogic.whist.StageType.DUEL) {
            return;
        }
        
        // Reset timer
        timeRemaining = 15;
        updateTimerDisplay();
        
        // Stop existing timer if running
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Create new timer
        gameTimer = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                timeRemaining--;
                updateTimerDisplay();
                
                if (timeRemaining <= 0) {
                    // Time's up, handle automatic move
                    // This will be implemented later
                    gameTimer.stop();
                }
            })
        );
        
        gameTimer.setCycleCount(15);
        gameTimer.play();
    }
    
    /**
     * Update the timer display
     */
    private void updateTimerDisplay() {
        timerLabel.setText("00:" + (timeRemaining < 10 ? "0" : "") + timeRemaining);
        timerProgressBar.setProgress(timeRemaining / 15.0);
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
}