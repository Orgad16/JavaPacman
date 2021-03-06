package group23.pacman.system;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import group23.pacman.model.Question;
import group23.pacman.model.Score;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SysData {

    final String questionPath = AssetManager.file("questions.json");
    final String gameRecordsPath = AssetManager.file("game_records.json");
    final String mapsPath = "maps/";
    final String gameSettingsPath = AssetManager.file("game_settings.json");
    private JsonHandler questionHandler;
    private JsonHandler recordsHandler;
    private JsonHandler gameSettingsHandler;
    private String mapContent;
    public static final SysData instance = new SysData();


    /**
     * this function will init the specified handler
     * @param pathTo the path to the json file, questions or game scores
     * @throws IOException
     */
    private void initHandler(String pathTo, String helper) throws IOException {

        switch (pathTo) {
            case "question":
                if (questionHandler == null)
                    questionHandler = new JsonHandler(questionPath,true,"{\"questions\" : []}",true);
                break;
            case "maps":
                if (mapContent == null) {
                    BufferedReader br = new BufferedReader(new FileReader(mapsPath + helper));
                    try {
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append(System.lineSeparator());
                            line = br.readLine();
                        }
                        mapContent = sb.toString();
                    } finally {
                        br.close();
                    }
                }
                break;
            case "gameSettings":
                if (gameSettingsHandler == null)
                    gameSettingsHandler = new JsonHandler(gameSettingsPath, true, "{\"settings\" : []}", true);
                break;
            default:
                // will go to records
                if (recordsHandler == null)
                    recordsHandler = new JsonHandler(gameRecordsPath,true, "{\"game_records\" : [] }",true);
                break;
        }
    }


    /**
     * this function will return the game questions
     * @return JsonArray of game questions
     * @throws IOException
     */
    public JsonArray getQuestions() throws IOException {

        initHandler("question", null);

        return questionHandler.getContent().get("questions").getAsJsonArray();
    }


    /**
     * this function will remove the specific question from the json file
     * @param questionId the question id
     * Field conditionField the conditional field that will check against
     * Field fromObjects the property name of the array
     * @throws IOException
     */
    public void removeQuestion(String questionId) throws IOException {

        initHandler("question",null);

        questionHandler.removeFromJson(questionId, "question", "questions");
    }


    /**
     * this function will add a question to the json file
     * @param objectToWrite JsonObject that will be written to the file
     * @throws IOException
     */
    public void addQuestion(JsonObject objectToWrite) throws IOException{

        initHandler("question", null);

        questionHandler.writeSingle(objectToWrite, "questions");
    }


    /**
     * this function will update specific question
     * @param questionToEdit the question id
     * @param contentToInsert the new content that will be updated
     * @throws IOException
     */
    public void editQuestion(String questionToEdit, JsonObject contentToInsert) throws IOException {

        initHandler("question", null);

        questionHandler.editQuestion(questionToEdit, contentToInsert, "questions");

    }


    /**
     * this function will return the highest game scores
     * @param numberOfRecords the number of records you want to get (0 -> all records)
     * @return JsonArray of game scores
     * @throws IOException
     */
    public JsonArray getGameScores(int numberOfRecords) throws IOException {

        initHandler("records", null);
        JsonArray jsonToReturn = recordsHandler.getContent().get("game_records").getAsJsonArray();
        if (numberOfRecords == 0) {
            return jsonToReturn;
        } else {
            JsonArray array = new JsonArray(numberOfRecords);
            int index = 0;
            for (JsonElement element : jsonToReturn) {
                // stopping after we reached the number of records required
                if (index >= numberOfRecords) { break; }

                // adding the elements to the returned array
                array.add(element);

                index ++;
            }
            return array;
        }
    }


    /**
     * this function will remove the game score from the json file
     * @param gameId the game id
     * @throws IOException
     */
    public void removeGameScore(String gameId) throws IOException{

        initHandler("records", null);

        recordsHandler.removeFromJson(gameId, "id", "game_records");
    }


    /**
     * this function will add a game score to the json file
     * @param objectToWrite the game score to add
     * @throws IOException
     */
    public void addGameScore(JsonObject objectToWrite) throws IOException{

        initHandler("records", null);

        recordsHandler.writeSingle(objectToWrite, "game_records");

    }

    public ArrayList<Question> getQuestionsFromJson() {
        ArrayList<Question> qList = new ArrayList<>();

        // getting the questions as json array
        SysData sysData = new SysData();
        JsonArray jsonList = new JsonArray();
        try {
            jsonList = sysData.getQuestions();
        } catch (IOException e) {
            qList = new ArrayList<>();
        }

        // loop over the json array and create a Question entity
        for (JsonElement element : jsonList) {

            // getting the question field
            String qField = element.getAsJsonObject().get("question").getAsString();

            // getting the answers for question
            ArrayList<String> aList = new ArrayList<>();
            for (JsonElement answer : element.getAsJsonObject().get("answers").getAsJsonArray()) {
                aList.add(answer.getAsString());
            }

            // getting the correct answer for that question
            int correctAns = element.getAsJsonObject().get("correct_ans").getAsInt();

            // getting the level of the question
            int qLevel = element.getAsJsonObject().get("level").getAsInt();

            // getting the team that wrote the question
            String qTeam = element.getAsJsonObject().get("team").getAsString();

            Question q = new Question(qField, aList, correctAns, qLevel, qTeam);

            qList.add(q);
        }

        return qList;


    }


    /**
     * this function will return the content of a given map in map folder
     * @param mapName the name of the map (file name including ending)
     * @return String value of the content of the file
     * @throws IOException
     */
    public String getMap(String mapName) throws IOException {

        initHandler(mapsPath, mapName);

        return this.mapContent;
    }

}

// json file example for game records:
// {
//         "game_records":[
//         {
//         "id": "1",
//         "name": "",
//         "score": "1234",
//         "time":"1:24",
//          "date":"22222222"/*timeStemp*/
//         },
//         {
//         "id": "2",
//         "name": "tony",
//         "score": "123",
//         "timer":"1:24"
//         "date":"22222222"
//         }
//         ]
//         }


// json file example for questions
//{
//        "questions":[
//          {
//              "question": "q1",
//              "answers": [
//                  "answer1",
//                  "answer2",
//                  "answer3",
//                  "answer4"
//              ],
//              "correct_ans": "2",
//              "level": "1",
//              "team": "animal"
//          },
//          {
//              "question": "q2",
//              "answers": [
//                  "answer1",
//                  "answer2",
//                  "answer3",
//                  "answer4"
//              ],
//              "correct_ans": "1",
//              "level": "2",
//              "team": "animal"
//          }
//        ]
// }
