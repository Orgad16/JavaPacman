package group23.pacman.controller;

import group23.pacman.MainApp;
import group23.pacman.model.Score;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ui.UITableView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created By Tony on 15/12/2018
 */
public class LeaderboardViewController extends RootController implements JoystickManager.JoystickListener {

    private UITableView<Score> tableView;

    @FXML
    private VBox container;

    public LeaderboardViewController() {
        super("/group23/pacman/view/LeaderboardViewController.fxml");

        tableView = new UITableView<Score>() {

            @Override
            public int numberOfColumns() {
                return 4;
            }

            @Override
            public Collection<? extends Score> dataSource() {
                //TODO hookup with valid data source
                List<Score> fakeData = new ArrayList<>();
                fakeData.add(new Score(0,"test1",4232,23,new Date().getTime()));
                fakeData.add(new Score(0,"test4",672,52,new Date().getTime()));
                fakeData.add(new Score(0,"test3",452,43,new Date().getTime()));
                fakeData.add(new Score(0,"test2",234,23,new Date().getTime()));
                return fakeData;
            }

            @Override
            public String bundleIdForIndex(int index) {
                String[] columns = { "NAME", "SCORE", "TIME", "DATE" };
                return columns[index];
            }

            @Override
            public TableColumnValue<Score> cellValueForColumnAt(int index) {
                switch (index){
                    // return name
                    case 0: return Score::getNickName;

                    // return score
                    case 1: return Score::getScore;

                    // return time
                    case 2: return Score::getTimeGame;

                    // return date
                    case 3: return score ->
                            new SimpleDateFormat("dd/MM/yyyy")
                                    .format(new Date(score.getGameDate()));
                }
                return null;
            }
        };

        container.getChildren().add(tableView);

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
        JoystickManager
                .shared
                .unsubscribe(identifier());
    }

    @Override
    public void onJoystickTriggered(int joystickId, JoystickManager.Key selectedKey) {
        if (joystickId == 1){
            switch (selectedKey) {
                case UP:
                    // scroll up
                    break;
                case DOWN:
                    // scroll down
                    break;
                case TWO:
                    MainApp.getInstance().popViewController();
                    break;
            }
        }
    }
}