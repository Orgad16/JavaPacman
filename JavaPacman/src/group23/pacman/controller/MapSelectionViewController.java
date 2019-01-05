package group23.pacman.controller;

import group23.pacman.MainApp;
import group23.pacman.system.AudioManager;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import sun.applet.Main;

/**
 * Created By Tony on 12/12/2018
 */
public class MapSelectionViewController extends RootController implements JoystickManager.JoystickListener {

    private static final String JOYSTICK_LISTENER_ID = "MapSelectionViewController";

    // the maps objects
    @FXML private ToggleButton map_0;
    @FXML private ToggleButton map_1;
    @FXML private ToggleButton map_2;
    @FXML private ToggleButton map_3;

    /**
     * navigation handler
     */
    private UINavigationAdapter<ToggleButton> adapter = new UINavigationAdapter<>();

    public MapSelectionViewController() {
        super("/group23/pacman/view/MapSelectionViewController.fxml");
        adapter.addRow(map_0,map_1,map_2,map_3);
        adapter.current().setSelected(true);
    }

    @Override
    public void didBecomeActive() {
        JoystickManager.shared.subscribe(JOYSTICK_LISTENER_ID,this);
    }

    @Override
    public void didEnterBackground() {
        JoystickManager.shared.unsubscribe(JOYSTICK_LISTENER_ID);
    }

    @Override
    public void onJoystickTriggered(int joystickId, JoystickManager.Key selectedKey) {
        if(joystickId == 1) {
            switch (selectedKey){
                /* NAVIGATION CONTROL*/
                case LEFT:
                    adapter.current().setSelected(false);
                    adapter.move_left().setSelected(true);
                    AudioManager.shared.play("highlight");
                    break;
                case RIGHT:
                    adapter.current().setSelected(false);
                    adapter.move_right().setSelected(true);
                    AudioManager.shared.play("highlight");
                    break;
                case ONE:
                    // map selected
                    GameSettings.instance.setMap(adapter.getY());

                    // init controller
                    GameViewController gameViewController = new GameViewController();

                    // push to navigation stack
                    MainApp.getInstance().pushViewController(gameViewController);
                    AudioManager.shared.play("confirmation");
                    break;
                case TWO:
                    //go back
                    MainApp.getInstance().popViewController();
                    AudioManager.shared.play("confirmation");
                    break;
            }
        }
    }
}
