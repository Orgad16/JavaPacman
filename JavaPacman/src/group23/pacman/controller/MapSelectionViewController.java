package group23.pacman.controller;

import group23.pacman.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

/**
 * Created By Tony on 12/12/2018
 */
public class MapSelectionViewController extends RootController implements JoystickManager.JoystickListener {

    private static final String JOYSTICK_LISTENER_ID = "MapSelectionViewController";
    private final MainApp app;

    @FXML private ToggleButton map_0;
    @FXML private ToggleButton map_1;
    @FXML private ToggleButton map_2;
    @FXML private ToggleButton map_3;

    UINavigationAdapter<ToggleButton> adapter = new UINavigationAdapter<>();

    public MapSelectionViewController(MainApp app) {
        super("/group23/pacman/view/MapSelectionViewController.fxml");
        this.app = app;
        adapter.addRow(map_0,map_1,map_2,map_3);
        adapter.current().setSelected(true);
    }

    @Override
    public void didBecomeActive() {
        view.setOnKeyPressed(JoystickManager.shared);
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
                case UP:
                    adapter.current().setSelected(false);
                    adapter.move_up().setSelected(true);
                    break;
                case LEFT:
                    adapter.current().setSelected(false);
                    adapter.move_left().setSelected(true);
                    break;
                case RIGHT:
                    adapter.current().setSelected(false);
                    adapter.move_right().setSelected(true);
                    break;
                case DOWN:
                    adapter.current().setSelected(false);
                    adapter.move_down().setSelected(true);
                    break;
                case ONE:
                    break;
                case TWO:
                    break;
            }
        }
    }
}
