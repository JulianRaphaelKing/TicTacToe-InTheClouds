package com.example.portfilioproject;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Main point for the Tic-Tac-Toe application.
 * This class handles the application lifecycle, UI setup, and game logic orchestration.
 */
public class TicTacToeApp extends Application {


    // Main UI components and state trackers
    private BorderPane mainPane;
    private Boolean preview = false;
    private char currentPlayer = 'X';
    private boolean[][] isClicked = new boolean[3][3];
    private static int xWinCount = 0;
    private static int oWinCount = 0;
    private static Label xWinsText = new Label("0");
    private static Label oWinsText = new Label("0");
    private static Stage menuStage;
    private MediaPlayer bgmPlayer;
    private MediaPlayer fastBgmPlayer;

    private BooleanProperty isMuted = new SimpleBooleanProperty(false); // Sound State


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Load required assets at startup
        FileAssets.loadFiles();

        // Load regular music
        String bgmPath = getClass().getResource("/images/bkgMusic.wav").toExternalForm();
        Media bgm = new Media(bgmPath);
        bgmPlayer = new MediaPlayer(bgm);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgmPlayer.play();

        // Load fast music
        String fastBgmPath = getClass().getResource("/images/bkgMusicSpeed.wav").toExternalForm();
        Media fastBgm = new Media(fastBgmPath);
        fastBgmPlayer = new MediaPlayer(fastBgm);
        fastBgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
        fastBgmPlayer.stop();

        // Set scene to main screen
        Scene scene = new Scene(getMainPane(primaryStage), 675, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Title Screen");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setHeight(800);
        primaryStage.setWidth(675);
        primaryStage.show();

        // Close the menuStage when primaryStage closes
        primaryStage.setOnCloseRequest(event -> {
            if (menuStage != null && menuStage.isShowing()) {
                menuStage.close();
            }
        });
    }

    /**
     * Creates and returns the main pane that serves as the primary layout for the application.
     *
     * This pane includes a title bar with buttons for minimizing, closing, and muting the application,
     * as well as a central area for the title screen components.
     *
     * The layout combines a background image and interactive elements, with drag functionality enabled
     * for the title bar to allow window repositioning.
     *
     * @param primaryStage the primary stage of the JavaFX application, used to apply actions like minimize and close
     * @return a StackPane containing the complete layout for the main application screen
     */
    public Pane getMainPane(Stage primaryStage) {

        // Background Image
        ImageView background = new ImageView(FileAssets.BACKGROUND);
        background.setFitWidth(675);
        background.setFitHeight(800);
        background.setPreserveRatio(false);

        mainPane = new BorderPane();

        // Create draggable title bar with buttons (mute, minimize, close)
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setSpacing(18);
        topBar.setPadding(new Insets(18, 32, 10, 10));

        // Button Close
        Button btnClose = new Button();
        btnClose.setGraphic(new ImageView(FileAssets.CLOSE));
        btnClose.setStyle("-fx-background-color: transparent; -fx-padding: 0");
        btnClose.setOnAction(e -> primaryStage.close());
        btnClose.setOnMouseEntered(e -> btnClose.setGraphic(new ImageView(FileAssets.CLOSE_HOVER)));
        btnClose.setOnMouseExited(e -> btnClose.setGraphic(new ImageView(FileAssets.CLOSE)));

        // Button Minimize
        Button btnMin = new Button();
        btnMin.setGraphic(new ImageView(FileAssets.MINIMIZE));
        btnMin.setStyle("-fx-background-color: transparent; -fx-padding: 0");
        btnMin.setOnAction(e -> primaryStage.setIconified(true));
        btnMin.setOnMouseEntered(e -> btnMin.setGraphic(new ImageView(FileAssets.MINIMIZE_HOVER)));
        btnMin.setOnMouseExited(e -> btnMin.setGraphic(new ImageView(FileAssets.MINIMIZE)));

        // Button Mute
        Button btnMute = new Button();
        btnMute.setGraphic(new ImageView(FileAssets.MUTE));
        btnMute.setStyle("-fx-background-color: transparent; -fx-padding: 0");
        btnMute.setOnAction(e -> isMuted.set(!isMuted.get()));
        btnMute.setOnMouseEntered(e -> btnMute.setGraphic(new ImageView(isMuted.get() ? FileAssets.MUTE : FileAssets.MUTE_HOVER)));
        btnMute.setOnMouseExited(e -> btnMute.setGraphic(new ImageView(isMuted.get() ? FileAssets.MUTE_HOVER : FileAssets.MUTE)));

        // Sync icon and music when mute state changes
        isMuted.addListener((observable, oldValue, newValue) -> {
            btnMute.setGraphic(new ImageView(newValue ? FileAssets.MUTE_HOVER : FileAssets.MUTE));
            if(isMuted.get()) {
                bgmPlayer.stop();
            } else {
                bgmPlayer.play();
            }
        });


        // Add buttons to the top bar
        topBar.getChildren().addAll(btnMute, btnMin, btnClose);

        // Make entire top bar draggable
        topBar.setOnMousePressed(e -> {
            primaryStage.setX(e.getScreenX());
            primaryStage.setY(e.getScreenY());
        });
        topBar.setOnMouseDragged(e -> {
            primaryStage.setX(e.getScreenX());
            primaryStage.setY(e.getScreenY());
        });

        // Assemble main layout
        mainPane.setTop(topBar);
        mainPane.setCenter(getTitlePane(primaryStage));
        StackPane paneLayering = new StackPane(background, mainPane);
        return paneLayering;
    }


    /**
     * Creates and returns a pane containing the title screen components such as a background image
     * and a start button with hover functionality that dynamically updates the displayed graphics.
     *
     * @return a StackPane containing the title screen layout with interactive start button
     */
    private Pane getTitlePane(Stage stage) {
        // Create background for center of the screen
        ImageView clouds = new ImageView(FileAssets.BACKGROUND_CLOUDS);

        // Start Button
        Button btnStart = new Button();
        btnStart.setGraphic(new ImageView(FileAssets.START));
        btnStart.setStyle("-fx-background-color: transparent; -fx-padding: 0");

        // Menu Button
        Button btnMenu = new Button();
        btnMenu.setGraphic(new ImageView(FileAssets.MENU));
        btnMenu.setStyle("-fx-background-color: transparent; -fx-padding: 0");

        // Start button hover effects (including background transition and music change)
        BooleanProperty isStartHovered = new SimpleBooleanProperty(false);
        isStartHovered.addListener((observable, oldValue, newValue) -> {
            if(isStartHovered.get()) {
                clouds.setImage(FileAssets.BACKGROUND_CLOUDS_HOVER);
                btnStart.setGraphic(new ImageView(FileAssets.START_HOVER));
                if(!isMuted.get()) {
                    bgmPlayer.stop();
                    fastBgmPlayer.play();
                }

            } else {
                clouds.setImage(FileAssets.BACKGROUND_CLOUDS);
                btnStart.setGraphic(new ImageView(FileAssets.START));
                if(!isMuted.get()) {
                    fastBgmPlayer.stop();
                    bgmPlayer.play();
                }
            }
        });

        // Trigger the changes when the start button is hovered
        btnStart.setOnMouseEntered(e -> isStartHovered.set(true));
        btnStart.setOnMouseExited(e -> isStartHovered.set(false));

        // When start button is clicked, change the center of the screen to the game play
        btnStart.setOnAction(e -> {
            mainPane.setCenter(getGamePane(stage));
        });

        // Menu button hover effects
        btnMenu.setOnMouseEntered(e -> {btnMenu.setGraphic(new ImageView(FileAssets.MENU_HOVER));});
        btnMenu.setOnMouseExited(e -> {btnMenu.setGraphic(new ImageView(FileAssets.MENU));});

        // When menu button is clicked, open the menu stage
        btnMenu.setOnAction(e -> {getMenu(stage);});

        // VBox to hold the start button in the center
        VBox centerContent = new VBox(btnStart);
        centerContent.setAlignment(Pos.CENTER);

        // Hold the menu button in the top right with adjusted margins
        StackPane.setAlignment(btnMenu, Pos.TOP_RIGHT);
        StackPane.setMargin(btnMenu, new Insets(25, 35, 0, 0));

        // Layer for everything that belongs in the center
        StackPane centerLayering = new StackPane();
        centerLayering.getChildren().addAll(clouds, centerContent, btnMenu);

        return centerLayering;
    }

    /**
     * Creates and returns a pane representing the game board for a Tic-Tac-Toe application.
     *
     * The returned pane is a stacked layout that includes a blurred background, grid lines image,
     * and a 3x3 grid of buttons. Each button in the grid represents a cell of the Tic-Tac-Toe
     * board and supports interactive gameplay features such as input actions and hover previews.
     *
     * @return a StackPane containing the game board elements, including the grid and background
     */
    private Pane getGamePane(Stage stage) {
        int board[][] = resetBoard();
        currentPlayer = 'X';

        // Create grid to host buttons
        GridPane grid = new GridPane();
        grid.setPrefSize(465, 465);
        grid.setAlignment(Pos.CENTER);

        // Setup grid constraints and add buttons
        for(int i = 0; i < 3; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(155));
            grid.getRowConstraints().add(new RowConstraints(155));

            for(int j = 0; j < 3; j++) {
                Button gridBtn = new Button();
                gridBtn.setMinSize(155, 155);
                gridBtn.setStyle("-fx-background-color: transparent; -fx-padding: 0");
                int row = i;
                int col = j;

                // When grid button is clicked, call turns method
                gridBtn.setOnAction(e -> {
                    if(!isClicked[row][col]) {
                        turns(row, col, gridBtn, currentPlayer, board, stage);
                        currentPlayer = currentPlayer == 'X' ? 'O' : 'X';
                    }

                });

                // Preview move on hover
                gridBtn.setOnMouseEntered(e -> {
                    if(gridBtn.getGraphic() == null && !isClicked[row][col]) {
                        gridBtn.setGraphic(new ImageView(currentPlayer == 'X' ? FileAssets.X : FileAssets.O));
                        preview = true;
                    }
                });
                gridBtn.setOnMouseExited(e -> {
                    if(preview && !isClicked[row][col]) {
                        gridBtn.setGraphic(null);
                        preview = false;
                    }
                });
                grid.add(gridBtn, i, j);
            }
        }

        // Stack all elements
        ImageView lines = new ImageView(FileAssets.GRID);
        ImageView bkgCloud = new ImageView(FileAssets.BKG_CLOUDS_BLURRED);
        StackPane gameLayering = new StackPane();
        gameLayering.getChildren().addAll(bkgCloud, lines, grid);
        return gameLayering;
    }

    /**
     * Updates the game state based on the current player's move, and checks for a winning condition.
     * This method modifies the graphical representation of the grid button, records the move on the
     * game board, and determines if the current player has won after making the move. If a win
     * condition is detected, the main layout is updated to display the winning screen.
     *
     * @param row the row index of the grid button that was clicked
     * @param col the column index of the grid button that was clicked
     * @param gridBtn the button on the grid that was clicked by the player
     * @param currentPlayer the current player's symbol ('X' or 'O')
     * @param board the 2D integer array representing the current state of the game board
     */
    private void turns(int row, int col, Button gridBtn, char currentPlayer, int[][] board, Stage stage){
        // Set the clicked grid to the current player's icon
        gridBtn.setGraphic(new ImageView(currentPlayer == 'X' ? FileAssets.X : FileAssets.O));
        isClicked[row][col] = true;
        board[row][col] = (currentPlayer == 'X' ? 0 : 1);

        // Check for a winner or tie
        if(checkWin(row, col, currentPlayer, board)) {
            mainPane.setCenter(currentPlayer == 'X' ? getXWinsPane(stage) : getOWinsPane(stage));
        } else if (isBoardFull(board)) {
            mainPane.setCenter(getTiePane(stage));
        }
    }

    /**
     * Checks if the current player has achieved a winning condition in the Tic-Tac-Toe game.
     *
     * This method evaluates rows, columns, and diagonals to determine if three consecutive
     * marks of the current player ('X' or 'O') are aligned in any direction. A win condition
     * will immediately return true, while the absence of a win condition will return false.
     *
     * @param row the row index of the cell most recently selected by the player
     * @param col the column index of the cell most recently selected by the player
     * @param currentPlayer the symbol of the current player ('X' or 'O')
     * @param board a 2D array representing the current game board, where integer values
     *        are used to differentiate between player moves
     * @return true if the current player wins, false otherwise
     */
    private boolean checkWin(int row, int col, char currentPlayer, int[][] board) {
        // Check to see if the current player has won
        int player = (currentPlayer == 'X' ? 0 : 1);
        return (board[0][0] == player && board[0][1] == player && board[0][2] == player) ||
                (board[1][0] == player && board[1][1] == player && board[1][2] == player) ||
                (board[2][0] == player && board[2][1] == player && board[2][2] == player) ||
                (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player) ||
                (board[0][0] == player && board[1][0] == player && board[2][0] == player) ||
                (board[0][1] == player && board[1][1] == player && board[2][1] == player) ||
                (board[0][2] == player && board[1][2] == player && board[2][2] == player);
    }

    /**
     * Creates a pane that contains the winner background, message, and restart button.
     * This method takes the arguments and displays them on a Stack Pane corresponding with who won.
     *
     * @param stage the stage that we are currently on
     * @param background the background image associated with the winner
     * @param winnerMessage the text image with the appropriate text displaying the winner
     * @return the stack pane
     */
    private Pane getWinnerPane(Stage stage, ImageView background, ImageView winnerMessage) {
        // Create restart button
        Button restart = new Button();
        restart.setGraphic(new ImageView(FileAssets.RESTART));
        restart.setStyle("-fx-background-color: transparent; -fx-padding: 0");
        restart.setOnAction(e -> {
            restartIsClicked();
            mainPane.setCenter(getTitlePane(stage));
        });

        // Restart Button Hover effects
        restart.setOnMouseEntered(mouseEvent -> {restart.setGraphic(new ImageView(FileAssets.RESTART_HOVER));});
        restart.setOnMouseExited(mouseEvent -> {restart.setGraphic(new ImageView(FileAssets.RESTART));});

        // VBox to hold the winner message and restart button
        VBox messageContainer = new VBox();
        messageContainer.setAlignment(Pos.CENTER);
        messageContainer.setSpacing(25);
        messageContainer.getChildren().addAll(winnerMessage, restart);

        // Stack Pane to hold all elements
        StackPane winnerLayer = new StackPane();
        winnerLayer.getChildren().addAll(background, messageContainer);

        return winnerLayer;
    }

    private Pane getTiePane(Stage stage) {
        // Create background and winner message
        ImageView bkgReg = new ImageView(FileAssets.BKG_DRAW);
        ImageView winner = new ImageView(FileAssets.DRAW_MSG);

        // Get the winner pane with the appropriate message
        return getWinnerPane(stage, bkgReg, winner);
    }

    /**
     * Creates and returns a pane representing the "X Wins" screen in the Tic-Tac-Toe application.
     *
     * The pane includes a background image, a message indicating that player X has won,
     * and a restart button. The restart button allows the game to be reset and navigates
     * back to the title screen. The restart button also supports hover effects that change
     * its appearance when the mouse enters or exits the button.
     *
     * @return a StackPane containing the "X Wins" screen layout, including background, message, and restart button
     */
    private Pane getXWinsPane(Stage stage) {
        // Give player X a point
        xWinCount++;
        xWinsText.setText(String.valueOf(xWinCount));

        // Create background and winner message
        ImageView bkgDay = new ImageView(FileAssets.BKG_DAY);
        ImageView winner = new ImageView(FileAssets.PLR_X_MSG);

        // Get the winner pane with the appropriate message
        return getWinnerPane(stage, bkgDay, winner);
    }

    /**
     * Creates and returns a pane representing the "O Wins" screen in the Tic-Tac-Toe application.
     *
     * The pane consists of a background image, a message indicating that player O has won,
     * and a restart button. The restart button resets the game and navigates back to the title screen.
     * Hover effects are applied to the restart button, changing its appearance when the mouse enters or exits the button.
     *
     * @return a StackPane containing the "O Wins" screen layout, including background, message, and restart button
     */
    private Pane getOWinsPane(Stage stage) {
        // Give O a point
        oWinCount++;
        oWinsText.setText(String.valueOf(oWinCount));

        // Create background and winner message
        ImageView bkgNight = new ImageView(FileAssets.BKG_NIGHT);
        ImageView winner = new ImageView(FileAssets.PLR_O_MSG);

        // Get the winner pane with the appropriate message
        return getWinnerPane(stage, bkgNight, winner);

    }

    public static void getMenu(Stage stage) {
        if (menuStage != null && menuStage.isShowing()) {
            menuStage.toFront();
            return;
        }
        if (menuStage != null && !menuStage.isShowing()) {
            menuStage.show();
            return;
        }

        menuStage = new Stage();
        menuStage.initStyle(StageStyle.UNDECORATED);
        menuStage.initOwner(stage);
        menuStage.setHeight(395);
        menuStage.setWidth(265);
        menuStage.setResizable(false);

        StackPane menuStack = new StackPane();
        double offset = 20;
        menuStage.setX(stage.getX() - menuStage.getWidth() - offset);
        menuStage.setY(stage.getY());

        // Make it follow the main stage when moved
        stage.xProperty().addListener((obs, oldVal, newVal) -> {
            menuStage.setX(newVal.doubleValue() - menuStage.getWidth() - 20);
        });
        stage.yProperty().addListener((obs, oldVal, newVal) -> {
            menuStage.setY(newVal.doubleValue());
        });

        ImageView MenuBackground = new ImageView(FileAssets.MENU_BKG);
        MenuBackground.setFitHeight(menuStage.getHeight());
        MenuBackground.setFitWidth(menuStage.getWidth());
        //make screen draggable only at the top
        menuStack.setOnMousePressed(e -> {
            menuStage.setX(e.getScreenX());
            menuStage.setY(e.getScreenY());
        });
        menuStack.setOnMouseDragged(e -> {
            menuStage.setX(e.getScreenX());
            menuStage.setY(e.getScreenY());
        });

        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.TOP_CENTER);
        menuLayout.setPadding(new Insets(0, 40, 40, 40));

        // --- SPACER TO PUSH DOWN SCORE BUTTONS ---
        Region spacerBetweenIconAndScores = new Region();
        spacerBetweenIconAndScores.setPrefHeight(30);

        Region spacerBelowScoreBoxes = new Region();
        VBox.setVgrow(spacerBelowScoreBoxes, Priority.ALWAYS);

        // Create Labels for X and O win counts
        xWinsText.setStyle("-fx-font-size: 32px; -fx-text-fill: #5a5a66;");
        xWinsText.setLayoutX(186);
        xWinsText.setLayoutY(111);

        oWinsText.setStyle("-fx-font-size: 32px; -fx-text-fill: #5a5a66;");
        oWinsText.setLayoutX(186);
        oWinsText.setLayoutY(178);

    // Add these to a Pane (which allows absolute positioning)
        Pane labelLayer = new Pane();
        labelLayer.getChildren().addAll(xWinsText, oWinsText);

        // Buttons
        Button btnResetScores = new Button();
        btnResetScores.setGraphic(new ImageView(FileAssets.RESET_SCORES));
        btnResetScores.setStyle("-fx-background-color: transparent;");
        btnResetScores.setOnMouseEntered(mouseEvent -> {btnResetScores.setGraphic(new ImageView(FileAssets.RESET_SCORES_HOVER));});
        btnResetScores.setOnMouseExited(mouseEvent -> {btnResetScores.setGraphic(new ImageView(FileAssets.RESET_SCORES));});
        btnResetScores.setOnAction(e -> {
            xWinCount = 0;
            oWinCount = 0;
            xWinsText.setText("0");
            oWinsText.setText("0");
        });

        Button btnExportScores = new Button();
        btnExportScores.setGraphic(new ImageView(FileAssets.EXPORT_SCORES));
        btnExportScores.setStyle("-fx-background-color: transparent;");
        btnExportScores.setOnMouseEntered(mouseEvent -> btnExportScores.setGraphic(new ImageView(FileAssets.EXPORT_SCORES_HOVER)));
        btnExportScores.setOnMouseExited(mouseEvent -> btnExportScores.setGraphic(new ImageView(FileAssets.EXPORT_SCORES)));
        btnExportScores.setOnAction(e -> exportScoresToFile());

        Button btnMenuClose = new Button();
        btnMenuClose.setGraphic(new ImageView(FileAssets.MENU_CLOSE));
        btnMenuClose.setStyle("-fx-background-color: transparent;");
        btnMenuClose.setOnMouseEntered(mouseEvent -> btnMenuClose.setGraphic(new ImageView(FileAssets.MENU_CLOSE_HOVER)));
        btnMenuClose.setOnMouseExited(mouseEvent -> btnMenuClose.setGraphic(new ImageView(FileAssets.MENU_CLOSE)));
        btnMenuClose.setOnAction(e -> menuStage.hide());

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(20, 20, 0, 20));
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.getChildren().add(btnMenuClose);

        VBox buttonBox = new VBox(20);
        buttonBox.setPadding(new Insets(0, 0, -20, 0));
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.getChildren().addAll(btnResetScores, btnExportScores);




        menuLayout.getChildren().addAll(
                topBar,
                spacerBetweenIconAndScores,
                spacerBelowScoreBoxes,
                buttonBox
        );

        menuStack.getChildren().addAll(MenuBackground, labelLayer, menuLayout);
        Scene menuScene = new Scene(menuStack, 265, 395); // use same dimensions
        menuStage.setScene(menuScene);
        menuStage.show();
    }

    private static void exportScoresToFile() {
        String fileName = "scores_log.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");


            writer.println("X Wins: " + xWinCount);
            writer.println("O Wins: " + oWinCount);
            writer.println("Timestamp: " + now.format(formatter));
            writer.println("----------------------------");

            System.out.println("Scores exported to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to score log: " + e.getMessage());
        }
    }

    private boolean isBoardFull(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Resets the `isClicked` array by setting all its elements to false.
     *
     * The `isClicked` array is a 2D boolean array used to track the click state
     * of grid buttons in the Tic-Tac-Toe game. This method iterates through
     * all rows and columns of the array and ensures every element is reset to
     * the default state of false.
     *
     * This method is typically called when the game is restarted to clear
     * any previous click states and prepare for a new game session.
     */
    private void restartIsClicked() {
    //reset isClicked values
        for(int i=0; i<isClicked.length; i++) {
            for(int j=0; j<isClicked[i].length; j++) {
                isClicked[i][j] = false;
            }
        }
    }

    /**
     * Resets the game board to its initial state with all cells set to -1.
     *
     * This method creates and returns a 3x3 integer array representing the Tic-Tac-Toe
     * board, where each element is initialized to -1. This state signifies that no moves
     * have been made and the board is empty.
     *
     * @return a 3x3 integer array initialized to -1, representing an empty Tic-Tac-Toe board
     */
    private int[][] resetBoard() {
        int[][] board = {
                {-1, -1, -1},
                {-1, -1, -1},
                {-1, -1, -1}
        };
        return board;
    }

}
