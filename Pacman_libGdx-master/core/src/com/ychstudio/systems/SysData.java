package com.ychstudio.systems;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

public class SysData {

    final String questionPath = "./assets/jsonFiles/questions.json";
    final String gameRecordsPath = "./assets/jsonFiles/gameRecords.json";
    private JsonHandler questionHandler;
    private JsonHandler recordsHandler;


    /**
     * this function will init the specified handler
     * @param pathTo the path to the json file, questions or game scores
     * @throws IOException
     */
    private void initHandler(String pathTo) throws IOException {
        if (pathTo.equals("question")) {
            if (questionHandler == null)
                questionHandler = new JsonHandler(questionPath);
        }
        else {
            if (recordsHandler == null)
                recordsHandler = new JsonHandler(gameRecordsPath);
        }
    }


    /**
     * this function will return the game questions
     * @return JsonArray of game questions
     * @throws IOException
     */
    public JsonArray getQuestions() throws IOException {

        initHandler("question");

        return questionHandler.getContent().get("questions").getAsJsonArray();
    }


    /**
     * this function will remove the specific question from the json file
     * @param questionId the question id
     * @param conditionField the conditional field that will check against
     * @param fromObjects the property name of the array
     * @throws IOException
     */
    public void removeQuestion(String questionId, String conditionField, String fromObjects) throws IOException {

        initHandler("question");

        questionHandler.removeFromJson(questionId, conditionField, "questions");
    }


    /**
     * this function will add a question to the json file
     * @param objectToWrite JsonObject that will be written to the file
     * @throws IOException
     */
    public void addQuestion(JsonObject objectToWrite) throws IOException{

        initHandler("question");

        questionHandler.writeSingle(objectToWrite, "questions");
    }


    /**
     * this function will update specific question
     * @param questionToEdit the question id
     * @param contentToInsert the new content that will be updated
     * @throws IOException
     */
    public void editQuestion(String questionToEdit, JsonObject contentToInsert) throws IOException {

        initHandler("question");

        questionHandler.editQuestion(questionToEdit, contentToInsert, "questions");

    }


    /**
     * this function will return the highest game scores
     * @return JsonArray of game scores
     * @throws IOException
     */
    public JsonArray getGameScores() throws IOException {

        initHandler("records");

        return recordsHandler.getContent().get("game_records").getAsJsonArray();
    }


    /**
     * this function will remove the game score from the json file
     * @param gameId the game id
     * @param conditionField the conditional field that we check against
     * @throws IOException
     */
    public void removeGameScore(String gameId, String conditionField) throws IOException{

        initHandler("records");

        recordsHandler.removeFromJson(gameId, conditionField, "game_records");
    }


    /**
     * this function will add a game score to the json file
     * @param objectToWrite the game score to add
     * @throws IOException
     */
    public void addGameScore(JsonObject objectToWrite) throws IOException{

        initHandler("records");

        recordsHandler.writeSingle(objectToWrite, "game_records");

    }

}

// json file example for game records:
// {"game_records":[
//      {"id": 1,
//      "name": "",
//      "score": 1234},{...},{...}
// ]}


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
