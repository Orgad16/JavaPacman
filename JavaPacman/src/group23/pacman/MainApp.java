package group23.pacman;

import group23.pacman.controller.IntroController;
import group23.pacman.controller.JoystickManager;
import group23.pacman.controller.MainViewController;
import group23.pacman.controller.RootController;
import group23.pacman.model.Question;
import group23.pacman.system.AssetManager;
import group23.pacman.system.AudioManager;
import group23.pacman.system.protocols.MapsAssetProtocol;
import group23.pacman.system.protocols.QuestionsAssetProtocol;
import group23.pacman.system.protocols.ScoresAssetProtocol;
import group23.pacman.system.protocols.SettingsAssetProtocol;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ui.UIViewController;

import java.io.File;
import java.util.Vector;

public class MainApp extends Application{

	private static MainApp instance;

	public static MainApp getInstance() {
		return instance;
	}

	public void exit(){
		gameWindow.close();
	}

	/* The window for showing the game/application */
	private Stage gameWindow;

	public int getScreenWidth(){
		return (int) gameWindow.getScene().getWidth();
	}

	public int getScreenHeight(){
		return (int) gameWindow.getScene().getHeight();
	}

	public Stage getGameWindow() {
		return gameWindow;
	}

	/**
	 * The navigation stack.
	 * Do not insert to this stack manually. you'll regret it.
	 *
	 * Use the following methods to manage the controller stack:
	 * - pushViewController
	 * - popViewController
	 * - insertViewController
	 */
	private Vector<UIViewController> navigationStack = new Vector<>();

	public int getNavigationStackSize(){
		return navigationStack.size();
	}

	public void pushViewController(UIViewController controller){
		pushViewController(controller,true);
	}

	/**
	 * Append new view controller to the stack
	 *
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
		controller.view.requestFocus();
	}

	/**
	 * Use this method to remove the most recent view controller from the navigation stack.
	 */
	public void popViewController(){
		popViewController(true);
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

		controller.view.requestFocus();
		controller.view.setOnMouseClicked(null);
	}

	/**
	 * Use this method to insert a navigation controller anywhere at the stack.
	 * If you insert the controller in the last index then pushViewController will be called with no animations.
	 *
	 * @param controller The controller to insert.
	 * @param index The index at which you wish to insert.
	 */
	public void insertViewController(UIViewController controller,int index){
		index = Math.min(index,navigationStack.size() - 1);
		navigationStack.insertElementAt(controller,index);

		if (navigationStack.lastElement() == controller) {
			navigationStack.remove(navigationStack.size() - 1);
			pushViewController(controller,false);
		}
	}

	@Override
	public void start(Stage gameWindow) {
		instance = this;

		this.gameWindow = gameWindow;

		Scene primaryScene = new Scene(new Pane());
		primaryScene.setFill(Color.BLACK);
		gameWindow.setScene(primaryScene);
		gameWindow.show();
		gameWindow.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		gameWindow.setFullScreen(true);

		IntroController introController = new IntroController();
		change_root(introController.view);

		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ignored) { }
				return null;
			}
		};
		sleeper.setOnSucceeded(event -> {
			MainViewController controller = new MainViewController();
			pushViewController(controller,true);
		});
		new Thread(sleeper).start();

		// joystick 1
		JoystickManager
				.shared
				.register(
						KeyCode.UP, //up
						KeyCode.DOWN, //down
						KeyCode.LEFT, //left
						KeyCode.RIGHT, //right
						KeyCode.ENTER, //one
						KeyCode.SHIFT // two
				);

		// joystick 2
		JoystickManager
				.shared
				.register(
						KeyCode.W,
						KeyCode.S,
						KeyCode.A,
						KeyCode.D,
						KeyCode.Z,
						KeyCode.X
				);

		primaryScene.setOnKeyPressed(JoystickManager.shared);



	}

	public static void main(String[] args) {

		//assure that all assets exist on disk
		AssetManager.init(
				MapsAssetProtocol.class,
				QuestionsAssetProtocol.class,
				ScoresAssetProtocol.class,
				SettingsAssetProtocol.class
		);

		AudioManager.shared.register("whip","/assets/sfx/whipSound.mp3");
		AudioManager.shared.register("chomp","/assets/sfx/chompNoise.wav");
		AudioManager.shared.register("confirmation","/assets/sfx/confirmation.mp3");
		AudioManager.shared.register("highlight","/assets/sfx/highlight.mp3");
		AudioManager.shared.register("highscore","/assets/sfx/highScore.mp3");
		AudioManager.shared.register("menu","/assets/sfx/menuSelect.mp3");
		AudioManager.shared.register("toggle","/assets/sfx/toggle.mp3");

		AudioManager.shared.register("candy1","/assets/sfx/candy_1.mp3");
		AudioManager.shared.register("candy2","/assets/sfx/candy_2.mp3");
		AudioManager.shared.register("death","/assets/sfx/death.mp3");
		AudioManager.shared.register("eat","/assets/sfx/eat.mp3");
		AudioManager.shared.register("gamestart","/assets/sfx/game_start.mp3");
		AudioManager.shared.register("newlevel","/assets/sfx/new_level.mp3");


		AudioManager.shared.setVolume(0.3);
		launch(args);
	}

	private void change_root(Parent root){
		change_root(root,0.05f);
	}

	private void change_root(Parent root,float delay) {
		gameWindow.getScene().setRoot(root);
		final long[] time = {System.currentTimeMillis()};
		final float[] opacity = {0};
		root.setOpacity(opacity[0]);

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