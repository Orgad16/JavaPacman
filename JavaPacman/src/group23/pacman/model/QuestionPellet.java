package group23.pacman.model;

import javafx.scene.image.Image;

public class QuestionPellet extends Pellet {


    protected static final int SPRITE_WIDTH = 10;
    protected static final int SPRITE_HEIGHT = 10;

    private Question question;

    public QuestionPellet(int x, int y, Question question) {
        super(x, y);
        this.question = question;

    }

    @Override
    public void setImage() {

        image = new Image("assets/tempQuestionPellet.png",SPRITE_WIDTH,SPRITE_HEIGHT,false,false);
        this.type = TYPE.QUESTION_PELLET;
    }

    public Question getQuestion() {
        return question;
    }
}
