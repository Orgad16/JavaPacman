package com.ychstudio.systems;

import com.google.gson.*;
import java.io.*;


public class JsonHandler {

    private String path;
    private JsonParser jsonParser;
    private FileWriter writer;
    private Gson gson;
    private JsonObject content;

    public JsonHandler(String path) throws IOException{
        this.path = path;
        this.jsonParser = new JsonParser();
        this.gson = new Gson();
        this.content = jsonParser.parse(new FileReader(this.path)).getAsJsonObject();
    }

    public JsonObject getContent(){
        return this.content;
    }


    private boolean writeAll(JsonObject newData){
        try {
            this.writer = new FileWriter(path);
            writer.write(gson.toJson(newData));
        } catch (IOException writeException) {
            writeException.printStackTrace();
            return false;
        } finally {
            try {
                writer.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
        }
        return true;
    }

    public void writeSingle(JsonObject objectToWrite, String field) {

        // getting the content of the json file
        JsonObject tempContent = content;

        // get the questions array list
        JsonArray array = tempContent.get(field).getAsJsonArray();

        // adding the new question to the array
        array.add(objectToWrite);

        // update the json file
        updateJson(tempContent, array, field);

    }

    public void editQuestion(String questionToEdit, JsonObject contentToChange, String field) {
        JsonObject tempContent = content;

        // get the specified field, in out questions case it will be: "questions"
        JsonArray array = tempContent.get(field).getAsJsonArray();

        for (JsonElement element : array) {
            if (element.getAsJsonObject().get("question").getAsString().equals(questionToEdit)) {

                // checking if the new content we got has NEW answers
                if (contentToChange.has("answers")) {

                    // removing the answers element from the json
                    element.getAsJsonObject().remove("answers");

                    // adding the new answers to the json
                    element.getAsJsonObject().add("answers", contentToChange.get("answers"));
                }

                // checking if the new content we got has NEW correct answer
                if (contentToChange.has("correct_ans")) {

                    // removing the answers element from the json
                    element.getAsJsonObject().remove("correct_ans");

                    // adding the new answers to the json
                    element.getAsJsonObject().add("correct_ans", contentToChange.get("correct_ans"));
                }

                // checking if the new content we got has NEW level
                if (contentToChange.has("level")) {

                    // removing the answers element from the json
                    element.getAsJsonObject().remove("level");

                    // adding the new answers to the json
                    element.getAsJsonObject().add("level", contentToChange.get("level"));
                }

                // checking if the new content we got has NEW team
                if (contentToChange.has("team")) {

                    // removing the answers element from the json
                    element.getAsJsonObject().remove("team");

                    // adding the new answers to the json
                    element.getAsJsonObject().add("team", contentToChange.get("team"));

                }
            }
        }

        updateJson(tempContent, array, field);

    }

    public void removeFromJson(String objectToRemove, String conditionField, String fromObjects) {
        JsonObject tempContent = content;

        JsonArray array = tempContent.get(fromObjects).getAsJsonArray();

        for (JsonElement element : array) {
            if (element.getAsJsonObject().get(conditionField).getAsInt() == Integer.valueOf(objectToRemove)) {
                array.remove(element);
                break;
            }
        }

        updateJson(tempContent, array, fromObjects);

    }

    private void updateJson(JsonObject contentToUpdate, JsonArray arrayToReplace, String field) {
        contentToUpdate.remove(field);
        contentToUpdate.add(field, arrayToReplace);
        content = contentToUpdate;
        writeAll(content);
    }

    //TODO: add function to remove game record
    // json file example for game records:
    // {"game_records":[
    //      {"id": 1,
    //      "name": "",
    //      "score": 1234},{...},{...}
    // ]}
}
