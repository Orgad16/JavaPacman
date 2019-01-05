package group23.pacman.controller;

import group23.pacman.MainApp;
import group23.pacman.system.AudioManager;
import group23.pacman.view.BackgroundAnimationManager;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    BackgroundAnimationManager backgroundAnimationManagerPacman;
    BackgroundAnimationManager backgroundAnimationManagerGhost;
    BackgroundAnimationManager backgroundAnimationManagerGhost2;

    /**
     * navigation handler
     */
    private UINavigationAdapter<ToggleButton> adapter = new UINavigationAdapter<>();

    public MapSelectionViewController() {
        super("/group23/pacman/view/MapSelectionViewController.fxml");
        adapter.addRow(map_0,map_1,map_2,map_3);
        adapter.current().setSelected(true);

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
                backgroundAnimationManagerPacman.startMoving();
                backgroundAnimationManagerGhost.startMoving();
                backgroundAnimationManagerGhost2.startMoving();

            }
        }.start();
    }

    public void stopAnimations() {
        backgroundAnimationManagerPacman.stop();
        backgroundAnimationManagerGhost.stop();
        backgroundAnimationManagerGhost2.stop();
    }

    public void setUpBackgroudAnimations(GraphicsContext gc) {
        backgroundAnimationManagerPacman = new BackgroundAnimationManager(-150, 900, 1590, "pacman", gc, 'R', 0.3f);
        backgroundAnimationManagerGhost = new BackgroundAnimationManager(-60, 900, 1590, "ghost1", gc, 'R', 0.3f);
        backgroundAnimationManagerGhost2 = new BackgroundAnimationManager(0, 900, 1590, "ghost2", gc, 'R', 0.3f);

        getRoot().getChildren().add(backgroundAnimationManagerPacman);
        getRoot().getChildren().add(backgroundAnimationManagerGhost);
        getRoot().getChildren().add(backgroundAnimationManagerGhost2);
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
