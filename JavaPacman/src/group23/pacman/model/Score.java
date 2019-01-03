package group23.pacman.model;

import group23.pacman.model.Timer;

public class Score {

    /* id of the score*/
    private String id;

    /*User's nickname in a game */
    private String nickName;

    /* keeps user's game score*/
    private int score;

    /* show time game duration*/
    private String timeGame;

    /* show game date*/
    private String gameDate;


    public Score(String nickName, int score, String timeGame, String gameDate) {
        this.nickName = nickName;
        this.score = score;
        this.timeGame = timeGame;
        this.gameDate=gameDate;
    }

    /*GETS/SETTERS */
    public String getNickName() {
        return nickName;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTimeGame() {
        return timeGame;
    }

    public void setTimeGame(String timeGame) {
        this.timeGame = timeGame;
    }

    public String getId() {
        return this.id;
    }
}
