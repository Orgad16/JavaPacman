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
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ui.UIView;
import javafx.scene.image.ImageView;
import group23.pacman.controller.GameStateController;



import java.util.ArrayList;

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
    private Label lifeLabel;

    // Timer
    @FXML
    private Label timerLabel;

    // Player Name
    @FXML
    private Label playerNameLabel;

    @FXML
    private UIView overlay;
    


    private Game game;


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


    /* GraphicsContext for separate canvas which is used to paint the countdown timer */
    private GraphicsContext countdownGraphicsContext;

    /* GraphicsContext for separate canvas which is used to paint the pause_overlay */
    private GraphicsContext pauseOverlay;

    /* GraphicsContext for separate canvas which is used to paint the exit confirmation screen */
    private GraphicsContext exitPrompt;

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
    UINavigationAdapter<Canvas> gameViewAdapter = new UINavigationAdapter<>();

    public GameViewController() {

        super("/group23/pacman/view/GameViewController.fxml");
        int numberOfPlayers = GameSettings.instance.getNumbrOfPlayers();
        int selectedMap = GameSettings.instance.getMap();

        //TODO: complete game setup

        //-----------------------------
        // how to create a dialog view
//        DialogView dialogView = new DialogView();
//
//        dialogView.titleLabel.setText("QUIT");
//        dialogView.descriptionLabel.setText("Are you sure you want to quit?");
//        ToggleButton quitButton= new ToggleButton();
//        quitButton.getStyleClass().add("button-retro");
//        quitButton.setText("Yes");
//
//        ToggleButton cancelButton = new ToggleButton();
//        cancelButton.getStyleClass().add("button-retro");
//        cancelButton.setText("Cancel");
//        cancelButton.setSelected(true);
//
//        HBox box = new HBox();
//        box.setSpacing(20);
//        box.getChildren().addAll(quitButton,cancelButton);
//
//        dialogView.contentView.getChildren().add(box);

//        overlay.getChildren().add(dialogView);
//        overlay.setVisible(false);

        ToggleButton cancelButton = new ToggleButton();
        cancelButton.getStyleClass().add("button-retro");
        cancelButton.setText("Cancel");
        cancelButton.setSelected(true);

        HBox box = new HBox();
        box.setSpacing(20);
        box.getChildren().addAll(quitButton,cancelButton);

        dialogView.contentView.getChildren().add(box);

        overlay.getChildren().add(dialogView);
        overlay.setVisible(false);

        //-----------------------------


        Board.canvasWidth = (int) maze_canvas.getWidth();
        // init game with map and number of players
        game = new Game(selectedMap, numberOfPlayers, 0, 0);
        gameStateController = new GameStateController(this, game);

        //gameStateController.listenSinglePlayer();

        // draw on maze canvas with walls only
        GraphicsContext mzGC = maze_canvas.getGraphicsContext2D();
        ArrayList<GameObject> objects = game.getOtherGameObjects();
        for (GameObject object : objects) {
            //if (object.getType() == GameObject.TYPE.WALL) {
            object.draw(gcWall);
            //}
            if (object.getType() == GameObject.TYPE.WALL)
                object.draw(mzGC);
        }
        // draw all other objects on game canvas

        // need to update the game first
        //game.update();
        draw(mzBg);
        //TODO: init game state controller
        //TODO: add functions to handle the game state update() function§
        //gameStateController.update();

        draw(mzGC);
        //maze_canvas.setVisible(false);

        gameViewAdapter.addRow(maze_canvas, game_canvas);

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
    }

    private void draw(GraphicsContext graphicsContext) {

        gameStateController.getGame().getPacman().draw(graphicsContext);
//        game.getPacman().getWhip().draw(graphicsContext);
        System.out.println(game.getPacman().getHitBox());

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
    public void showLivesLeft() {

        for (int i = gameStateController.getGame().getPacman().getLives(); i < 3; i++) {
            setLivesImage("assets/misc/empty.png", i);
        }
    }

    /* Helper function for showLivesLeft() */
    public void setLivesImage(String image, int number) {

        switch (number) {
            case 0 :
                lifeLabel.setText("0");
                break;
            case 1 :
                lifeLabel.setText("1");
                break;
            case 2 :
                lifeLabel.setText("2");
                break;
        }
    }

    public boolean countingDown() {

        return this.countingDown;
    }

    public boolean gamePaused() {

        return (this.running==false);
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

        pauseOverlay.clearRect(0, 0, 1366, 768);
        pauseOverlay.drawImage(new Image("bg/backgrounds-game/pause_panel.png"),0,0);
        this.running = false;
        playSfx();
    }

    public void resumeGame() {

        pauseOverlay.clearRect(0,0,1366,768);
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

        //countdownGraphicsContext.drawImage(new Image("bg/backgrounds-game/countdown_overlay.png"), 0, 0);
        //countdownGraphicsContext.drawImage(new Image("assets/Elements-GameView/ready.png"),470,360);

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
                            //countdownGraphicsContext.clearRect(0, 0, 1366, 768);
                            //countdownGraphicsContext.drawImage(new Image("bg/backgrounds-game/countdown_overlay.png"), 0, 0);
                            //countdownGraphicsContext.drawImage(new Image(getDigit((char)timerStart.getSecOnes()),100,100,false,false),483,334);
                        }
                    }
					/* After time has counted to 0, start the game */
                    if (timerStart.timedOut()) {
                        this.stop();
                        //countdownGraphicsContext.clearRect(0,0,1366,768);
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
                if (running == true) {
                    //graphicsContext.clearRect(0, 0, 1366, 768);
                    gameStateController.update();
                    draw(graphicsContext);
                    //updateTimer();
                }
                else {
                    time = System.currentTimeMillis();
                }
            }
        };
        animationLoop.start();

    }
}