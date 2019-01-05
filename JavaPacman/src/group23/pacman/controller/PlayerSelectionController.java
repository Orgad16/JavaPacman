package group23.pacman.controller;

import group23.pacman.MainApp;
import group23.pacman.system.AudioManager;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import ui.UIViewController;

/**
 * Created By Tony on 12/12/2018
 */
public class PlayerSelectionController extends RootController implements JoystickManager.JoystickListener{

    @FXML
    private ToggleButton players_1;

    @FXML
    private ToggleButton players_2;

    private UINavigationAdapter<ToggleButton> navigationAdapter = new UINavigationAdapter<>();

    private static final String JOYSTICK_LISTENER_ID = "PlayerSelectionController";

    public PlayerSelectionController() {
        super("/group23/pacman/view/PlayerSelectionController.fxml");
        players_1.setSelected(true);
        navigationAdapter.addRow(players_1,players_2);
    }

    @Override
    public void didBecomeActive() {

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
                    navigationAdapter.current().setSelected(false);
                    navigationAdapter.move_left().setSelected(true);
                    AudioManager.shared.play("highlight");
                    break;
                case RIGHT:
                    // move right
                    navigationAdapter.current().setSelected(false);
                    navigationAdapter.move_right().setSelected(true);
                    AudioManager.shared.play("highlight");
                    break;
                case ONE:
                    // select
                    GameSettings.instance.setNumbrOfPlayers(navigationAdapter.getY() + 1);
                    NameInputViewController controller = new NameInputViewController(0);
                    MainApp.getInstance().pushViewController(controller,true);
                    AudioManager.shared.play("confirmation");
                    break;
                case TWO:
                    // go back
                    //app.show_main_menu();
                    MainApp.getInstance().popViewController(true);
                    AudioManager.shared.play("confirmation");
                    break;
            }
        }
    }
}
