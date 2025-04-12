package gui.controllers.games;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import gamelogic.Player;
import gamelogic.connectfour.ConnectFour;
import gui.ScreenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Controller for the Connect Four game screen.
 * Handles game logic, UI updates, and user interactions.
 * This implements a local couch co-op mode where two players take turns using the same device.
 */
public class ConnectFourController implements Initializable {

    // Main container
    @FXML private BorderPane mainContainer;
    
    // Header components
    @FXML private Button backButton;
    @FXML private Text gameTitle;
    @FXML private Label statusLabel;
    @FXML private Label currentPlayerLabel;
    @FXML private Label timerLabel;
    @FXML private Label chatStatusLabel;
    @FXML private Label moveCountLabel;
    
    // Player information
    @FXML private ImageView player1Avatar;
    @FXML private Label player1Name;
    @FXML private Label player1Score;
    @FXML private ImageView player2Avatar;
    @FXML private Label player2Name;
    @FXML private Label player2Score;
    @FXML private Label matchIdLabel;
    
    // Game controls
    @FXML private Button restartGameButton;
    @FXML private Button forfeitGameButton;
    @FXML private Button exitGameButton;
    
    // Game board
    @FXML private GridPane gameBoard;
    
    // Game board cells
    @FXML private Button[][] boardCells = new Button[6][7]; // [row][column]
    
    // Chat components
    @FXML private TextArea chatMessagesArea;
    @FXML private TextField chatInputField;
    @FXML private Button sendMessageButton;
    
    // Move history
    @FXML private ListView<String> moveHistoryList;

    // Game state variables
    private List<Button> columnButtons;
    private ConnectFour game;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private int player1ScoreCount = 0;
    private int player2ScoreCount = 0;
    private boolean gameInProgress = false;
    private ObservableList<String> moveHistory = FXCollections.observableArrayList();
    private String matchId;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private int moveCount = 0;
    
    // Timer variables
    private javafx.animation.Timeline moveTimer;
    private int timeRemaining = 30;

    /**
     * Initialize the controller.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // We've removed the column buttons, so we don't need to initialize them anymore
        
        // Initialize the board cells array from FXML elements
        try {
            // Initialize the board cells (link the FXML buttons to the array)
            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 7; col++) {
                    String id = "cell" + row + col;
                    Button cellButton = (Button) gameBoard.lookup("#" + id);
                    boardCells[row][col] = cellButton;
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing board cells: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Initialize players with proper error handling
        try {
            // Create UserAccount objects for the players
            networking.accounts.UserAccount account1 = new networking.accounts.UserAccount("Player 1", "temp_pass1");
            networking.accounts.UserAccount account2 = new networking.accounts.UserAccount("Player 2", "temp_pass2");
            
            // Create Player objects with the UserAccount objects
            player1 = new Player(account1);
            player2 = new Player(account2);
            
            // Apply color effects to avatar images
            if (player1Avatar != null) {
                player1Avatar.setEffect(new javafx.scene.effect.ColorAdjust(0, 0.5, 0, -0.2)); // Blue tint
            }
            
            if (player2Avatar != null) {
                player2Avatar.setEffect(new javafx.scene.effect.ColorAdjust(0.7, 0.5, 0, 0)); // Red tint
            }
        } catch (Exception e) {
            System.err.println("Error initializing game: " + e.getMessage());
            e.printStackTrace();
        }

        // Initialize player information
        player1Name.setText(player1.getUsername());
        player2Name.setText(player2.getUsername());
        player1Score.setText("Score: " + player1ScoreCount);
        player2Score.setText("Score: " + player2ScoreCount);
        
        // Generate a random match ID
        matchId = "M" + (10000 + (int)(Math.random() * 90000));
        matchIdLabel.setText(matchId);
        
        // Set up chat functionality
        setupChat();
        
        // Set up move history
        moveHistoryList.setItems(moveHistory);
        
        // Initialize move count
        updateMoveCount();
        
        // Set up chat status
        if (chatStatusLabel != null) {
            chatStatusLabel.setText("Online");
        }
        
        // Initialize timer
        setupMoveTimer();
        
        // Start a new game
        startNewGame();
    }

    /**
     * Set the match ID for this game session
     */
    public void setMatchId(String matchId) {
        this.matchId = matchId;
        if (matchIdLabel != null) {
            matchIdLabel.setText(matchId);
        }
    }
    
    /**
     * Set up chat functionality
     */
    private void setupChat() {
        // Disable text area editing
        chatMessagesArea.setEditable(false);
        
        // Add initial message
        addSystemMessage("Game started. Have fun!");
        
        // Set up enter key press handling for chat input
        chatInputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendChatMessage();
            }
        });
    }
    
    /**
     * Add a system message to the chat
     */
    private void addSystemMessage(String message) {
        String timeStamp = LocalTime.now().format(timeFormatter);
        chatMessagesArea.appendText("[" + timeStamp + "] System: " + message + "\n");
    }
    
    /**
     * Add a player message to the chat
     */
    private void addPlayerMessage(Player player, String message) {
        String timeStamp = LocalTime.now().format(timeFormatter);
        String playerName = player.getUsername();
        chatMessagesArea.appendText("[" + timeStamp + "] " + playerName + ": " + message + "\n");
    }
    
    /**
     * Send a chat message
     */
    @FXML
    private void onSendMessageClicked() {
        sendChatMessage();
    }
    
    /**
     * Process and send the chat message
     */
    private void sendChatMessage() {
        String message = chatInputField.getText().trim();
        if (!message.isEmpty()) {
            addPlayerMessage(currentPlayer, message);
            chatInputField.clear();
        }
    }
    
    /**
     * Add a move to the move history
     */
    private void addMoveToHistory(Player player, int column) {
        String playerName = player.getUsername();
        String symbol = (player == player1) ? "Red" : "Yellow";
        String move = playerName + " (" + symbol + ") → Column " + (column + 1);
        moveHistory.add(move);
        
        // Scroll to the latest move
        moveHistoryList.scrollTo(moveHistory.size() - 1);
        
        // Update move count
        moveCount++;
        updateMoveCount();
    }
    
    /**
     * Update the move count label
     */
    private void updateMoveCount() {
        if (moveCountLabel != null) {
            moveCountLabel.setText("Moves: " + moveCount);
        }
    }

    /**
     * Handle column button clicks.
     */
    @FXML
    private void onColumnButtonClicked(javafx.event.ActionEvent event) {
        if (!gameInProgress) {
            showAlert("Game not started", "Please start a new game first.");
            return;
        }

        Button clickedButton = (Button) event.getSource();
        int column = getColumnIndex(clickedButton);
        
        // Try to drop piece in this column
        makeMove(column);
    }

    /**
     * Handle the forfeit game button click.
     */
    @FXML
    private void onForfeitGameClicked() {
        if (!gameInProgress) {
            showAlert("Game not started", "No active game to forfeit.");
            return;
        }
        
        // Current player forfeits
        Player winner = (currentPlayer == player1) ? player2 : player1;
        
        // Update score
        if (winner == player1) {
            player1ScoreCount++;
            statusLabel.setText("Player 2 forfeited. Player 1 wins!");
            addSystemMessage(currentPlayer.getUsername() + " forfeited the game.");
        } else {
            player2ScoreCount++;
            statusLabel.setText("Player 1 forfeited. Player 2 wins!");
            addSystemMessage(currentPlayer.getUsername() + " forfeited the game.");
        }
        
        updateScoreLabels();
        gameInProgress = false;
    }

    /**
     * Handle the back button click to return to the game library.
     */
    @FXML
    private void onBackButtonClicked() {
        // Stop any active timers
        if (moveTimer != null) {
            moveTimer.stop();
        }
        
        // Navigate back to game library with a fresh reload to ensure proper state reset
        GameLibraryController controller = (GameLibraryController)
            ScreenManager.getInstance().reloadAndNavigateTo(ScreenManager.GAME_LIBRARY_SCREEN, ScreenManager.GAME_LIBRARY_CSS);
    }

    /**
     * Handle the new game button click.
     */
    @FXML
    private void onNewGameClicked() {
        startNewGame();
    }

    /**
     * Start a new game by resetting the board and initializing game state.
     */
    private void startNewGame() {
        // Reset UI
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (boardCells[row][col] != null) {
                    boardCells[row][col].setText("");
                    boardCells[row][col].getStyleClass().remove("red-piece");
                    boardCells[row][col].getStyleClass().remove("yellow-piece");
                    boardCells[row][col].getStyleClass().remove("winning");
                }
            }
        }

        // Initialize game with players
        game = new ConnectFour(Arrays.asList(player1, player2));
        
        // Set player1 as the starting player
        currentPlayer = player1;
        
        // Update UI with current game state
        statusLabel.setText("Game started!");
        if (currentPlayerLabel != null) {
            currentPlayerLabel.setText("Player 1 (Red)");
        }
        gameInProgress = true;
        
        // Reset move count
        moveCount = 0;
        updateMoveCount();
        
        // Clear move history for new game
        moveHistory.clear();
        
        addSystemMessage("New game started.");
        
        // Restart timer
        restartTimer();
        
        // Highlight current player's info
        highlightCurrentPlayer();
    }

    /**
     * Set up the move timer
     */
    private void setupMoveTimer() {
        moveTimer = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), event -> {
                timeRemaining--;
                updateTimerLabel();
                
                if (timeRemaining <= 0) {
                    // Time's up, auto-forfeit the current player's turn
                    handleTimeOut();
                }
            })
        );
        moveTimer.setCycleCount(javafx.animation.Timeline.INDEFINITE);
    }
    
    /**
     * Update the timer label
     */
    private void updateTimerLabel() {
        if (timerLabel != null) {
            String timeText = String.format("%02d:%02d", timeRemaining / 60, timeRemaining % 60);
            timerLabel.setText(timeText);
            
            // Apply warning style for low time
            if (timeRemaining <= 10) {
                timerLabel.getStyleClass().add("timer-warning");
            } else {
                timerLabel.getStyleClass().remove("timer-warning");
            }
        }
    }
    
    /**
     * Restart the move timer
     */
    private void restartTimer() {
        if (moveTimer != null) {
            moveTimer.stop();
        }
        
        timeRemaining = 30; // Reset to 30 seconds
        updateTimerLabel();
        
        if (gameInProgress) {
            moveTimer.play();
        }
    }
    
    /**
     * Handle timer timeout
     */
    private void handleTimeOut() {
        if (!gameInProgress) {
            return;
        }
        
        // Auto-play a random valid move or forfeit
        boolean moveMade = false;
        
        // Try to make a random move
        if (!moveMade) {
            addSystemMessage(currentPlayer.getUsername() + " ran out of time!");
            onForfeitGameClicked();
        }
    }

    /**
     * Make a move on the board.
     * 
     * @param column The column to drop the piece (0-6)
     */
    private void makeMove(int column) {
        // Check if the move is valid (column not full)
        if (isColumnFull(column)) {
            showAlert("Invalid Move", "This column is full. Choose another column.");
            return;
        }
        
        // Find the landing row BEFORE updating the game state
        int landingRow = findLandingRow(column);
        
        // Update game state
        game.drop(currentPlayer, column);
        
        // Add move to history
        addMoveToHistory(currentPlayer, column);
        
        // Animate the piece dropping
        animatePieceDrop(column, landingRow, currentPlayer == player1);
        
        // Get the winner from the game using reflection
        Player winner = getWinnerFromGame();
        
        // Check for game end
        if (winner != null) {
            handleGameWon();
        } else if (game.isDrawn()) {
            handleGameDraw();
        } else {
            // Switch players
            currentPlayer = (currentPlayer == player1) ? player2 : player1;
            
            // Update turn indicators
            statusLabel.setText((currentPlayer == player1) ? "Player 1's turn" : "Player 2's turn");
            if (currentPlayerLabel != null) {
                currentPlayerLabel.setText((currentPlayer == player1) ? "Player 1 (Red)" : "Player 2 (Yellow)");
            }
            
            // Reset the timer for the new player's turn
            restartTimer();
            
            // Highlight current player's info
            highlightCurrentPlayer();
        }
    }

    /**
     * Find the row where a piece will land when dropped in the given column
     * Return the row index where the piece should land (5 is bottom, 0 is top)
     */
    private int findLandingRow(int column) {
        String board = game.getBoard();
        
        // Start from the bottom row (row 5) and move up until finding an empty cell
        for (int row = 5; row >= 0; row--) {
            int index = row * 7 + column;
            if (board.charAt(index) == 'o') {
                return row; // Found an empty cell, return this row
            }
        }
        
        // If no empty cell found (shouldn't happen if isColumnFull check passed)
        return -1;
    }

    /**
     * Update the board UI based on the game state
     */
    private void updateBoardDisplay() {
        String board = game.getBoard();
        
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                int index = row * 7 + col;
                if (index < board.length()) {
                    char piece = board.charAt(index);
                    
                    if (boardCells[row][col] != null) {
                        if (piece == '1') {
                            boardCells[row][col].getStyleClass().remove("yellow-piece");
                            boardCells[row][col].getStyleClass().add("red-piece");
                        } else if (piece == '2') {
                            boardCells[row][col].getStyleClass().remove("red-piece");
                            boardCells[row][col].getStyleClass().add("yellow-piece");
                        }
                    }
                }
            }
        }
    }

    private void handleGameWon() {
        // Get the winner from the game
        Player winner = getWinnerFromGame();

        // Stop the timer
        if (moveTimer != null) {
            moveTimer.stop();
        }

        // Update score and status
        if (winner == player1) {
            player1ScoreCount++;
            statusLabel.setText("Player 1 wins!");
        } else {
            player2ScoreCount++;
            statusLabel.setText("Player 2 wins!");
        }

        updateScoreLabels();
        gameInProgress = false;

        // Highlight winning pieces
        highlightWinningPieces();

        // Add game end message to chat
        addSystemMessage(winner.getUsername() + " has won the game!");

        // ✅ Show alert
        String winnerName = winner.getUsername();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("We have a winner!");
        alert.setContentText(winnerName + " won the game!");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/alert-style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
    }

    
    /**
     * Handle a game draw scenario
     */
    private void handleGameDraw() {
        // Stop the timer
        if (moveTimer != null) {
            moveTimer.stop();
        }
        
        statusLabel.setText("Game ended in a draw!");
        gameInProgress = false;
        
        // Add game end message to chat
        addSystemMessage("The game ended in a draw!");
    }
    
    /**
     * Highlight the current player's info panel
     */
    private void highlightCurrentPlayer() {
        if (currentPlayer == player1) {
            player1Name.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-player-red-color;");
            player2Name.setStyle("-fx-font-weight: normal; -fx-text-fill: -fx-text-color;");
        } else {
            player1Name.setStyle("-fx-font-weight: normal; -fx-text-fill: -fx-text-color;");
            player2Name.setStyle("-fx-font-weight: bold; -fx-text-fill: -fx-player-yellow-color;");
        }
    }
    
    /**
     * Update score labels with current scores
     */
    private void updateScoreLabels() {
        player1Score.setText("Score: " + player1ScoreCount);
        player2Score.setText("Score: " + player2ScoreCount);
    }
    
    /**
     * Highlight winning pieces
     */
    private void highlightWinningPieces() {
        String board = game.getBoard();
        
        // Variables to track winning cells to avoid duplicate highlighting
        boolean[][] winningCells = new boolean[6][7];
        boolean foundWin = false;
        
        // Check horizontal wins
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                int index = row * 7 + col;
                char current = board.charAt(index);
                
                if (current != 'o' &&
                    current == board.charAt(index + 1) &&
                    current == board.charAt(index + 2) &&
                    current == board.charAt(index + 3)) {
                    
                    // Found a horizontal win, highlight these pieces
                    for (int i = 0; i < 4; i++) {
                        winningCells[row][col+i] = true;
                    }
                    foundWin = true;
                }
            }
        }
        
        // Check vertical wins
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 7; col++) {
                int index = row * 7 + col;
                char current = board.charAt(index);
                
                if (current != 'o' &&
                    current == board.charAt(index + 7) &&
                    current == board.charAt(index + 14) &&
                    current == board.charAt(index + 21)) {
                    
                    // Found a vertical win, highlight these pieces
                    for (int i = 0; i < 4; i++) {
                        winningCells[row+i][col] = true;
                    }
                    foundWin = true;
                }
            }
        }
        
        // Check diagonal-right wins
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                int index = row * 7 + col;
                char current = board.charAt(index);
                
                if (current != 'o' &&
                    current == board.charAt(index + 8) &&
                    current == board.charAt(index + 16) &&
                    current == board.charAt(index + 24)) {
                    
                    // Found a diagonal-right win, highlight these pieces
                    for (int i = 0; i < 4; i++) {
                        winningCells[row+i][col+i] = true;
                    }
                    foundWin = true;
                }
            }
        }
        
        // Check diagonal-left wins
        for (int row = 0; row < 3; row++) {
            for (int col = 3; col < 7; col++) {
                int index = row * 7 + col;
                char current = board.charAt(index);
                
                if (current != 'o' &&
                    current == board.charAt(index + 6) &&
                    current == board.charAt(index + 12) &&
                    current == board.charAt(index + 18)) {
                    
                    // Found a diagonal-left win, highlight these pieces
                    for (int i = 0; i < 4; i++) {
                        winningCells[row+i][col-i] = true;
                    }
                    foundWin = true;
                }
            }
        }
        
        // Apply winning style to all winning cells
        if (foundWin) {
            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 7; col++) {
                    if (winningCells[row][col]) {
                        Button cell = boardCells[row][col];
                        // Ensure the winning style is applied
                        if (!cell.getStyleClass().contains("winning")) {
                            cell.getStyleClass().add("winning");
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Get the column index for a clicked button
     */
    private int getColumnIndex(Button button) {
        return GridPane.getColumnIndex(button);
    }
    
    /**
     * Check if a column is full
     */
    private boolean isColumnFull(int column) {
        // Check the top row of the column
        String board = game.getBoard();
        return board.charAt(column) != 'o';
    }
    
    /**
     * Show an alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Animate dropping a piece into the specified column
     * 
     * @param column The column to drop the piece into
     * @param row The final row where the piece lands
     * @param isPlayer1 Whether it's player 1's piece
     */
    private void animatePieceDrop(int column, int row, boolean isPlayer1) {
        // Create a temporary piece for animation
        Button animPiece = new Button();
        animPiece.getStyleClass().add("board-cell");
        animPiece.getStyleClass().add(isPlayer1 ? "red-piece" : "yellow-piece");
        
        // Match the size of the existing cells
        double cellWidth = boardCells[0][0].getWidth();
        double cellHeight = boardCells[0][0].getHeight();
        animPiece.setMinSize(cellWidth, cellHeight);
        animPiece.setPrefSize(cellWidth, cellHeight);
        animPiece.setMaxSize(cellWidth, cellHeight);
        
        // Position it at the top of the column
        gameBoard.add(animPiece, column, 0);
        
        // Calculate the distance for animation (accounting for spacing between cells)
        double verticalGap = 10; // Approximate spacing between cells
        // Distance needs to account for both the cell height and the vertical gap between cells
        double distance = row * (cellHeight + verticalGap);
        
        // Calculate the total duration based on the distance
        double durationMillis = 100 + (row * 100); // Longer duration for lower rows
        
        // Create a translation animation
        javafx.animation.TranslateTransition dropAnim = 
            new javafx.animation.TranslateTransition(javafx.util.Duration.millis(durationMillis), animPiece);
        dropAnim.setByY(distance); // Set the distance to move
        dropAnim.setInterpolator(javafx.animation.Interpolator.EASE_IN); // Physics-like drop
        
        // When animation completes, remove the temp piece and update the actual board
        dropAnim.setOnFinished(e -> {
            gameBoard.getChildren().remove(animPiece);
            
            // Apply the style to the actual cell - but preserve the winning style if present
            boolean hasWinningStyle = boardCells[row][column].getStyleClass().contains("winning");
            
            // Clear and add basic styles
            boardCells[row][column].getStyleClass().clear();
            boardCells[row][column].getStyleClass().add("board-cell");
            boardCells[row][column].getStyleClass().add(isPlayer1 ? "red-piece" : "yellow-piece");
            
            // Re-add the winning style if it was present
            if (hasWinningStyle) {
                boardCells[row][column].getStyleClass().add("winning");
            }
            
            // After animation is complete, check if we need to re-apply winning style
            // This handles the case where this piece is part of a winning combination
            if (game.getWinner() != null) {
                // Using Platform.runLater to ensure this runs after any potential win detection
                javafx.application.Platform.runLater(() -> {
                    // If this specific cell should be highlighted as winning, ensure it has the style
                    if (isPartOfWinningCombo(row, column)) {
                        if (!boardCells[row][column].getStyleClass().contains("winning")) {
                            boardCells[row][column].getStyleClass().add("winning");
                        }
                    }
                });
            }
            
            // Add a bounce effect
            javafx.animation.ScaleTransition bounceAnim = 
                new javafx.animation.ScaleTransition(javafx.util.Duration.millis(150), boardCells[row][column]);
            bounceAnim.setFromX(0.8);
            bounceAnim.setFromY(0.8);
            bounceAnim.setToX(1.0);
            bounceAnim.setToY(1.0);
            bounceAnim.play();
        });
        
        // Play the drop animation
        dropAnim.play();
    }

    /**
     * Check if a cell is part of a winning combination
     * 
     * @param row The row of the cell
     * @param col The column of the cell
     * @return true if the cell is part of a winning combination
     */
    private boolean isPartOfWinningCombo(int row, int col) {
        // Get the winner from the game using reflection
        Player winner = getWinnerFromGame();
        
        if (winner == null) {
            return false;
        }
        
        String board = game.getBoard();
        char currentPiece = board.charAt(row * 7 + col);
        
        if (currentPiece == 'o') {
            return false;
        }
        
        // Check horizontal wins
        if (col <= 3) {
            if (currentPiece == board.charAt(row * 7 + col + 1) &&
                currentPiece == board.charAt(row * 7 + col + 2) &&
                currentPiece == board.charAt(row * 7 + col + 3)) {
                return true;
            }
        }
        
        if (col >= 3) {
            if (currentPiece == board.charAt(row * 7 + col - 1) &&
                currentPiece == board.charAt(row * 7 + col - 2) &&
                currentPiece == board.charAt(row * 7 + col - 3)) {
                return true;
            }
        }
        
        // Check vertical wins
        if (row <= 2) {
            if (currentPiece == board.charAt((row + 1) * 7 + col) &&
                currentPiece == board.charAt((row + 2) * 7 + col) &&
                currentPiece == board.charAt((row + 3) * 7 + col)) {
                return true;
            }
        }
        
        // Check diagonal-right wins
        if (row <= 2 && col <= 3) {
            if (currentPiece == board.charAt((row + 1) * 7 + col + 1) &&
                currentPiece == board.charAt((row + 2) * 7 + col + 2) &&
                currentPiece == board.charAt((row + 3) * 7 + col + 3)) {
                return true;
            }
        }
        
        // Check diagonal-left wins
        if (row <= 2 && col >= 3) {
            if (currentPiece == board.charAt((row + 1) * 7 + col - 1) &&
                currentPiece == board.charAt((row + 2) * 7 + col - 2) &&
                currentPiece == board.charAt((row + 3) * 7 + col - 3)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Helper method to get the winner from the game using reflection
     * @return The winner Player, or null if no winner
     */
    private Player getWinnerFromGame() {
        // Now we can use the getter from the Game class
        return game.getWinner();
    }

    /**
     * Handler for cell mouse entered event
     */
    @FXML
    private void onCellMouseEntered(javafx.scene.input.MouseEvent event) {
        if (!gameInProgress) {
            return;
        }
        
        Button cellButton = (Button) event.getSource();
        int colIndex = GridPane.getColumnIndex(cellButton);
        
        // Highlight all cells in this column
        highlightColumn(colIndex, true);
    }
    
    /**
     * Handler for cell mouse exited event
     */
    @FXML
    private void onCellMouseExited(javafx.scene.input.MouseEvent event) {
        Button cellButton = (Button) event.getSource();
        int colIndex = GridPane.getColumnIndex(cellButton);
        
        // Remove highlight from all cells in this column
        highlightColumn(colIndex, false);
    }
    
    /**
     * Handler for cell mouse clicked event
     */
    @FXML
    private void onCellMouseClicked(javafx.scene.input.MouseEvent event) {
        if (!gameInProgress) {
            showAlert("Game not started", "Please start a new game first.");
            return;
        }
        
        Button cellButton = (Button) event.getSource();
        int colIndex = GridPane.getColumnIndex(cellButton);
        
        // Make move in the clicked column
        makeMove(colIndex);
    }
    
    /**
     * Highlight a column when hovered
     * 
     * @param column The column to highlight
     * @param highlight Whether to highlight or unhighlight
     */
    private void highlightColumn(int column, boolean highlight) {
        // Highlight the cells in this column with a visible effect
        for (int row = 0; row < 6; row++) {
            if (boardCells[row][column] != null && 
                !boardCells[row][column].getStyleClass().contains("red-piece") && 
                !boardCells[row][column].getStyleClass().contains("yellow-piece")) {
                
                if (highlight) {
                    // Add highlight effect
                    boardCells[row][column].getStyleClass().add("column-hover");
                } else {
                    // Remove highlight effect
                    boardCells[row][column].getStyleClass().remove("column-hover");
                }
            }
        }
    }
} 