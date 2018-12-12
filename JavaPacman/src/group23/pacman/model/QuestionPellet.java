package group23.pacman.model;

import javafx.scene.image.Image;

public class QuestionPellet extends Pellet {


    protected static final int SPRITE_WIDTH = 20;
    protected static final int SPRITE_HEIGHT = 20;

    private Question question;

    public QuestionPellet(int x, int y, Question question) {
        super(x, y, TYPE.QUESTION_PELLET.toString());
        this.question = question;
        setImage();
    }

    @Override
    public void setImage() {

        image = new Image("assets/tempPellet.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
        this.type = TYPE.QUESTION_PELLET;
    }
}
