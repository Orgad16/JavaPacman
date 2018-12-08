package group23.pacman.model;

import group23.pacman.view.Animation;
import group23.pacman.view.AnimationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/** The class which handles the Ghost object, the antagonist(s) of the main character in this game
 */
public class Ghost extends GameObject implements MovingCharacter {
	
	
	public enum STATE {
		DEAD,
		ALIVE
	}
	
	/* Constants */
	private final int SPRITE_HEIGHT = 30;
	private final int SPRITE_WIDTH = 30;
	private final int OFFSET = 10;
	
	/* Pixels moved per update */
	private final int SPEED = 2;
	
	/* Handles character animations */
	private AnimationManager animationManager;
	
	/* AI for ghost, and the type of AI*/
	private AI ai;
	private boolean isAI;
	
	/* Direction to move and planned direction */
	private char vector;
	private char queuedDirection;
	
	private STATE state;
	
	/* Condition for moving in the ghost spawn point */
	private boolean hasLeftSpawn;
	
	/* Ghost position */
	private int x;
	private int y;
	private int spawnX;
	private int spawnY;
	
	/* The image passed in */
	String ghost;
	
	/* Time and condition to create a new timer */
	private long deathTime = 0;
	private boolean timerStarted;
	
	/* Used to manipulate time for showing to screen */
	private Timer timer;

	
	
	public Ghost(int x,int y,Board board, int type,int ghost) {
		
		switch (ghost) {
			case 1:
				this.ghost = "ghost1";
				break;
			case 2:
				this.ghost = "ghost2";
				break;
			case 3:
				this.ghost = "ghost3";
				break;
			case 4:
				this.ghost = "ghost4";
				break;
			default :
				this.ghost = "ghost1";
				break;
		}
		
		setUpAnimations();

		
		this.spawnX = x;
		this.spawnY = y;
		hitBox = new Rectangle();
		spawnGhost();
		this.hitBox.setHeight(SPRITE_HEIGHT - OFFSET);
		this.hitBox.setWidth(SPRITE_WIDTH - OFFSET);
		this.type = GameObject.TYPE.GHOST;

		
		this.vector = 'S';
		this.queuedDirection = 'S';
		
		hasLeftSpawn = false;
		timerStarted = false;
		
		
		if (type != 0) {
			isAI = true;
		}
		else {
			isAI = false;
		}
		ai = new AI(board, type);
	}
	
	
	public void update(int pacmanX, int pacmanY, char direction) {
		
		if (state == Ghost.STATE.ALIVE) {
			/* If this character is meant to be an AI, generate movement using the AI object created in this class */
			if (isAI) {
				if (ai.canTurn((int)getX(), (int)getY())) {
					queueMovement(ai.chooseMovement(hasLeftSpawn, vector, (int)getX(), (int)getY(), pacmanX, pacmanY, direction));
				}
			}
			
			/* Play the animation for this character */
			animationManager.update();
			playAnimation();
		}
		else {
			// Timer
			updateDeathTimer();
		}
		
	}
	
	/* To change the movement behaviour to scatter for 5 seconds */
	public void updateDeathTimer() {
		
		if (!timerStarted) {
			timer = new Timer(3);
			deathTime = System.currentTimeMillis();
			timerStarted = true;
		}
		
		/* Count down 1 second */
			if (System.currentTimeMillis() - deathTime >= 1000) {
				timer.countDown(1);
				deathTime = System.currentTimeMillis();
			}
		
			/* If 5 seconds is up, respawn ghost */
			if (timer.timedOut()) {
				spawnGhost();
				this.timerStarted = false;
			}
	}
	
	public void spawnGhost() {
		
		this.x = spawnX;
		this.y = spawnY;
		this.hitBox.setX(x + OFFSET/2);
		this.hitBox.setY(y + OFFSET/2);
		this.state = Ghost.STATE.ALIVE;
		this.hasLeftSpawn = false;
	}
	
	 /* Changes character animation depending on the direction it's currently facing */
    private void playAnimation() {
    	
    	if (this.vector == 'S') {
			animationManager.playAction(1);
		}
		if (this.vector == 'U') {
			animationManager.playAction(2);
		}
		else if (this.vector == 'D') {
			animationManager.playAction(3);
		}
		else if (this.vector == 'L') {
			animationManager.playAction(0);
		}
		else if (this.vector == 'R') {
			animationManager.playAction(1);
		}
    }
	
	public void queueMovement(char queuedDirection) {
		
		this.queuedDirection = queuedDirection;
	}

    /* Determines if the current direction is the same as the queued direction */
    public boolean checkforQueuedAction() {
		
	    return (queuedDirection != vector);
    }
    
    /* Determines if the current direction is the opposite direction of the queued direction */
    public boolean oppositeDirection() {
    	
    	switch (vector) {
    		case 'S':
    			return true;
			case 'U':
				if (queuedDirection == 'D') {
					return true;
				}
			case 'D':
				if (queuedDirection == 'U') {
					return true;
				}
			case 'L':
				if (queuedDirection == 'R') {
					return true;
				}
			case 'R':
				if (queuedDirection == 'L') {
					return true;
				}
    	}
    	return false;
    }
     
    
    /* Updates (x,y) coordinates of character */
    public void updateDestination() {
    
    		
    	if (this.vector == 'U') {
			this.hitBox.setY((int)hitBox.getY() - SPEED);
			this.y = y - SPEED;
		}
		else if (this.vector == 'D') {
			this.hitBox.setY((int)hitBox.getY() + SPEED);
			this.y = y + SPEED;
		}
		else if (this.vector == 'L') {
			this.hitBox.setX((int)hitBox.getX() - SPEED);
			this.x = x - SPEED;
		}
		else if (this.vector == 'R') {
			this.hitBox.setX((int)hitBox.getX() + SPEED);
			this.x = x + SPEED;
		}
    }

	
	public void draw(GraphicsContext graphicsContext) {
		if (state == Ghost.STATE.ALIVE) {

			animationManager.draw(graphicsContext, this.x, this.y);
		}
	}

	/* Reset position when Ghost or Pacman dies and Pacman still has lives left. */
	public void reset(int x, int y) {
		
		this.hitBox.setX(x + OFFSET/2);
		this.hitBox.setY(y + OFFSET/2);
		this.x = x;
		this.y = y;
		this.hasLeftSpawn = false;
		setDirection('S');
		setState(Ghost.STATE.ALIVE);
	}
	
	
	private void setUpAnimations() {
		
		
		Image left1 = new Image("assets/Ghost/" + ghost + "Left1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
		Image left2 = new Image("assets/Ghost/" + ghost + "Left2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
		Image right1 = new Image("assets/Ghost/" + ghost + "Right1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
		Image right2 = new Image("assets/Ghost/" + ghost + "Right2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
		Image up1 = new Image("assets/Ghost/" + ghost + "Up1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
		Image up2 = new Image("assets/Ghost/" + ghost + "Up2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
		Image down1 = new Image("assets/Ghost/" + ghost + "Down1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
		Image down2 = new Image("assets/Ghost/" + ghost + "Down2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
		
		Image[] leftMove = new Image[2];
		leftMove[0] = left1;
		leftMove[1] = left2;
		
		Image[] rightMove = new Image[2];
		rightMove[0] = right1;
		rightMove[1] = right2;
		
		Image[] upMove = new Image[2];
		upMove[0] = up1;
		upMove[1] = up2;
		
		Image[] downMove = new Image[2];
		downMove[0] = down1;
		downMove[1] = down2;
		
		Animation leftAnimation = new Animation(leftMove,0.3f);
		Animation rightAnimation = new Animation(rightMove,0.3f);
		Animation upAnimation = new Animation(upMove,0.3f);
		Animation downAnimation = new Animation(downMove,0.3f);
		
		Animation[] movementAnimations = new Animation[4];
		movementAnimations[0] = leftAnimation;
		movementAnimations[1] = rightAnimation;
		movementAnimations[2] = upAnimation;
		movementAnimations[3] = downAnimation;
		
		animationManager = new AnimationManager(movementAnimations);
		
	}
	
	@Override
	public boolean collidedWith(GameObject object) {
		Rectangle hitBox = object.getHitBox();
		return this.hitBox.intersects(hitBox);
	}
	
	/* PUBLIC SETTERS */
	
    public void setDirection(char vector) {
    	
    	this.vector = vector;	
    }
    
    public void setState(STATE state) {
    	
    	this.state = state;
    }
    
    public void setHasLeftSpawn() {
    	this.hasLeftSpawn = true;
    }
    
	/* PUBLIC GETTERS */
    public STATE getState() {
    	
    	return this.state;
    }
    
    public AI getAI() {
    	return this.ai;
    }
    
    public double getX() {
    	
    	return this.x;
    }
    
    public double getY() {
    	
    	return this.y;
    }

    public char getDirection() {
    	
    	return this.vector;
    }
    
    public char getQDirection() {
    	
    	return this.queuedDirection;
    }
   
    /* Determines if the ghost has left the spawn point */
    public boolean getHasLeftSpawn() {
    	return this.hasLeftSpawn;
    }
    
    public Rectangle getHitBox() {
    	return this.hitBox;
    }


	
	
}
