package group23.pacman.view;
import javafx.scene.canvas.GraphicsContext;

/** This class handles an array of animations, each of which has its own array of images(frames).*/
public class AnimationManager {

    private Animation animations[];
    private int animationIndex;

    public AnimationManager(Animation[] animations){

        this.animations = animations;
        this.animationIndex = 0;
        
    }

    public void playAction(int index){
    	
    	/* If the current animation isn't already playing, start playing it, then move onto the next animation */
        if (!animations[animationIndex].isPlaying()){
        	animations[animationIndex].playAnimation();
        }

        animationIndex = index;
    }
    
    public void stopAction() {
    	
    	animations[animationIndex].stopAnimation();
    	animations[animationIndex].reset();
    }

    public void draw(GraphicsContext graphicsContext,double x,double y){

        if (animations[animationIndex].isPlaying()){
        	animations[animationIndex].draw(graphicsContext,x,y);
        }
    }

    public void update(){

        if (animations[animationIndex].isPlaying()){
        	animations[animationIndex].update();
        }
    }
    
    public int getFrameIndex() {
    	
    	return this.animations[animationIndex].getIndex();
    }

}
