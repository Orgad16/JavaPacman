package group23.pacman.controller;

import group23.pacman.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import ui.UIViewController;

/**
 * Created By Tony on 12/12/2018
 */
public class PlayerSelectionController extends RootController implements JoystickManager.JoystickListener{

    @FXML
    ToggleButton players_1;

    @FXML
    ToggleButton players_2;

    private int current_index = 0;

    private void move_left(){
        all_buttons()[current_index].setSelected(false);
        current_index--;

        if(current_index == -1){
            current_index = all_buttons().length - 1;
        }
        all_buttons()[current_index].setSelected(true);
    }

    private void move_right(){
        all_buttons()[current_index].setSelected(false);
        current_index++;

        if(current_index == all_buttons().length){
            current_index = 0;
        }
        all_buttons()[current_index].setSelected(true);
    }

    ToggleButton[] all_buttons(){
        return new ToggleButton[]{players_1,players_2};
    }

    private static final String JOYSTICK_LISTENER_ID = "PlayerSelectionController";

    private MainApp app;

    public PlayerSelectionController(MainApp app) {
        super("/group23/pacman/view/PlayerSelectionController.fxml");
        this.app = app;
        players_1.setSelected(true);
        view.setOnKeyPressed(JoystickManager.shared);
        JoystickManager.shared.subscribe(JOYSTICK_LISTENER_ID,this);
    }

    @Override
    public void didBecomeActive() {
        view.setOnKeyPressed(JoystickManager.shared);

        // register controller to joystick manager
        JoystickManager
                .shared
                .subscribe(JOYSTICK_LISTENER_ID, this);
    }

    @Override
    public void didEnterBackground() {
        JoystickManager.shared.unsubscribe(JOYSTICK_LISTENER_ID);
    }

    @Override
    public void onJoystickTriggered(int joystickId, JoystickManager.Key selectedKey) {
        if(joystickId == 1) {
            switch (selectedKey){
                case LEFT:
                    // move left
                    move_left();
                    break;
                case RIGHT:
                    // move right
                    move_right();
                    break;
                case ONE:
                    // select
                    NameInputViewController controller = new NameInputViewController(app,current_index + 1);
                    app.pushViewController(controller,true);
                    break;
                case TWO:
                    // go back
                    //app.show_main_menu();
                    app.popViewController(true);
                    break;
            }
        }
    }
}
