package group23.pacman.model;

import javafx.scene.image.Image;

public class PoisonPellet extends RandomPellet {

    public PoisonPellet(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean shouldShowPellet(Timer timer) {
        return timer.getTimeRemaining()%10 == 0  && timer.getTimeRemaining() != 120;
    }

    @Override
    public int appearForSeconds() {
        return 8;
    }

    @Override
    public TYPE getType() {
        return TYPE.POISON_PELLET;
    }

    @Override
    public void setImage() {
        image = new Image("assets/tempPoisonPellet.png", SPRITE_WIDTH, SPRITE_HEIGHT, false, false);
    }
}


