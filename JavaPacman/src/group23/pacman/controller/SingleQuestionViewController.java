package group23.pacman.controller;

import group23.pacman.model.Question;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import ui.UIView;
import ui.UIViewController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public class SingleQuestionViewController extends UIViewController{

    /**
     * The field that displays the question
     */
    @FXML private TextField questionTextField;

    /**
     * The first answer
     */
    @FXML private TextField answer1TextField;

    /**
     * The second answer
     */
    @FXML private TextField answer2TextField;

    /**
     * The third answer
     */
    @FXML private TextField answer3TextField;

    /**
     * The forth answer
     */
    @FXML private TextField answer4TextField;


    /**
     * Radio buttons to select the correct answer.
     */
    @FXML private RadioButton rd_1;
    @FXML private RadioButton rd_2;
    @FXML private RadioButton rd_3;
    @FXML private RadioButton rd_4;

    private final RadioButton[] radioButtons = {rd_1,rd_2,rd_3,rd_4};

    @FXML private TextField level;
    @FXML private TextField team;
    /**
     * Cancellation button
     */
    @FXML private Button btn_cancel;

    /**
     * Confirmation Button
     */
    @FXML private Button btn_save;


    private QuestionsViewController.PersistCallBack callBack;

    private int index;

    public SingleQuestionViewController(int index,Question question, QuestionsViewController.PersistCallBack callBack) {
        super("/group23/pacman/view/QuestionView.fxml");

        this.callBack = callBack;
        this.index = index;

        // setup toggle group
        final ToggleGroup group = new ToggleGroup();
        rd_1.setToggleGroup(group);
        rd_2.setToggleGroup(group);
        rd_3.setToggleGroup(group);
        rd_4.setToggleGroup(group);



        // load question
        if(question != null){
            questionTextField.setText(question.getQuestionID());

            List<String> answers = question.getAnswers();

            answer1TextField.setText(answers.get(0));
            answer2TextField.setText(answers.get(1));
            answer3TextField.setText(answers.get(2));
            answer4TextField.setText(answers.get(3));

            radioButtons[question.getCorrect_ans()].setSelected(true);

            team.setText(question.getTeam());
            level.setText(question.getLevel() + "");
        }

        // setup buttons listeners
        btn_save.setOnAction(event -> {
            Question newQuestion = collectQuestion();
            callBack.onPersist(index,newQuestion,false);
        });

        btn_cancel.setOnAction(event -> {
            if(index != -1)
                callBack.onPersist(index,null,false);
        });
    }

    private int getAnswer(){
        int i = 0;

        for (RadioButton radioButton : radioButtons) {
            if(radioButton.isSelected())
                return i;
            i++;
        }

        return -1;
    }

    private ArrayList<String> collectAnswers(){
        ArrayList<String> answers = new ArrayList<>();

        answers.add(answer1TextField.getText().trim());
        answers.add(answer2TextField.getText().trim());
        answers.add(answer3TextField.getText().trim());

        if (!answer4TextField.getText().trim().isEmpty())
            answers.add(answer4TextField.getText());

        return answers;
    }

    private Question collectQuestion() {

        String questionStr = questionTextField.getText();

        ArrayList<String> answers = collectAnswers();

        int correctAnswer = getAnswer();

        int qlevel = Integer.parseInt(level.getText().trim());

        String qteam = team.getText().trim();

        //TODO validate objects before sending into constructor

        Question question = new Question(questionStr,answers,correctAnswer,qlevel,qteam);

        return question;
    }
}
