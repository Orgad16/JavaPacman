package group23.pacman.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created By Tony on 13/12/2018
 */
public abstract class RandomPellet extends Pellet{

    private Random rand;

    /* Timer */
    private Timer spawnTimer;
    private long time;

    private int objectTime;

    private boolean drawCandy = false;

    public RandomPellet(int x, int y) {
        super(x, y);
        rand = new Random();
        /* Timer which lasts as long as the game. To determine the spawn times of the poison candy */
        spawnTimer = new Timer(120);
        time = System.currentTimeMillis();
        drawCandy = false;
        type = getType();
    }

    public abstract boolean shouldShowPellet(Timer timer);

    public abstract int appearForSeconds();

    @Override
    public abstract TYPE getType();

    @Override
    public abstract void setImage();

    public void draw(GraphicsContext graphicsContext) {
        if (drawCandy) {
            graphicsContext.drawImage(image,x,y);
        }
    }

    public void update(ArrayList<GameObject> emptySpaces) {

        /* Every 15 seconds, a gas zone will appear in a random area of the map */
        if (shouldShowPellet(spawnTimer) && !drawCandy ){
            /*Random coordinates for poison candy*/
            if (emptySpaces.isEmpty()) return;

            int rnd = rand.nextInt(emptySpaces.size());
            int newX = (int) emptySpaces.get(rnd).hitBox.getX();
            int newY = (int) emptySpaces.get(rnd).hitBox.getY();

            x = (newX );
            y = (newY );

            // update hitbox
            hitBox = emptySpaces.get(rnd).hitBox;
            drawCandy=true;
            /* Gas zone lasts for 8 seconds */
            objectTime = spawnTimer.getTimeRemaining() - appearForSeconds();

        }
        if (drawCandy) {
            if (spawnTimer.getTimeRemaining() <= objectTime) {
                stopDrawing();
            }
        }
        updateTimer();
    }

    public void stopDrawing() {
        drawCandy = false;
        hitBox.setWidth(0);
        hitBox.setHeight(0);
        hitBox.setX(0);
        hitBox.setY(0);
    }

    private void updateTimer() {
        if (!spawnTimer.timedOut()) {
            if (System.currentTimeMillis() - time >= 1000) {
                spawnTimer.countDown(1);
                time = System.currentTimeMillis();
            }
        }
    }

}
