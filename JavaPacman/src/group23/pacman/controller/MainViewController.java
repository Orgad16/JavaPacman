package group23.pacman.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import ui.UIViewController;

/**
 * Created by Antonio Zaitoun on 10/12/2018.
 */
public class MainViewController extends UIViewController {

    @FXML
    Button playBtn;

    public MainViewController() {
        super("/group23/pacman/view/MainViewController.fxml");
    }
}
