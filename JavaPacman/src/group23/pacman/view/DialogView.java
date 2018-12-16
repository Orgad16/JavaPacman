package group23.pacman.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ui.UIView;

import java.util.ResourceBundle;

/**
 * Created By Tony on 14/12/2018
 */
public class DialogView extends UIView {

    @FXML
    public UIView contentView;

    @FXML
    public Label titleLabel;

    @FXML
    public Label descriptionLabel;

    public DialogView() {
        super("/group23/pacman/view/DialogView.fxml");
    }

    @Override
    public void layoutSubviews(ResourceBundle bundle) {
        super.layoutSubviews(bundle);
    }

    @Override
    public void layoutBundle(ResourceBundle bundle) {
        super.layoutBundle(bundle);
    }
}
