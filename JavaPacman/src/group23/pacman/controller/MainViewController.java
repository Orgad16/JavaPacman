package group23.pacman.controller;

import group23.pacman.MainApp;
import group23.pacman.system.AudioManager;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

/**
 * Created by Antonio Zaitoun on 10/12/2018.
 */
public class MainViewController extends RootController implements JoystickManager.JoystickListener {

    private static final String JOYSTICK_LISTENER_ID = "MainViewController";

    /**
     * play button menu
     */
    @FXML
    private ToggleButton playBtn;

    /**
     * settings button menu
     */
    @FXML
    private ToggleButton settingsBtn;

    /**
     * leaderboard button menu
     */
    @FXML
    private ToggleButton leaderboardBtn;

    @FXML
    private ToggleButton exitBtn;

    /**
     * navigation handler.
     */
    private UINavigationAdapter<ToggleButton> navigationAdapter = new UINavigationAdapter<>();


    public MainViewController() {
        super("/group23/pacman/view/MainViewController.fxml");

        // add buttons to navigation adapter
        navigationAdapter.addRow(playBtn);
        navigationAdapter.addRow(settingsBtn);
        navigationAdapter.addRow(leaderboardBtn);
        navigationAdapter.addRow(exitBtn);

        // highlight default
        navigationAdapter.current().setSelected(true);
    }

    @Override
    public void onJoystickTriggered(int joystickId, JoystickManager.Key selectedKey) {
        // only allow menu control for the first joystick
        if(joystickId == 1) {

            switch (selectedKey){
                case UP:
                    navigationAdapter.current().setSelected(false);
                    navigationAdapter.move_up().setSelected(true);
                    AudioManager.shared.play("highlight");
                    break;
                case DOWN:
                    navigationAdapter.current().setSelected(false);
                    navigationAdapter.move_down().setSelected(true);
                    AudioManager.shared.play("highlight");
                    break;
                case ONE:
                    handleAction();
                    AudioManager.shared.play("confirmation");
                    break;
            }
        }
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
        JoystickManager
                .shared
                .unsubscribe(JOYSTICK_LISTENER_ID);
    }

    public void handleAction() {
        switch (navigationAdapter.getX()){
            case 0:
                // play
                PlayerSelectionController controller = new PlayerSelectionController();
                MainApp.getInstance().pushViewController(controller,true);
                break;
            case 1:
                // options
                OptionsController questionController = new OptionsController();
                MainApp.getInstance().pushViewController(questionController);
                break;
            case 2:
                // leaderboards
                LeaderboardViewController leaderboardViewController = new LeaderboardViewController();
                MainApp.getInstance().pushViewController(leaderboardViewController);
                break;
            case 3:
                MainApp.getInstance().exit();
                break;
        }
    }
}
