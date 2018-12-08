package group23.pacman.controller;

import group23.pacman.model.Game;
import group23.pacman.model.Pacman.STATE;
import group23.pacman.view.GameViewController;
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
		this.scene = gameViewController.getScene();
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
		
	
	/**
	 * KEY LISTENERS FOR DIFFERENT GAME MODES 
	 */
	public void listenSinglePlayer() {
		
		 scene.setOnKeyPressed(new EventHandler<KeyEvent> (){
		    	@Override
		    	public void handle(KeyEvent e) {
			    	if (e.getCode() == KeyCode.UP) {
			    		game.getPacman().queueMovement('U');
			    	}
			    	else if (e.getCode() == KeyCode.DOWN) {
			    		game.getPacman().queueMovement('D');
			    	}
			    	else if (e.getCode() == KeyCode.LEFT) {
			    		game.getPacman().queueMovement('L');
			    	}
			    	else if (e.getCode() == KeyCode.RIGHT) {
			    		game.getPacman().queueMovement('R');
			    	}
			    	else if (e.getCode() == KeyCode.ENTER) {
			    		if (!gameViewController.countingDown() && !gameViewController.gamePaused()){
			    			game.getPacman().whip();
					}
			    	}
			    	/* For debugging */
			    	else if (e.getCode() == KeyCode.PAGE_DOWN) {
			    		gameViewController.getTimer().endTimer();
			    		gameViewController.setTimerImage();
			    	}
			    	/* Pause button */
			    	else if (e.getCode() == KeyCode.P) {
			    		gameViewController.toggleState();
			    	}
			    	
			    	/* Show exit prompt */
			    	else if (e.getCode() == KeyCode.ESCAPE) {
			    		
			    		/* Can only show if not currently shown and the countdown timer is not counting */
			    		if (!escapePressed) {
			    			if (!gameViewController.countingDown()) {
				    			gameViewController.pauseGame();
					    		gameViewController.showExitConfirmation();
					    		escapePressed = true;
			    			}
			    		}
			    		
			    	}
			    	
			    	/* Yes/No keys to respond to exit prompt */
			    	else if (e.getCode() == KeyCode.Y) {
			    		if (escapePressed) {
			    			gameViewController.showMenu();
			    		}
			    	}
			    	else if (e.getCode() == KeyCode.N) {
			    		if (escapePressed) {
			    			gameViewController.clearExitPrompt();
			    			escapePressed = false;
			    		}
			    	}
		    	}
		    });
	}
	
	public void listenTwoPlayer() {
		
		 scene.setOnKeyPressed(new EventHandler<KeyEvent> (){
		    	@Override
		    	public void handle(KeyEvent e) {
			    	if (e.getCode() == KeyCode.UP) {
			    		game.getPacman().queueMovement('U');
			    	}
			    	else if (e.getCode() == KeyCode.DOWN) {
			    		game.getPacman().queueMovement('D');
			    	}
			    	else if (e.getCode() == KeyCode.LEFT) {
			    		game.getPacman().queueMovement('L');
			    	}
			    	else if (e.getCode() == KeyCode.RIGHT) {
			    		game.getPacman().queueMovement('R');
			    	}
			    	else if (e.getCode() == KeyCode.ENTER) {
					if (!gameViewController.countingDown() && !gameViewController.gamePaused()){
			    			game.getPacman().whip();
					}
			    	}
			    	else if (e.getCode() == KeyCode.W) {
			    		game.getGhost().queueMovement('U');
			    	}
					else if (e.getCode() == KeyCode.A) {
						game.getGhost().queueMovement('L');	    		
								    	}
					else if (e.getCode() == KeyCode.S) {
						game.getGhost().queueMovement('D');
					}
					else if (e.getCode() == KeyCode.D) {
						game.getGhost().queueMovement('R');
					}
			    	else if (e.getCode() == KeyCode.PAGE_DOWN) {
			    		gameViewController.getTimer().endTimer();
			    		gameViewController.setTimerImage();
			    	}
			    	/* Pause button */
			    	else if (e.getCode() == KeyCode.P) {
			    		gameViewController.toggleState();
			    	}
			    	else if (e.getCode() == KeyCode.ESCAPE) {
			    		
			    		if (!escapePressed) {
			    			if (!gameViewController.countingDown()) {
				    			gameViewController.pauseGame();
					    		gameViewController.showExitConfirmation();
					    		escapePressed = true;
			    			}
			    		}
			    		
			    	}
			    	else if (e.getCode() == KeyCode.Y) {
			    		if (escapePressed) {
			    			gameViewController.showMenu();
			    		}
			    	}
			    	else if (e.getCode() == KeyCode.N) {
			    		if (escapePressed) {
			    			gameViewController.clearExitPrompt();
			    			escapePressed = false;
			    		}
			    	}
		    	}
		    });
	}
	
	public void listenThreePlayer() {
		
		 scene.setOnKeyPressed(new EventHandler<KeyEvent> (){
		    	@Override
		    	public void handle(KeyEvent e) {
			    	if (e.getCode() == KeyCode.UP) {
			    		game.getPacman().queueMovement('U');
			    	}
			    	else if (e.getCode() == KeyCode.DOWN) {
			    		game.getPacman().queueMovement('D');
			    	}
			    	else if (e.getCode() == KeyCode.LEFT) {
			    		game.getPacman().queueMovement('L');
			    	}
			    	else if (e.getCode() == KeyCode.RIGHT) {
			    		game.getPacman().queueMovement('R');
			    	}
			    	else if (e.getCode() == KeyCode.ENTER) {
			    		if (!gameViewController.countingDown() && !gameViewController.gamePaused()){
			    			game.getPacman().whip();
					}
			    	}
			    	else if (e.getCode() == KeyCode.W) {
			    		game.getGhost().queueMovement('U');
			    	}
					else if (e.getCode() == KeyCode.A) {
						game.getGhost().queueMovement('L');	    		
								    	}
					else if (e.getCode() == KeyCode.S) {
						game.getGhost().queueMovement('D');
					}
					else if (e.getCode() == KeyCode.D) {
						game.getGhost().queueMovement('R');
					}
					else if (e.getCode() == KeyCode.I) {
			    		game.getGhost2().queueMovement('U');
			    	}
					else if (e.getCode() == KeyCode.J) {
						game.getGhost2().queueMovement('L');	    		
								    	}
					else if (e.getCode() == KeyCode.K) {
						game.getGhost2().queueMovement('D');
					}
					else if (e.getCode() == KeyCode.L) {
						game.getGhost2().queueMovement('R');
					}
			    	else if (e.getCode() == KeyCode.PAGE_DOWN) {
			    		gameViewController.getTimer().endTimer();
			    		gameViewController.setTimerImage();
			    	}
			    	/* Pause button */
			    	else if (e.getCode() == KeyCode.P) {
			    		gameViewController.toggleState();
			    	}
			    	
			    	else if (e.getCode() == KeyCode.ESCAPE) {
			    		
			    		if (!escapePressed) {
			    			if (!gameViewController.countingDown()) {
				    			gameViewController.pauseGame();
					    		gameViewController.showExitConfirmation();
					    		escapePressed = true;
			    			}
			    		}
			    		
			    	}
			    	else if (e.getCode() == KeyCode.Y) {
			    		if (escapePressed) {
			    			gameViewController.showMenu();
			    		}
			    	}
			    	else if (e.getCode() == KeyCode.N) {
			    		if (escapePressed) {
			    			gameViewController.clearExitPrompt();
			    			escapePressed = false;
			    		}
			    	}
		    	}
		    });
	}
	

}
