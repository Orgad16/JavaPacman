package group23.pacman.controller;

import group23.pacman.model.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import ui.UIView;
import ui.UIViewController;

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

    /**
     * Cancellation button
     */
    @FXML private Button btn_cancel;

    /**
     * Confirmation Button
     */
    @FXML private Button btn_save;


    public SingleQuestionViewController(Question question) {
        super("/group23/pacman/view/QuestionView.fxml");

        // setup toggle group
        final ToggleGroup group = new ToggleGroup();
        rd_1.setToggleGroup(group);
        rd_2.setToggleGroup(group);
        rd_3.setToggleGroup(group);
        rd_4.setToggleGroup(group);

        RadioButton[] radioButtons = {rd_1,rd_2,rd_3,rd_4};

        // load question
        if(question != null){
            questionTextField.setText(question.getQuestionID());

            List<String> answers = question.getAnswers();

            answer1TextField.setText(answers.get(0));
            answer2TextField.setText(answers.get(1));
            answer3TextField.setText(answers.get(2));
            answer4TextField.setText(answers.get(3));

            radioButtons[question.getCorrect_ans()].setSelected(true);
        }

        // setup buttons listeners
    }
}
