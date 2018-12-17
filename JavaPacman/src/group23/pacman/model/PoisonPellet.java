package group23.pacman.model;

import javafx.scene.image.Image;

public class PoisonPellet extends RandomPellet {



    public PoisonPellet(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean shouldShowPellet(Timer timer) {
        System.out.println(timer.getTimeRemaining());
        boolean boo = timer.getTimeRemaining() != 120;
        System.out.println(boo);
        return boo;

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


}


