package group23.pacman.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import ui.UIViewController;

/**
 * Created by Antonio Zaitoun on 10/12/2018.
 */
public class MainViewController extends UIViewController {

    @FXML
    ToggleButton playBtn;

    @FXML
    ToggleButton settingsBtn;

    @FXML
    ToggleButton leaderboardBtn;

    private int current_index = 0;

    private void move_up(){
        all_buttons()[current_index].setSelected(false);
        current_index--;

        if(current_index == -1){
            current_index = all_buttons().length - 1;
        }
        all_buttons()[current_index].setSelected(true);
    }

    private void move_down(){
        all_buttons()[current_index].setSelected(false);
        current_index++;

        if(current_index == all_buttons().length){
            current_index = 0;
        }
        all_buttons()[current_index].setSelected(true);
    }

    ToggleButton[] all_buttons(){
        return new ToggleButton[]{playBtn,settingsBtn,leaderboardBtn};
    }

    public MainViewController() {
        super("/group23/pacman/view/MainViewController.fxml");
        playBtn.setSelected(true);

        view.setOnKeyPressed(JoystickManager.shared);
        JoystickManager
                .shared
                .subscribe("MainViewController", (joystickId, selectedKey) -> {

            // only allow menu control for the first joystick
            if(joystickId == 1) {
                switch (selectedKey){
                    case UP:
                        move_up();
                        break;
                    case DOWN:
                        move_down();
                        break;
                    case ONE:
                        break;
                }
            }
        });
    }
}
