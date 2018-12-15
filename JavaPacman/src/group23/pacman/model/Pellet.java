package group23.pacman.model;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/** The object that Pacman will be eating */

public class Pellet extends GameObject {
	
	protected static final int SPRITE_HEIGHT = 10;
	protected static final int SPRITE_WIDTH = 10;
	
	protected Image image;

	protected int x;
	protected int y;
	
	public Pellet(int x, int y) {
		
		hitBox = new Rectangle();
		hitBox.setHeight(SPRITE_HEIGHT);
		hitBox.setWidth(SPRITE_WIDTH);
		hitBox.setX(x);
		hitBox.setY(y);
		this.x = x;
		this.y = y;
		setImage();

		
	}
	
	public void setImage() {
		
		image = new Image("assets/pellet_normal.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
		this.type = GameObject.TYPE.PELLET;
	}
	
	public void draw(GraphicsContext graphicsContext) {

		graphicsContext.drawImage(image, x,y);
	}

	
}
