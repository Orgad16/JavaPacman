package group23.pacman.controller;

import group23.pacman.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;

/**
 * Created By Tony on 12/12/2018
 */
public class NameInputViewController extends RootController implements JoystickManager.JoystickListener{

    /**
     * The back button.
     */
    @FXML
    private ToggleButton back;

    /**
     * The next button
     */
    @FXML
    private ToggleButton next;

    /**
     * The hbox for the first 13 letters
     */
    @FXML
    private HBox letters1;

    /**
     * The hbox for the remaining 13 letters
     */
    @FXML
    private HBox letters2;

    /**
     * The name label which displays the current name of the player.
     */
    @FXML
    private Label nameLabel;

    /**
     * The title label.
     */
    @FXML
    private Label title_label;

    /**
     * String builder to hold the current name being inserted.
     */
    private StringBuilder currentName = new StringBuilder();

    /**
     * The current player index (player 0 or player 1)
     */
    private int playerIndex;

    /**
     * adapter used for joystick navigation
     */
    private UINavigationAdapter<ToggleButton> movementAdapter = new UINavigationAdapter<>();

    public NameInputViewController(int playerIndex) {
        //load view
        super("/group23/pacman/view/NameInputViewController.fxml");

        // init variables
        this.playerIndex = playerIndex;

        // update label
        title_label.setText("SELECT PLAYER "+ ( playerIndex + 1 )+ " NAME");

        ToggleButton[] firstRow = new ToggleButton[13];
        ToggleButton[] secondRow = new ToggleButton[13];

        for (int i = 0; i < 26; i++) {
            ToggleButton button = new ToggleButton();
            button.getStyleClass().add("button-retro");
            button.setStyle("-fx-max-width: 50px;-fx-min-width: 50px;-fx-font-size: 18px;-fx-padding: 3 3 3 3;");
            String letter = Character.toString((char) ('A' + i));
            button.setId(letter);
            button.setText(letter);

            if (i < 13){
                letters1.getChildren().add(button);
                firstRow[i] = button;
            }else{
                letters2.getChildren().add(button);
                secondRow[i-13] = button;
            }
        }

        // setup movement adapter
        movementAdapter.addRow(firstRow);
        movementAdapter.addRow(secondRow);
        movementAdapter.addRow(back,next);
        movementAdapter.current().setSelected(true);
    }

    @Override
    public void didBecomeActive() {
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
        if(joystickId == playerIndex + 1){
            switch (selectedKey){
                /* NAVIGATION CONTROL */
                case UP:
                    movementAdapter.current().setSelected(false);
                    movementAdapter.move_up().setSelected(true);
                    break;
                case RIGHT:
                    movementAdapter.current().setSelected(false);
                    movementAdapter.move_right().setSelected(true);
                    break;
                case DOWN:
                    movementAdapter.current().setSelected(false);
                    movementAdapter.move_down().setSelected(true);
                    break;
                case LEFT:
                    movementAdapter.current().setSelected(false);
                    movementAdapter.move_left().setSelected(true);
                    break;
                /* ACTION CONTROL */
                case ONE:
                    ToggleButton current = movementAdapter.current();
                    if(current == next) {

                        //go to next
                        if(playerIndex + 1 == GameSettings.instance.getNumbrOfPlayers()){
                            // go to map selection
                            GameSettings.instance.addPlayerName(currentName.toString(),playerIndex);
                            MainApp.getInstance().pushViewController(
                                    new MapSelectionViewController(),
                                    true
                            );
                        }else {
                            // go to next player name
                            GameSettings.instance.addPlayerName(currentName.toString(),playerIndex);
                            MainApp.getInstance().pushViewController(
                                    new NameInputViewController(playerIndex + 1),
                                    true
                            );
                        }

                    } else if(current == back) {
                        // go back
                        MainApp.getInstance().popViewController();
                    } else {
                      // append letter
                      appendLetter(current.getText());
                    }
                    break;
                case TWO:
                    deleteLetter();
                    break;
            }
        }
    }

    /**
     * Helper method used delete a letter.
     */
    private void deleteLetter(){
        if(currentName.length() == 0)
            return;
        currentName.deleteCharAt(currentName.length()-1);
        nameLabel.setText("NAME: "+currentName.toString());
    }

    /**
     * Helper method used to append a letter
     * @param letter the letter to add to the name.
     */
    private void appendLetter(String letter){
        currentName.append(letter);
        nameLabel.setText("NAME: "+currentName.toString());
    }
}
