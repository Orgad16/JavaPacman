package group23.pacman.view.question;

import group23.pacman.model.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ui.UIView;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public class QuestionView extends UIView{

    @FXML
    private Label questionName;

    @FXML
    private Label levelDiff;

    @FXML
    private Label teamName;

    public QuestionView(Question question) {
        super("/group23/pacman/view/QuestionCellView.fxml");
        questionName.setText(question.getQuestionID());
        levelDiff.setText("Level: " + question.getLevel());
        teamName.setText("Team: " + question.getTeam());
    }
}
