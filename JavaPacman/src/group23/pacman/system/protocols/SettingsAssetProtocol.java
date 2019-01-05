package group23.pacman.system.protocols;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import group23.pacman.system.AssetProtocol;
import group23.pacman.system.JsonHandler;

import java.io.File;
import java.io.IOException;

/**
 * Created by Antonio Zaitoun on 04/01/2019.
 */
public class SettingsAssetProtocol implements AssetProtocol{

    private final String SETTINGS_FILE = "settings.json";

    @Override
    public String name() {
        return "Settings";
    }

    @Override
    public boolean checkIfExist(File directory) {

        try {
            JsonHandler jsonHandler = new JsonHandler(directory.getAbsolutePath() + File.separator + SETTINGS_FILE, true, new JsonObject().toString(), false);
            JsonObject content = jsonHandler.getContent();
            if (content != null) {
                //correct file and return true
                if (content.get("settings").getAsJsonObject() != null)
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
        JsonObject object = new JsonObject();

        // we fetch the data from external file so that we will always have at least 2 questions
        try {

            // fetch the data from the file
            JsonHandler jsonHandler = new JsonHandler("src/assets/jsonFiles/settings.json", true, "", false);
            JsonObject content = jsonHandler.getContent().get("settings").getAsJsonObject();

            // adding the data to the json object
            object.add("settings", content);

            // writing the data into the new json file
            makeFile(new File(directory.getAbsolutePath() + File.separator + SETTINGS_FILE), object.toString());
            System.out.println("created settings file from backup");
        } catch (IOException e) {
        }
    }
}
