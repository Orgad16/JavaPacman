package group23.pacman.controller;

import group23.pacman.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import ui.UIViewController;

/**
 * Created by Antonio Zaitoun on 10/12/2018.
 */
public class MainViewController extends UIViewController implements JoystickManager.JoystickListener {

    private final MainApp mainApp;

    @FXML
    private ToggleButton playBtn;

    @FXML
    private ToggleButton settingsBtn;

    @FXML
    private ToggleButton leaderboardBtn;

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

    private static final String JOYSTICK_LISTENER_ID = "MainViewController";

    public MainViewController(MainApp app) {
        super("/group23/pacman/view/MainViewController.fxml");
        mainApp = app;
        playBtn.setSelected(true);

        view.setOnKeyPressed(JoystickManager.shared);

        // register controller to joystick manager
        JoystickManager
                .shared
                .subscribe(JOYSTICK_LISTENER_ID, this);
    }

    @Override
    public void onJoystickTriggered(int joystickId, JoystickManager.Key selectedKey) {
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
                    handleAction();
                    break;
            }
        }
    }

    public void handleAction() {
        JoystickManager.shared.unsubscribe(JOYSTICK_LISTENER_ID);

        switch (current_index){
            case 0:
                // play
                mainApp.show_player_selection_menu();
                break;
            case 1:
                // options
                //TODO: go to options
                break;
            case 2:
                // leaderboards
                //TODO: go to leaderboards
                break;
        }
    }
}
