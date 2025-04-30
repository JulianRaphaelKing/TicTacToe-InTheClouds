package com.example.portfilioproject;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URISyntaxException;


public class TicTacToeApp extends Application {


    //Private Variables
    private BorderPane mainPane;
    private Boolean preview = false;
    private char currentPlayer = 'X';
    private boolean[][] isClicked = new boolean[3][3];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //load images
        FileAssets.loadFiles();

        //load music & mediaPlayers:
//        Media regMusic = null;
//        try {
//            regMusic = new Media(getClass().getResource("/images/bkgMusic.wav").toURI().toString());
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
        //Media fastMusic = new Media("images/bkgMusicSpeed.wav");
//        MediaPlayer mediaPlayer = new MediaPlayer(regMusic);
//        //MediaPlayer fastMediaPlayer = new MediaPlayer(fastMusic);
//        mediaPlayer.setVolume(0.5);
//        mediaPlayer.play();


        //set scene to main screen
        Scene scene = new Scene(getMainPane(primaryStage), 675, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Title Screen");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setHeight(800);
        primaryStage.setWidth(675);
        primaryStage.show();
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
        //Sound State
        BooleanProperty isMuted = new SimpleBooleanProperty(false);

        //background
        ImageView background = new ImageView(FileAssets.BACKGROUND);
        background.setFitWidth(675);
        background.setFitHeight(800);
        background.setPreserveRatio(false);

        mainPane = new BorderPane();

        //create horizontal box
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setSpacing(18);
        topBar.setPadding(new Insets(18, 32, 10, 10));

        //Create children - for hBox

        //Button Close
        Button btnClose = new Button();
        btnClose.setGraphic(new ImageView(FileAssets.CLOSE));
        btnClose.setStyle("-fx-background-color: transparent; -fx-padding: 0");
        btnClose.setOnAction(e -> primaryStage.close());
        btnClose.setOnMouseEntered(e -> btnClose.setGraphic(new ImageView(FileAssets.CLOSE_HOVER)));
        btnClose.setOnMouseExited(e -> btnClose.setGraphic(new ImageView(FileAssets.CLOSE)));

        //Button Minimize
        Button btnMin = new Button();
        btnMin.setGraphic(new ImageView(FileAssets.MINIMIZE));
        btnMin.setStyle("-fx-background-color: transparent; -fx-padding: 0");
        btnMin.setOnAction(e -> primaryStage.setIconified(true));
        btnMin.setOnMouseEntered(e -> btnMin.setGraphic(new ImageView(FileAssets.MINIMIZE_HOVER)));
        btnMin.setOnMouseExited(e -> btnMin.setGraphic(new ImageView(FileAssets.MINIMIZE)));

        //Button Mute
        Button btnMute = new Button();
        btnMute.setGraphic(new ImageView(FileAssets.MUTE));
        btnMute.setStyle("-fx-background-color: transparent; -fx-padding: 0");
        btnMute.setOnAction(e -> isMuted.set(!isMuted.get()));

        btnMute.setOnMouseEntered(e -> {
            if(isMuted.get()) {
                btnMute.setGraphic(new ImageView(FileAssets.MUTE));
            } else {
                btnMute.setGraphic(new ImageView(FileAssets.MUTE_HOVER));
                btnMute.setGraphic(new ImageView(FileAssets.MUTE_HOVER));
            }
        });
        btnMute.setOnMouseExited(e -> {
            if(isMuted.get()) {
                btnMute.setGraphic(new ImageView(FileAssets.MUTE_HOVER));
            } else {
                btnMute.setGraphic(new ImageView(FileAssets.MUTE));
            }
        });

        isMuted.addListener((observable, oldValue, newValue) -> {
            if (isMuted.get()) {
                btnMute.setGraphic(new ImageView(FileAssets.MUTE_HOVER));
            } else {
                btnMute.setGraphic(new ImageView(FileAssets.MUTE));
            }
        });

        //add hBox to top of the screen
        topBar.getChildren().addAll(btnMute, btnMin, btnClose);

        //make screen draggable only at the top
        topBar.setOnMousePressed(e -> {
            primaryStage.setX(e.getScreenX());
            primaryStage.setY(e.getScreenY());
        });
        topBar.setOnMouseDragged(e -> {
            primaryStage.setX(e.getScreenX());
            primaryStage.setY(e.getScreenY());
        });
        //apply menu bar to the top of the border pane
        mainPane.setTop(topBar);

        //apply title pane to the center of the border pane
        mainPane.setCenter(getTitlePane());

        //wrap everything with StackPane
        StackPane paneLayering = new StackPane();
        paneLayering.getChildren().addAll(background, mainPane);
        return paneLayering;
    }


    /**
     * Creates and returns a pane containing the title screen components such as a background image
     * and a start button with hover functionality that dynamically updates the displayed graphics.
     *
     * @return a StackPane containing the title screen layout with interactive start button
     */
    private Pane getTitlePane() {
        //Create center of the screen
        ImageView clouds = new ImageView(FileAssets.BACKGROUND_CLOUDS);
        Button btnStart = new Button();
        btnStart.setGraphic(new ImageView(FileAssets.START));
        btnStart.setStyle("-fx-background-color: transparent; -fx-padding: 0");

        //Handle hover actions for button start
        BooleanProperty isStartHovered = new SimpleBooleanProperty(false);
        isStartHovered.addListener((observable, oldValue, newValue) -> {
            if(isStartHovered.get()) {
                clouds.setImage(FileAssets.BACKGROUND_CLOUDS_HOVER);
                btnStart.setGraphic(new ImageView(FileAssets.START_HOVER));
                //mediaPlayer.stop();
                //fastMediaPlayer.play();

            } else {
                clouds.setImage(FileAssets.BACKGROUND_CLOUDS);
                btnStart.setGraphic(new ImageView(FileAssets.START));
                //fastMediaPlayer.stop();
                //mediaPlayer.play();
            }
        });
        btnStart.setOnMouseEntered(e -> isStartHovered.set(true));
        btnStart.setOnMouseExited(e -> isStartHovered.set(false));
        btnStart.setOnAction(e -> {
            mainPane.setCenter(getGamePane());
        });

        //layer for everything that belongs in the center
        StackPane centerLayering = new StackPane();
        centerLayering.getChildren().addAll(clouds, btnStart);

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
    private Pane getGamePane() {
        int board[][] = resetBoard();
        currentPlayer = 'X';

        //create grid to host buttons
        GridPane grid = new GridPane();
        grid.setPrefSize(465, 465);
        grid.setAlignment(Pos.CENTER);

        //Set row & column sizes - 465 total size / 3
        for(int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints(155);
            RowConstraints row = new RowConstraints(155);
            grid.getColumnConstraints().add(col);
            grid.getRowConstraints().add(row);
        }

        //Add the buttons for grid
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                Button gridBtn = new Button();
                gridBtn.setMinSize(155, 155);
                gridBtn.setStyle("-fx-background-color: transparent; -fx-padding: 0");
                int row = i;
                int col = j;

                //when grid button is clicked, call turns method
                gridBtn.setOnAction(e -> {
                    if(!isClicked[row][col]) {
                        turns(row, col, gridBtn, currentPlayer, board);
                        currentPlayer = currentPlayer == 'X' ? 'O' : 'X';
                    }

                });

                //grid button hovered preview
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
                //add button to grid and the 2D array
                grid.add(gridBtn, i, j);
            }
        }
        //stack all elements
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
    private void turns(int row, int col, Button gridBtn, char currentPlayer, int[][] board){
        gridBtn.setGraphic(new ImageView(currentPlayer == 'X' ? FileAssets.X : FileAssets.O));
        isClicked[row][col] = true;
        board[row][col] = currentPlayer == 'X' ? 0 : 1;
        if(checkWin(row, col, currentPlayer, board)) {
            mainPane.setCenter(currentPlayer == 'X' ? getXWinsPane() : getOWinsPane());
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
        int player;
        if(currentPlayer == 'X') {
            player = 0;
        } else {
            player = 1;
        }
        // First Row
        if ( (board[0][0] == player && board[0][1] == player && board[0][2] == player) ) {
            return true;
        }
        // Second Row
        else if( (board[1][0] == player && board[1][1] == player && board[1][2] == player) ) {
            return true;
        }
        // Third Row
        else if( (board[2][0] == player && board[2][1] == player && board[2][2] == player) ) {
            return true;
        }
        // Diag top left down
        else if( (board[0][0] == player && board[1][1] == player && board[2][2] == player) ) {
            return true;
        }
        // Diag Right
        else if( (board[0][2] == player && board[1][1] == player && board[2][0] == player) ) {
            return true;
        }
        // First col down
        else if( (board[0][0] == player && board[1][0] == player && board[2][0] == player) ) {
            return true;
        }
        // Second col down
        else if( (board[0][1] == player && board[1][1] == player && board[2][1] == player) ) {
            return true;
        }
        // Third col down
        else if( (board[0][2] == player && board[1][2] == player && board[2][2] == player) ) {
            return true;
        }
        return false;
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
    private Pane getXWinsPane() {
        StackPane winnerLayer = new StackPane();

        ImageView bkgDay = new ImageView(FileAssets.BKG_DAY);

        ImageView winner = new ImageView(FileAssets.PLR_X_MSG);

        Button restart = new Button();
        restart.setGraphic(new ImageView(FileAssets.RESTART));
        restart.setStyle("-fx-background-color: transparent; -fx-padding: 0");
        restart.setOnAction(e -> {
            restartIsClicked();
            mainPane.setCenter(getTitlePane());
        });
        restart.setOnMouseEntered(mouseEvent -> {
            restart.setGraphic(new ImageView(FileAssets.RESTART_HOVER));
        });
        restart.setOnMouseExited(mouseEvent -> {
            restart.setGraphic(new ImageView(FileAssets.RESTART));
        });

        VBox messageContainer = new VBox();
        messageContainer.setAlignment(Pos.CENTER);
        messageContainer.setSpacing(25);
        messageContainer.getChildren().addAll(winner, restart);

        winnerLayer.getChildren().addAll(bkgDay, messageContainer);

        return winnerLayer;
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
    private Pane getOWinsPane() {
        StackPane winnerLayer = new StackPane();

        ImageView bkgNight = new ImageView(FileAssets.BKG_NIGHT);

        ImageView winner = new ImageView(FileAssets.PLR_O_MSG);

        Button restart = new Button();
        restart.setGraphic(new ImageView(FileAssets.RESTART));
        restart.setStyle("-fx-background-color: transparent; -fx-padding: 0");
        restart.setOnAction(e -> {
            restartIsClicked();
            mainPane.setCenter(getTitlePane());
        });
        restart.setOnMouseEntered(mouseEvent -> {
            restart.setGraphic(new ImageView(FileAssets.RESTART_HOVER));
        });
        restart.setOnMouseExited(mouseEvent -> {
            restart.setGraphic(new ImageView(FileAssets.RESTART));
        });

        VBox messageContainer = new VBox();
        messageContainer.setAlignment(Pos.CENTER);
        messageContainer.setSpacing(25);
        messageContainer.getChildren().addAll(winner, restart);

        winnerLayer.getChildren().addAll(bkgNight, messageContainer);

        return winnerLayer;
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
