package group23.pacman.model;

import group23.pacman.view.Animation;
import group23.pacman.view.AnimationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/** Whip class is the weapon that the Pacman object uses when it picks up the SilverPellet object */

public class Whip extends GameObject implements MovingCharacter {
	
	/* Constant - do not change */
	private final int MAX_CHARGES = 6;
	
	/* Number of times left that the whip can be activated */
	private int charges;
	
	/* Reference to Pacman object */
	private Pacman pacman;
	
	/* Direction Pacman is facing */
	private char vector;
	
	/* Handles the animations */
	private AnimationManager animationManager;
	
	/* Boolean to determine if one whip animation has been played */
	private boolean shouldPlay;
	
	/* Coordinates of the whip */
	private int x;
	private int y;
	
	
	public Whip(Pacman pacman) {
		
		this.x = 0;
		this.y = 0;
		
		this.pacman = pacman;
		
		/* Whip initially cannot be used (no charges) */
		this.charges = 0;
		
		hitBox = new Rectangle();
		hitBox.setWidth(0);
		hitBox.setHeight(0);
		hitBox.setX(this.x);
		hitBox.setY(this.y);
		
		shouldPlay = false;
		setUpAnimations();
		
	}
	
	
	/* Consumes a charge of the whip */
	public void useCharge() {
		
		if (charges > 0) {
			charges--;
			shouldPlay = true;
		}

	}
	
	
	/* Cap charges at 6 */
	public void addCharges() {
		
		charges = charges + 3;
		charges = (charges > MAX_CHARGES) ? MAX_CHARGES : charges;
	}
	
	
	/* Updates whip hit-box and coordinates */
	public void update(int x,int y) {
		
		this.vector = pacman.getDirection();
		
		hitBox.enableHitBox(true);
		
		if (vector == 'U') {
			
			setX(x);
			setY(y - 60);
			
			hitBox.setWidth(10);
			hitBox.setHeight(60);
			hitBox.setX(x+10);
			hitBox.setY(y-60);
			
			
		}
		else if (vector == 'D') {
			
			setX(x);
			setY(y);
			
			hitBox.setWidth(10);
			hitBox.setHeight(60);
			hitBox.setX(x+10);
			hitBox.setY(y+30);
			
		}
		else if (vector == 'L') {
			
			setX(x - 60);
			setY(y);
			
			hitBox.setWidth(60);
			hitBox.setHeight(10);
			hitBox.setX(x-60);
			hitBox.setY(y+10);
			
		}
		
		/* Include Stop vector in case user wants to whip right after respawning without moving */
		else if (vector == 'R' || vector == 'S') {
			
			setX(x);
			setY(y);
			
			hitBox.setWidth(60);
			hitBox.setHeight(10);
			hitBox.setX(x+30);
			hitBox.setY(y+10);
			
		}
		
		/* When playing last frame for whip animation, end the animation and set Pacman to non-powered state */
		if (animationManager.getFrameIndex() == 2) {
			animationManager.stopAction();
			hitBox.enableHitBox(false);
			shouldPlay = false;
			pacman.endWhipAnim();
		}
		
		animationManager.update();
		playAnimation();
		
	}
	
	
	/* Updates the next frame of animation according to direction of whip */
	public void playAnimation() {
		
		if (shouldPlay) {
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
	}
	
	
	public void draw(GraphicsContext graphicsContext) {
		
		if (shouldPlay) {
			
			animationManager.draw(graphicsContext,this.x,this.y);
			

		}
		
		/* FOR debugging - draw hit box */
//		graphicsContext.setFill(Color.WHITESMOKE);
//		graphicsContext.fillRect(hitBox.getX(),hitBox.getY(),hitBox.getWidth(),hitBox.getHeight());
//		graphicsContext.setFill(Color.GREEN);
//		graphicsContext.setStroke(Color.BLUE);
		
	}
	
	
	/* Set up frame animations */
	private void setUpAnimations() {
		
		Image left1 = new Image("assets/Pacman/Whip/left-whip1.png",90,30,false,false);
		Image left2 = new Image("assets/Pacman/Whip/left-whip2.png",90,30,false,false);
		Image left3 = new Image("assets/Pacman/Whip/left-whip3.png",90,30,false,false);
		
		Image right1 = new Image("assets/Pacman/Whip/right-whip1.png",90,30,false,false);
		Image right2 = new Image("assets/Pacman/Whip/right-whip2.png",90,30,false,false);
		Image right3 = new Image("assets/Pacman/Whip/right-whip3.png",90,30,false,false);
		
		Image up1 = new Image("assets/Pacman/Whip/up-whip1.png",30,90,false,false);
		Image up2 = new Image("assets/Pacman/Whip/up-whip2.png",30,90,false,false);
		Image up3 = new Image("assets/Pacman/Whip/up-whip3.png",30,90,false,false);
		
		Image down1 = new Image("assets/Pacman/Whip/down-whip1.png",30,90,false,false);
		Image down2 = new Image("assets/Pacman/Whip/down-whip2.png",30,90,false,false);
		Image down3 = new Image("assets/Pacman/Whip/down-whip3.png",30,90,false,false);
		
		Image[] left = new Image[3];
		left[0] = left1;
		left[1] = left2;
		left[2] = left3;
		
		Image[] right = new Image[3];
		right[0] = right1;
		right[1] = right2;
		right[2] = right3;
		
		Image[] up = new Image[3];
		up[0] = up1;
		up[1] = up2;
		up[2] = up3;
		
		Image[] down = new Image[3];
		down[0] = down1;
		down[1] = down2;
		down[2] = down3;
		
		Animation leftAnim = new Animation(left,0.3f);
		Animation rightAnim = new Animation(right,0.3f);
		Animation upAnim = new Animation(up,0.3f);
		Animation downAnim = new Animation(down,0.3f);
		
		Animation[] animations = new Animation[4];
		animations[0] = leftAnim;
		animations[1] = rightAnim;
		animations[2] = upAnim;
		animations[3] = downAnim;
		
		animationManager = new AnimationManager(animations);
	}
	
	
	
	/** ALL PUBLIC GETTERS AND SETTERS BELOW **/
	
	public int getCharges() {
		
		return this.charges;
	}
	
    public Rectangle getHitBox() {
    	return this.hitBox;
    }
    
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean inAnimation() {
		
		return this.shouldPlay;
	}
	
	/* Public setter to end whip animation if Pacman dies while whipping to avoid buggy behaviour with whip */
	public void endAnim() {
		
		this.shouldPlay = false;
	}
	
	@Override
	public boolean checkforQueuedAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDirection(char qDirection) {
		// TODO Auto-generated method stub
	}

	@Override
	public char getQDirection() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateDestination() {
		// TODO Auto-generated method stub
	}

	@Override
	public char getDirection() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void reset(int x, int y) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setHasLeftSpawn() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getHasLeftSpawn() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean collidedWith(GameObject object) {
		// TODO Auto-generated method stub
		return false;
	}
}
