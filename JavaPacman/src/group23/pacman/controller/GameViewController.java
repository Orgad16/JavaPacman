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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ui.UIView;
import javafx.scene.image.ImageView;
import group23.pacman.controller.GameStateController;


import java.io.File;
import java.util.ArrayList;
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
    


    private Game game;

    private GameStateController gameStateController;

    private long holdTime;
    private long countDownTime;

    /* Reference to the main app */
    private MainApp mainApp;

    /* Paint to canvas */
    private GraphicsContext graphicsContext;

    /* Stores game state (paused, running)*/
    private boolean running = false;

    /* Essentially the game loop */
    private AnimationTimer animationLoop;

    /* Time */
    private long time = 0;

    /* Used to manipulate time for showing to screen */
    private Timer timer;

    /* Determines if the countdown timer is timed out, i.e game has started */
    private boolean countingDown;

    /* Recorded name of person who beats high score */
    private String name;

    /* Media variable for playing sound effect for pause/resume */
    private MediaPlayer mediaPlayer;


    private boolean timerPaused;

    // adapter used for joystick navigation
    UINavigationAdapter<ToggleButton> currentDialogAdapter = null;

    public GameViewController() {

        super("/group23/pacman/view/GameViewController.fxml");
        int numberOfPlayers = GameSettings.instance.getNumbrOfPlayers();
        int selectedMap = GameSettings.instance.getMap();
        Board.canvasWidth = (int) maze_canvas.getWidth();

        List<String> players = GameSettings.instance.getPlayerNames();
        int currentPlayer = 0;

        if (numberOfPlayers == 1) {
            String playerName = players.get(0);
            playerNameLabel.setText(playerName);
        }

        showLivesLeft(3);

        Media toggle = new Media(new File("bin/assets/sfx/toggle.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(toggle);
        mediaPlayer.setVolume(0.3);

        // init timer
        timer = new Timer(120);

        // init game with map and number of players
        game = new Game(selectedMap, numberOfPlayers, 0, 0);
        gameStateController = new GameStateController(this, game);


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

        draw(graphicsContext);

        startCountdown();

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
                    if (!countingDown() && !gamePaused())
                        game.getPacman().whip();

                    if(gamePaused() && currentDialogAdapter != null) {
                        int index = currentDialogAdapter.getY();
                        if(index == 0) {
                            // exit game
                            int pops = MainApp.getInstance().getNavigationStackSize();
                            for (int i = 0; i < pops - 1; i++) {
                                MainApp.getInstance().popViewController( i == pops - 2);
                            }
                        } else {
                            // resume
                            resumeGame();
                        }
                    }

                    break;
                case TWO:
                    // pause game
                    pauseGame();
                    break;
            }
        }
    }

    private void draw(GraphicsContext graphicsContext) {

        updateScore();

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

        ArrayList<TemporaryGhost> tempGhosts = gameStateController.getGame().getTempGhost();

        for (TemporaryGhost tempGhost : tempGhosts) {
            tempGhost.draw(graphicsContext);
        }
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

    public boolean countingDown() {

        return this.countingDown;
    }

    public boolean gamePaused() {

        return (!this.running);
    }

    public Timer getTimer() {

        return this.timer;
    }

    public void toggleState() {

		/* Can only toggle if not counting down and exit confirmation is not dispalyed */
        if (!countingDown && !gameStateController.escapePressed()) {
			/* When trying to pause,draw pause panel*/
            if (this.running == true) {
                pauseGame();
            }
            else {
                resumeGame();
            }
        }
    }

    public void pauseGame() {

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
					/* Calculate bonuses */
                    int time = timer.getTimeRemaining();
                    int lives = gameStateController.getGame().getPacman().getLives();
                    int score = gameStateController.getGame().getIntScore();
                    //mainApp.showResults(time,lives,score,gameStateController.getGame().getMap());

                    //TODO: add method in MainApp to show the time, lives and score result
                }
            }
        }.start();
    }

    private void playSfx() {
        mediaPlayer.setStartTime(Duration.ZERO);
        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.play();
    }

    public void startCountdown() {

        countingDown = true;
        timerPaused = false;

		/* Count down timer starts at 3 seconds but we need 2 extra seconds allow player to get ready */
        Timer timerStart = new Timer(5);

		/* The game is paused while counting down */
        running = false;

        countDownTime = System.currentTimeMillis();

        //show overlay with count down dialog
        DialogView dialogView = new DialogView();
        dialogView.titleLabel.setText("GAME STARTING IN");
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
                timer.countDown(1);
                setTimerLabel();
            }
            time = System.currentTimeMillis();
        }

    }

    public void setTimerLabel() {

        String time = String.valueOf(timer.getTimeRemaining());
        char mins = time.charAt(0);
        char tensSecs = time.charAt(1);
        char onesSecs = time.charAt(2);
        time = "0" + mins + ":" + tensSecs + onesSecs;
        timerLabel.setText(time);

    }

    /* Updates the images of score digits to reflect user's score */
    private void updateScore() {
		scoreLabel.setText(String.valueOf(game.getIntScore()));
    }
}