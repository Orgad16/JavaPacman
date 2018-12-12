package group23.pacman.model;

import javafx.scene.image.Image;

/** Special pellet which gives power up to Pacman object */

public class SpecialPellet extends Pellet{
	
	public SpecialPellet(int x, int y) {
		
		super(x,y, TYPE.SPECIAL_PELLET.toString());
		setImage();
		
	}
	
	
	@Override
	public void setImage() {
		
		image = new Image("assets/tempSpecialPellet.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
		this.type = GameObject.TYPE.SPECIAL_PELLET;
	}

}
