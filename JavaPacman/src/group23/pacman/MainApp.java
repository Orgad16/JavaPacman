package group23.pacman;

import java.io.IOException;
import java.util.Vector;

import group23.pacman.controller.*;
import group23.pacman.model.Game;
import group23.pacman.view.CharacterSelectController;
import group23.pacman.view.CreditsController;
import group23.pacman.view.EditNameController;
import group23.pacman.view.GameViewController;
import group23.pacman.view.HelpScreenController;
import group23.pacman.view.LeaderboardController;
import group23.pacman.view.LevelSelectController;
import group23.pacman.view.ResultsController;
import group23.pacman.view.WelcomeScreenController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.UIViewController;


/** The class that creates the platform and shows the main menu. Also has the methods for showing other screens */
public class MainApp extends Application{ 
	
	/* The window for showing the game/application */
	private Stage gameWindow;

	private Vector<UIViewController> navigationStack = new Vector<>();

	/**
	 * append new view controller to the stack
	 * @param controller
	 * @param animated
	 */
	public void pushViewController(UIViewController controller, boolean animated) {

		// get the previous element in the stack
		UIViewController olderController = navigationStack.size() > 0 ? navigationStack.lastElement() : null;

		// append the newer element
		navigationStack.add(controller);

		// handle transition
		if (animated) {
			change_root(controller.view);
		}else {
			gameWindow.getScene().setRoot(controller.view);
		}

		// notify new controller to become active
		if(controller instanceof RootController){
			((RootController)controller).didBecomeActive();
		}

		// notify old controller to become in-active
		if (olderController != null && olderController instanceof RootController){
			((RootController)olderController).didEnterBackground();
		}
	}

	/**
	 * Use this method to remove the most recent view controller from the navigation stack.
	 * @param animated
	 */
	public void popViewController(boolean animated) {

		if(navigationStack.size() == 1) {
			System.err.println("Nothing to pop! you only have one controller left in the stack.");
			return;
		}

		// remove from stack
		UIViewController toRemove = navigationStack.remove(navigationStack.size() - 1);
		UIViewController controller = navigationStack.lastElement();

		if (animated) {
			change_root(controller.view);
		}else {
			gameWindow.getScene().setRoot(controller.view);
		}

		// notify new controller to become active
		if(controller instanceof RootController){
			((RootController)controller).didBecomeActive();
		}

		// notify old controller to become in-active
		if (toRemove != null && toRemove instanceof RootController){
			((RootController)toRemove).didEnterBackground();
		}
	}

	@Override
	public void start(Stage gameWindow) {
		this.gameWindow = gameWindow;

		Scene primaryScene = new Scene(new Pane());
		primaryScene.setFill(Color.BLACK);
		gameWindow.setScene(primaryScene);
		gameWindow.show();
		//gameWindow.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		//gameWindow.setFullScreen(true);

		IntroController introController = new IntroController();
		change_root(introController.view);

		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(event -> {
			MainViewController controller = new MainViewController(this);
			pushViewController(controller,true);
			//change_root(controller.view,0.005f);
		});
		new Thread(sleeper).start();

		// joystick 1
		JoystickManager
				.shared
				.register(
						KeyCode.I, //up
						KeyCode.K, //down
						KeyCode.J, //left
						KeyCode.L, //right
						KeyCode.ENTER, //one
						KeyCode.SHIFT // two
				);
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void show_main_menu(){
		MainViewController controller = new MainViewController(this);
		change_root(controller.view);
	}

	public void show_player_selection_menu() {
		PlayerSelectionController controller = new PlayerSelectionController(this);
		change_root(controller.view);
	}

	public void show_name_selection(int numPlayers){

	}

	public void show_map_selection(int numPlayers){

	}

	private void change_root(Parent root){
		change_root(root,0.05f);
	}
	private void change_root(Parent root,float delay) {
		gameWindow.getScene().setRoot(root);
		final long[] time = {System.currentTimeMillis()};
		final float[] opacity = {0};


		AnimationTimer primaryAnimator = new AnimationTimer() {
			public void handle(long now) {

				/* Every 0.05 seconds increase the opacity of the black background to give illusion of fading out of this scene */
				if (System.currentTimeMillis() - time[0] > 0.05f) {

					opacity[0] += delay;
					root.setOpacity(opacity[0]);
					time[0] = System.currentTimeMillis();
				}
				if (opacity[0] >= 1) {
					this.stop();
				}
			}
		};
		primaryAnimator.start();
	}


	

}