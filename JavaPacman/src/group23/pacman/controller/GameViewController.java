package group23.pacman.controller;

import group23.pacman.MainApp;
import group23.pacman.model.Board;
import group23.pacman.model.Game;
import group23.pacman.model.GameObject;
import group23.pacman.model.TemporaryGhost;
import group23.pacman.view.DialogView;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.UIView;
import javafx.scene.image.ImageView;


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
    


    private Game game;

    // adapter used for joystick navigation
    UINavigationAdapter<Canvas> gameViewAdapter = new UINavigationAdapter<>();

    public GameViewController() {
        super("/group23/pacman/view/GameViewController.fxml");
        int numberOfPlayers = GameSettings.instance.getNumbrOfPlayers();
        int selectedMap = GameSettings.instance.getMap();

        //TODO: complete game setup
        GraphicsContext mzBg = maze_canvas.getGraphicsContext2D();
        GraphicsContext gcWall = game_canvas.getGraphicsContext2D();

        //-----------------------------
        // how to create a dialog view
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
        overlay.setVisible(false);

        //-----------------------------


        Board.canvasWidth = (int) maze_canvas.getWidth();
        // init game with map and number of players
        game = new Game(selectedMap, numberOfPlayers, 0, 0);

        //draw(mzBg, gcWall);
        // draw on game canvas with walls only
        ArrayList<GameObject> objects = game.getOtherGameObjects();
        for (GameObject object : objects) {
            //if (object.getType() == GameObject.TYPE.WALL) {
            object.draw(gcWall);
            //}
        }

        // need to update the game first
        //game.update();
        draw(mzBg);

        // draw all other objects on objects canvas - pellets, pacman, ghosts

        gameViewAdapter.addRow(maze_canvas, game_canvas);
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
        // TODO: handle joystick controller input
    }


    /* Draws all objects */
    private void draw(GraphicsContext...graphicsContext) {


        game.getPacman().draw(graphicsContext[0]);
        game.getPacman().getWhip().draw(graphicsContext[0]);

		/* Draws other objects (pellets) */
        ArrayList<GameObject> objects = game.getOtherGameObjects();
        for (GameObject object : objects) {
            if (object.getType() != GameObject.TYPE.WALL)
                object.draw(graphicsContext[0]);
        }

        game.getGhost().draw(graphicsContext[0]);
        game.getGhost2().draw(graphicsContext[0]);
        game.getGhost3().draw(graphicsContext[0]);
        ArrayList<TemporaryGhost> tempGhosts = game.getTempGhost();
        for (TemporaryGhost tempGhost : tempGhosts) {
            tempGhost.draw(graphicsContext[0]);
        }



    }


}