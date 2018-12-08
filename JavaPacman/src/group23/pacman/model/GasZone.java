package group23.pacman.model;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/** This class is the random obstacle which kills any moving character that touches it */

public class GasZone extends GameObject {
	
	private static final int GRID_SIZE = 30;
	private static final int X_OFFSET = 158;
	private static final int Y_OFFSET = 9;
	private static final int OFFSET = 20;
	private static final int GAS_ZONE_SIZE = 90;
	
	private boolean drawGas;
	private boolean drawWarning;
	
	private Random rand;
	
	private Image warningZoneImage;
	private Image gasZoneImage;
	
	/* Timer */
	private Timer spawnTimer;
	private long time;
	
	/* Position to draw the GasZone */
	private int x;
	private int y;
	
	private int warningTime;
	private int gasTime;
	

	public GasZone() {
		
		this.x = 0;
		this.y = 0;
		
		/* Random number to determine a new spawn area */
		rand = new Random();
		
		/* Set Up hitbox */
		hitBox = new Rectangle();
		hitBox.setWidth(0);
		hitBox.setHeight(0);
		hitBox.setX(this.x);
		hitBox.setY(this.y);
		
		String warningZone = "assets/GasZone/warningZone.png";
		String gasZone = "assets/GasZone/gasZone.png";
		this.warningZoneImage = new Image(warningZone,GAS_ZONE_SIZE,GAS_ZONE_SIZE,false,false);
		this.gasZoneImage = new Image(gasZone,GAS_ZONE_SIZE,GAS_ZONE_SIZE,false,false);
		this.type = GameObject.TYPE.GASZONE;
		
		/* Timer which lasts as long as the game. To determine the spawn times of the gas zone */
		spawnTimer = new Timer(120);
		time = System.currentTimeMillis();
		
		drawGas = false;
		drawWarning = false;
	}
	
	
	public void update() {
		
		/* Every 15 seconds, a gas zone will appear in a random area of the map */
		if (spawnTimer.getTimeRemaining()%10 == 0  && spawnTimer.getTimeRemaining() != 120 && warningTime != spawnTimer.getTimeRemaining() - 5) {
			/* Warning area will display for the first 5 seconds */
			warningTime = spawnTimer.getTimeRemaining() - 5;
			drawWarning = true;
			drawGas = false;
			int newX = rand.nextInt(23);
			int newY = rand.nextInt(23);
			
			setX(newX * GRID_SIZE + X_OFFSET);
			setY(newY * GRID_SIZE + Y_OFFSET);
			hitBox.setWidth(GAS_ZONE_SIZE - OFFSET);
			hitBox.setHeight(GAS_ZONE_SIZE - OFFSET);
			hitBox.setX(newX * GRID_SIZE + X_OFFSET + OFFSET/2);
			hitBox.setY(newY * GRID_SIZE + Y_OFFSET + OFFSET/2);
		}
		if (drawWarning) {
			/* After 5 seconds, the warning area will be replaced by a gas zone*/
			if (spawnTimer.getTimeRemaining() <= warningTime) {
				drawWarning = false;
				drawGas = true;
				/* Gas zone lasts for 2 seconds */
				gasTime = spawnTimer.getTimeRemaining() - 2;
			}
		}
		if (drawGas) {
			if (spawnTimer.getTimeRemaining() <= gasTime) {
				stopDrawing();
			}
		}
		updateTimer();
	}
	
	/* Updates time remaining */
	private void updateTimer() {
		if (!spawnTimer.timedOut()) {
			if (System.currentTimeMillis() - time >= 1000) {
				spawnTimer.countDown(1);
				time = System.currentTimeMillis();
			}
		}
	}
	
	public void draw(GraphicsContext graphicsContext) {
		if (drawGas) {
			graphicsContext.drawImage(gasZoneImage,x,y);
		}
		if (drawWarning) {
			graphicsContext.drawImage(warningZoneImage,x,y);
		}
    }
	
	/* Function to move the zone off the map and stop drawing the zone */
	public void stopDrawing() {
		drawWarning = false;
		drawGas = false;
		hitBox.setWidth(0);
		hitBox.setHeight(0);
		hitBox.setX(0);
		hitBox.setY(0);
	}
	
	/* PUBLIC SETTERS */
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	/* PUBLIC GETTER */
	public boolean getDrawGas() {
		return this.drawGas;
	}
	
}
