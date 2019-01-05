package group23.pacman.controller;

import group23.pacman.MainApp;
import group23.pacman.system.AudioManager;
import group23.pacman.view.BackgroundAnimationManager;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    BackgroundAnimationManager backgroundAnimationManagerGhost;
    private boolean isGoingUp = true;

    private UINavigationAdapter<ToggleButton> navigationAdapter = new UINavigationAdapter<>();

    private static final String JOYSTICK_LISTENER_ID = "PlayerSelectionController";

    public PlayerSelectionController() {
        super("/group23/pacman/view/PlayerSelectionController.fxml");
        players_1.setSelected(true);
        navigationAdapter.addRow(players_1,players_2);
        Canvas canvas = new Canvas(1440, 1000);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        setUpBackgroudAnimations(gc);

        getRoot().getChildren().add(canvas);

        startAnimations();
    }

    public void startAnimations() {
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                backgroundAnimationManagerGhost.startMoving();
            }
        }.start();
    }

    public void stopAnimations() {
        backgroundAnimationManagerGhost.stop();
    }

    public void setUpBackgroudAnimations(GraphicsContext gc) {

        backgroundAnimationManagerGhost = new BackgroundAnimationManager(800, 1000, 80, "ghost1", gc, 'U', 0.3f);

        getRoot().getChildren().add(backgroundAnimationManagerGhost);

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
                    stopAnimations();
                    GameSettings.instance.setNumbrOfPlayers(navigationAdapter.getY() + 1);
                    NameInputViewController controller = new NameInputViewController(0);
                    MainApp.getInstance().pushViewController(controller,true);
                    AudioManager.shared.play("confirmation");
                    break;
                case TWO:
                    // go back
                    //app.show_main_menu();
                    stopAnimations();
                    MainApp.getInstance().popViewController(true);
                    AudioManager.shared.play("confirmation");
                    break;
            }
        }
    }
}
