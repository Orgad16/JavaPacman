package group23.pacman.controller;
import java.util.Date;

/**
 * Created By Orgad on 12/12/2018
 */
public class GameSettings {

    public static final GameSettings instance = new GameSettings();

    /**
     * The number of lives per player
     */
    private int numOfLife;

    /**
     * The map that was selected
     */
    private int map;

    /**
     * The current date/time
     */
    private Date time ;

    /**
     * The number of players playing
     */
    private int numbrOfPlayers;

    private GameSettings() { }

    
    public int getNumOfLife() {
        return numOfLife;
    }

    public void setNumOfLife(int numOfLife) {
        this.numOfLife = numOfLife;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getNumbrOfPlayers() {
        return numbrOfPlayers;
    }

    public void setNumbrOfPlayers(int numbrOfPlayers) {
        this.numbrOfPlayers = numbrOfPlayers;
    }
}
