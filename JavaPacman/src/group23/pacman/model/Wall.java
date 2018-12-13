package group23.pacman.model;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/** This class contains the information of a wall which is 10x10 pixels */

public class Wall extends GameObject{
	
	private Rectangle wallHitBox;
	private Image mapBlock;
	private double x;
	private double y;
	public Wall(Rectangle rectangle,int map) {
		
		
		/* Choose which tile block to load into the map */
		String mapBlock;
		switch (map) {
			case 0 :
				mapBlock = "assets/tiles/mapBlock-ruins.png";
				break;
			case 1 :
				mapBlock = "assets/tiles/mapBlock-forest.png";
				break;
			case 2 :
				mapBlock = "assets/tiles/mapBlock-deserttemple.png";
				break;
			case 3 :
				mapBlock = "assets/tiles/mapBlock-sea.png";
				break;
			default :
				mapBlock = "assets/tiles/mapBlock-ruins.png";
				break;
		}
		this.mapBlock = new Image(mapBlock,10,10,false,false);
		
		/* Set up object for collision detection */
		this.wallHitBox = rectangle;
		this.type = GameObject.TYPE.WALL;
		this.x = wallHitBox.getX();
		this.y = wallHitBox.getY();

	}
	
	public void draw(GraphicsContext graphicsContext) {
		
		graphicsContext.drawImage(mapBlock,x,y);

	}
	
	/* PUBLIC GETTERS */
	public double getX() {
		
		return this.wallHitBox.getX();
	}
	
	public double getY() {
		
		return this.wallHitBox.getY();
	}
	
	
	public Rectangle getHitBox() {
		 
		return this.wallHitBox;
	}
}
