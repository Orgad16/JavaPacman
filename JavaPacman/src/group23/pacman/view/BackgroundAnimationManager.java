package group23.pacman.view;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.List;

public class BackgroundAnimationManager extends Pane{

    private final int SPRITE_HEIGHT = 30;
    private final int SPRITE_WIDTH = 30;
    private final int SPEED = 2;

    private int x;
    private int y;
    private int wayToGo;
    private int initX;
    private int initY;
    private AnimationTimer time;
    private AnimationManager animationManager;
    private GraphicsContext gc;
    private char direction;

    public BackgroundAnimationManager(int x, int y, int wayToGo, String character, GraphicsContext context, char direction) {
        this.x = x;
        this.y = y;
        this.initX = x;
        this.initY = y;
        this.direction = direction;
        this.wayToGo = wayToGo;
        setUpAnimations(character);
        this.gc = context;


    }

    public void startMoving() {
        if (direction == 'R')
            updateDestination(x+SPEED, y);
        if (direction == 'U')
            updateDestination(x, y-SPEED);
        drawAnimation(x,y);
        update();

    }

    public void drawAnimation(int x, int y) {
        gc.clearRect(x,y,SPRITE_HEIGHT,SPRITE_WIDTH);
        animationManager.draw(gc, x, y);
    }

    public void updateDestination(int i, int k) {
        if (direction == 'R' || direction == 'L') {
            if (i < initX + wayToGo) {
                this.x = i;
                this.y = k;
            } else {
                resetAnimation();
            }
        }
        if (direction == 'U') {
            if (k > initY - wayToGo) {
                this.x = i;
                this.y = k;
            }
            else {
                resetAnimation();
            }
        }
    }

    public void resetAnimation() {
        gc.clearRect(x,y,SPRITE_HEIGHT,SPRITE_WIDTH);
        this.x = initX;
        this.y = initY;
        startMoving();
    }

    public void update() {
        animationManager.update();
        playAnimation();
    }

    public void playAnimation() {
        switch (direction) {
            case 'R':
                animationManager.playAction(0);
                break;
            case 'U':
                animationManager.playAction(1);
                break;
            case 'D':
                animationManager.playAction(3);
                break;
            case 'L':
                animationManager.playAction(2);
                break;
        }
    }

    public void setUpAnimations(String character) {
        Image[] imagesRight = new Image[2];
        Image[] imagesUp = new Image[2];

        switch (character){
            case "ghost":
                imagesRight[0] = new Image("assets/Ghost/ghost1Right2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesRight[1] = new Image("assets/Ghost/ghost1Right1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesUp[0] = new Image("assets/Ghost/ghost1Up2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesUp[1] = new Image("assets/Ghost/ghost1Up1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                break;
            case "pacman":
                imagesRight[0] = new Image("assets/Pacman/rightClose.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesRight[1] = new Image("assets/Pacman/rightOpen.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesUp[0] = new Image("assets/Pacman/rightClose.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesUp[1] = new Image("assets/Pacman/rightOpen.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                break;
        }
        Animation animationsRight = new Animation(imagesRight, 0.3f);
        Animation animationsUp = new Animation(imagesUp, 0.3f);
        Animation[] animations = new Animation[2];
        animations[0] = animationsRight;
        animations[1] = animationsUp;

        animationManager = new AnimationManager(animations);

    }

    public void start(char direction) {
        //startMoving(direction);
    }

    public void stop() {
        //timer.stop();
    }

    /*
    PUBLIC GETTERS FUNCTIONS
     */
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWayToGo() {
        return wayToGo;
    }

}
