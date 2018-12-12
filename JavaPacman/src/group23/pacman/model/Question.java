package group23.pacman.model;

import java.util.ArrayList;

public class Question{

    private String questionID;
    private ArrayList<String> answers;
    private int correct_ans;
    private int level;
    private String team;

    public Question(String question, ArrayList<String> answers, int correct_ans, int level, String team) {

        this.questionID = question;
        this.answers = answers;
        this.correct_ans = correct_ans;
        this.level = level;
        this.team = team;

    }


    public String getQuestionID(){
        return this.questionID;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public int getCorrect_ans() {
        return correct_ans;
    }

    public int getLevel() {
        return level;
    }

    public String getTeam() {
        return team;
    }

    public void setAnswers(ArrayList<String> newAnswers) {
        this.answers = newAnswers;
    }

    public void setCorrect_ans(int newCorrect_ans) {
        this.correct_ans = newCorrect_ans;
    }

    public void setLevel(int newLevel) {
        this.level = newLevel;
    }

    public void setTeam(String newTeam) {
        this.team = newTeam;
    }
}
