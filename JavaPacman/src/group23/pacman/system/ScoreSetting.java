package group23.pacman.system;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import group23.pacman.model.Score;

import java.io.IOException;
import java.util.ArrayList;

public class ScoreSetting {



    public void saveGame (Score scoreGame) throws IOException {

        JsonObject jsonObj = new JsonObject();

        jsonObj.addProperty("id",scoreGame.getId());

        jsonObj.addProperty("name",scoreGame.getNickName());

        jsonObj.addProperty("score",scoreGame.getScore());

        jsonObj.addProperty("time",scoreGame.getTimeGame());

        jsonObj.addProperty("date",scoreGame.getGameDate());

        try {
            SysData.instance.addGameScore(jsonObj);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * The purpose of this function is to bring all scores of the game,
     * for showing them in 'leaders board' screen
     * @return list of all time scoring playing the game
     */

    public static ArrayList<Score> getAllScore(){
        ArrayList<Score> sList = new ArrayList<>();
        // getting the questions as json array
        SysData sysData = new SysData();
        JsonArray jsonList = null;
        try {
            jsonList = sysData.getGameScores();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // loop over the json array and create a score entity
        for (JsonElement element : jsonList) {


            // getting the the game id
            int id = element.getAsJsonObject().get("id").getAsInt();

            // getting the nickname of the user in game
            String nickName = element.getAsJsonObject().get("name").getAsString();

            // getting the final score in game
            int gScore = element.getAsJsonObject().get("score").getAsInt();

            // getting the game time
            int gTime= element.getAsJsonObject().get("time").getAsInt();

            //getting the game date
            long dateStamp= element.getAsJsonObject().get("date").getAsLong();

            Score s= new Score(id,nickName,gScore,gTime,dateStamp);

            sList.add(s);
        }

            return sList;
    }
}
