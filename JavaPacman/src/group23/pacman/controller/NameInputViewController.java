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

    @FXML
    private ToggleButton back;

    @FXML
    private ToggleButton next;

    @FXML
    private HBox letters1;

    @FXML
    private HBox letters2;

    @FXML
    private Label nameLabel;

    @FXML
    private Label title_label;


    private StringBuilder currentName = new StringBuilder();

    private int playerIndex;

    // adapter used for joystick navigation
    UINavigationAdapter<ToggleButton> movmentAdapter = new UINavigationAdapter<>();

    public NameInputViewController(int playerIndex) {
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
        movmentAdapter.addRow(firstRow);
        movmentAdapter.addRow(secondRow);
        movmentAdapter.addRow(back,next);
        movmentAdapter.current().setSelected(true);


        // reset all player names
//        if(playerIndex == 0){
//            GameSettings.instance.getPlayerNames().clear();
//        }

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
        //TODO change this later to match the current joystick
        if(joystickId == playerIndex + 1){
            switch (selectedKey){
                case UP:
                    movmentAdapter.current().setSelected(false);
                    movmentAdapter.move_up().setSelected(true);
                    break;
                case RIGHT:
                    movmentAdapter.current().setSelected(false);
                    movmentAdapter.move_right().setSelected(true);
                    break;
                case DOWN:
                    movmentAdapter.current().setSelected(false);
                    movmentAdapter.move_down().setSelected(true);
                    break;
                case LEFT:
                    movmentAdapter.current().setSelected(false);
                    movmentAdapter.move_left().setSelected(true);
                    break;
                case ONE:
                    ToggleButton current = movmentAdapter.current();
                    if(current == next) {

                        //go to next
                        if(playerIndex + 1 == GameSettings.instance.getNumbrOfPlayers()){
                            // go to map selection
                            GameSettings.instance.addPlayerName(currentName.toString());
                            MainApp.getInstance().pushViewController(
                                    new MapSelectionViewController(),
                                    true
                            );
                        }else {
                            // go to next player name
                            GameSettings.instance.addPlayerName(currentName.toString());
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

    public void deleteLetter(){
        if(currentName.length() == 0)
            return;
        currentName.deleteCharAt(currentName.length()-1);
        nameLabel.setText("NAME: "+currentName.toString());
    }

    public void appendLetter(String letter){
        currentName.append(letter);
        nameLabel.setText("NAME: "+currentName.toString());
    }
}
