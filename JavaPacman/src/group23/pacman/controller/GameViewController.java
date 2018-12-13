package group23.pacman.controller;

import group23.pacman.model.Board;
import group23.pacman.model.Game;
import group23.pacman.model.GameObject;
import group23.pacman.view.DialogView;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.UIView;


import java.util.ArrayList;

/**
 * Created By Tony on 13/12/2018
 */
public class GameViewController extends RootController implements JoystickManager.JoystickListener{

    @FXML
    private Canvas maze_canvas;

    @FXML
    private Canvas game_canvas;

    // score
    @FXML
    private Label scoreLabel;

    // Life
    @FXML
    private Label lifeLabel;

    // Timer
    @FXML
    private Label timerLabel;

    // Player Name
    @FXML
    private Label playerNameLabel;

    @FXML
    private UIView overlay;
    

    // adapter used for joystick navigation
    UINavigationAdapter<Canvas> gameViewAdapter = new UINavigationAdapter<>();

    public GameViewController() {
        super("/group23/pacman/view/GameViewController.fxml");
        int numberOfPlayers = GameSettings.instance.getNumbrOfPlayers();
        int selectedMap = GameSettings.instance.getMap();

        //TODO: complete game setup

        DialogView dialogView = new DialogView();

        dialogView.titleLabel.setText("QUIT");
        dialogView.descriptionLabel.setText("Are you sure you want to quit?");
        ToggleButton quitButton= new ToggleButton();
        quitButton.getStyleClass().add("button-retro");
        quitButton.setText("Yes");

        ToggleButton cancelButton = new ToggleButton();
        cancelButton.getStyleClass().add("button-retro");
        cancelButton.setText("Cancel");
        cancelButton.setSelected(true);

        HBox box = new HBox();
        box.setSpacing(20);
        box.getChildren().addAll(quitButton,cancelButton);

        dialogView.contentView.getChildren().add(box);

        overlay.getChildren().add(dialogView);
        overlay.setVisible(true);


        Board.canvasWidth = (int) maze_canvas.getWidth();
        // init game with map and number of players
        Game game = new Game(selectedMap, numberOfPlayers, 0, 0);

        // draw on maze canvas with walls only
        GraphicsContext gcWall = maze_canvas.getGraphicsContext2D();
        ArrayList<GameObject> objects = game.getOtherGameObjects();
        for (GameObject object : objects) {
            if (object.getType() == GameObject.TYPE.WALL) {
                object.draw(gcWall);
            }
        }

        // draw all other objects on game canvas

        gameViewAdapter.addRow(maze_canvas, game_canvas);
    }

    @Override
    public void didBecomeActive() {
        view.setOnKeyPressed(JoystickManager.shared);

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
        // TODO: handle joystick controller input
    }


}