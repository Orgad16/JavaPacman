package group23.pacman.model;

import javafx.scene.image.Image;

import java.util.List;
import java.util.Random;

public class QuestionPellet extends RandomPellet {


    private List<Question>  questions;

    public QuestionPellet(int x, int y,List<Question> questions) {
        super(x, y);
        this.questions = questions;
    }

    @Override
    public boolean shouldShowPellet(Timer timer) {
        return !questions.isEmpty() && timer.getTimeRemaining() % 10 == 0  && timer.getTimeRemaining() != 120;
    }

    @Override
    public int appearForSeconds() {
        return 8;
    }

    @Override
    public TYPE getType() {
        return TYPE.QUESTION_PELLET;
    }

    @Override
    public void setImage() {

        image = new Image("assets/pellet_question.png",SPRITE_WIDTH,SPRITE_HEIGHT,true,true);
        this.type = TYPE.QUESTION_PELLET;
    }

    public Question getQuestion() {
        return questions.remove(new Random().nextInt(questions.size()));
    }


}
