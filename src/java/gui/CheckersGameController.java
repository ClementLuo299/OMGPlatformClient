package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Random;

import gamelogic.Game;
import gamelogic.GameType;
import gamelogic.Player;
import gamelogic.checkers.CheckerBoard;
import gamelogic.checkers.CheckersGame;
import gamelogic.pieces.Checker;
import gamelogic.pieces.Colour;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import networking.accounts.UserAccount;

/**
 * Controller for the Checkers game screen
 */
public class CheckersGameController implements Initializable {

    // FXML Elements - Header
    @FXML private Button backButton;
    @FXML private Text gameTitle;
    @FXML private Label statusLabel;
    @FXML private Label turnLabel;
    @FXML private Label currentPlayerLabel;
    @FXML private Label timerLabel;
    
    // FXML Elements - Left Section
    @FXML private Button restartGameButton;
    @FXML private Button forfeitGameButton;
    @FXML private Button exitGameButton;
    @FXML private Label matchIdLabel;
    @FXML private ImageView player1Avatar;
    @FXML private Label player1Name;
    @FXML private Label player1Score;
    @FXML private ImageView player2Avatar;
    @FXML private Label player2Name;
    @FXML private Label player2Score;
    
    // FXML Elements - Game Board
    @FXML private GridPane gameBoard;
    
    // FXML Elements - Right Section
    @FXML private Label moveCountLabel;
    @FXML private ListView<String> moveHistoryList;
    @FXML private Label chatStatus;
    @FXML private TextArea chatMessages;
    @FXML private TextField chatInput;
    @FXML private Button sendButton;
    
    // Game state
    private CheckersGame checkersGame;
    private List<Player> players;
    private String matchId;
    private boolean gameInProgress = false;
    private Player currentPlayer;
    private int moveCount = 0;
    private ObservableList<String> moveHistory = FXCollections.observableArrayList();
    private Timeline timer;
    private int timeRemaining = 30;
    
    // Checker piece selection state
    private Checker selectedChecker;
    private List<int[]> validMoves = new ArrayList<>();
    private List<StackPane> highlightedSquares = new ArrayList<>();
    
    // UI Elements 
    private StackPane[][] boardSquares = new StackPane[8][8];
    private Circle[][] checkerPieces = new Circle[8][8];
    
    // Utility
    private final Random random = new Random();
    
    // Reference to ScreenManager
    private ScreenManager screenManager = ScreenManager.getInstance();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the UI components
        initializeBoard();
        initializeMoveHistory();
        
        // Set up the game
        setupNewGame();
    }
    
    /**
     * Sets the match ID for this game
     * @param matchId The match ID
     */
    public void setMatchId(String matchId) {
        this.matchId = matchId;
        matchIdLabel.setText(matchId);
    }
    
    /**
     * Sets up a new game with fresh state
     */
    private void setupNewGame() {
        // Create a new CheckersGame instance
        players = new ArrayList<>();
        
        try {
            // Create player accounts
            networking.accounts.UserAccount account1 = new networking.accounts.UserAccount("Player 1", "pass", false);
            networking.accounts.UserAccount account2 = new networking.accounts.UserAccount("Player 2", "pass", false);
            
            // Create Player objects with the accounts
            Player player1 = new Player(account1);
            Player player2 = new Player(account2);
            
            // Add players to the game
            players.add(player1); // Player 1 is WHITE
            players.add(player2); // Player 2 is BLACK
            
            // Create the game with these players
            checkersGame = new CheckersGame(players);
            
            // Player 1 (WHITE) plays first
            currentPlayer = players.get(0);
            
            // Initialize player displays
            player1Name.setText(player1.getUsername());
            player2Name.setText(player2.getUsername());
            
            // Update turn display
            updateTurnDisplay();
            
            // Set game status
            gameInProgress = true;
            
            // Draw the board
            drawBoard();
            
            // Start the timer
            startTimer();
            
            // Update player info display (colors, etc.)
            updatePlayerInfo();
            
            // Update game status message
            updateGameStatus("Game started. " + player1.getUsername() + " (White) goes first.");
            
            // Initialize move count
            moveCount = 0;
            updateMoveCount();
            
            // Clear and initialize move history
            initializeMoveHistory();
        } catch (Exception e) {
            System.err.println("Error setting up game: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes the checkerboard grid
     */
    private void initializeBoard() {
        // Create the 8x8 checkerboard with alternating colors
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // Create a stack pane for each square
                StackPane square = new StackPane();
                
                // Set the square size
                square.setPrefSize(80, 80);
                square.getStyleClass().add("board-square");
                
                // Add alternating colors
                if ((row + col) % 2 == 0) {
                    square.getStyleClass().add("light-square");
                } else {
                    square.getStyleClass().add("dark-square");
                }
                
                // Save reference to the square
                boardSquares[row][col] = square;
                
                // Add square to the grid
                gameBoard.add(square, col, 7 - row); // Invert row for visual layout
                
                // Add click handler for square
                final int finalRow = row;
                final int finalCol = col;
                square.setOnMouseClicked(event -> handleSquareClick(finalRow, finalCol));
            }
        }
    }
    
    /**
     * Initialize the move history list
     */
    private void initializeMoveHistory() {
        moveHistoryList.setItems(moveHistory);
    }
    
    /**
     * Draw the board with all checkers in their current positions
     */
    private void drawBoard() {
        // Clear existing pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardSquares[row][col].getChildren().clear();
                checkerPieces[row][col] = null;
            }
        }
        
        // Get all checkers from the game
        CheckerBoard board = checkersGame.getBoard();
        List<Checker> checkers = board.getCheckers();
        
        // Draw each checker
        for (Checker checker : checkers) {
            int col = checker.getXPosition() - 1; // Convert from 1-indexed to 0-indexed
            int row = checker.getYPosition() - 1; // Convert from 1-indexed to 0-indexed
            
            // Draw a checker piece on the board
            drawChecker(row, col, checker.getColour(), checker.isStacked());
        }
        
        // Update the score labels with captured pieces count
        updateCapturedPiecesCount();
    }
    
    /**
     * Draw a checker piece on the board
     * @param row Row index (0-7)
     * @param col Column index (0-7)
     * @param color The color of the checker
     * @param isKing Whether the checker is a king
     */
    private void drawChecker(int row, int col, Colour color, boolean isKing) {
        // Create a new piece (circle)
        Circle piece = new Circle(25);
        piece.getStyleClass().add("checker-piece");
        
        // Set the color of the piece
        // Note: WHITE pieces will be displayed with white styling in the UI
        if (color == Colour.WHITE) {
            piece.getStyleClass().add("white-piece"); // White styling for WHITE checkers
        } else if (color == Colour.BLACK) {
            piece.getStyleClass().add("black-piece");
        }
        
        // Add king crown effect if the piece is a king
        if (isKing) {
            piece.getStyleClass().add("king-piece");
        }
        
        // Add the piece to the board
        boardSquares[row][col].getChildren().add(piece);
        
        // Store the reference to the piece for later use
        checkerPieces[row][col] = piece;
    }
    
    /**
     * Handles clicks on the board squares
     * @param row The row of the clicked square (0-7)
     * @param col The column of the clicked square (0-7)
     */
    private void handleSquareClick(int row, int col) {
        // Convert to 1-indexed for game logic
        int gameCol = col + 1;
        int gameRow = row + 1;
        
        if (selectedChecker != null) {
            // A checker is already selected, check if clicked on a valid move square
            boolean isValidMoveSquare = false;
            for (int[] move : validMoves) {
                if (move[0] == gameCol && move[1] == gameRow) {
                    isValidMoveSquare = true;
                    break;
                }
            }
            
            if (isValidMoveSquare) {
                // Move the checker
                moveChecker(row, col);
            } else {
                // Check if player clicked on another one of their pieces
                Checker clickedChecker = checkersGame.getBoard().getChecker(gameCol, gameRow);
                if (clickedChecker != null && 
                    ((clickedChecker.getColour() == Colour.WHITE && currentPlayer == players.get(0)) ||
                     (clickedChecker.getColour() == Colour.BLACK && currentPlayer == players.get(1)))) {
                    // Clear previous selection and select the new checker
                    clearSelection();
                    selectChecker(row, col);
                } else {
                    // Show error for invalid move
                    showErrorAlert("Invalid Move", "That is not a valid move. Please select one of the highlighted squares.");
                }
            }
        } else {
            // Try to select a checker
            Checker clickedChecker = checkersGame.getBoard().getChecker(gameCol, gameRow);
            
            if (clickedChecker != null) {
                // Check if the clicked checker belongs to the current player
                boolean isCurrentPlayerChecker = 
                    (clickedChecker.getColour() == Colour.WHITE && currentPlayer == players.get(0)) ||
                    (clickedChecker.getColour() == Colour.BLACK && currentPlayer == players.get(1));
                
                if (isCurrentPlayerChecker) {
                    selectChecker(row, col);
                } else {
                    showErrorAlert("Wrong Piece", "You can only move your own pieces. It's " + 
                        (currentPlayer == players.get(0) ? "White" : "Black") + "'s turn.");
                }
            }
        }
    }
    
    /**
     * Select a checker and highlight its valid moves
     * @param row The row of the selected checker (0-7)
     * @param col The column of the selected checker (0-7)
     */
    private void selectChecker(int row, int col) {
        // Clear any previous selection
        clearSelection();
        
        // Convert to 1-indexed for game logic
        int gameRow = row + 1;
        int gameCol = col + 1;
        
        // Get the checker
        Checker checker = checkersGame.getBoard().getChecker(gameCol, gameRow);
        if (checker == null) return;
        
        // Check if the checker belongs to the current player
        boolean isCurrentPlayerChecker = 
            (checker.getColour() == Colour.WHITE && currentPlayer == players.get(0)) ||
            (checker.getColour() == Colour.BLACK && currentPlayer == players.get(1));
        
        if (!isCurrentPlayerChecker) {
            showErrorAlert("Wrong Piece", "You can only move your own pieces.");
            return;
        }
        
        // Highlight the selected checker
        Circle piece = checkerPieces[row][col];
        if (piece != null) {
            piece.getStyleClass().add("selected-piece");
        }
        
        // Store the selected checker
        selectedChecker = checker;
        
        // Get and highlight valid moves
        validMoves = checkersGame.getValidMoves(gameCol, gameRow);
        if (validMoves == null || validMoves.isEmpty()) {
            showErrorAlert("No Moves", "This piece has no valid moves.");
            clearSelection();
            return;
        }
        
        // Highlight valid moves
        highlightValidMoves();
    }
    
    /**
     * Move the selected checker to the given position
     * @param row The 0-indexed row
     * @param col The 0-indexed column
     */
    private void moveChecker(int row, int col) {
        // Convert to 1-indexed for game logic
        int gameRow = row + 1;
        int gameCol = col + 1;
        
        // Check if the selected square is a valid move
        boolean isValidMove = false;
        
        for (int[] move : validMoves) {
            if (move[0] == gameCol && move[1] == gameRow) {
                isValidMove = true;
                break;
            }
        }
        
        if (isValidMove) {
            // Get the starting position for animation
            int startCol = selectedChecker.getXPosition() - 1;
            int startRow = selectedChecker.getYPosition() - 1;
            
            // Make the move in the game logic
            List<int[]> doubleJumpMoves = checkersGame.makeMove(currentPlayer, selectedChecker, gameCol, gameRow);
            
            // Animate the move
            animateMove(startCol, startRow, col, row, doubleJumpMoves);
            
            // If the move is a double jump and there are more jumps available
            if (doubleJumpMoves != null && !doubleJumpMoves.isEmpty()) {
                // Continue with the same player's turn - they get another move
                updateGameStatus("Double jump opportunity! Make your next move.");
                
                // Clear previous selection highlights
                clearHighlights();
                
                // The selected checker is still selected, but now at its new position
                // Update the valid moves
                validMoves = doubleJumpMoves;
                
                // Highlight the selected checker and new valid moves
                Circle piece = checkerPieces[row][col];
                if (piece != null) {
                    piece.getStyleClass().add("selected-piece");
                }
                
                // Highlight the new valid jump moves
                highlightValidMoves();
                
                // Record this move in the move history
                recordMove(selectedChecker, startCol+1, startRow+1, gameCol, gameRow);
                
                // Restart the timer for the double jump
                startTimer();
                
                return; // Keep the same player's turn
            }
            
            // Record this move in the move history
            recordMove(selectedChecker, startCol+1, startRow+1, gameCol, gameRow);
            
            // Check if the game is over
            if (checkersGame.gameWon(players.get(0)) || checkersGame.gameWon(players.get(1))) {
                handleGameEnd();
            } else {
                // Switch turns
                switchTurns();
            }
        } else {
            // Deselect the piece and clear highlights if they clicked an invalid square
            clearSelection();
            updateGameStatus((currentPlayer == players.get(0) ? "White" : "Black") + " turn. Select a piece to move.");
        }
    }
    
    /**
     * Animate a checker move from one position to another
     * @param startCol Starting column (0-indexed)
     * @param startRow Starting row (0-indexed)
     * @param endCol Ending column (0-indexed)
     * @param endRow Ending row (0-indexed)
     * @param doubleJumpMoves List of available double jump moves after this move, or null
     */
    private void animateMove(int startCol, int startRow, int endCol, int endRow, List<int[]> doubleJumpMoves) {
        // Get the piece to animate
        Circle piece = checkerPieces[startRow][startCol];
        if (piece == null) return;
        
        // Store the original piece's color to maintain the correct piece color
        boolean isWhitePiece = piece.getStyleClass().contains("white-piece");
        boolean isKingPiece = piece.getStyleClass().contains("king-piece");
        
        // Clear the piece from the starting position
        boardSquares[startRow][startCol].getChildren().clear();
        checkerPieces[startRow][startCol] = null;
        
        // Create a clone of the piece for animation
        Circle animatedPiece = new Circle(25);
        animatedPiece.getStyleClass().addAll(piece.getStyleClass());
        
        // Add the animated piece to the GridPane directly at the start position
        gameBoard.add(animatedPiece, startCol, 7 - startRow);
        GridPane.setHalignment(animatedPiece, HPos.CENTER);
        GridPane.setValignment(animatedPiece, VPos.CENTER);
        
        // Check if this was a capturing move (diagonal jump of 2 squares)
        boolean isCapture = Math.abs(startCol - endCol) == 2 && Math.abs(startRow - endRow) == 2;
        
        if (isCapture) {
            // Find the position of the captured piece
            int capturedCol = (startCol + endCol) / 2;
            int capturedRow = (startRow + endRow) / 2;
            
            // Clear the captured piece from the board
            boardSquares[capturedRow][capturedCol].getChildren().clear();
            checkerPieces[capturedRow][capturedCol] = null;
            
            // Play a small animation for the captured piece
            Circle capturedPiece = new Circle(25);
            capturedPiece.getStyleClass().add("checker-piece");
            
            // Set the color of the captured piece - opposite of the current piece
            capturedPiece.getStyleClass().add(isWhitePiece ? "black-piece" : "white-piece");
            
            // Add the piece temporarily for fade-out animation
            gameBoard.add(capturedPiece, capturedCol, 7 - capturedRow);
            GridPane.setHalignment(capturedPiece, HPos.CENTER);
            GridPane.setValignment(capturedPiece, VPos.CENTER);
            
            // Create a fade-out animation for the captured piece
            Timeline captureAnimation = new Timeline(
                new KeyFrame(Duration.millis(500), 
                    new KeyValue(capturedPiece.opacityProperty(), 0),
                    new KeyValue(capturedPiece.scaleXProperty(), 0.5),
                    new KeyValue(capturedPiece.scaleYProperty(), 0.5))
            );
            
            captureAnimation.setOnFinished(e -> {
                // Remove the captured piece from the grid
                gameBoard.getChildren().remove(capturedPiece);
            });
            
            captureAnimation.play();
        }
        
        // Calculate exact positions for animation in GridPane coordinates
        double endX = (endCol - startCol) * 80; // Each square is 80px
        double endY = (startRow - endRow) * 80; // Negate the Y difference due to inverted rows in the GridPane
        
        // Create the move animation
        Timeline moveAnimation = new Timeline();
        
        // If this is a capturing move, use a curved path animation
        if (isCapture) {
            // First keyframe: start position
            moveAnimation.getKeyFrames().add(
                new KeyFrame(Duration.ZERO, 
                    new KeyValue(animatedPiece.translateXProperty(), 0),
                    new KeyValue(animatedPiece.translateYProperty(), 0))
            );
            
            // Midpoint keyframe with arc
            double arcHeight = -40; // Arc height in pixels, negative means arc up
            moveAnimation.getKeyFrames().add(
                new KeyFrame(Duration.millis(250), 
                    new KeyValue(animatedPiece.translateXProperty(), endX/2),
                    new KeyValue(animatedPiece.translateYProperty(), endY/2 + arcHeight))
            );
            
            // Final keyframe: end position
            moveAnimation.getKeyFrames().add(
                new KeyFrame(Duration.millis(500), 
                    new KeyValue(animatedPiece.translateXProperty(), endX),
                    new KeyValue(animatedPiece.translateYProperty(), endY))
            );
        } else {
            // Simple linear animation for regular moves
            moveAnimation.getKeyFrames().add(
                new KeyFrame(Duration.ZERO, 
                    new KeyValue(animatedPiece.translateXProperty(), 0),
                    new KeyValue(animatedPiece.translateYProperty(), 0))
            );
            
            moveAnimation.getKeyFrames().add(
                new KeyFrame(Duration.millis(300), 
                    new KeyValue(animatedPiece.translateXProperty(), endX),
                    new KeyValue(animatedPiece.translateYProperty(), endY))
            );
        }
        
        // Remove the animated piece when animation completes and update the board
        moveAnimation.setOnFinished(e -> {
            gameBoard.getChildren().remove(animatedPiece);
            
            // Place the piece in its final position
            // Check if the piece became a king
            boolean wasKing = isKingPiece;
            boolean isKingNow = selectedChecker.isStacked();
            
            // Create a new piece with the appropriate style
            Circle finalPiece = new Circle(25);
            finalPiece.getStyleClass().add("checker-piece");
            
            // Preserve the original piece color
            finalPiece.getStyleClass().add(isWhitePiece ? "white-piece" : "black-piece");
            
            if (isKingNow) {
                finalPiece.getStyleClass().add("king-piece");
                
                // If the piece just became a king, do a small celebration animation
                if (!wasKing) {
                    Timeline kingAnimation = new Timeline(
                        new KeyFrame(Duration.ZERO, 
                            new KeyValue(finalPiece.scaleXProperty(), 1),
                            new KeyValue(finalPiece.scaleYProperty(), 1)),
                        new KeyFrame(Duration.millis(150), 
                            new KeyValue(finalPiece.scaleXProperty(), 1.3),
                            new KeyValue(finalPiece.scaleYProperty(), 1.3)),
                        new KeyFrame(Duration.millis(300), 
                            new KeyValue(finalPiece.scaleXProperty(), 1),
                            new KeyValue(finalPiece.scaleYProperty(), 1))
                    );
                    kingAnimation.play();
                }
            }
            
            // Add the final piece to the board at the exact end position
            boardSquares[endRow][endCol].getChildren().add(finalPiece);
            checkerPieces[endRow][endCol] = finalPiece;
            
            // Update the captured pieces count
            updateCapturedPiecesCount();
            
            // Clear the selection after the animation completes, unless it's a double jump
            if (doubleJumpMoves == null || doubleJumpMoves.isEmpty()) {
                clearSelection();
            }
        });
        
        moveAnimation.play();
    }
    
    /**
     * Highlight all valid move squares
     */
    private void highlightValidMoves() {
        // First ensure all previous highlights are cleared
        clearHighlights();
        
        // If there are no valid moves
        if (validMoves == null || validMoves.isEmpty()) {
            updateGameStatus("No valid moves available for this piece. Try another piece.");
            return;
        }
        
        for (int[] move : validMoves) {
            int col = move[0] - 1; // Convert from 1-indexed to 0-indexed
            int row = move[1] - 1; // Convert from 1-indexed to 0-indexed
            
            // Make sure coordinates are valid
            if (row < 0 || row >= 8 || col < 0 || col >= 8) {
                continue;
            }
            
            // Create a highlight indicator (circle to show where pieces can move)
            Circle highlight = new Circle(25);
            highlight.setOpacity(0.3);
            highlight.getStyleClass().add("valid-move");
            
            // Check if this is a capture move (2 squares away)
            if (selectedChecker != null) {
                int selectedCol = selectedChecker.getXPosition() - 1;
                int selectedRow = selectedChecker.getYPosition() - 1;
                
                if (Math.abs(selectedCol - col) == 2 && Math.abs(selectedRow - row) == 2) {
                    // This is a capture move, highlight it differently
                    highlight.setOpacity(0.5);
                    highlight.getStyleClass().add("capture-move");
                }
            }
            
            // Add the highlight to the square
            boardSquares[row][col].getChildren().add(0, highlight); // Add at bottom so piece is still visible on top
            
            // Keep track of highlighted squares for later removal
            highlightedSquares.add(boardSquares[row][col]);
        }
    }
    
    /**
     * Clear the selected checker and all move highlights
     */
    private void clearSelection() {
        // Clear all highlights
        clearHighlights();
        
        // Clear selected checker
        if (selectedChecker != null) {
            int col = selectedChecker.getXPosition() - 1;
            int row = selectedChecker.getYPosition() - 1;
            
            // Make sure coordinates are valid
            if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                Circle piece = checkerPieces[row][col];
                
                if (piece != null) {
                    piece.getStyleClass().remove("selected-piece");
                }
            }
            
            selectedChecker = null;
            validMoves.clear();
        }
    }
    
    /**
     * Clear all move highlight indicators
     */
    private void clearHighlights() {
        // First remove highlights from the tracked squares
        for (StackPane square : highlightedSquares) {
            // Remove all highlight nodes that may be Circle instances with valid-move or capture-move classes
            square.getChildren().removeIf(node -> {
                if (node instanceof Circle) {
                    Circle circle = (Circle) node;
                    return circle.getStyleClass().contains("valid-move") || 
                           circle.getStyleClass().contains("capture-move");
                }
                return false;
            });
        }
        
        // As a safety measure, check all squares on the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                StackPane square = boardSquares[row][col];
                if (square != null) {
                    // Remove highlight circles but keep checker pieces
                    square.getChildren().removeIf(node -> {
                        if (node instanceof Circle) {
                            Circle circle = (Circle) node;
                            return circle.getStyleClass().contains("valid-move") || 
                                   circle.getStyleClass().contains("capture-move");
                        }
                        return false;
                    });
                }
            }
        }
        
        // Clear the list of tracked highlighted squares
        highlightedSquares.clear();
    }
    
    /**
     * Update the count of captured pieces for each player
     */
    private void updateCapturedPiecesCount() {
        int player1Captured = players.get(0).getSpoils().size();
        int player2Captured = players.get(1).getSpoils().size();
        
        player1Score.setText("Captured: " + player1Captured);
        player2Score.setText("Captured: " + player2Captured);
    }
    
    /**
     * Record a move in the move history
     * @param checker The checker that was moved
     * @param fromCol Starting column (1-indexed)
     * @param fromRow Starting row (1-indexed)
     * @param toCol Ending column (1-indexed)
     * @param toRow Ending row (1-indexed)
     */
    private void recordMove(Checker checker, int fromCol, int fromRow, int toCol, int toRow) {
        moveCount++;
        
        // Create a readable notation for the move
        String moveText = String.format("%d. %s moves %c%d to %c%d", 
            moveCount,
            checker.getColour() == Colour.WHITE ? "White" : "Black",
            (char)('a' + fromCol - 1), // Convert column to letter (a-h)
            fromRow,
            (char)('a' + toCol - 1), // Convert column to letter (a-h)
            toRow);
        
        // Add extra info if the move was a capture
        if (Math.abs(fromCol - toCol) == 2) {
            moveText += " (capture)";
        }
        
        // Add extra info if the piece was promoted to king
        if (!checker.isStacked() && (toRow == 1 || toRow == 8)) {
            moveText += " (king)";
        }
        
        // Add to the move history
        moveHistory.add(moveText);
        moveHistoryList.scrollTo(moveHistory.size() - 1);
        updateMoveCount();
    }
    
    /**
     * Update the move count display
     */
    private void updateMoveCount() {
        moveCountLabel.setText("Moves: " + moveCount);
    }
    
    /**
     * Switch turns to the other player
     */
    private void switchTurns() {
        // Switch the current player
        currentPlayer = (currentPlayer == players.get(0)) ? players.get(1) : players.get(0);
        checkersGame.setTurnHolder(currentPlayer);
        
        // Update the UI
        updateTurnDisplay();
        updateGameStatus((currentPlayer == players.get(0) ? "White" : "Black") + " turn. Select a piece to move.");
        
        // Add a chat message
        addChatMessage("System", "It's " + 
            (currentPlayer == players.get(0) ? "White" : "Black") + " turn now.");
        
        // Reset the timer
        startTimer();
    }
    
    /**
     * Update the turn display in the UI
     */
    private void updateTurnDisplay() {
        if (currentPlayer == players.get(0)) {
            currentPlayerLabel.setText("Player 1 (White)");
        } else {
            currentPlayerLabel.setText("Player 2 (Black)");
        }
    }
    
    /**
     * Start/restart the turn timer
     */
    private void startTimer() {
        // Stop any existing timer
        if (timer != null) {
            timer.stop();
        }
        
        // Reset time remaining
        timeRemaining = 20; // 20 seconds per turn
        updateTimerDisplay();
        
        // Create a new timer
        timer = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                timeRemaining--;
                updateTimerDisplay();
                
                // If time runs out, automatically forfeit the turn
                if (timeRemaining <= 0) {
                    handleTimeOut();
                }
            })
        );
        
        timer.setCycleCount(timeRemaining);
        timer.play();
    }
    
    /**
     * Handle timeout when a player's turn expires
     */
    private void handleTimeOut() {
        // Clear any selection
        clearSelection();
        
        // Add chat message
        addChatMessage("System", (currentPlayer == players.get(0) ? "White" : "Black") + 
            " ran out of time and forfeited their turn!");
            
        // Switch turns
        switchTurns();
    }
    
    /**
     * Update the timer display
     */
    private void updateTimerDisplay() {
        String timeText = String.format("00:%02d", timeRemaining);
        timerLabel.setText(timeText);
        
        // Add warning style if time is running low
        if (timeRemaining <= 5) {
            timerLabel.getStyleClass().add("timer-warning");
        } else {
            timerLabel.getStyleClass().remove("timer-warning");
        }
    }
    
    /**
     * Update the player information in the UI
     */
    private void updatePlayerInfo() {
        player1Name.setText("Player 1 (White)");
        player2Name.setText("Player 2 (Black)");
        
        updateCapturedPiecesCount();
    }
    
    /**
     * Update the game status message
     * @param message The status message to display
     */
    private void updateGameStatus(String message) {
        statusLabel.setText(message);
    }
    
    /**
     * Handle game end when someone wins
     */
    private void handleGameEnd() {
        gameInProgress = false;
        
        // Stop the timer
        if (timer != null) {
            timer.stop();
        }
        
        // Determine the winner
        Player winner;
        if (checkersGame.gameWon(players.get(0))) {
            winner = players.get(1); // Player 1 has no pieces left, so Player 2 wins
            checkersGame.setWinner(winner);
        } else {
            winner = players.get(0); // Player 2 has no pieces left, so Player 1 wins
            checkersGame.setWinner(winner);
        }
        
        // Update the UI
        updateGameStatus(winner.getUsername() + " wins the game!");
        
        // Add a chat message
        addChatMessage("System", winner.getUsername() + " wins the game! Congratulations!");
        
        // Show a victory animation or dialog
        Platform.runLater(() -> {
            showGameEndDialog(winner);
        });
    }
    
    /**
     * Show a dialog when the game ends
     * @param winner The winning player
     */
    private void showGameEndDialog(Player winner) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(winner.getUsername() + " Wins!");
        alert.setContentText("Congratulations to " + winner.getUsername() + " for winning the game!\n\n" +
                            "Would you like to play again?");
        
        alert.showAndWait();
    }
    
    /**
     * Add a message to the chat
     * @param sender The sender name
     * @param message The message content
     */
    private void addChatMessage(String sender, String message) {
        chatMessages.appendText(sender + ": " + message + "\n");
        // Auto-scroll to the bottom
        chatMessages.setScrollTop(Double.MAX_VALUE);
    }
    
    /**
     * Event handler for sending chat messages
     */
    @FXML
    private void onSendMessageClicked() {
        String message = chatInput.getText().trim();
        
        if (!message.isEmpty()) {
            // Determine the current player's name for the chat
            String playerName = (currentPlayer == players.get(0)) ? "Player 1" : "Player 2";
            
            // Add the message to the chat
            addChatMessage(playerName, message);
            
            // Clear the input field
            chatInput.clear();
        }
    }
    
    /**
     * Event handler for starting a new game
     */
    @FXML
    private void onNewGameClicked() {
        // Stop the current game timer
        if (timer != null) {
            timer.stop();
        }
        
        // Clear the board
        clearSelection();
        
        // Ask for confirmation if a game is in progress
        if (gameInProgress && checkersGame.getWinner() == null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("New Game");
            alert.setHeaderText("Start a New Game?");
            alert.setContentText("The current game will be lost. Are you sure?");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    setupNewGame();
                }
            });
        } else {
            setupNewGame();
        }
    }
    
    /**
     * Event handler for forfeiting the game
     */
    @FXML
    private void onForfeitGameClicked() {
        if (!gameInProgress || checkersGame.getWinner() != null) {
            return; // Game is not in progress or already has a winner
        }
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Forfeit Game");
        alert.setHeaderText("Forfeit the Game?");
        alert.setContentText("Are you sure you want to forfeit? This will count as a loss.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Determine the winner (the other player)
                Player winner = (currentPlayer == players.get(0)) ? players.get(1) : players.get(0);
                checkersGame.setWinner(winner);
                
                // Update the game status
                gameInProgress = false;
                updateGameStatus((currentPlayer == players.get(0) ? "White" : "Black") + 
                    " forfeited. " + (winner == players.get(0) ? "White" : "Black") + " wins!");
                
                // Add a chat message
                addChatMessage("System", (currentPlayer == players.get(0) ? "White" : "Black") + 
                    " forfeited the game. " + (winner == players.get(0) ? "White" : "Black") + " wins!");
                
                // Stop the timer
                if (timer != null) {
                    timer.stop();
                }
            }
        });
    }
    
    /**
     * Event handler for the back button
     */
    @FXML
    private void onBackButtonClicked() {
        // Check if a game is in progress
        if (gameInProgress && checkersGame.getWinner() == null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit Game");
            alert.setHeaderText("Exit the Game?");
            alert.setContentText("The current game will be lost. Are you sure?");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    // Stop the timer
                    if (timer != null) {
                        timer.stop();
                    }
                    
                    // Navigate back to the game library
                    screenManager.navigateTo(ScreenManager.GAME_LIBRARY_SCREEN, ScreenManager.GAME_LIBRARY_CSS);
                }
            });
        } else {
            // Stop the timer
            if (timer != null) {
                timer.stop();
            }
            
            // Navigate back to the game library
            screenManager.navigateTo(ScreenManager.GAME_LIBRARY_SCREEN, ScreenManager.GAME_LIBRARY_CSS);
        }
    }
    
    /**
     * Shows a popup for game errors
     * @param title The error title
     * @param message The error message
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 