package group23.pacman.controller;

import group23.pacman.MainApp;
import group23.pacman.model.Game;
import group23.pacman.model.Pacman.STATE;
import group23.pacman.controller.GameViewController;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/** The class that handles the state of the game and the player input.
 * Contains a GameViewController object to update graphics if necessary.
 */

public class GameStateController {
	
	/* Scene used to add key listener */
	private Scene scene;
	
	/* Manipulate the game object */
	private Game game;
	
	/* Reference to game view */
	private GameViewController gameViewController;
	
	/* Keep track of pacman's lives */
	private int pacmanLives;
	
	/* Keep track of game state */
	private boolean gameOver;
	private boolean levelCleared;
	private boolean escapePressed;
	
	/* Public constructor */
	public GameStateController(GameViewController gameViewController,Game game) {
		
		this.gameViewController = gameViewController;
		this.game = game;
		this.pacmanLives = game.getPacman().getLives();
		this.levelCleared = false;
		this.gameOver = false;
		this.escapePressed = false;
	}
		
	
	/* Updates the game state and score */
	public void update() {
		
		/* Updates object coordinates and checks collisions */
		game.update();
		
		/* Check if player has died */
		game.checkState();
		
		/* Check to make sure we're not out of time */
		checkTimer();
		
		
		/* Make sure timer doesn't continue running while death animation is being played */
		if (game.getPacman().getState() == STATE.DEATH_ANIMATION) {
			gameViewController.stopTimer(true);
		}


		/* If Pacman lost a life, show this to the screen */
		if (pacmanLives != game.getPacman().getLives()) {

			/* If all lives lost, stop the game */
			if (game.getPacman().getLives() == 0) {

				pacmanLives = game.getPacman().getLives();
				gameViewController.showLivesLeft();
				gameViewController.stopGame();
				gameOver = true;
				gameViewController.showGameEnd();

			}

			/* Otherwise, just show number of lives to the screen */
			else {

				gameViewController.showLivesLeft();
				pacmanLives = game.getPacman().getLives();
				gameViewController.startCountdown();
			}
		}

		else if (game.levelCleared()) {
			levelCleared = true;
			gameViewController.showGameEnd();

		}

	}
	
	
	/* Checks if the player is losing on time */
	private void checkTimer() {
		
		/* If player ran out of time, end the game */
		if (gameViewController.getTimer().timedOut()) {

			gameOver = true;
			game.getPacman().setLives(0);
		}
		
	}
	
	
	
	/* Public getter to allow GameViewController(the view class) to reference objects(to draw) */
	public Game getGame() {
		
		return this.game;
	}
	
	public boolean gameOver() {
		
		return this.gameOver;
	}
	
	public boolean levelCleared() {
		
		return this.levelCleared;
	}
	
	public boolean escapePressed() {
		
		return this.escapePressed;
	}

	

}
