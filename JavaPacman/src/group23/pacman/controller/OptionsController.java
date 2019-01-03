package group23.pacman.controller;

import group23.pacman.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

public class OptionsController extends RootController implements JoystickManager.JoystickListener {

    @FXML private ToggleButton sound_btn;
    @FXML private Label sound_lbl;

    @FXML private ToggleButton fs_btn;
    @FXML private Label fs_lbl;

    @FXML private ToggleButton tongue_btn;
    @FXML private Label tongue_lbl;

    @FXML private ToggleButton tutorial_btn;
    @FXML private ToggleButton questions_btn;

    private UINavigationAdapter<ToggleButton> navigationAdapter = new UINavigationAdapter<>();

    public OptionsController() {
        super("/group23/pacman/view/OptionsController.fxml");

        // setup navigation adapter
        navigationAdapter.addRow(sound_btn);
        navigationAdapter.addRow(fs_btn);
        navigationAdapter.addRow(tongue_btn);
        navigationAdapter.addRow(tutorial_btn);
        navigationAdapter.addRow(questions_btn);

        // highlight first button
        navigationAdapter.current().setSelected(true);
        tongue_lbl.setText(GameSettings.instance.isTonugeEnabled()? "ON": "OFF");
        fs_lbl.setText(GameSettings.instance.isFullScreenEnabled()? "ON": "OFF");
        sound_lbl.setText(GameSettings.instance.isSoundEnabled()? "ON": "OFF");
    }

    @Override
    public void didBecomeActive() {

        // register controller to joystick manager
        JoystickManager
                .shared
                .subscribe(identifier(), this);
    }

    @Override
    public void didEnterBackground() {
        JoystickManager.shared.unsubscribe(identifier());
    }

    @Override
    public void onJoystickTriggered(int joystickId, JoystickManager.Key selectedKey) {
        if (joystickId == 1){
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
                    handleAction(navigationAdapter.getX());
                    break;
                case TWO:
                    MainApp.getInstance().popViewController();
                    break;
            }
        }
    }

    private void handleAction(int index) {
        switch (index) {
            case 0:
                boolean oldSoundValue = GameSettings.instance.isSoundEnabled();
                sound_lbl.setText(!oldSoundValue ? "ON" : "OFF");
                GameSettings.instance.setSoundEnabled(!oldSoundValue);
                break;
            case 1:
                boolean oldFSValue = MainApp.getInstance().getGameWindow().isFullScreen();
                fs_lbl.setText(!oldFSValue ? "ON" : "OFF");
                MainApp.getInstance().getGameWindow().setFullScreen(!oldFSValue);
                break;
            case 2:
                boolean oldToungeValue = GameSettings.instance.isTonugeEnabled();
                tongue_lbl.setText(!oldToungeValue? "ON": "OFF");
                GameSettings.instance.setTonugeEnabled(!oldToungeValue);
                break;
            case 3:
                // tutorial
                break;
            case 4:
                // question management
                break;
        }
    }
}
