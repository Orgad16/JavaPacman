package group23.pacman.view;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**This class holds all the frames of a single animation */

public class Animation {

    private Image[] frames;
    private int frameIndex;
    private boolean isPlaying = false;
    private float frameTime;
    private long lastFrame;
    
    /* The animation is constructed using frames, and the time per frame */
    public Animation(Image[] frames,float animTime){

        this.frames = frames;
        frameIndex = 0;
        frameTime = animTime/frames.length;
        lastFrame = System.currentTimeMillis();

    }

    public boolean isPlaying(){
    	
        return this.isPlaying;
    }

    public void playAnimation(){
    	
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }

    public void stopAnimation(){
    	
       isPlaying = false;
    }

    public void update(){
    	
    	/* Do nothing if the animation isn't supposed to be updated */
        if (!isPlaying){
            return;
        }
        /* If the time since the last frame has exceeded the expected time per frame, update to the next frame */
        if  (System.currentTimeMillis() - lastFrame > frameTime * 1000) {
            frameIndex++;
            frameIndex = (frameIndex >= frames.length) ? 0 : frameIndex;
            lastFrame = System.currentTimeMillis();
        }
    }
    
    
    public void draw(GraphicsContext graphicsContext,double x, double y){
        if (!isPlaying){
            return;
        }
        else {
        	graphicsContext.drawImage(frames[frameIndex],x,y);
        }
    }
    
    public void reset() {
    	frameIndex = 0;
    }
    
    public int getIndex() {
    	
    	return this.frameIndex;
    }


}
