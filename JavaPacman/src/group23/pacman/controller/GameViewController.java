package group23.pacman.controller;

import group23.pacman.MainApp;
import group23.pacman.model.Board;
import group23.pacman.model.Game;
import group23.pacman.model.GameObject;
import group23.pacman.model.TemporaryGhost;
import group23.pacman.MainApp;
import group23.pacman.model.*;
import group23.pacman.view.DialogView;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import ui.UIView;
import javafx.scene.image.ImageView;
import group23.pacman.controller.GameStateController;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By Tony on 13/12/2018
 */
public class GameViewController extends RootController implements JoystickManager.JoystickListener{


    @FXML
    private Canvas maze_canvas;

    @FXML
    private Canvas game_canvas;

    // score
    @FXML
    private Label scoreLabel;

    // Life
    @FXML
    private ImageView lifeImage;

    @FXML
    private ImageView lifeImage1;

    @FXML
    private ImageView lifeImage2;

    // Timer
    @FXML
    private Label timerLabel;

    // Player Name
    @FXML
    private Label playerNameLabel;

    @FXML
    private UIView overlay;

    @FXML
    private Label whipLabel;

    private final static int initialMultiTimer = 15;


    private int currentPlayerIndex = 0;
    private GameStateController[] allGameStates = {null, null};
    private Game[] allGames = {null, null};
    private boolean[] playersStatus = {true, true};
    private String[] nameLabelColor = {"-fx-text-fill: #e74c3c;","-fx-text-fill: #3498db;"};

    private Game game;

    private GameStateController gameStateController;

    private long holdTime;
    private long countDownTime;

    /* Paint to canvas */
    private GraphicsContext graphicsContext;

    /* Stores game state (paused, running)*/
    private boolean running = false;

    /* Essentially the game loop */
    private AnimationTimer animationLoop;

    /* Time */
    private long time = 0;

    /* Used to manipulate time for showing to screen */
//    private Timer timer;

    /* Determines if the countdown timer is timed out, i.e game has started */
    private boolean countingDown;

    /* Recorded name of person who beats high score */
    private String name;

    /* Media variable for playing sound effect for pause/resume */
    private MediaPlayer mediaPlayer;


    private boolean timerPaused;

    public boolean duringQuestion = false;

    private boolean isPauseGameBtnClicked = false;

    // adapter used for joystick navigation
    UINavigationAdapter<ToggleButton> currentDialogAdapter = null;

    public GameViewController() {

        super("/group23/pacman/view/GameViewController.fxml");
        int numberOfPlayers = GameSettings.instance.getNumbrOfPlayers();
        int selectedMap = GameSettings.instance.getMap();
        Board.canvasWidth = (int) maze_canvas.getWidth();

        List<String> players = GameSettings.instance.getPlayerNames();
        int currentPlayer = currentPlayerIndex;

        if (numberOfPlayers == 1) {
            String playerName = players.get(0);
            playerNameLabel.setText(playerName);
            game = new Game(selectedMap, numberOfPlayers, 0, 0);
            game.setGameViewController(this);
            gameStateController = new GameStateController(this, game);
            gameStateController.setTimer(new Timer(120));

        } else {

            // init games and game state controllers for the players
            allGames[0] = new Game(selectedMap, 1, 0, 0);
            allGames[0].setGameViewController(this);
            allGames[1] = new Game(selectedMap, 1, 0,0);
            allGames[1].setGameViewController(this);
            allGameStates[0] = new GameStateController(this, allGames[0]);
            allGameStates[1] = new GameStateController(this, allGames[1]);

            // assign the current player to be the first one to play
            game = allGames[currentPlayer];
            gameStateController = allGameStates[currentPlayer];
            gameStateController.setTimer(new Timer(initialMultiTimer));
        }

        showLivesLeft(3);

        Media toggle = new Media(new File("bin/assets/sfx/toggle.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(toggle);
        mediaPlayer.setVolume(0.3);

        // init timer
//        timer = new Timer(120);

        // create reference
        graphicsContext = game_canvas.getGraphicsContext2D();

        // draw walls
        GraphicsContext gcWall = maze_canvas.getGraphicsContext2D();
        ArrayList<GameObject> objects = gameStateController.getGame().getOtherGameObjects();
        for (GameObject object : objects) {
            if (object.getType() == GameObject.TYPE.WALL) {
                object.draw(gcWall);
            }
        }

        gameStateController.update();

        // draw the characters and the objects of the game
        draw(graphicsContext);

        // showing countdown for the player to get ready
        startCountdown("Starting game in");

        // updating the name label of the current player
        updatePlayerName();
        
        updateWhipLabel();

        updateTimer();

        // starting the game
        startGame();

    }

    @Override
    public void didBecomeActive() {

        // register controller to joystick manager
        JoystickManager
                .shared
                .subscribe(identifier(), this);
    }

    @Override
    public void didEnterBackground() {
        JoystickManager.shared.unsubscribe(identifier());
    }

    @Override
    public void onJoystickTriggered(int joystickId, JoystickManager.Key selectedKey) {
        // TODO: handle joystick controller input

        if(joystickId == 1) {
            switch (selectedKey) {
                case UP:
                    if (!countingDown() && !gamePaused())
                        game.getPacman().queueMovement('U');
                    break;
                case DOWN:
                    if (!countingDown() && !gamePaused())
                        game.getPacman().queueMovement('D');
                    break;
                case LEFT:
                    if (!countingDown() && !gamePaused())
                        game.getPacman().queueMovement('L');

                    if(gamePaused() && currentDialogAdapter != null) {
                        currentDialogAdapter.current().setSelected(false);
                        currentDialogAdapter.move_left().setSelected(true);
                    }
                    break;
                case RIGHT:
                    if (!countingDown() && !gamePaused())
                        game.getPacman().queueMovement('R');

                    if(gamePaused() && currentDialogAdapter != null) {
                        currentDialogAdapter.current().setSelected(false);
                        currentDialogAdapter.move_right().setSelected(true);
                    }
                    break;
                case ONE:

                    if (!countingDown() && !gamePaused() && GameSettings.instance.isTonugeEnabled())
                        game.getPacman().whip();

                    if ((gamePaused() && currentDialogAdapter != null && !duringQuestion) ||
                            ((game.levelCleared() || game.getPacman().getLives() == 0))) {
                        int index = currentDialogAdapter.getY();
                        isPauseGameBtnClicked = false;
                        if(index == 0) {
                            // exit game
                            popWindows();
                            stopGame();
                        } else {
                            // resume
                            resumeGame();
                        }
                    }

                    if (duringQuestion) {
                        if (isPauseGameBtnClicked) {
                            isPauseGameBtnClicked = false;
                            int index = currentDialogAdapter.getY();
                            if(index == 0) {
                                // exit game
                                popWindows();
                                stopGame();

                            } else {
                                // resume
                                resumeGame();
                            }
                        }
                        else {
                            resumeGame();
                        }
                    }


                    break;
                case TWO:
                    // pause game
                    if (running && game.getPacman().getState() != Pacman.STATE.DEATH_ANIMATION && game.getPacman().getState() != Pacman.STATE.DEAD) {
                        isPauseGameBtnClicked = true;
                        pauseGame();
                    }
                    break;
            }
        }
    }

    private void popWindows() {
        int pops = MainApp.getInstance().getNavigationStackSize();
        for (int i = 0; i < pops - 1; i++) {
            MainApp.getInstance().popViewController( i == pops - 2);
        }
    }

    private void draw(GraphicsContext graphicsContext) {

        updateScore();

        updateWhipLabel();

        gameStateController.getGame().getPacman().draw(graphicsContext);

		/* Draws other objects (pellets) */
        ArrayList<GameObject> objects = gameStateController.getGame().getOtherGameObjects();

        for (GameObject object : objects) {
            if (object.getType() != GameObject.TYPE.WALL)
                object.draw(graphicsContext);
        }

        gameStateController.getGame().getGhost().draw(graphicsContext);
        gameStateController.getGame().getGhost2().draw(graphicsContext);
        gameStateController.getGame().getGhost3().draw(graphicsContext);

        gameStateController.getGame().getPoisonPellet().draw(graphicsContext);

        ArrayList<TemporaryGhost> tempGhosts = gameStateController.getGame().getTempGhost();
        if (!duringQuestion) {
            // handle not during question situation

            // draw the pellets
            if (tempGhosts.size() == 0) {
                gameStateController.getGame().getQuestionPellet().draw(graphicsContext);
            }

            // set up question view with the ghosts
            if (tempGhosts.size() > 0)
                setUpQuestionView(tempGhosts.get(0).getQuestion());

        } else {
            // handle during question situation
            for (TemporaryGhost tempGhost : tempGhosts) {
                tempGhost.draw(graphicsContext);
            }
        }

        // if there are no temp ghosts -> we ran into temp ghost -> not answering question
        if (tempGhosts.size() == 0) {
            duringQuestion = false;
        }


    }


    public void setUpQuestionView(Question q) {

        // creating the dialog for the question
        DialogView dialogView = new DialogView();
        dialogView.titleLabel.setText("Question");
        dialogView.descriptionLabel.setText(q.getQuestionID());

        // vbox to hold the ghosts and the answers
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        // hbox to hold the first answer with the ghost assigned to it
        HBox firstHbox = new HBox();
        firstHbox.setAlignment(Pos.CENTER_LEFT);
        firstHbox.setSpacing(20);

        // hbox to hold the second answer with the ghost assigned to it
        HBox secondHbox = new HBox();
        secondHbox.setAlignment(Pos.CENTER_LEFT);
        secondHbox.setSpacing(20);

        // hbox to hold the third answer with the ghost assigned to it
        HBox thirdHbox = new HBox();
        thirdHbox.setAlignment(Pos.CENTER_LEFT);
        thirdHbox.setSpacing(20);

        HBox fourthHbox = new HBox();
        fourthHbox.setAlignment(Pos.CENTER_LEFT);
        fourthHbox.setSpacing(20);

        // three buttons for the answers
        Label answer1 = new Label();
        answer1.getStyleClass().add("label-retro");
        answer1.setStyle("-fx-font-size: 18px");

        Label answer2 = new Label();
        answer2.getStyleClass().add("label-retro");
        answer2.setStyle("-fx-font-size: 18px");

        Label answer3 = new Label();
        answer3.getStyleClass().add("label-retro");
        answer3.setStyle("-fx-font-size: 18px");

        Label answer4 = new Label();
        answer4.getStyleClass().add("label-retro");
        answer4.setStyle("-fx-font-size: 18px");

        ToggleButton okBtn = new ToggleButton("Start Chasing");
        okBtn.getStyleClass().add("button-retro");
        okBtn.setAlignment(Pos.CENTER);

        // the answers for the question
        ArrayList<String> answers = q.getAnswers();
        int index = 4;
        int btnIndex = 1;
        for (String answer : answers) {

            // set the image of the ghost
            ImageView ghost = new ImageView(new Image("assets/Ghost/ghost" + index + "Right1.png"));
            ghost.setFitHeight(50.0);
            ghost.setFitWidth(50.0);

            // set the button with the answer
            switch (btnIndex) {
                case 1:
                    answer1.setText(answer);
                    firstHbox.getChildren().addAll(ghost, answer1);
                    vBox.getChildren().add(firstHbox);
                    break;
                case 2:
                    answer2.setText(answer);
                    secondHbox.getChildren().addAll(ghost, answer2);
                    vBox.getChildren().add(secondHbox);
                    break;
                case 3:
                    answer3.setText(answer);
                    thirdHbox.getChildren().addAll(ghost, answer3);
                    vBox.getChildren().add(thirdHbox);
                    break;
                case 4:
                    answer4.setText(answer);
                    fourthHbox.getChildren().addAll(ghost, answer4);
                    vBox.getChildren().add(fourthHbox);
            }
            index++;
            btnIndex++;
        }

        // add the hboxs to the vBox container
        vBox.getChildren().addAll(okBtn);

        dialogView.contentView.getChildren().add(vBox);

        currentDialogAdapter = new UINavigationAdapter<>();
        currentDialogAdapter.addRow(okBtn);
        currentDialogAdapter.move_right().setSelected(true);

        overlay.getChildren().add(dialogView);
        overlay.setVisible(true);


        // to exit the question view with a key
        duringQuestion = true;

        // stoping the game
        this.running = false;

    }



    public void stopTimer(boolean pauseTimer) {

        timerPaused = pauseTimer;
    }

    /* Draws to the screen how many lives the player has left */
    public void showLivesLeft(int number) {

        // reset the lives of the pacman
        String img = "assets/misc/empty.png";
        for (int i=0; i<3; i++) {
            setLivesImage(img, i);
        }

        // adding the current number of lives left for pacman
        String img1 = "assets/Pacman/leftOpen.png";
        for (int i = 0; i < number; i++) {
            setLivesImage(img1, i);
        }
    }

    public void setLivesImage(String img, int number) {

        switch (number) {
            case 0 :
                lifeImage2.setImage(new Image(img));
                break;
            case 1 :
                lifeImage1.setImage(new Image(img));
                break;
            case 2 :
                lifeImage.setImage(new Image(img));
                break;
        }
    }

    public void updateWhipLabel() {
        if (GameSettings.instance.isTonugeEnabled()) {
            whipLabel.setText(String.valueOf(game.getPacman().getWhip().getCharges()));
            whipLabel.setStyle(nameLabelColor[currentPlayerIndex] + "-fx-font-size: 40px;");
        } else {
            whipLabel.setVisible(false);
        }
    }

    public boolean countingDown() {

        return this.countingDown;
    }

    public boolean gamePaused() {

        return (!this.running);
    }

    public void pauseGame() {

        // creating a dialog box and enter exit question and confirm
        DialogView dialogView = new DialogView();
        dialogView.titleLabel.setText("Pause");
        dialogView.descriptionLabel.setText("Exit game?");

        ToggleButton exitBtn = new ToggleButton("Exit");
        exitBtn.getStyleClass().add("button-retro");

        ToggleButton resumeBtn = new ToggleButton("Resume");
        resumeBtn.getStyleClass().add("button-retro");

        HBox box = new HBox();
        box.getChildren().addAll(exitBtn,resumeBtn);
        box.setSpacing(10);

        dialogView.contentView.getChildren().add(box);

        // setup navigation adapter
        currentDialogAdapter = new UINavigationAdapter<>();
        currentDialogAdapter.addRow(exitBtn,resumeBtn);
        currentDialogAdapter.move_right().setSelected(true);

        // add to overlay the dialog and show
        overlay.getChildren().add(dialogView);
        overlay.setVisible(true);

        this.running = false;
        playSfx();
    }

    public void resumeGame() {

        //pauseOverlay.clearRect(0,0,1366,768);
        currentDialogAdapter = null;
        overlay.getChildren().clear();
        overlay.setVisible(false);

        this.running = true;
        playSfx();
    }

    public void stopGame() {

        running = false;
        animationLoop.stop();
    }

    public void showGameEnd() {

        stopGame();
        holdFrame();
    }

    private void holdFrame() {


        Timer holdTimer = new Timer(2);
        holdTime = System.currentTimeMillis();

        new AnimationTimer() {

            public void handle(long now) {


                if (System.currentTimeMillis() - holdTime >= 1000) {
                    holdTimer.countDown(1);
                    holdTime = System.currentTimeMillis();

                }
                if (holdTimer.timedOut()) {
                    this.stop();

                    showResultGame();
                }
            }
        }.start();
    }

    private void playSfx() {
        mediaPlayer.setStartTime(Duration.ZERO);
        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.play();
    }

    public void startCountdown(String message) {

        countingDown = true;
        timerPaused = false;

		/* Count down timer starts at 3 seconds but we need 2 extra seconds allow player to get ready */
        Timer timerStart = new Timer(5);

		/* The game is paused while counting down */
        running = false;

        countDownTime = System.currentTimeMillis();

        //show overlay with count down dialog
        DialogView dialogView = new DialogView();
        dialogView.titleLabel.setText(message);
        dialogView.descriptionLabel.setText("3");

        // add to overlay
        overlay.getChildren().add(dialogView);

        // reveal overlay
        overlay.setVisible(true);

        new AnimationTimer() {

            public void handle(long now) {

				/* While not in exit confirmation screen */
                if (!gameStateController.escapePressed()) {

					/* Count down every second */
                    if (System.currentTimeMillis() - countDownTime >= 1000) {

                        timerStart.countDown(1);
                        countDownTime = System.currentTimeMillis();

						/* Wait for timer to count from 5 seconds to 3 seconds before removing "Ready" sign.
						 * Add 48 because getSecOnes() method returns seconds + 48 for ascii value */
                        if (timerStart.getSecOnes() <= 3 + 48) {

                            // update the dialog to the current time
                            char time = (char) timerStart.getSecOnes();
                            dialogView.descriptionLabel.setText("" + time);
                        }
                    }
					/* After time has counted to 0, start the game */
                    if (timerStart.timedOut()) {
                        this.stop();
                        //countdownGraphicsContext.clearRect(0,0,1366,768);
                        // remove dialog and hide overlay
                        overlay.getChildren().remove(dialogView);
                        overlay.setVisible(false);

                        running = true;
                        countingDown = false;
                    }
                }
                else {
                    countDownTime = System.currentTimeMillis();
                }

            }

        }.start();

    }

    public void startGame() {

        time = System.currentTimeMillis();

		/* Animation timer to update frames */
        animationLoop = new AnimationTimer() {
            public void handle(long now) {

				/* Make sure game isn't in paused state */
                if (running) {
                    //graphicsContext.clearRect(0, 0, 1366, 768)

                    graphicsContext.clearRect(0, 0, game_canvas.getWidth(), game_canvas.getHeight());
                    gameStateController.update();
                    draw(graphicsContext);
                    updateTimer();
                }
                else {
                    time = System.currentTimeMillis();
                }
            }
        };
        animationLoop.start();

    }

    private void updateTimer() {

        if (System.currentTimeMillis() - time >= 1000) {
            if (!timerPaused) {
                gameStateController.getTimer().countDown(1);
                setTimerLabel(gameStateController.getTimer().getTimeRemaining());
            }
            time = System.currentTimeMillis();
        }

        if (gameStateController.gameOver() && GameSettings.instance.getNumbrOfPlayers() == 1)
            showGameEnd();
    }

    public void setTimerLabel(int timer) {

        String newTime = getStringFormatedTimer(timer);

        timerLabel.setText(newTime);

    }

    public String getStringFormatedTimer(int timer) {
        int mins;
        int tensSecs;
        String newTime;

        mins = timer / 60;
        newTime = "0" + String.valueOf(mins) + ":";

        tensSecs = timer - mins*60;

        if (tensSecs < 10) {
            newTime += "0" + String.valueOf(tensSecs);
        } else {
            newTime += String.valueOf(tensSecs);
        }
        return newTime;
    }

    /* Updates the images of score digits to reflect user's score */
    private void updateScore() {
		scoreLabel.setText(String.valueOf(game.getIntScore()));
    }


    public void showResultGame() {

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        // init a dialog view for the result game status
        DialogView dialogView = new DialogView();

        VBox containerVbox = VboxFactory(Pos.CENTER);
        HBox hBox = null;

        // handle when the pacman won or lose the level

        String dialogViewText = "LEVEL FAILED";
        String dialogViewStyle = "-fx-font-size:40px; -fx-text-fill:red;";
        String dialogViewDescription = " ";

        if (GameSettings.instance.getNumbrOfPlayers() > 1) {

            // handle multiplayer
            if (game.levelCleared() || (allGames[getOtherPlayer()] != null && allGames[getOtherPlayer()].levelCleared())) {
                // level is cleared

                // checking the other player if he cleared the lever or not
                // if he didnt cleared it -> we know that the current player won and we will save his game
                boolean checkOtherPlayerClearence = allGames[getOtherPlayer()] != null && allGames[getOtherPlayer()].levelCleared();

                // saving the current player's game
                if (!checkOtherPlayerClearence || game.getIntScore() >= allGames[getOtherPlayer()].getIntScore()) {

                } else if (checkOtherPlayerClearence && game.levelCleared()){
                    if (game.getIntScore() <= allGames[getOtherPlayer()].getIntScore())
                        currentPlayerIndex = getOtherPlayer();
                }

                // set the game to winner player
                game = allGames[currentPlayerIndex];

                // set the gameStateController to winner player
                gameStateController = allGameStates[currentPlayerIndex];

                // getting the info we need to update the json file and the level score
                String playerName = GameSettings.instance.getPlayerNames().get(currentPlayerIndex);
                int game_score = game.getIntScore() + 100;
                String game_timer = getStringFormatedTimer(120 - gameStateController.getTimer().getTimeRemaining());

                // saving the game into json file
                Score score = new Score(playerName, game_score, game_timer, format.format(new Date()));
                saveGame(score);

                // setting the dialog with the current info
                game.setScore(game_score);
                dialogViewText = "LEVEL CLEARED";
                dialogViewStyle = "-fx-font-size:40px; -fx-text-fill:green;";

                // checking which player one and print
                dialogViewDescription = "Player " + playerName + " Won!";

            }
        } else {
            // handle one player
            if (game.levelCleared()) {
                // game level cleared
                game.setScore(game.getIntScore() + 100);
                dialogViewText = "LEVEL CLEARED";
                dialogViewStyle = "-fx-font-size:40px; -fx-text-fill:green;";

                dialogView.descriptionLabel.setText("YOU WON THE LEVEL!");
                Score score = new Score(GameSettings.instance.getPlayerNames().get(0), game.getIntScore(), getStringFormatedTimer(120 - gameStateController.getTimer().getTimeRemaining()), format.format(new Date()));
                saveGame(score);
            }
            // only for testing
//            else {
//                Score score = new Score(GameSettings.instance.getPlayerNames().get(0), game.getIntScore(), getStringFormatedTimer(120 - gameStateController.getTimer().getTimeRemaining()), format.format(new Date()));
//                saveGame(score);
//            }
        }


        dialogView.titleLabel.setText(dialogViewText);
        dialogView.titleLabel.setStyle(dialogViewStyle);
        dialogView.descriptionLabel.setText(dialogViewDescription);

        // getting the game results for single and multiplayer
        hBox = showLevelResults();

        // create an ok button to continue
        ToggleButton okBtn = retroButtonFactory("MENU");
        okBtn.setAlignment(Pos.CENTER);

        // setup navigation adapter
        currentDialogAdapter = new UINavigationAdapter<>();
        currentDialogAdapter.addRow(okBtn);
        currentDialogAdapter.move_right().setSelected(true);

        // adding all element to the container
        containerVbox.getChildren().addAll(hBox, okBtn);

        // adding the container into the dialog
        dialogView.contentView.getChildren().add(containerVbox);

        // show and set the overlay that cover the map
        overlay.getChildren().add(dialogView);
        overlay.setVisible(true);
    }


    public void saveGame(Score score) {
//        String playerName = GameSettings.instance.getPlayerNames().get(currentPlayerIndex);
        switch (GameSettings.instance.getNumbrOfPlayers()) {
            case 1:
                game.saveGameIntoJson(score);
                break;
            case 2:
                allGames[currentPlayerIndex].saveGameIntoJson(score);
                break;
        }


    }

    // show the game result after winning the level
    public HBox showLevelResults() {

        HBox containerHbox = HboxFactory(Pos.CENTER);

        // -------------------------------------------------------------------
        // construct the first column
        // -------------------------------------------------------------------
        VBox firstColumnContainer = VboxFactory(Pos.CENTER);
        Label constNameLabel = new Label("NAME");
        Label firstPlayerNameLabel = new Label(GameSettings.instance.getPlayerNames().get(currentPlayerIndex));

        // check if we have multiplayer scoreboard
        if (GameSettings.instance.getNumbrOfPlayers() > 1) {

            // setting the container with the other player
            Label secondPlayerNameLabel = new Label(GameSettings.instance.getPlayerNames().get(getOtherPlayer()));
            firstColumnContainer = insertElementsIntoConstainer(firstColumnContainer, constNameLabel, firstPlayerNameLabel, secondPlayerNameLabel);
        } else {

            // only one player is playing
            firstColumnContainer = insertElementsIntoConstainer(firstColumnContainer, constNameLabel, firstPlayerNameLabel);
        }
        containerHbox.getChildren().add(firstColumnContainer);


        // -------------------------------------------------------------------
        // construct the second column
        // -------------------------------------------------------------------
        VBox secondColumnContainer = VboxFactory(Pos.CENTER);
        Label constScoreLabel = new Label("SCORE");
        constScoreLabel.setTextFill(Color.YELLOW);
        Label firstPlayerScoreLabel = new Label(String.valueOf(game.getIntScore()));
        if (GameSettings.instance.getNumbrOfPlayers() > 1) {
            Label secondplayerScoreLabel = new Label(String.valueOf(allGames[getOtherPlayer()].getIntScore()));
            secondColumnContainer = insertElementsIntoConstainer(secondColumnContainer, constScoreLabel, firstPlayerScoreLabel, secondplayerScoreLabel);
        } else{
            secondColumnContainer = insertElementsIntoConstainer(secondColumnContainer, constScoreLabel, firstPlayerScoreLabel);
        }
        containerHbox.getChildren().add(secondColumnContainer);


        // -------------------------------------------------------------------
        // construct the third column
        // -------------------------------------------------------------------
        VBox thirdColumnContainer = VboxFactory(Pos.CENTER);
        Label constTimeLabel = new Label("TIME");
        constTimeLabel.setTextFill(Color.YELLOW);
        Label firstPlayerTimeLabel = new Label(getStringFormatedTimer(120-gameStateController.getTimer().getTimeRemaining()));
        if (GameSettings.instance.getNumbrOfPlayers() > 1) {
            Label secondplayerTimeLabel = new Label(getStringFormatedTimer(allGameStates[getOtherPlayer()].getTimer().getTimeRemaining()));
            thirdColumnContainer = insertElementsIntoConstainer(thirdColumnContainer, constTimeLabel, firstPlayerTimeLabel, secondplayerTimeLabel);
        } else {
            thirdColumnContainer = insertElementsIntoConstainer(thirdColumnContainer, constTimeLabel, firstPlayerTimeLabel);
        }
        containerHbox.getChildren().add(thirdColumnContainer);


        return containerHbox;

    }

    public VBox insertElementsIntoConstainer(VBox Vcontainer, Label...labels) {

        int index = 0;
        for (Label label : labels) {
            if (index == 0) {
                label.setStyle("-fx-font-size: 20px; -fx-text-fill:yellow;");
            } else {
                label.setStyle("-fx-font-size: 18px;");
            }
            label.getStyleClass().add("label-retro");
            index++;
        }

        for (Label label : labels) {
            Vcontainer.getChildren().add(label);
        }

        return Vcontainer;
    }


    public ToggleButton retroButtonFactory(String text) {
        ToggleButton btn = new ToggleButton(text);
        btn.getStyleClass().add("button-retro");
        return btn;
    }

    public Label retroLabelFactory(String text, int fontSize, String fontColor) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size:" + fontSize + "px;" + (fontColor == null ? "" : "-fx-text-fill:" + fontColor + ";"));
        return label;
    }

    public VBox VboxFactory(Pos position) {
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(position);
        return vBox;
    }

    public HBox HboxFactory(Pos position) {
        HBox hBox = new HBox();
        hBox.setSpacing(35);
        hBox.setAlignment(position);
        return hBox;
    }

    public int handleSwitch(boolean isDone, boolean timeOut) {

        // check number of players in game settings
        int numberOfPlayers = GameSettings.instance.getNumbrOfPlayers();

        if (numberOfPlayers == 1) { return 1; }

        // if the player cleared or failed the level
        if (isDone) {
            //change the status of the player to false -> will not switch back to it
            playersStatus[currentPlayerIndex] = false;
            if (!playersStatus[getOtherPlayer()]) {

                // return because the other player is dead/done -> go the leaderboard
                showResultGame();

                return 2;
            }
            currentPlayerIndex = getOtherPlayer();

            // hide timer because it is meaningless with only one player alive.
            timerLabel.setVisible(false);
        }

        // if the timer (of 40 secs) ran out
        if (timeOut)
            if (playersStatus[getOtherPlayer()])
                currentPlayerIndex = getOtherPlayer();

        switchPlayer();

        return 2;

    }

    public void switchPlayer() {

        // stoping the current game
        animationLoop.stop();
        game = allGames[currentPlayerIndex];
        gameStateController = allGameStates[currentPlayerIndex];
        gameStateController.setTimer(new Timer(initialMultiTimer));

        // updating the score
        updateScore();

        // updating player's life
        showLivesLeft(game.getPacman().getLives());

        // update new timer
        updateTimer();

        // TODO: update whip label
        updateWhipLabel();

        // update player's name
        updatePlayerName();

        // setting the countdown with the current player
        String playerName = GameSettings.instance.getPlayerNames().get(currentPlayerIndex);
        startCountdown(playerName + " game is starting in");

        // starting the game
        animationLoop.start();
    }

    public void updatePlayerName() {
        String name = GameSettings.instance.getPlayerNames().get(currentPlayerIndex);
        playerNameLabel.setText(name);
        playerNameLabel.setStyle(nameLabelColor[currentPlayerIndex] + "-fx-font-size: 40px;");
    }

    public int getOtherPlayer() {
        if (currentPlayerIndex == 1)
            return 0;
        else
            return 1;
    }

    public void setDuringQuestion(boolean duringQuestion) {
        this.duringQuestion = duringQuestion;
    }
}