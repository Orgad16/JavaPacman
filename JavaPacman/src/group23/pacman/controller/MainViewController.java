package group23.pacman.controller;

import group23.pacman.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

/**
 * Created by Antonio Zaitoun on 10/12/2018.
 */
public class MainViewController extends RootController implements JoystickManager.JoystickListener {

    private final MainApp mainApp;

    @FXML
    private ToggleButton playBtn;

    @FXML
    private ToggleButton settingsBtn;

    @FXML
    private ToggleButton leaderboardBtn;

    private int current_index = 0;

    UINavigationAdapter<ToggleButton> navigationAdapter = new UINavigationAdapter<>();

    private static final String JOYSTICK_LISTENER_ID = "MainViewController";

    public MainViewController(MainApp app) {
        super("/group23/pacman/view/MainViewController.fxml");
        mainApp = app;
        playBtn.setSelected(true);
        navigationAdapter.addRow(playBtn);
        navigationAdapter.addRow(settingsBtn);
        navigationAdapter.addRow(leaderboardBtn);



    }

    @Override
    public void onJoystickTriggered(int joystickId, JoystickManager.Key selectedKey) {
        // only allow menu control for the first joystick
        if(joystickId == 1) {
            switch (selectedKey){
                case UP:
                    navigationAdapter.current().setSelected(false);
                    navigationAdapter.move_up().setSelected(true);
                    break;
                case DOWN:
                    navigationAdapter.current().setSelected(false);
                    navigationAdapter.move_down().setSelected(true);
                    break;
                case ONE:
                    handleAction();
                    break;
            }
        }
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

    public void handleAction() {
        switch (navigationAdapter.getX()){
            case 0:
                // play
                PlayerSelectionController controller = new PlayerSelectionController(mainApp);
                mainApp.pushViewController(controller,true);
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
