package group23.pacman.view;


import group23.pacman.MainApp;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/** Controller class for the LevelSelect screen */

public class LevelSelectController {
	
	/* Constant - do not change */
	private final int MAX_BACKGROUND_INDEX = 3;
	private final float FADE_SPEED = 0.02f;
	
	/* FXML elements in LevelSelect.fxml */
	@FXML
	private ImageView background;
	@FXML
	private ImageView levelImage;
	@FXML
	private ImageView leftArrow;
	@FXML
	private ImageView rightArrow;
	@FXML
	private ImageView map_label;
	@FXML
	private ImageView fade;
	
	
	/* Main app copy kept to use when referencing to get its scene. */
	private MainApp mainApp;
	private Scene scene;

	
	/* Variables for showing which background/level/map will be set */
	private int index;
	private Image ruinsBackground;
	private Image forestBackground;
	private Image desertBackground;
	private Image seaBackground;
	private Image ruinsLabel;
	private Image forestLabel;
	private Image desertLabel;
	private Image seaLabel;



	private Image[] backgrounds;
	private Image[] labels;
	
	/* Variable to control scroll speed */
	private long lastTime;
		
	/* Boolean to prevent animation to happen to already animated image */
	private boolean animated;
	
	/* Fade variables */
	private float opacity;
	private long time;
	
	private boolean enterPressed;
	
	
	/* Constructor */
	public LevelSelectController() {
		
		lastTime = 0;
		animated = false;
		
	}
	
	
	/* Sets up images and backgrounds for initial view */
	@FXML
	private void initialize() {
		
		/* Set up background of this view */
		Image backgroundImage = new Image("bg/backgrounds-LevelSelect/background-levelSelect.png");
		background.setImage(backgroundImage);
		
		/* Fade */
		fade.setImage(new Image("bg/blackbg.png"));
		opacity = 1;
		fade.setOpacity(opacity);
		fadeTransition(0);
		
		/* Set up level backgrounds to scroll through */
		ruinsBackground = new Image("bg/backgrounds-LevelSelect/background-ruins_levelselect.png");
		forestBackground = new Image("bg/backgrounds-LevelSelect/background-forest_levelselect.png");
		desertBackground = new Image("bg/backgrounds-LevelSelect/background-deserttemple_levelselect.png");
		seaBackground = new Image("bg/backgrounds-LevelSelect/background-sea_levelselect.png");
		ruinsLabel = new Image("assets/Elements-LevelSelect/label-ruins.png");
		forestLabel = new Image("assets/Elements-LevelSelect/label-forest.png");
		desertLabel = new Image("assets/Elements-LevelSelect/label-desert.png");
		seaLabel = new Image("assets/Elements-LevelSelect/label-sea.png");
		
		
		backgrounds = new Image[4];
		labels = new Image[4];
		
		backgrounds[0] = ruinsBackground;
		backgrounds[1] = forestBackground;
		backgrounds[2] = desertBackground;
		backgrounds[3] = seaBackground;
		labels[0] = ruinsLabel;
		labels[1] = forestLabel;
		labels[2] = desertLabel;
		labels[3] = seaLabel;
		
		index = 0;
		
		levelImage.setImage(backgrounds[index]);
		map_label.setImage(labels[index]);
		
		
		/* Load the arrows */
		Image leftArrowImage = new Image("assets/Elements-LevelSelect/leftArrow.png",110,110,false,false);
		Image rightArrowImage = new Image("assets/Elements-LevelSelect/rightArrow.png",110,110,false,false);		
		leftArrow.setImage(leftArrowImage);
		rightArrow.setImage(rightArrowImage);
		
		enterPressed = false;
	}
	
	
	private void fadeTransition(int mode) {
		
		time = System.currentTimeMillis();
		
		AnimationTimer fadeAnimation = new AnimationTimer() {
			public void handle(long now) {
				
				if (System.currentTimeMillis() - time > 0.05f) {
					
					if (mode == 0) {
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
						//mainApp.showGameView();
						this.stop();
						
					}
				}
				else if (mode == 2) {
					
					if (opacity >= 1) {
						//mainApp.showWelcomeScreen();
						this.stop();
						
					}
				}
			}
		};
		fadeAnimation.start();
		
	}
	

	/* Adds key listener to scene */
	public void listenToKeyEvents() {
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
		    @Override
		    public void handle(KeyEvent event) {
		    	
		    	/* Selects the currently shown map/level */
		    	if (event.getCode() == KeyCode.ENTER) {
		    		
		    		/* Prevents button being held down */
		    		if (!enterPressed) {
		    			enterPressed = true;
				    	char level;
				    	switch (index) {
				    		case 0 :
				    			level = 'r';
				    			break;
				    		case 1 :
				   				level = 'f';
				   				break;
				    		case 2 :
				    			level = 'd';
				    			break;
				    		case 3 :
				    			level = 's';
				    			break;
			    			default :
				    			level = 'r';
				    			break;
				    	}
				    	//mainApp.setMap(level);
				    	fadeTransition(1);
		    		}
			    		
		    	}
		    	else if (event.getCode() == KeyCode.LEFT) {
		    		
		    		/* Prevents background from scrolling too fast if key is held down
		    		 * Only changes every half second */
					if (System.currentTimeMillis() - lastTime > 500) {
						lastTime = System.currentTimeMillis();
						setLeftBackground();
					}
					if (!animated) {
						animateLeft();
						animated = true;
					}

				}
				
				else if (event.getCode() == KeyCode.RIGHT) {
					
					/* Prevents background from scrolling too fast if key is held down
		    		 * Only changes every half second */
					if (System.currentTimeMillis() - lastTime > 500) {
						lastTime = System.currentTimeMillis();
						setRightBackground();
					}
					if (!animated) {
						animateRight();
						animated = true;
					}
				}
				else if (event.getCode() == KeyCode.ESCAPE) {
					
					fadeTransition(2);
				}
				
		    }	    
		});
		
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		    @Override
		    public void handle(KeyEvent event) {
		    	if (event.getCode() == KeyCode.LEFT) {
		    		resetLArrow();
		    		lastTime = 0;
		    		animated = false;
				}
				
				else if (event.getCode() == KeyCode.RIGHT) {
					resetRArrow();
		    		lastTime = 0;
		    		animated = false;
				}
		    }	    
		});
	}
	
	
	/* Public setter for this class to reference the main application */
	public void setMainApp(MainApp mainApp) {
		
		this.mainApp = mainApp;
		//this.scene = mainApp.getScene();
	}
		
	
	
	/** BELOW ARE HELPER FUNCTIONS WHICH HELP WITH THE ANIMATION OF THIS VIEW **/
	
	/* Set background functions - 
	 * Help scroll the background to the left or the right
	 */
	private void setLeftBackground() {
		index--;
		index = (index < 0) ? MAX_BACKGROUND_INDEX : index;
		levelImage.setImage(backgrounds[index]);
		map_label.setImage(labels[index]);
	}
	
	private void setRightBackground() {
		index++;
		index = (index > MAX_BACKGROUND_INDEX) ? 0 : index;
		levelImage.setImage(backgrounds[index]);
		map_label.setImage(labels[index]);
	}
	
	/* Animate functions help "animate" the arrow keys, by
	 * enlarging them as the respective key is held down.
	 * This gives the user feedback on the key press and is a nice little feature for the UI*/
	private void animateLeft() {
		
		leftArrow.setX( -20);
        leftArrow.setY(- 20);
		leftArrow.setFitHeight(150);
		leftArrow.setFitWidth(150);
        leftArrow.setImage(new Image("assets/Elements-LevelSelect/leftArrow.png",150,150,false,false));
        
	}
	
	private void animateRight() {
		
		rightArrow.setX(-20);
		rightArrow.setY(-20);
		rightArrow.setFitHeight(150);
		rightArrow.setFitWidth(150);
		rightArrow.setImage(new Image("assets/Elements-LevelSelect/rightArrow.png",150,150,false,false));

	}
	
	/* Reset functions help reset the arrows to their original size to let the 
	 * user know when the key is released.
	 */
	private void resetLArrow() {
		
		leftArrow.setX(0);
        leftArrow.setY(0);
		leftArrow.setFitHeight(110);
		leftArrow.setFitWidth(110);
        leftArrow.setImage(new Image("assets/Elements-LevelSelect/leftArrow.png",110,110,false,false));
	}
	
	private void resetRArrow() {
		
		rightArrow.setX(0);
		rightArrow.setY(0);
		rightArrow.setFitHeight(110);
		rightArrow.setFitWidth(110);
		rightArrow.setImage(new Image("assets/Elements-LevelSelect/rightArrow.png",110,110,false,false));
	}

}
