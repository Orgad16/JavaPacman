package group23.pacman.controller;

public class OptionsController extends RootController implements JoystickManager.JoystickListener {

    public OptionsController() {
        super("/group23/pacman/view/OptionsController.fxml");
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

    }
}
