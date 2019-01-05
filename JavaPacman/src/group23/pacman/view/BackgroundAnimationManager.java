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
    private boolean repeat;

    public BackgroundAnimationManager(int x, int y, int wayToGo, String character, GraphicsContext context, char direction, float speed) {
        this.x = x;
        this.y = y;
        this.initX = x;
        this.initY = y;
        this.direction = direction;
        this.wayToGo = wayToGo;
        setUpAnimations(character, speed);
        this.gc = context;
        this.repeat = true;
    }

    public void startMoving() {
        if (direction == 'R')
            updateDestination(x+SPEED, y);
        if (direction == 'U')
            updateDestination(x, y-SPEED);
        if (direction == 'D')
            updateDestination(x, y+SPEED);
        if (direction == 'L')
            updateDestination(x-SPEED, y);
        drawAnimation(x,y);
        update();

    }

    public void drawAnimation(int x, int y) {
        gc.clearRect(x,y,SPRITE_HEIGHT + 10,SPRITE_WIDTH + 10);
        animationManager.draw(gc, x, y);
    }

    public void updateDestination(int i, int k) {
        if (direction == 'R') {
            if (i < initX + wayToGo) {
                this.x = i;
                this.y = k;
            } else {
                if (repeat)
                    changeDirection(getOppositeDirection(direction));

            }
        }

        if (direction == 'L') {
            if (i >= initX) {
                this.x = i;
                this.y = k;
            } else {
                if (repeat)
                    changeDirection(getOppositeDirection(direction));

            }
        }

        if (direction == 'U') {
            this.x = i;
            this.y = k;
            if (k < initY - wayToGo) {
                if (repeat)
                    changeDirection(getOppositeDirection(direction));
            }
            return;
        }

        if (direction == 'D') {
            this.x = i;
            this.y = k;
            if (k >= initY + wayToGo) {
                if (repeat)
                    changeDirection(getOppositeDirection(direction));
            }
        }

    }

    public char getOppositeDirection(char direction) {
        switch (direction) {
            case 'U':
                return 'D';
            case 'D':
                return 'U';
            case 'R':
                return 'L';
            case 'L':
                return 'R';
        }
        return 'D';
    }

    public void changeDirection(char newDirection) {
        this.direction = newDirection;
    }

    public void resetAnimation() {
        gc.clearRect(x,y,SPRITE_HEIGHT,SPRITE_WIDTH + 10);
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
                animationManager.playAction(2);
                break;
            case 'L':
                animationManager.playAction(3);
                break;
        }
    }

    public void setUpAnimations(String character, float speed) {
        Image[] imagesRight = new Image[2];
        Image[] imagesUp = new Image[2];
        Image[] imagesDown = new Image[2];
        Image[] imagesLeft = new Image[2];

        switch (character){
            case "pacman":
                imagesRight[0] = new Image("assets/Pacman/rightClose.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesRight[1] = new Image("assets/Pacman/rightOpen.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesUp[0] = new Image("assets/Pacman/upClose.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesUp[1] = new Image("assets/Pacman/upOpen.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesDown[0] = new Image("assets/Pacman/downClose.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesDown[1] = new Image("assets/Pacman/downOpen.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesLeft[0] = new Image("assets/Pacman/leftClose.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesLeft[1] = new Image("assets/Pacman/leftOpen.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                break;
            default:
                imagesRight[0] = new Image("assets/Ghost/" + character + "Right2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesRight[1] = new Image("assets/Ghost/" + character + "Right1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesUp[0] = new Image("assets/Ghost/" + character + "Up2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesUp[1] = new Image("assets/Ghost/" + character + "Up1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesDown[0] = new Image("assets/Ghost/" + character + "Down2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesDown[1] = new Image("assets/Ghost/" + character + "Down1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesLeft[0] = new Image("assets/Ghost/" + character + "Left2.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                imagesLeft[1] = new Image("assets/Ghost/" + character + "Left1.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
                break;
        }
        Animation animationsRight = new Animation(imagesRight, speed);
        Animation animationsUp = new Animation(imagesUp, speed);
        Animation animationDown = new Animation(imagesDown, speed);
        Animation animationLeft = new Animation(imagesLeft, speed);
        Animation[] animations = new Animation[4];
        animations[0] = animationsRight;
        animations[1] = animationsUp;
        animations[2] = animationDown;
        animations[3] = animationLeft;

        animationManager = new AnimationManager(animations);

    }

    public void start(char direction) {
        //startMoving(direction);
    }

    public void stop() {
        gc.clearRect(x,y,SPRITE_WIDTH + 10,SPRITE_HEIGHT + 10);
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

    public void setRepeat(boolean b) {
        this.repeat = b;
    }

}
