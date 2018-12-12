package group23.pacman.view;

import group23.pacman.MainApp;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/** Controller class for the credits view **/
public class CreditsController {
	
	@FXML
	private ImageView background;
	
	private MainApp mainApp;
	
	
	public CreditsController() {
		
	}
	
	@FXML
	private void initialize() {
		
		background.setImage(new Image("bg/credits.png"));
	}
	
	@FXML
	private void handleButton(KeyEvent event) {
		
		if (event.getCode() == KeyCode.ESCAPE) {
			//mainApp.showWelcomeScreen();
		}
	}
	
	
	public void setMainApp(MainApp mainApp) {
		
		this.mainApp = mainApp;
	}
}
