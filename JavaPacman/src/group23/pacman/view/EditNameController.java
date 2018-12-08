package group23.pacman.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


/** Controller class for the view which takes in user's name for a new high score */

public class EditNameController {
	
	
	/* View elements in EditName.fxml */
	@FXML
	private TextField nameField;
	@FXML
	private ImageView background;
	@FXML
	private ImageView errorMessage;
	
	
	/* The stage which will contain the text field for input */
	private Stage dialogStage;
	
	/* Name is set when input is valid */
	private String name;
	
	
	/* Constructor */
	public EditNameController() {
		
	}
	
	
	@FXML
	private void initialize() {
		
		/* Set up the images in this view */
		background.setImage(new Image("bg/dialogBg.png"));
		errorMessage.setImage(new Image("assets/misc/error_message.png"));
		errorMessage.setOpacity(0);

	}
	
	
	@FXML
	private void handleEnter(KeyEvent event) {
		
		if (event.getCode() == KeyCode.ENTER) {
			
			if (isValidInput(nameField.getText())){
				
				this.name = nameField.getText();
				dialogStage.close();
			}
			else {
				errorMessage.setOpacity(1);
			}
		}
	}
	

	/* Tests if the user's input conforms to the rules */
	private boolean isValidInput(String text) {
		
		if (text.length() == 0 || text.length() > 10) {
			
			return false;
		}
		
		for (int i = 0; i < text.length(); i++) {
			
			if (!Character.isLetter(text.charAt(i)) && !Character.isDigit(text.charAt(i))){
				return false;
			}
			if (text.charAt(i) == ' '){
				return false;
			}
		}
		
		return true;
	}
	
	
	/** PUBLIC GETTERS AND SETTERS BELOW */
	
	public String getName() {
		
		return name;
	}
	
	
	public void setDialogStage(Stage dialogStage) {
		
		this.dialogStage = dialogStage;
	}
}
