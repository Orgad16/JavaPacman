package group23.pacman.system;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import group23.pacman.model.Question;
import group23.pacman.model.Score;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SysData {

    final String questionPath = "src/assets/jsonFiles/questions.json";
    final String gameRecordsPath = "src/assets/jsonFiles/gameRecords.json";
    private JsonHandler questionHandler;
    private JsonHandler recordsHandler;

    public static final SysData instance = new SysData();


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
     * Field conditionField the conditional field that will check against
     * Field fromObjects the property name of the array
     * @throws IOException
     */
    public void removeQuestion(String questionId) throws IOException {

        initHandler("question");

        questionHandler.removeFromJson(questionId, "question", "questions");
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
    public void removeGameScore(String gameId) throws IOException{

        initHandler("records");

        recordsHandler.removeFromJson(gameId, "id", "game_records");
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

    public ArrayList<Question> getQuestionsFromJson() {
        ArrayList<Question> qList = new ArrayList<>();

        // getting the questions as json array
        SysData sysData = new SysData();
        JsonArray jsonList = null;
        try {
            jsonList = sysData.getQuestions();
        } catch (IOException e) {
            e.printStackTrace();
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
//         "time":"1:24"
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