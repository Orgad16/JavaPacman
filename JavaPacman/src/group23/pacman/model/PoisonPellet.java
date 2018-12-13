package group23.pacman.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public class PoisonPellet extends Pellet {

    private Random rand;
    private Image poisonImage;

    /* Timer */
    private Timer spawnTimer;
    private long time;

    /*Poison candy time*/
    private int poisonTime;

    /*Draw poison candy*/
    private boolean drawCandy = false;



    public PoisonPellet(int x, int y) {
        super(x, y);
        /* Random number to determine a new spawn area */
        rand = new Random();
        /* Timer which lasts as long as the game. To determine the spawn times of the poison candy */
        spawnTimer = new Timer(120);
        time = System.currentTimeMillis();
        drawCandy = false;
        //setImage();
        this.type = GameObject.TYPE.POISON_PELLET;
    }

    @Override
    public void setImage() {

        poisonImage = new Image("assets/tempPoisonPellet.png", SPRITE_WIDTH, SPRITE_HEIGHT, false, false);
    }


    public void update(ArrayList<GameObject> emptySpaces) {

        /* Every 15 seconds, a gas zone will appear in a random area of the map */
        if (spawnTimer.getTimeRemaining()%10 == 0  && spawnTimer.getTimeRemaining() != 120 && !drawCandy ){
          /*Random coordinates for poison candy*/
            if (emptySpaces.isEmpty()) return;

            int rnd = rand.nextInt(emptySpaces.size());
            int newX = (int) emptySpaces.get(rnd).hitBox.getX();
            int newY = (int) emptySpaces.get(rnd).hitBox.getY();

            setX(newX );
            setY(newY );

            // update hitbox
            hitBox = emptySpaces.get(rnd).hitBox;
            drawCandy=true;
            /* Gas zone lasts for 8 seconds */
            poisonTime = spawnTimer.getTimeRemaining() - 8;

        }
        if (drawCandy) {
            if (spawnTimer.getTimeRemaining() <= poisonTime) {
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
        if (drawCandy) {
            graphicsContext.drawImage(poisonImage,x,y);
        }

    }

    /* Function to move the zone off the map and stop drawing the zone */
    public void stopDrawing() {
        drawCandy = false;
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
    public boolean getDrawpoisonCandy() {
        return this.drawCandy;
    }


}


