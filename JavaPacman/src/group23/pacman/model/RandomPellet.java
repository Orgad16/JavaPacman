package group23.pacman.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created By Tony on 13/12/2018
 */
public abstract class RandomPellet extends Pellet{

    public static int X_OFFSET(){
        return (Board.canvasWidth - (75 * 10)) / 2;
    }

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

        // giving the pacman 8 second to clear some candies before placing poison and question candies
        if (spawnTimer.getTimeRemaining() < 118 && spawnTimer.getTimeRemaining()%10 == 0 && !drawCandy){

            /*Random coordinates for poison candy*/
            if (emptySpaces.isEmpty()) return;

            int rnd = rand.nextInt(emptySpaces.size());

            hitBox = emptySpaces.get(rnd).hitBox;

            int newX = (int) hitBox.getX();
            int newY = (int) hitBox.getY();

            x = (newX);
            y = (newY);

            // update hitbox
//            hitBox.setX(x);
//            hitBox.setY(y);
            drawCandy=true;
            /* Gas zone lasts for 8 seconds */
            objectTime = spawnTimer.getTimeRemaining() - appearForSeconds();


        } else if (drawCandy) {
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
            //time = System.currentTimeMillis();
        }
    }

    public boolean getDrawCandy() {
        return drawCandy;
    }

    public void setDrawCandy(boolean boo) {
        this.drawCandy = boo;
    }


}
