package group23.pacman.system.protocols;

import com.google.gson.*;
import group23.pacman.system.AssetProtocol;
import group23.pacman.system.JsonHandler;

import java.io.*;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public class ScoresAssetProtocol implements AssetProtocol{

    private final String SCORE_FILE = "game_records.json";

    @Override
    public String name() {
        return "Scores";
    }

    @Override
    public boolean checkIfExist(File directory) {

        try {
            JsonHandler jsonHandler = new JsonHandler(directory.getAbsolutePath() + File.separator + SCORE_FILE, true, "", false);
            JsonObject content = jsonHandler.getContent();
            if (content != null) {
                //correct file and return true
                if (content.get("game_records").getAsJsonArray() != null)
                    return true;
            } else {
                // corrupt file and return false
                return false;
            }
        } catch (Exception e) {
        }

        //file not found -> return false
        return false;
    }

    @Override
    public void createDefaultFiles(File directory) {

        // init json object
        JsonObject object = new JsonObject();

        // adding property game records array
        object.add("game_records", new JsonArray());

        // make file with default value
        makeFile(new File(directory.getAbsolutePath() + File.separator + SCORE_FILE), object.toString());

    }
}
