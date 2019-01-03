package group23.pacman.model;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/** The object that Pacman will be eating */

public class Pellet extends GameObject {
	
	protected static final int SPRITE_HEIGHT = 30;
	protected static final int SPRITE_WIDTH = 30;
	
	protected Image image;

	protected int x;
	protected int y;
	
	public Pellet(int x, int y) {

		x -= 10;
		y -= 10;

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
		
		image = new Image("assets/pellet_normal.png",SPRITE_WIDTH,SPRITE_HEIGHT,true,true);
		this.type = GameObject.TYPE.PELLET;
	}
	
	public void draw(GraphicsContext graphicsContext) {

		graphicsContext.drawImage(image, x,y);
//		graphicsContext.setStroke(Color.RED);
//		graphicsContext.setLineWidth(1);
//		graphicsContext.strokeRect(x,y,image.getWidth(),image.getHeight());
//
//		graphicsContext.setStroke(Color.BLUE);
//		graphicsContext.strokeRect(hitBox.getX(),hitBox.getY(),hitBox.getWidth(),hitBox.getHeight());
	}

	
}
