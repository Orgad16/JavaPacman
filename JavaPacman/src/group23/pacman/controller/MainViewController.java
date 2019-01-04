package group23.pacman.controller;

import group23.pacman.MainApp;
import group23.pacman.view.BackgroundAnimationManager;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;

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

    BackgroundAnimationManager backgroundAnimationManagerPacman;
    BackgroundAnimationManager backgroundAnimationManagerGhost;
    BackgroundAnimationManager backgroundAnimationManagerGhost2;

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
        backgroundAnimationManagerPacman = new BackgroundAnimationManager(-150, 100, 1590, "pacman", gc, 'R');
        backgroundAnimationManagerGhost = new BackgroundAnimationManager(-60, 100, 1590, "ghost", gc, 'R');
        backgroundAnimationManagerGhost2 = new BackgroundAnimationManager(0, 100, 1590, "ghost", gc, 'R');

        getRoot().getChildren().add(backgroundAnimationManagerPacman);
        getRoot().getChildren().add(backgroundAnimationManagerGhost);
        getRoot().getChildren().add(backgroundAnimationManagerGhost2);
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
                    stopAnimations();
                    handleAction();
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
