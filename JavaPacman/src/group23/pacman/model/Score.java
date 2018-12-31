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
    private int timeGame;

    /* show game date*/
    private long gameDate;



    public Score(String nickName, int score, int timeGame, long gameDate) {
        this.nickName = nickName;
        this.score = score;
        this.timeGame = timeGame;
        this.gameDate=gameDate;
    }

    /*GETS/SETTERS */
    public String getNickName() {
        return nickName;
    }

    public long getGameDate() {
        return gameDate;
    }

    public void setGameDate(long gameDate) {
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

    public int getTimeGame() {
        return timeGame;
    }

    public void setTimeGame(int timeGame) {
        this.timeGame = timeGame;
    }

    public String getId() {
        return this.id;
    }
}
