package group23.pacman.model;

import javafx.scene.image.Image;

/** Special pellet which gives power up to Pacman object */

public class SilverPellet extends Pellet{
	
	public SilverPellet(int x, int y) {
		
		super(x,y);
		
	}
	
	
	@Override
	public void setImage() {
		
		image = new Image("assets/pellet_powerup.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
		this.type = GameObject.TYPE.SILVER_PELLET;
	}

}
