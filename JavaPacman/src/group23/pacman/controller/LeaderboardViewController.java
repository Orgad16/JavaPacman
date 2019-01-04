package group23.pacman.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import group23.pacman.MainApp;
import group23.pacman.model.Score;
import group23.pacman.system.SysData;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import ui.UITableView;

import java.io.IOException;
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
                List<Score> data = new ArrayList<>();
                SysData sysData = new SysData();
                try {
                    JsonArray array = sysData.getGameScores(10).getAsJsonArray();
                    for (JsonElement element : array) {

                        // getting the element as json object
                        JsonObject jsonObject = element.getAsJsonObject();

                        // getting the info from the specific record
                        String name = jsonObject.get("name").getAsString();
                        int game_score = jsonObject.get("score").getAsInt();
                        String timer = jsonObject.get("timer").getAsString();
                        String date = jsonObject.get("date").getAsString();

                        // append the score to the list
                        data.add(new Score(name, game_score, timer, date));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return data;
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