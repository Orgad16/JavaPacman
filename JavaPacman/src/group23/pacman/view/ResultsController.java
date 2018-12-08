package group23.pacman.view;

import java.io.File;

import group23.pacman.MainApp;
import group23.pacman.model.ScoreHandler;
import group23.pacman.model.Timer;
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

/** Controller class for the results screen view **/
public class ResultsController {
	
	/* Reference to main app */
	private MainApp mainApp;
	
	/* View elements in Results.fxml*/
	@FXML
	private ImageView background;
	@FXML
	private ImageView record_score;
	@FXML
	private ImageView lives_remaining;
	@FXML
	private ImageView time_digit1;
	@FXML
	private ImageView time_digit2;
	@FXML
	private ImageView time_digit3;
	@FXML
	private ImageView score_digit1;
	@FXML
	private ImageView score_digit2;
	@FXML
	private ImageView score_digit3;
	@FXML
	private ImageView score_digit4;
	@FXML
	private ImageView total_digit1;
	@FXML
	private ImageView total_digit2;
	@FXML
	private ImageView total_digit3;
	@FXML
	private ImageView total_digit4;

	
	/* Show these variables to the screen */
	private int time;
	private int lives;
	private int score;
	private int totalScore;
	private char map;
	
	/* Boolean used to determine if we should ask user to record name */
	private boolean scoreBeaten;
	
	/* Variable to help with animation delay */
	private long holdTime;
	
	
	private int waitMode;
	
	
	/* Constructor */
	public ResultsController() {
		
	}
	
	
	@FXML
	private void initialize() {
		
		scoreBeaten = false;
		waitMode = 0;
	}
	
	
	/* Adds key listener to the scene from the main app */
	public void addKeyListener() {
		
		mainApp.getScene().setOnKeyPressed(new EventHandler<KeyEvent> (){
	    	@Override
	    	public void handle(KeyEvent event) {
		    	if (event.getCode() == KeyCode.ESCAPE) {
		    		if (waitMode == 1 || waitMode == 2) {
		    			mainApp.showWelcomeScreen();
		    		}
		    	}
		    	else if (event.getCode() == KeyCode.ENTER) {
		    		if (waitMode == 1 || waitMode == 2) {
		    			mainApp.showLevelSelect();
		    		}
		    	}
		    	else if (event.getCode() == KeyCode.Y) {
		    		if (scoreBeaten) {
		    			mainApp.setName();
		    			new ScoreHandler().writeScore(totalScore, mainApp.getName(),map);
		    			mainApp.showWelcomeScreen();
		    		}
		    	}
		    	else if (event.getCode() == KeyCode.N) {
		    		if (scoreBeaten) {
		    			mainApp.showWelcomeScreen();
		    		}
		    	}
	    	}
	    });
	}
	
	
	/* Public method used by main app to print the score to the screen */
	public void showScore() {
		
		lives_remaining.setImage(new Image(getDigit((char) (lives + 48))));
		
		String timeString = Integer.toString(this.time);
		
		if (timeString.length() == 3) {
			time_digit1.setImage(new Image(getDigit(timeString.charAt(0))));
			time_digit2.setImage(new Image(getDigit(timeString.charAt(1))));
			time_digit3.setImage(new Image(getDigit(timeString.charAt(2))));
		}
		else if (timeString.length() == 2) {
			time_digit1.setImage(new Image(getDigit(timeString.charAt(0))));
			time_digit2.setImage(new Image(getDigit(timeString.charAt(1))));
		}
		else if (timeString.length() == 1) {
			time_digit1.setImage(new Image(getDigit(timeString.charAt(0))));
		}
		
		/* Score showing */
		String scoreString = Integer.toString(this.score);
		if (score > 1000) {
			score_digit1.setImage(new Image(getDigit(scoreString.charAt(0))));
			score_digit2.setImage(new Image(getDigit(scoreString.charAt(1))));
			score_digit3.setImage(new Image(getDigit(scoreString.charAt(2))));
			score_digit4.setImage(new Image(getDigit(scoreString.charAt(3))));
		}
		else if (score > 100) {
			score_digit1.setImage(new Image(getDigit(scoreString.charAt(0))));
			score_digit2.setImage(new Image(getDigit(scoreString.charAt(1))));
			score_digit3.setImage(new Image(getDigit(scoreString.charAt(2))));
		}
		else if (score > 10) {
			score_digit1.setImage(new Image(getDigit(scoreString.charAt(0))));
			score_digit2.setImage(new Image(getDigit(scoreString.charAt(1))));
		}
		else {
			score_digit1.setImage(new Image(getDigit(scoreString.charAt(0))));

		}
		
		/* Show total score */
		String totalScoreString = Integer.toString(this.totalScore);
		if (totalScore > 1000) {
			total_digit1.setImage(new Image(getDigit(totalScoreString.charAt(0))));
			total_digit2.setImage(new Image(getDigit(totalScoreString.charAt(1))));
			total_digit3.setImage(new Image(getDigit(totalScoreString.charAt(2))));
			total_digit4.setImage(new Image(getDigit(totalScoreString.charAt(3))));
		}
		else if (totalScore > 100) {
			total_digit1.setImage(new Image(getDigit(totalScoreString.charAt(0))));
			total_digit2.setImage(new Image(getDigit(totalScoreString.charAt(1))));
			total_digit3.setImage(new Image(getDigit(totalScoreString.charAt(2))));
		}
		else if (totalScore > 10) {
			total_digit1.setImage(new Image(getDigit(totalScoreString.charAt(0))));
			total_digit2.setImage(new Image(getDigit(totalScoreString.charAt(1))));
		}
		else {
			total_digit1.setImage(new Image(getDigit(totalScoreString.charAt(0))));

		}
		
		ScoreHandler scoreHandler = new ScoreHandler();
		int scores[] = scoreHandler.getHighScores();
		
		for (int i = 0;i<scores.length;i++) {
			if (totalScore > scores[i]) {
				scoreBeaten = true;
			}
		}
		
		/* Show a different results screen if no high score is beaten */
		if (scoreBeaten) {
			background.setImage(new Image("bg/background-results/background-results-high_score.png"));
			recordScorePrompt();
		}
		else {
			background.setImage(new Image("bg/background-results/background-results.png"));
			waitMode = 1;
		}
		
	}
	
	
	/* Shows the message which asks user if they want to record their high score after three seconds of showing results (if a score is beaten)*/
	private void recordScorePrompt() {
		
		/* Wait three seconds */
		Timer holdTimer = new Timer(3);
		holdTime = System.currentTimeMillis();
		
		new AnimationTimer() {
			
			public void handle(long now) {
				
				if (System.currentTimeMillis() - holdTime >= 1000) {
					holdTimer.countDown(1);
					holdTime = System.currentTimeMillis();
					
				}
				/* After three seconds, show the message */
				if (holdTimer.timedOut()) {
					this.stop();
					waitMode = 2;
					Media congratulationsNoise = new Media(new File("bin/assets/sfx/highScore.mp3").toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(congratulationsNoise);
					mediaPlayer.setVolume(0.3f);
					mediaPlayer.play();
					record_score.setImage(new Image("assets/misc/record_score.png"));
				}
			}
		}.start();
	}
	
	
	/* Helper function to break score digits down (to more easily show in UI) */
	private String getDigit(char digit){

        switch (digit){
            case '0' :
                return "assets/numbers/0.png";
            case '1' :
                return "assets/numbers/1.png";
            case '2' :
                return "assets/numbers/2.png";
            case '3' :
                return "assets/numbers/3.png";
            case '4' :
                return "assets/numbers/4.png";
            case '5' :
                return "assets/numbers/5.png";
            case '6' :
                return "assets/numbers/6.png";
            case '7' :
                return "assets/numbers/7.png";
            case '8' :
                return "assets/numbers/8.png";
            case '9' :
                return "assets/numbers/9.png";
            default :
                return "assets/numbers/0.png";
        }
    }
	
	
	/* Public setter for class to calculate score */
	public void addParameters(int time,int lives,int score,char map) {
		
		this.time = time;
		this.lives = lives;
		this.score = score;
		this.map = map;
		
		/* Calculate the total score including bonuses */
		this.totalScore = (time * 5) + (lives * 150) + score;
		
		
		
	}
	
	
	/* Public setter to reference main app */
	public void setMainApp(MainApp mainApp) {
		
		this.mainApp = mainApp;
	}
}
