package group23.pacman.model;

import javafx.scene.image.Image;

public class PoisonPellet extends RandomPellet {

    private boolean shouldShow;

    public PoisonPellet(int x, int y) {
        super(x, y);
        shouldShow = true;
    }

    @Override
    public boolean shouldShowPellet(Timer timer) {
        if (shouldBeShowing()) {
            setShouldShow(timer.getTimeRemaining() % 8 == 0 && timer.getTimeRemaining() != 120);
            //System.out.println(timer.getTimeRemaining());
            //System.out.println(timer.getTimeRemaining() % 8 == 0 && timer.getTimeRemaining() != 120);
            return shouldBeShowing();
        } else {
            return false;
        }
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
        image = new Image("assets/pellet_poison.png", SPRITE_WIDTH, SPRITE_HEIGHT, true, true);
    }

    @Override
    public String toString() {
        return "POISON_PELLET";
    }

    public boolean shouldBeShowing() {
        return shouldShow;
    }

    public void setShouldShow(boolean b) {
        this.shouldShow = b;
    }
}


