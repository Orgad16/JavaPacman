package group23.pacman.view;

import java.io.File;

import group23.pacman.MainApp;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/** The controller class for the welcome screen view */
public class WelcomeScreenController {
	
	/* Constants - do not change */
	private final int BUTTON_WIDTH = 300;
	private final int BUTTON_HEIGHT = 50;
	private final float FADE_SPEED = 0.02f;
	
	
	/* View elements in WelcomeScreen.fxml */
	@FXML 
	private ImageView playBtnImage;
	@FXML 
	private ImageView tutorialBtnImage;
	@FXML
	private ImageView leaderboardBtnImage;
	@FXML
	private ImageView creditsBtnImage;
	@FXML
	private ImageView singlePlayerImage;
	@FXML
	private ImageView twoPlayerImage;
	@FXML
	private ImageView threePlayerImage;
	@FXML
	private ImageView exitBtnImage;
	@FXML
	private ImageView background;
	@FXML
	private ImageView fade;
	@FXML
	private ImageView title;
	
	
	/* Main app copy kept to use when referencing to show other views */ 
	private MainApp mainApp;
	
	/* Helps keep track of which button is hovered over */
	private int buttonIndex;
	
	/* Locks the user in game mode selection */
	private boolean playSelected;
	
	/* Keeps track of which game mode is hovered over */
	private int numPlayers;
	
	
	/* Fade variables */
	private float opacity;
	private long time;
	
	/* Sound effects variables */
	private MediaPlayer mediaPlayerHighlight;
	private MediaPlayer mediaPlayerConfirmation;
	
	
	
	/* Constructor */
	public WelcomeScreenController() {
		
		
	}

	
	/* Sets up images and backgrounds for initial view */
	@FXML
	private void initialize() {
		
		/* Load media for playing sound effects */
		Media confirmation = new Media(new File("bin/assets/sfx/confirmation.mp3").toURI().toString());
		Media highlight = new Media(new File("bin/assets/sfx/highlight.mp3").toURI().toString());
		
		mediaPlayerConfirmation = new MediaPlayer(confirmation);
		mediaPlayerHighlight = new MediaPlayer(highlight);
		mediaPlayerConfirmation.setVolume(0.3);
		mediaPlayerHighlight.setVolume(0.3);
		
		/* Loads all button and background assets to their respective ImageView elements */
		title.setImage(new Image("assets/Elements-welcomeScreen/title.png"));
		background.setImage(new Image("bg/background-welcomeScreen/background-main.png"));

		/* Images for layering over buttons */
		playBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-play-highlighted.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		tutorialBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-tutorial.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		leaderboardBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-leaderboard.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		creditsBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-credits.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		exitBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-exit.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		
		/* Fade animation for coming into and out of this view*/
		fade.setImage(new Image("bg/blackbg.png"));
		opacity = 0;
		fade.setOpacity(opacity);

		
		/* Player does not start off with the selection of game modes(i.e. single,two, or three player) */
		playSelected = false;
		
		/* Default game mode is single player */
		numPlayers = 1;
		
		/* First button highlighted and selected is the Play button */
		buttonIndex= 0;
		
	}
	
	
	private void fadeTransition() {
		
		time = System.currentTimeMillis();
		
		AnimationTimer fadeAnimation = new AnimationTimer() {
			public void handle(long now) {
				
				/* Every 0.05 seconds increase the opacity of the black background to give illusion of fading out of this scene */
				if (System.currentTimeMillis() - time > 0.05f) {
					
					opacity += FADE_SPEED;
					fade.setOpacity(opacity);
					time = System.currentTimeMillis();
				}
				if (opacity >= 1) {
					if (numPlayers == 1) {
						this.stop();
						//mainApp.showLevelSelect();
					}
					else {
						this.stop();
						//mainApp.showCharacterSelect();
					}	
					
				}
			}
		};
		fadeAnimation.start();
	}
	
	
	/* Adds listener to the button in this view */
	public void addKeyListener() {

	}
	
	
	/* Helper function for playing sound effects */
	private void playSfx(int type) {
		/* Type 0 = confirmation */
		if (type == 0){
			mediaPlayerConfirmation.setStartTime(Duration.ZERO);
			mediaPlayerConfirmation.seek(Duration.ZERO);
			mediaPlayerConfirmation.play();
		}
		/* Type 1 = highlight*/
		else if (type == 1) {
			mediaPlayerHighlight.setStartTime(Duration.ZERO);
			mediaPlayerHighlight.seek(Duration.ZERO);
			mediaPlayerHighlight.play();
		} 
		
	}
	
	
	/* Public setter to reference main application */
	public void setMainApp(MainApp mainApp) {
		
		this.mainApp = mainApp;
	}
	
	
	
	/** BELOW ARE HELPER FUNCTIONS WHICH HELP WITH THE ANIMATION OF THIS VIEW **/
	/* Helper function for highlighting buttons to show which button is being hovered over*/
	private void highlightPlayers() {
		
		if (numPlayers == 1) {
			singlePlayerImage.setImage(new Image("assets/Elements-welcomeScreen/singlePlayer-highlighted.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			twoPlayerImage.setImage(new Image("assets/Elements-welcomeScreen/twoPlayer.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			threePlayerImage.setImage(new Image("assets/Elements-welcomeScreen/threePlayer.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		}
		else if (numPlayers == 2) {
			singlePlayerImage.setImage(new Image("assets/Elements-welcomeScreen/singlePlayer.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			twoPlayerImage.setImage(new Image("assets/Elements-welcomeScreen/twoPlayer-highlighted.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			threePlayerImage.setImage(new Image("assets/Elements-welcomeScreen/threePlayer.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		}
		else if (numPlayers == 3) {
			singlePlayerImage.setImage(new Image("assets/Elements-welcomeScreen/singlePlayer.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			twoPlayerImage.setImage(new Image("assets/Elements-welcomeScreen/twoPlayer.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			threePlayerImage.setImage(new Image("assets/Elements-welcomeScreen/threePlayer-highlighted.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		}
	}
	
	/* Helper function for highlighting buttons to show which button is being hovered over*/
	private void highlightButton() {
		
		if (buttonIndex == 0) {
			playBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-play-highlighted.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			tutorialBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-tutorial.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			leaderboardBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-leaderboard.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			creditsBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-credits.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			exitBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-exit.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		}
		else if (buttonIndex == 1) {
			playBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-play.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			tutorialBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-tutorial-highlighted.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			leaderboardBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-leaderboard.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			creditsBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-credits.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			exitBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-exit.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		}
		else if (buttonIndex == 2) {
			playBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-play.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			tutorialBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-tutorial.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			leaderboardBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-leaderboard-highlighted.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			creditsBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-credits.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			exitBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-exit.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		}
		else if (buttonIndex == 3) {
			playBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-play.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			tutorialBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-tutorial.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			leaderboardBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-leaderboard.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			creditsBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-credits-highlighted.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			exitBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-exit.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		}
		else if (buttonIndex == 4) {
			playBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-play.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			tutorialBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-tutorial.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			leaderboardBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-leaderboard.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			creditsBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-credits.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
			exitBtnImage.setImage(new Image("assets/Elements-welcomeScreen/button-exit-highlighted.png",BUTTON_WIDTH,BUTTON_HEIGHT,false,false));
		}

	}

}
