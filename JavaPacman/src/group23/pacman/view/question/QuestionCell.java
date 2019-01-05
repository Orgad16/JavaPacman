package group23.pacman.view.question;

import group23.pacman.model.Question;
import ui.UIListViewCell;
import ui.UIView;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public class QuestionCell extends UIListViewCell<Question,UIView> {
    @Override
    public UIView load(Question item) {
        return new QuestionView(item);
    }
}
