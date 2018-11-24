package com.ychstudio.utils;

import com.google.gson.*;
import java.io.*;


public class JsonHandler {

    private String path;
    private BufferedReader bufferedReader;
    private FileWriter writer;
    private Gson gson;

    public JsonHandler(String path) throws IOException{
        this.path = path;
        this.bufferedReader = new BufferedReader(new FileReader(path));
        this.writer = new FileWriter(path);
        this.gson = new Gson();
    }

    public JsonObject getContent() {
        JsonObject content = gson.fromJson(bufferedReader, JsonObject.class);

        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public boolean writeAll(JsonObject newData){
        try {
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

    public JsonObject writeSingle(JsonObject question, String field) {

        // getting the content of the json file
        JsonObject tempContent = getContent();

        // get the questions array list
        JsonArray array = tempContent.get(field).getAsJsonArray();

        // adding the new question to the array
        array.add(question);

        // removing the questions array to append new one
        tempContent.remove(field);

        // appending the new questions array (with the new question) to the content.
        tempContent.add(field, array);

        // writing the new content to the json file and getting response (true/false)
        boolean wrote = writeAll(tempContent);

        // if the writing was successful -> return the new content, else -> return empty JsonObject
        if (wrote) {
            return tempContent;
        } else { return new JsonObject(); }
    }

    public boolean editJson(String questionToEdit, JsonObject contentToChange, String field) {
        JsonObject tempContent = getContent();

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

        return true;
    }
}
