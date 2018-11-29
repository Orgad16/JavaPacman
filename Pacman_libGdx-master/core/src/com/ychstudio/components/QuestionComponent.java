package com.ychstudio.components;

import com.badlogic.ashley.core.Component;

import java.util.ArrayList;

public class QuestionComponent implements Component {

    private String question;
    private ArrayList<String> answers;
    private int correct_ans;
    private int level;
    private String team;

    public QuestionComponent(String question, ArrayList<String> answers, int correct_ans, int level, String team) {

        this.question = question;
        this.answers = answers;
        this.correct_ans = correct_ans;
        this.level = level;
        this.team = team;

    }


    public String getQuestion(){
        return this.question;
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
