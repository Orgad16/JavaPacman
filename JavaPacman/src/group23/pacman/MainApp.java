package group23.pacman;

import java.io.IOException;

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


/** The class that creates the platform and shows the main menu. Also has the methods for showing other screens */
public class MainApp extends Application{ 
	
	/* The window for showing the game/application */
	private Stage gameWindow;
	
	/* Layout to draw UI onto */
	private BorderPane rootLayout;
	
	private Scene scene;
	
	/* Stores the player mode selected from GameModeSelect */
	private int numPlayers;
	private int player2;
	private int player3;
	
	/* Stores the map selected from LevelSelect */
	private char map;
	
	/* Stores name of new high - scorer */
	private String name;


	@Override
	public void start(Stage gameWindow) {
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
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(event -> {
			MainViewController controller = new MainViewController(this);
			change_root(controller.view,0.005f);
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
		NameInputViewController controller = new NameInputViewController(this,numPlayers);
		change_root(controller.view);
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

	public void initRootLayout() {

		try {
			 /* Load root layout from fxml file */
			 FXMLLoader loader = new FXMLLoader();
			 loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			 rootLayout = (BorderPane) loader.load();

			 /* Show the scene containing the root layout */
			 scene = new Scene(rootLayout);
			 gameWindow.setScene(scene);
			 gameWindow.show();

		 }
		 catch (IOException e) {
			 e.printStackTrace();
		 }
	}

	public void gameToMenu() {

		try {
			 /* Load root layout from fxml file */
			 FXMLLoader loader = new FXMLLoader();
			 loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			 rootLayout = (BorderPane) loader.load();

			 /* Show the scene containing the root layout */
			 scene = new Scene(rootLayout);
			 gameWindow.setScene(scene);

		 }
		 catch (IOException e) {
			 e.printStackTrace();
		 }
	}

	
	/* The screen that greets the user */
	@Deprecated
	public void showWelcomeScreen() {
		
		try {
			
			/* Load/show the welcome screen */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/WelcomeScreen.fxml"));
			AnchorPane welcomeScreen = (AnchorPane) loader.load();
			rootLayout.setCenter(welcomeScreen);

            /* Get the controller to manipulate this class */
			WelcomeScreenController controller = loader.getController();
			controller.setMainApp(this);
			controller.addKeyListener();
			
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/* Shows tutorial slides to teach user the game mechanics */
	@Deprecated
	public void showHelp() {
		
		try {
			
			/* Load/show the help screen layout */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/HelpScreen.fxml"));
			AnchorPane helpScreen = (AnchorPane) loader.load();
			rootLayout.setCenter(helpScreen);
            /* Get the controller to manipulate this class */
			HelpScreenController controller = loader.getController();
			controller.setMainApp(this);
			controller.listenToKeyEvents();

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/* The screen that allows the user to select a map */
	@Deprecated
	public void showLevelSelect() {
		
		try {
			
			/* Load/show the level select layout */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/LevelSelect.fxml"));
			AnchorPane levelSelectScreen = (AnchorPane) loader.load();
			rootLayout.setCenter(levelSelectScreen);

            /* Get the controller to manipulate this class */
			LevelSelectController controller = loader.getController();
			controller.setMainApp(this);
			controller.listenToKeyEvents();

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/* Shows character select view */
	@Deprecated
	public void showCharacterSelect() {
		
		try {
			/* Load/show the character select view layout */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/CharacterSelect.fxml"));
			AnchorPane characterSelectScreen = (AnchorPane) loader.load();
			rootLayout.setCenter(characterSelectScreen);
			/* Get the controller to manipulate this class */
			CharacterSelectController controller = loader.getController();
			controller.setMainApp(this);
			controller.setPlayers(numPlayers);
			controller.addKeyListener();
			
			
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	/* Show high scores */
	@Deprecated
	public void showLeaderboard() {
		
		try {
			/* Load/show the character select view layout */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Leaderboard.fxml"));
			AnchorPane leaderBoard = (AnchorPane) loader.load();
			rootLayout.setCenter(leaderBoard);
			
			
			/* Get the controller to manipulate this class */
			LeaderboardController controller = loader.getController();
			controller.setMainApp(this);
			controller.addKeyListener();
			
			
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	/* Show credits */
	@Deprecated
	public void showCredits() {
		
		try {
			
			/* Load/show the credits view layout */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Credits.fxml"));
			AnchorPane creditScreen = (AnchorPane) loader.load();
			rootLayout.setCenter(creditScreen);
			
			/* Get the controller to manipulate this class */
			CreditsController controller = loader.getController();
			controller.setMainApp(this);
			
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	/* The game screen which will be showing the actual game play */
	@Deprecated
	public void showGameView() {
		
		try {
			/* Load/show the game view layout */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/GameView.fxml"));
			AnchorPane gameView = (AnchorPane) loader.load();
			rootLayout.setCenter(gameView);
			/* Get the controller to manipulate this class */
			GameViewController controller = loader.getController();
			controller.setMainApp(this);
			
			/* Create game and pass to controller */
			Game game = new Game(map,numPlayers,player2,player3);
			controller.setGame(game);
			controller.initialDraw();
			controller.startGame();	
			
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	/* Prints the results at the end of a game */
	@Deprecated
	public void showResults(int time,int lives, int score,char map) {
		
		try {
			
			/* Load/show the end of game results view layout */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Results.fxml"));
			AnchorPane results = (AnchorPane) loader.load();
			rootLayout.setCenter(results);
			
			
			/* Get the controller to manipulate this class */
			ResultsController controller = loader.getController();
			controller.setMainApp(this);
			controller.addKeyListener();
			controller.addParameters(time,lives,score,map);
			controller.showScore();
			

		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	/* Public method for creating new stage for inputting name on new high score*/
	@Deprecated
	public void setName() {
		
		try {
			
            /* Load the fxml file and create a new stage for the pop-up dialog */
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EditName.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();

            /* Create the new stage for recording name */
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Set Name");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(gameWindow);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            EditNameController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
            
            name = controller.getName();
            
        }
		
		catch (IOException e) {
            e.printStackTrace();
        }

	}

	@Deprecated
	public String getName() {
		
		return this.name;
	}
	
	/* Public setter to pass game mode back to this class from GameModeSelectController */
	@Deprecated
	public void setPlayers(int players) {
		
		this.numPlayers = players;
	}
	@Deprecated
	public void setPlayer2(int ghostIndex) {
		
		player2 = ghostIndex;
	}

	@Deprecated
	public void setPlayer3(int ghostIndex) {
		
		player3 = ghostIndex;
	}
	
	/* Public setter to pass map back to this class from LevelSelectController */
	@Deprecated
	public void setMap(char map) {
		
		this.map = map;
	}
	
	/* Public getter to add elements to the layout (Currently only used by GameViewController) */
	@Deprecated
	public BorderPane getPane() {
		
		return this.rootLayout;
		
	}

	/* Public getter for scene (mainly used to add key listeners) */
	@Deprecated
	public Scene getScene() {
		
		return this.scene;
	}
	@Deprecated
	public Stage getStage() {
		
		return this.gameWindow;
	}
	

}