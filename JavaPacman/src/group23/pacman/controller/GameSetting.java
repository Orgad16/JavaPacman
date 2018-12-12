package group23.pacman.controller;

import java.util.Date;

/**
 * Created By Orgad on 12/12/2018
 */
public class GameSetting {

    private int NumOfLife;
    private char map;
    private Date time ;
    private boolean numbrOfPlayers; //true- one player, false-two player
    


    public GameSetting (int numOfLife, char map, Date time, boolean numbrOfPlayers){

        numOfLife= numOfLife;
        map = map;
        time = time;
        numbrOfPlayers = numbrOfPlayers;

    }

    public int getNumOfLife() {
        return NumOfLife;
    }

    public void setNumOfLife(int numOfLife) {
        NumOfLife = numOfLife;
    }

    public char getMap() {
        return map;
    }

    public void setMap(char map) {
        this.map = map;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isNumbrOfPlayers() {
        return numbrOfPlayers;
    }

    public void setNumbrOfPlayers(boolean numbrOfPlayers) {
        this.numbrOfPlayers = numbrOfPlayers;
    }




}
