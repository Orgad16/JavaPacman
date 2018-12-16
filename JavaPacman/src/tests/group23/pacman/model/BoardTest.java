package group23.pacman.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created By Tony on 15/12/2018
 */
public class BoardTest { //

    public static final int TILE_SIZE = 10;
    public static final int Y_OFFSET = 9;
    public static final int OFFSET = 1;

    private Random rand;
    private Board tester;

    @Before
    public void setUp() throws Exception {
        tester = new Board();
        rand = new Random();

    }

    @Test
    public void isNode() {
        /*random number to test the junit*/
        int x= rand.nextInt();
        int y= rand.nextInt();

        assertFalse("X: "+x+" Y: "+y+" is:",tester.isNode(x,y));
    }

    @Test
    public void isValidPos() {
        int positionX= rand.nextInt();
        int positionY= rand.nextInt();

        assertFalse("position" +positionX+ "," +positionY+ "is:", tester.isValidPos(positionX,positionY));

    }

    @After
    public void tearDown() throws Exception {
        tester = null;
        rand = null;
    }
}