package group23.pacman.view;

import java.io.File;
import group23.pacman.MainApp;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/** This class allows the player(s) 2 (and 3) to select their preferred character sprite */
public class CharacterSelectController {
	
	/* View elements in CharacterSelect.fxml */
	@FXML
	private ImageView background;
	@FXML
	private ImageView background2;
	@FXML
	private ImageView ghost1;
	@FXML
	private ImageView ghost2;
	@FXML
	private ImageView ghost3;
	@FXML
	private ImageView ghost4;
	@FXML
	private ImageView player_banner;
	@FXML
	private ImageView fade;
	
	/* Constants - do not change */
	private final int MAX_GHOSTS = 4;
	private final int SPRITE_HEIGHT = 150;
	private final int SPRITE_WIDTH = 150;
	private final int SCROLL_RESET = 594;
	private final int SCROLL_SPEED = 1;
	private final float FADE_SPEED = 0.02f;
	
	/* Main app copy kept to use when referencing to show other views */ 
	private MainApp mainApp;
	
	/* For media player */
	private Media buttonPress;

	/* Keep tracks of chosen ghost */
	private int ghostIndex;
	
	
	/* Number of players - Game mode */
	private int numPlayers;
	
	
	/* Variable is used when there are 3 players, helps differentiate between the first and second character selections */
	private boolean firstPick;
	
	
	/* Variables for manipulating the looping background of this view */
	private AnimationTimer animationLoop;
	private long time;
	private float xScroll;
	
	/* Fade variables */
	private float opacity;
	
	
	
	public CharacterSelectController() {
		
		
	}

	
	public void animate() {
		
		animationLoop = new AnimationTimer() {
			public void handle(long now) {
				
				/* Every 0.05 seconds, move the two backgrounds to the left at SCROLL_SPEED pixels
				 * When it is time to loop,move the images back to the right by the amount scrolled. */
				
				if (System.currentTimeMillis() - time > 0.05f) {
					
					background.setX(background.getX() - SCROLL_SPEED);
					background2.setX(background2.getX() - SCROLL_SPEED);
					xScroll = xScroll + SCROLL_SPEED;
					if (xScroll == SCROLL_RESET) {
						background.setX(background.getX() + SCROLL_RESET);
						background2.setX(background2.getX() +SCROLL_RESET);
						xScroll = 0;
					}
					
				}
			}
		};
		animationLoop.start();
	}
	
	
	private void fadeTransition(int mode) {
		
		time = System.currentTimeMillis();
		
		AnimationTimer fadeAnimation = new AnimationTimer() {
			public void handle(long now) {
				
				if (System.currentTimeMillis() - time > 0.05f) {
					
					if (mode ==0) {
						opacity -= FADE_SPEED;
					}
					else {
						opacity += FADE_SPEED;
					}
					
					fade.setOpacity(opacity);
					time = System.currentTimeMillis();
				}
				
				if (mode == 0) {
					if (opacity <= 0) {
						this.stop();
					}
				}
				
				else if (mode == 1){
					if (opacity >= 1) {
						animationLoop.stop();
						//mainApp.showLevelSelect();
						this.stop();
						
					}
				}
				else if (mode == 2){
					if (opacity >= 1) {
						animationLoop.stop();
						//mainApp.showWelcomeScreen();
						this.stop();
						
					}
				}
			}
		};
		fadeAnimation.start();
	}
	
	@FXML
	private void initialize() {
		
		/* Initialize the default view */
		background.setImage(new Image("assets/Elements-CharSel/bg1.png",1366,768,false,false));
		background2.setImage(new Image("assets/Elements-CharSel/bg2.png",1366,768,false,false));
		xScroll = 0;
		animate();
		
		
		/* Fade */
		fade.setImage(new Image("bg/blackbg.png"));
		opacity = 1;
		fade.setOpacity(opacity);
		
		
		ghost1.setImage(new Image("assets/Elements-CharSel/ghost1-highlighted.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
		ghost2.setImage(new Image("assets/Elements-CharSel/ghost2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
		ghost3.setImage(new Image("assets/Elements-CharSel/ghost3.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
		ghost4.setImage(new Image("assets/Elements-CharSel/ghost4.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
		player_banner.setImage(new Image("assets/Elements-CharSel/player_two_banner.png"));
		fadeTransition(0);

		/* Set up sound effect button presses */
		buttonPress = new Media(new File("bin/assets/sfx/menuSelect.mp3").toURI().toString());

		
		/* Set variable to determine which sprite is chosen for which character */
		firstPick = true;
		ghostIndex = 1;

		
	}
	
	
	/* Helper function for highlighting the currently selected sprite - allows user to know what they're choosing */
	private void highlightGhost() {
		
		if (ghostIndex == 1) {
			ghost1.setImage(new Image("assets/Elements-CharSel/ghost1-highlighted.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost2.setImage(new Image("assets/Elements-CharSel/ghost2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost3.setImage(new Image("assets/Elements-CharSel/ghost3.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost4.setImage(new Image("assets/Elements-CharSel/ghost4.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
		}
		else if (ghostIndex == 2) {
			ghost1.setImage(new Image("assets/Elements-CharSel/ghost1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost2.setImage(new Image("assets/Elements-CharSel/ghost2-highlighted.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost3.setImage(new Image("assets/Elements-CharSel/ghost3.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost4.setImage(new Image("assets/Elements-CharSel/ghost4.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
		}
		else if (ghostIndex == 3) {
			ghost1.setImage(new Image("assets/Elements-CharSel/ghost1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost2.setImage(new Image("assets/Elements-CharSel/ghost2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost3.setImage(new Image("assets/Elements-CharSel/ghost3-highlighted.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost4.setImage(new Image("assets/Elements-CharSel/ghost4.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));	
		}
		else if (ghostIndex == 4) {
			ghost1.setImage(new Image("assets/Elements-CharSel/ghost1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost2.setImage(new Image("assets/Elements-CharSel/ghost2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost3.setImage(new Image("assets/Elements-CharSel/ghost3.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
			ghost4.setImage(new Image("assets/Elements-CharSel/ghost4-highlighted.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false));
		}
	}
	
	/* Plays sound effect for navigating this view */
	public void playSfx() {
		MediaPlayer mediaPlayer = new MediaPlayer(buttonPress);
		mediaPlayer.play();
		mediaPlayer.setOnEndOfMedia(() -> {
                	mediaPlayer.dispose();
           	 });
	}
	
	
	/* PUBLIC SETTERS */
	public void setPlayers(int players) {
		
		this.numPlayers = players;
	}
	
	public void setMainApp(MainApp mainApp) {
		
		this.mainApp = mainApp;
	}
}
